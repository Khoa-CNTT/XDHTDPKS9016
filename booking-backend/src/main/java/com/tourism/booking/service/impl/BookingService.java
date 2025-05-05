package com.tourism.booking.service.impl;

import com.tourism.booking.dto.*;
import com.tourism.booking.dto.booking.*;
import com.tourism.booking.dto.user.UserProfileResponse;
import com.tourism.booking.model.*;
import com.tourism.booking.repository.*;
import com.tourism.booking.service.IAccountService;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class BookingService {

    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

    IBookingRepository bookingRepository;

    IRoomTypeRepository roomTypeRepository;

    IUserProfileRepository userRepository;

    IServiceRepository serviceRepository;

    IBillRepository billRepository;

    IAccountService accountService;

    RoomTypeBookingService roomTypeService;

    UserProfileService userProfileService;

    // Temporary storage for bookings in progress
    private static final Map<Long, Booking> temporaryBookings = new HashMap<>();

    // Khóa đồng bộ cho từng booking
    private static final Map<Long, Object> bookingLocks = new ConcurrentHashMap<>();

    /**
     * Step 1: Initialize booking - create a temporary booking with room selection
     */
    @Transactional
    public BookingResponseDTO initializeBooking(BookingRequestDTO request, Principal principal) {
        // Lấy thông tin RoomType
        RoomType roomType = roomTypeRepository.findById(request.getRoomTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy loại phòng"));

        // Kiểm tra số lượng người
        if (request.getNumberPeople() > roomType.getMaximum_people()) {
            throw new IllegalArgumentException("Số lượng người vượt quá giới hạn của loại phòng này");
        }

        Account acc = accountService.getAccountByUsername(principal.getName());
        UserProfile user = userProfileService.findUserProfileByAccoutId(acc.getAccount_id())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng"));
        // Lấy thông tin Services
        Set<Services> services = serviceRepository.findAllByServiceIdIn(request.getServiceIds());

        // Tạo Booking mới (temporary, not saved to database yet)
        Booking booking = new Booking();
        booking.setCheck_in_date(request.getCheckInDate());
        booking.setCheck_out_date(request.getCheckOutDate());
        booking.setCheck_in_time(request.getCheckInTime());
        booking.setCheck_out_time(request.getCheckOutTime());
        booking.setNumber_people(request.getNumberPeople());
        booking.setStatus("TEMP"); // Temporary status
        booking.setRoom_type(roomType);
        booking.setUser_profile(user);
        booking.setServices(services);
        booking.setVersion(0L); // Khởi tạo giá trị version để tránh lỗi khi lưu vào DB

        // Tạo ID tạm thời để sử dụng mà không lưu vào database
        Long tempId = (long) (temporaryBookings.size() + 1);
        // Nếu ID này đã tồn tại, tạo ID mới
        while (temporaryBookings.containsKey(tempId)) {
            tempId++;
        }
        booking.setId_booking(tempId);

        logger.info("Creating temporary booking with ID: {} (NOT SAVED TO DATABASE)", tempId);

        // Store in temporary map for further steps
        temporaryBookings.put(tempId, booking);

        // Calculate the total amount for display purposes
        BigDecimal totalAmount = calculateTotalAmount(roomType, services, request);

        // Return response without creating bill yet
        return convertToTempResponseDTO(booking, totalAmount);
    }

    /**
     * Step 2: Update contact information
     */
    @Transactional
    public BookingResponseDTO updateContactInfo(ContactInfoDTO contactInfo) {
        // Get the temporary booking
        Booking booking = temporaryBookings.get(contactInfo.getBookingId());
        if (booking == null) {
            throw new EntityNotFoundException("Không tìm thấy đơn đặt phòng tạm thời");
        }

        logger.info("Updating contact info for temporary booking ID: {} (NOT SAVED TO DATABASE)",
                contactInfo.getBookingId());

        // Update contact information
        booking.setContact_name(contactInfo.getFullName());
        booking.setContact_email(contactInfo.getEmail());
        booking.setContact_phone(contactInfo.getPhone());
        booking.setContact_address(contactInfo.getAddress());
        booking.setSpecial_requests(contactInfo.getSpecialRequests());

        // Update in temporary map, not in database
        temporaryBookings.put(contactInfo.getBookingId(), booking);

        // Calculate total amount for display
        BookingRequestDTO tempRequest = new BookingRequestDTO();
        tempRequest.setCheckInDate(booking.getCheck_in_date());
        tempRequest.setCheckOutDate(booking.getCheck_out_date());
        BigDecimal totalAmount = calculateTotalAmount(booking.getRoom_type(), booking.getServices(), tempRequest);

        // Return updated response
        return convertToTempResponseDTO(booking, totalAmount);
    }

    /**
     * Step 3: Finalize booking - complete the booking process with payment details
     * CHỈ lưu vào database khi thanh toán thành công
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
    public BookingResponseDTO finalizeBooking(Long bookingId) {
        Object lock = bookingLocks.computeIfAbsent(bookingId, k -> new Object());
        synchronized (lock) {
            try {
                logger.info("*** STARTING finalizeBooking for ID: {} ***", bookingId);

                // Kiểm tra xem đã tồn tại booking với trạng thái PAID chưa
                try {
                    Booking existingBooking = bookingRepository.findById(bookingId).orElse(null);
                    if (existingBooking != null && "PAID".equals(existingBooking.getStatus())) {
                        logger.info("Booking already paid with ID: {}, returning existing booking", bookingId);
                        Bill bill = billRepository.findByBookingId(bookingId);
                        return convertToResponseDTO(existingBooking, bill);
                    }
                } catch (Exception e) {
                    logger.info("No existing booking in database, will create new: {}", e.getMessage());
                }

                // Lấy booking từ bộ nhớ tạm
                Booking booking = temporaryBookings.get(bookingId);
                if (booking == null) {
                    logger.error("Booking ID: {} not found in temporary storage", bookingId);
                    throw new EntityNotFoundException("Không tìm thấy đơn đặt phòng tạm thời với ID: " + bookingId);
                }

                try {
                    // Tạo đối tượng mới không liên quan đến persistence context
                    Booking newBooking = new Booking();
                    newBooking.setId_booking(null); // Để cho Hibernate tự tạo ID mới
                    newBooking.setVersion(0L); // Khởi tạo giá trị cho version
                    newBooking.setCheck_in_date(booking.getCheck_in_date());
                    newBooking.setCheck_out_date(booking.getCheck_out_date());
                    newBooking.setCheck_in_time(booking.getCheck_in_time());
                    newBooking.setCheck_out_time(booking.getCheck_out_time());
                    newBooking.setNumber_people(booking.getNumber_people());
                    newBooking.setStatus("PAID");
                    newBooking.setContact_name(booking.getContact_name());
                    newBooking.setContact_email(booking.getContact_email());
                    newBooking.setContact_phone(booking.getContact_phone());
                    newBooking.setContact_address(booking.getContact_address());
                    newBooking.setSpecial_requests(booking.getSpecial_requests());
                    newBooking.setRoom_type(booking.getRoom_type());
                    newBooking.setUser_profile(booking.getUser_profile());
                    newBooking.setServices(booking.getServices());

                    logger.info("Setting booking status to PAID");

                    // Tính toán giá tiền
                    BookingRequestDTO tempRequest = new BookingRequestDTO();
                    tempRequest.setCheckInDate(booking.getCheck_in_date());
                    tempRequest.setCheckOutDate(booking.getCheck_out_date());
                    BigDecimal totalAmount = calculateTotalAmount(booking.getRoom_type(), booking.getServices(),
                            tempRequest);

                    // Lưu booking mới vào database
                    logger.info("SAVING NEW BOOKING TO DATABASE with status PAID");
                    Booking savedBooking = bookingRepository.save(newBooking);
                    logger.info("Successfully saved booking with ID: {}", savedBooking.getId_booking());

                    // Tạo hóa đơn mới
                    Bill bill = new Bill();
                    bill.setTotal_amount(totalAmount);
                    bill.setDeposit(totalAmount.multiply(new BigDecimal("0.3"))); // 30% đặt cọc
                    bill.setPrint_date(LocalDate.now());
                    bill.setPrint_time(LocalTime.now());
                    bill.setBooking(savedBooking);

                    // Save bill
                    logger.info("Saving bill for booking ID: {}", savedBooking.getId_booking());
                    Bill savedBill = billRepository.save(bill);
                    logger.info("Successfully saved bill with ID: {}", savedBill.getBill_id());

                    // Xóa khỏi bộ nhớ tạm
                    logger.info("Removing booking ID: {} from temporary storage", bookingId);
                    temporaryBookings.remove(bookingId);

                    logger.info("*** COMPLETED finalizeBooking with saved ID: {} ***", savedBooking.getId_booking());
                    return convertToResponseDTO(savedBooking, savedBill);
                } catch (Exception e) {
                    logger.error("Error saving booking to database: {}", e.getMessage(), e);
                    throw e;
                }
            } finally {
                bookingLocks.remove(bookingId);
            }
        }
    }

    /**
     * Kiểm tra xem booking có tồn tại trong bộ nhớ tạm không
     */
    public boolean isBookingInTemporaryStorage(Long bookingId) {
        return temporaryBookings.containsKey(bookingId);
    }

    /**
     * Original createBooking method - for backward compatibility (all steps at
     * once)
     * Nghiệp vụ: Xử lý khi người dùng đặt phòng, tạo booking với status PENDING và
     * tạo bill
     */
    @Transactional
    public BookingResponseDTO createBooking(BookingRequestDTO request, Principal principal) {
        // Lấy thông tin RoomType
        RoomType roomType = roomTypeRepository.findById(request.getRoomTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy loại phòng"));

        // Kiểm tra số lượng người
        if (request.getNumberPeople() > roomType.getMaximum_people()) {
            throw new IllegalArgumentException("Số lượng người vượt quá giới hạn của loại phòng này");
        }

        // Lấy thông tin User
        Account acc = accountService.getAccountByUsername(principal.getName());
        UserProfile user = userProfileService.findUserProfileByAccoutId(acc.getAccount_id())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng"));

        // Lấy thông tin Services
        Set<Services> services = serviceRepository.findAllByServiceIdIn(request.getServiceIds());

        // Tạo Booking mới
        Booking booking = new Booking();
        booking.setCheck_in_date(request.getCheckInDate());
        booking.setCheck_out_date(request.getCheckOutDate());
        booking.setCheck_in_time(request.getCheckInTime());
        booking.setCheck_out_time(request.getCheckOutTime());
        booking.setNumber_people(request.getNumberPeople());
        booking.setStatus("PENDING");
        booking.setRoom_type(roomType);
        booking.setUser_profile(user);
        booking.setServices(services);

        // Tính tổng tiền
        BigDecimal totalAmount = calculateTotalAmount(roomType, services, request);

        // Tạo Bill
        Bill bill = new Bill();
        bill.setTotal_amount(totalAmount);
        bill.setDeposit(totalAmount.multiply(new BigDecimal("0.3"))); // 30% đặt cọc
        bill.setPrint_date(LocalDate.now());
        bill.setPrint_time(LocalTime.now());
        bill.setBooking(booking);

        // Lưu Booking
        booking = bookingRepository.save(booking);

        // Lưu Bill
        billRepository.save(bill);

        return convertToResponseDTO(booking, bill);
    }

    /**
     * Tính tổng số tiền cho booking
     * Nghiệp vụ: Tính giá phòng + giá dịch vụ để tạo bill
     */
    private BigDecimal calculateTotalAmount(RoomType roomType, Set<Services> services, BookingRequestDTO request) {
        // Tính số ngày lưu trú
        long days = ChronoUnit.DAYS.between(request.getCheckInDate(), request.getCheckOutDate());
        if (days <= 0) {
            days = 1; // Tối thiểu 1 ngày
        }

        // Tính tiền phòng = giá phòng × số ngày
        BigDecimal roomPrice = roomType.getPrice().multiply(BigDecimal.valueOf(days));

        // Tính tiền dịch vụ = tổng giá các dịch vụ đã chọn
        BigDecimal servicePrice = services.stream()
                .map(Services::getService_price)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Tổng tiền = tiền phòng + tiền dịch vụ
        return roomPrice.add(servicePrice);
    }

    /**
     * Lấy danh sách booking của một người dùng
     * Nghiệp vụ: Hiển thị lịch sử đặt phòng cho người dùng
     */
    public List<BookingResponseDTO> getBookingsByUserId(Long userId) {
        List<Booking> bookings = bookingRepository.findByUserProfileId(userId);
        return bookings.stream()
                .map(booking -> {
                    Bill bill = billRepository.findByBookingId(booking.getId_booking());
                    return convertToResponseDTO(booking, bill);
                })
                .collect(Collectors.toList());
    }

    /**
     * Lấy danh sách booking của một khách sạn
     * Nghiệp vụ: Hiển thị danh sách đặt phòng cho quản lý khách sạn
     */
    public List<BookingResponseDTO> getBookingsByHotelId(Long hotelId) {
        List<Booking> bookings = bookingRepository.findByHotelId(hotelId);
        return bookings.stream()
                .map(booking -> {
                    Bill bill = billRepository.findByBookingId(booking.getId_booking());
                    return convertToResponseDTO(booking, bill);
                })
                .collect(Collectors.toList());
    }

    /**
     * Lấy danh sách booking theo trạng thái của một khách sạn
     * Nghiệp vụ: Hiển thị danh sách đặt phòng theo trạng thái cho quản lý khách sạn
     */
    public List<BookingResponseDTO> getBookingsByHotelIdAndStatus(Long hotelId, String status) {
        List<Booking> bookings = bookingRepository.findByHotelIdAndStatus(hotelId, status);
        return bookings.stream()
                .map(booking -> {
                    Bill bill = billRepository.findByBookingId(booking.getId_booking());
                    return convertToResponseDTO(booking, bill);
                })
                .collect(Collectors.toList());
    }

    /**
     * Cập nhật trạng thái booking
     * Nghiệp vụ: Xác nhận hoặc hủy booking từ phía khách sạn
     */
    @Transactional
    public BookingResponseDTO updateBookingStatus(Long bookingId, String status) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy booking với ID: " + bookingId));

        booking.setStatus(status);
        booking = bookingRepository.save(booking);

        // Nếu xác nhận booking, giảm số lượng phòng trống
        if ("CONFIRMED".equals(status)) {
            roomTypeService.updateAvailableRoom(booking.getRoom_type().getRoom_type_id(), true);
        }
        // Nếu hủy booking đã xác nhận trước đó, tăng số lượng phòng trống
        else if ("CANCELLED".equals(status) && "CONFIRMED".equals(booking.getStatus())) {
            roomTypeService.updateAvailableRoom(booking.getRoom_type().getRoom_type_id(), false);
        }

        Bill bill = billRepository.findByBookingId(bookingId);
        return convertToResponseDTO(booking, bill);
    }

    /**
     * Lấy danh sách booking theo khoảng thời gian và trạng thái
     * Nghiệp vụ: Lọc booking theo ngày check-in, check-out và trạng thái
     */
    public List<BookingResponseDTO> getBookingsByDateRangeAndStatus(
            LocalDate checkInFrom,
            LocalDate checkInTo,
            LocalDate checkOutFrom,
            LocalDate checkOutTo,
            String status) {

        List<Booking> bookings;
        if (status != null && !status.trim().isEmpty() && !"Tất cả".equals(status)) {
            String statusFilter = mapStatusForFilter(status);
            bookings = bookingRepository.findByDateRangeAndStatus(
                    checkInFrom, checkInTo, checkOutFrom, checkOutTo, statusFilter);
        } else {
            bookings = bookingRepository.findByDateRange(
                    checkInFrom, checkInTo, checkOutFrom, checkOutTo);
        }

        return bookings.stream()
                .map(booking -> {
                    Bill bill = billRepository.findByBookingId(booking.getId_booking());
                    return convertToResponseDTO(booking, bill);
                })
                .collect(Collectors.toList());
    }

    /**
     * Chuyển đổi trạng thái hiển thị sang mã trạng thái trong hệ thống
     */
    private String mapStatusForFilter(String statusDisplay) {
        return switch (statusDisplay) {
            case "Chờ xác nhận" -> "PENDING";
            case "Đã xác nhận" -> "CONFIRMED";
            case "Hoàn tất" -> "COMPLETED";
            case "Đã hủy" -> "CANCELLED";
            case "Đã thanh toán" -> "PAID";
            default -> statusDisplay;
        };
    }

    /**
     * Chuyển đổi mã trạng thái trong hệ thống sang trạng thái hiển thị
     */
    private String mapStatusToDisplay(String status) {
        return switch (status) {
            case "PENDING" -> "Chờ xác nhận";
            case "CONFIRMED" -> "Đã xác nhận";
            case "COMPLETED" -> "Hoàn tất";
            case "CANCELLED" -> "Đã hủy";
            case "PAID" -> "Đã thanh toán";
            case "TEMP" -> "Đang nhập";
            default -> status;
        };
    }

    /**
     * Convert to temporary response DTO (for steps 1 and 2)
     */
    private BookingResponseDTO convertToTempResponseDTO(Booking booking, BigDecimal totalAmount) {
        BookingResponseDTO dto = new BookingResponseDTO();
        dto.setBookingId(booking.getId_booking());
        dto.setCheckInDate(booking.getCheck_in_date());
        dto.setCheckOutDate(booking.getCheck_out_date());
        dto.setCheckInTime(booking.getCheck_in_time());
        dto.setCheckOutTime(booking.getCheck_out_time());
        dto.setNumberPeople(booking.getNumber_people());
        dto.setStatus(booking.getStatus());
        dto.setStatusDisplay(mapStatusToDisplay(booking.getStatus()));

        // Contact information
        dto.setContactName(booking.getContact_name());
        dto.setContactEmail(booking.getContact_email());
        dto.setContactPhone(booking.getContact_phone());
        dto.setContactAddress(booking.getContact_address());
        dto.setSpecialRequests(booking.getSpecial_requests());

        // Convert RoomType
        RoomTypeDTO roomTypeDTO = new RoomTypeDTO();
        roomTypeDTO.setRoomTypeId(booking.getRoom_type().getRoom_type_id());
        roomTypeDTO.setTypeName(booking.getRoom_type().getType_name());
        roomTypeDTO.setPrice(booking.getRoom_type().getPrice());
        roomTypeDTO.setNumberBed(booking.getRoom_type().getNumber_bed());
        roomTypeDTO.setMaximumPeople(booking.getRoom_type().getMaximum_people());
        roomTypeDTO.setRoomImage(booking.getRoom_type().getRoom_image());
        dto.setRoomType(roomTypeDTO);

        // Add hotel information
        HotelDTO hotelDTO = new HotelDTO();
        hotelDTO.setHotelId(booking.getRoom_type().getHotel().getHotel_id());
        hotelDTO.setHotelName(booking.getRoom_type().getHotel().getName());
        hotelDTO.setAddress(booking.getRoom_type().getHotel().getAddress());
        hotelDTO.setPhone(booking.getRoom_type().getHotel().getHotline());
        dto.setHotel(hotelDTO);

        // Convert Services
        Set<ServiceDTO> serviceDTOs = booking.getServices().stream().map(service -> {
            ServiceDTO serviceDTO = new ServiceDTO();
            serviceDTO.setServiceId(service.getService_id());
            serviceDTO.setServiceName(service.getService_name());
            serviceDTO.setServicePrice(service.getService_price());
            serviceDTO.setServiceImage(service.getService_image());
            serviceDTO.setDescription(service.getDescription());
            return serviceDTO;
        }).collect(Collectors.toSet());
        dto.setServices(serviceDTOs);

        // Create temporary bill DTO for display
        BillDTO billDTO = new BillDTO();
        billDTO.setTotalAmount(totalAmount);
        billDTO.setDeposit(totalAmount.multiply(new BigDecimal("0.3"))); // 30% deposit
        dto.setBill(billDTO);

        return dto;
    }

    /**
     * Convert from Entity to DTO (final response with complete billing)
     */
    private BookingResponseDTO convertToResponseDTO(Booking booking, Bill bill) {
        BookingResponseDTO dto = new BookingResponseDTO();
        dto.setBookingId(booking.getId_booking());
        dto.setCheckInDate(booking.getCheck_in_date());
        dto.setCheckOutDate(booking.getCheck_out_date());
        dto.setCheckInTime(booking.getCheck_in_time());
        dto.setCheckOutTime(booking.getCheck_out_time());
        dto.setNumberPeople(booking.getNumber_people());
        dto.setStatus(booking.getStatus());
        dto.setStatusDisplay(mapStatusToDisplay(booking.getStatus()));

        // Contact information
        dto.setContactName(booking.getContact_name());
        dto.setContactEmail(booking.getContact_email());
        dto.setContactPhone(booking.getContact_phone());
        dto.setContactAddress(booking.getContact_address());
        dto.setSpecialRequests(booking.getSpecial_requests());

        // Convert RoomType
        RoomTypeDTO roomTypeDTO = new RoomTypeDTO();
        roomTypeDTO.setRoomTypeId(booking.getRoom_type().getRoom_type_id());
        roomTypeDTO.setTypeName(booking.getRoom_type().getType_name());
        roomTypeDTO.setPrice(booking.getRoom_type().getPrice());
        roomTypeDTO.setNumberBed(booking.getRoom_type().getNumber_bed());
        roomTypeDTO.setMaximumPeople(booking.getRoom_type().getMaximum_people());
        roomTypeDTO.setRoomImage(booking.getRoom_type().getRoom_image());
        dto.setRoomType(roomTypeDTO);

        // Add hotel information
        HotelDTO hotelDTO = new HotelDTO();
        hotelDTO.setHotelId(booking.getRoom_type().getHotel().getHotel_id());
        hotelDTO.setHotelName(booking.getRoom_type().getHotel().getName());
        hotelDTO.setAddress(booking.getRoom_type().getHotel().getAddress());
        hotelDTO.setPhone(booking.getRoom_type().getHotel().getHotline());
        dto.setHotel(hotelDTO);

        // Convert Services
        Set<ServiceDTO> serviceDTOs = booking.getServices().stream().map(service -> {
            ServiceDTO serviceDTO = new ServiceDTO();
            serviceDTO.setServiceId(service.getService_id());
            serviceDTO.setServiceName(service.getService_name());
            serviceDTO.setServicePrice(service.getService_price());
            serviceDTO.setServiceImage(service.getService_image());
            serviceDTO.setDescription(service.getDescription());
            return serviceDTO;
        }).collect(Collectors.toSet());
        dto.setServices(serviceDTOs);

        // Convert Bill
        if (bill != null) {
            BillDTO billDTO = new BillDTO();
            billDTO.setBillId(bill.getBill_id());
            billDTO.setTotalAmount(bill.getTotal_amount());
            billDTO.setDeposit(bill.getDeposit());
            billDTO.setPrintDate(bill.getPrint_date());
            billDTO.setPrintTime(bill.getPrint_time());
            dto.setBill(billDTO);
        }

        return dto;
    }

    /**
     * Lấy thông tin chi tiết booking theo ID
     * Nghiệp vụ: Hiển thị thông tin booking
     */
    public BookingResponseDTO getBookingById(Long bookingId) {
        // Kiểm tra trong temporary bookings trước
        if (temporaryBookings.containsKey(bookingId)) {
            Booking booking = temporaryBookings.get(bookingId);

            // Tính toán tổng tiền để hiển thị
            BookingRequestDTO tempRequest = new BookingRequestDTO();
            tempRequest.setCheckInDate(booking.getCheck_in_date());
            tempRequest.setCheckOutDate(booking.getCheck_out_date());
            BigDecimal totalAmount = calculateTotalAmount(booking.getRoom_type(), booking.getServices(), tempRequest);

            return convertToTempResponseDTO(booking, totalAmount);
        }

        // Không tìm thấy trong temporary, tìm trong database
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy booking với ID: " + bookingId));

        Bill bill = billRepository.findByBookingId(bookingId);
        return convertToResponseDTO(booking, bill);
    }
}
