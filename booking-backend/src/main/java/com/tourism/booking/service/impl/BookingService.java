package com.tourism.booking.service.impl;

import com.tourism.booking.dto.booking.BookingRequestDTO;
import com.tourism.booking.dto.booking.BookingResponseDTO;
import com.tourism.booking.dto.booking.ContactInfoDTO;
import com.tourism.booking.dto.booking.RoomBookingRequest;
import com.tourism.booking.dto.booking.RoomTypeDTO;
import com.tourism.booking.dto.booking.HotelDTO;
import com.tourism.booking.dto.booking.ServiceDTO;
import com.tourism.booking.dto.booking.BillDTO;
import com.tourism.booking.dto.booking.BookedRoomDTO;
import com.tourism.booking.dto.user.UserProfileResponse;
import com.tourism.booking.model.Booking;
import com.tourism.booking.model.Room;
import com.tourism.booking.model.RoomType;
import com.tourism.booking.model.UserProfile;
import com.tourism.booking.model.Hotel;
import com.tourism.booking.model.Services;
import com.tourism.booking.model.Account;
import com.tourism.booking.model.Bill;
import com.tourism.booking.model.BookingRoom;
import com.tourism.booking.repository.IBookingRepository;
import com.tourism.booking.repository.IRoomRepository;
import com.tourism.booking.repository.IRoomTypeRepository;
import com.tourism.booking.repository.IHotelRepository;
import com.tourism.booking.repository.IServiceRepository;
import com.tourism.booking.repository.IBillRepository;
import com.tourism.booking.service.IAccountService;
import com.tourism.booking.service.IBookingService;
import com.tourism.booking.service.IUserProfileService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Isolation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.HashSet;
import java.util.Set;
import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.Objects;

@Service
public class BookingService implements IBookingService {

    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);
    private final ConcurrentHashMap<Long, BookingResponseDTO> temporaryBookings = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, BookingRequestDTO> temporaryRequests = new ConcurrentHashMap<>();
    private long tempBookingIdCounter = 1000L;

    @Autowired
    private IBookingRepository bookingRepository;

    @Autowired
    private IUserProfileService userProfileService;

    @Autowired
    private IRoomRepository roomRepository;

    @Autowired
    private IRoomTypeRepository roomTypeRepository;

    @Autowired
    private IHotelRepository hotelRepository;

    @Autowired
    private IServiceRepository serviceRepository;

    @Autowired
    private IBillRepository billRepository;

    @Autowired
    private IAccountService accountService;

    @Override
    public BookingResponseDTO getBookingById(Long id) {
        // First check in temporary storage
        if (temporaryBookings.containsKey(id)) {
            return temporaryBookings.get(id);
        }

        // If not found in temporary storage, check database
        Booking booking = getBookingEntityById(id);
        return convertToResponseDTO(booking);
    }

    @Override
    public Booking getBookingEntityById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + id));
    }

    @Override
    public Booking saveBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    public List<BookingResponseDTO> getBookingsByDateRangeAndStatus(
            LocalDate checkInFrom, LocalDate checkInTo,
            LocalDate checkOutFrom, LocalDate checkOutTo,
            String status) {
        List<Booking> bookings;

        // Filter logic based on provided parameters
        if (status != null && !status.isEmpty()) {
            if (checkInFrom != null && checkInTo != null && checkOutFrom != null && checkOutTo != null) {
                bookings = bookingRepository.findByStatusAndDateRange(
                        status, checkInFrom, checkInTo, checkOutFrom, checkOutTo);
            } else if (checkInFrom != null && checkInTo != null) {
                bookings = bookingRepository.findByStatusAndCheckInDateBetween(
                        status, checkInFrom, checkInTo);
            } else if (checkOutFrom != null && checkOutTo != null) {
                bookings = bookingRepository.findByStatusAndCheckOutDateBetween(
                        status, checkOutFrom, checkOutTo);
            } else {
                bookings = bookingRepository.findByStatus(status);
            }
        } else {
            if (checkInFrom != null && checkInTo != null && checkOutFrom != null && checkOutTo != null) {
                bookings = bookingRepository.findByDateRange(
                        checkInFrom, checkInTo, checkOutFrom, checkOutTo);
            } else if (checkInFrom != null && checkInTo != null) {
                bookings = bookingRepository.findByCheckInDateBetween(checkInFrom, checkInTo);
            } else if (checkOutFrom != null && checkOutTo != null) {
                bookings = bookingRepository.findByCheckOutDateBetween(checkOutFrom, checkOutTo);
            } else {
                bookings = bookingRepository.findAll();
            }
        }

        return convertToResponseDTOList(bookings);
    }

    @Override
    public BookingResponseDTO initializeBooking(BookingRequestDTO request, Principal principal) {
        logger.info("Initializing booking for request: {}", request);

        try {
            Account acc = accountService.getAccountByUsername(principal.getName());
            UserProfile user = userProfileService.findUserProfileByAccoutId(acc.getAccount_id())
                    .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng"));

            BookingResponseDTO bookingResponse = new BookingResponseDTO();
            bookingResponse.setBookingId(generateTemporaryBookingId());
            setBasicBookingInfo(bookingResponse, request);

            // Set user information
            setUserInformation(bookingResponse, user.getUser_id());

            // Calculate prices and set room/service information
            calculatePricesAndSetInfo(bookingResponse, request);

            // Save to temporary storage
            temporaryBookings.put(bookingResponse.getBookingId(), bookingResponse);
            temporaryRequests.put(bookingResponse.getBookingId(), request);

            logger.info("Booking initialized with ID: {}", bookingResponse.getBookingId());
            return bookingResponse;

        } catch (Exception e) {
            logger.error("Error initializing booking: {}", e.getMessage());
            throw new RuntimeException("Failed to initialize booking", e);
        }
    }

    @Override
    public BookingResponseDTO updateContactInfo(ContactInfoDTO contactInfo) {
        logger.info("Updating contact info for booking: {}", contactInfo.getBookingId());

        BookingResponseDTO booking = temporaryBookings.get(contactInfo.getBookingId());
        if (booking == null) {
            throw new EntityNotFoundException(
                    "Booking not found in temporary storage with id: " + contactInfo.getBookingId());
        }

        try {
            // Update contact information
            booking.setContactName(contactInfo.getContactName());
            booking.setContactEmail(contactInfo.getContactEmail());
            booking.setContactPhone(contactInfo.getContactPhone());
            booking.setContactAddress(contactInfo.getContactAddress());
            booking.setSpecialRequests(contactInfo.getSpecialRequests());
            booking.setStatus("CONTACT_INFO_ADDED");

            // Update in temporary storage
            temporaryBookings.put(contactInfo.getBookingId(), booking);

            logger.info("Contact info updated for booking: {}", contactInfo.getBookingId());
            return booking;
        } catch (Exception e) {
            logger.error("Error updating contact info: {}", e.getMessage());
            throw new RuntimeException("Failed to update contact information", e);
        }
    }

    @Override
    public List<BookingResponseDTO> getBookingsByUserId(Long userId) {
        List<Booking> bookings = bookingRepository.findByUserProfileUserId(userId);
        return convertToResponseDTOList(bookings);
    }

    @Override
    public List<BookingResponseDTO> getBookingsByHotelId(Long hotelId) {
        List<Booking> bookings = bookingRepository.findByRoomRoomTypeHotelHotelId(hotelId);
        return convertToResponseDTOList(bookings);
    }

    @Override
    public List<BookingResponseDTO> getBookingsByHotelIdAndStatus(Long hotelId, String status) {
        List<Booking> bookings = bookingRepository.findByRoomRoomTypeHotelHotelIdAndStatus(hotelId, status);
        return convertToResponseDTOList(bookings);
    }

    @Override
    @Transactional
    public BookingResponseDTO updateBookingStatus(Long id, String status) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + id));

        booking.setStatus(status);
        Booking updatedBooking = bookingRepository.save(booking);

        return convertToResponseDTO(updatedBooking);
    }

    @Override
    public boolean isBookingInTemporaryStorage(Long bookingId) {
        return temporaryBookings.containsKey(bookingId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    public BookingResponseDTO finalizeBooking(Long bookingId, Principal principal) {
        logger.info("Finalizing booking: {}", bookingId);

        try {
            BookingResponseDTO tempBooking = temporaryBookings.get(bookingId);
            BookingRequestDTO originalRequest = temporaryRequests.get(bookingId);

            if (tempBooking == null || originalRequest == null) {
                throw new EntityNotFoundException("Booking not found in temporary storage with id: " + bookingId);
            }

            // 1. Create and save booking entity first
            Booking booking = createBookingEntity(tempBooking, originalRequest, principal);
            booking.setVersion(0L); // Initialize version for optimistic locking
            booking = bookingRepository.saveAndFlush(booking);
            logger.info("Booking entity saved with ID: {}", booking.getId_booking());

            // 2. Create bill with the correct booking reference
            Bill bill = new Bill();
            bill.setBooking(booking);
            bill.setTotal_amount(tempBooking.getBill().getTotal());
            bill.setDeposit(tempBooking.getBill().getDeposit());
            bill.setPrint_date(LocalDate.now());
            bill.setPrint_time(LocalTime.now());
            bill.setVersion(0L);

            // Save bill and handle potential optimistic locking exceptions
            try {
                bill = billRepository.saveAndFlush(bill);
                logger.info("Bill created successfully with ID: {}", bill.getBill_id());
            } catch (Exception e) {
                logger.error("Error saving bill: {}", e.getMessage());
                // If bill already exists, try to get and update it
                Bill existingBill = billRepository.findByBookingId(booking.getId_booking());
                if (existingBill != null) {
                    existingBill.setTotal_amount(bill.getTotal_amount());
                    existingBill.setDeposit(bill.getDeposit());
                    existingBill.setPrint_date(bill.getPrint_date());
                    existingBill.setPrint_time(bill.getPrint_time());
                    bill = billRepository.saveAndFlush(existingBill);
                    logger.info("Existing bill updated with ID: {}", bill.getBill_id());
                } else {
                    throw new RuntimeException("Failed to create or update bill", e);
                }
            }

            // 3. Clean up temporary storage
            temporaryBookings.remove(bookingId);
            temporaryRequests.remove(bookingId);
            logger.info("Temporary storage cleaned up for booking: {}", bookingId);

            // 4. Return response with complete booking information
            BookingResponseDTO response = convertToResponseDTO(booking);
            logger.info("Booking finalized successfully: {}", bookingId);
            return response;

        } catch (Exception e) {
            logger.error("Error finalizing booking: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to finalize booking", e);
        }
    }

    @Override
    @Transactional
    public BookingResponseDTO createBooking(BookingRequestDTO bookingRequest) {
        Booking booking = new Booking();
        booking.setCheck_in_date(bookingRequest.getCheckInDate());
        booking.setCheck_out_date(bookingRequest.getCheckOutDate());
        booking.setCheck_in_time(
                bookingRequest.getCheckInTime() != null ? bookingRequest.getCheckInTime() : LocalTime.of(14, 0));
        booking.setCheck_out_time(
                bookingRequest.getCheckOutTime() != null ? bookingRequest.getCheckOutTime() : LocalTime.of(12, 0));
        booking.setNumber_people(bookingRequest.getNumberOfPeople());
        booking.setStatus("PENDING");
        booking.setContact_name(bookingRequest.getContactName());
        booking.setContact_email(bookingRequest.getContactEmail());
        booking.setContact_phone(bookingRequest.getContactPhone());
        booking.setContact_address(bookingRequest.getContactAddress());
        booking.setSpecial_requests(bookingRequest.getSpecialRequests());

        if (bookingRequest.getUserId() != null) {
            UserProfile user = userProfileService.findUserProfileEntityById(bookingRequest.getUserId())
                    .orElseThrow(
                            () -> new EntityNotFoundException("User not found with id: " + bookingRequest.getUserId()));
            booking.setUser_profile(user);
        }

        booking = bookingRepository.save(booking);

        // Process room selections - FIXED to use correct numberOfRooms
        for (com.tourism.booking.dto.booking.RoomSelectionDTO roomSelection : bookingRequest.getRoomSelections()) {
            RoomType roomType = roomTypeRepository.findById(roomSelection.getRoomTypeId())
                    .orElseThrow(
                            () -> new EntityNotFoundException("Room type not found: " + roomSelection.getRoomTypeId()));

            List<Room> availableRooms = roomRepository.findAvailableRoomsByTypeAndDateRange(
                    roomType.getRoom_type_id(),
                    bookingRequest.getCheckInDate(),
                    bookingRequest.getCheckOutDate(),
                    1); // Just need one room of this type

            if (availableRooms.isEmpty()) {
                throw new RuntimeException("Not enough rooms available");
            }

            // Create a single booking_room entry with the correct numberOfRooms
            BookingRoom bookingRoom = new BookingRoom();
            bookingRoom.setBooking(booking);
            bookingRoom.setRoom(availableRooms.get(0)); // Use the first available room
            bookingRoom.setNumberOfRooms(roomSelection.getNumberOfRooms()); // Set the actual number of rooms
            bookingRoom.setRoomTypeId(roomType.getRoom_type_id());

            booking.getBookingRooms().add(bookingRoom);
        }

        booking = bookingRepository.save(booking);
        return convertToResponseDTO(booking);
    }

    private void setBasicBookingInfo(BookingResponseDTO booking, BookingRequestDTO request) {
        booking.setCheckInDate(request.getCheckInDate());
        booking.setCheckOutDate(request.getCheckOutDate());
        booking.setCheckInTime(request.getCheckInTime());
        booking.setCheckOutTime(request.getCheckOutTime());
        booking.setNumberPeople(request.getNumberOfPeople());
        booking.setStatus("PENDING");
    }

    private void calculatePricesAndSetInfo(BookingResponseDTO booking, BookingRequestDTO request) {
        BigDecimal totalRoomPrice = BigDecimal.ZERO;
        int numberOfDays = calculateNumberOfDays(request.getCheckInDate(), request.getCheckOutDate());
        List<BookedRoomDTO> bookedRooms = new ArrayList<>();

        // Process room selections
        if (request.getRoomSelections() != null && !request.getRoomSelections().isEmpty()) {
            for (com.tourism.booking.dto.booking.RoomSelectionDTO roomSelection : request.getRoomSelections()) {
                try {
                    // Get room type information
                    RoomType roomType = roomTypeRepository.findById(roomSelection.getRoomTypeId())
                            .orElseThrow(() -> new EntityNotFoundException(
                                    "Room type not found: " + roomSelection.getRoomTypeId()));

                    // Get a sample room for this room type to get price
                    List<Room> rooms = roomRepository.findByRoomTypeRoomTypeId(roomType.getRoom_type_id());
                    if (rooms.isEmpty()) {
                        throw new EntityNotFoundException(
                                "No rooms found for room type: " + roomType.getRoom_type_id());
                    }

                    Room sampleRoom = rooms.get(0);
                    BigDecimal roomPrice = sampleRoom.getPrice();
                    BigDecimal roomTotalPrice = roomPrice
                            .multiply(BigDecimal.valueOf(roomSelection.getNumberOfRooms()));

                    // Add to total room price
                    totalRoomPrice = totalRoomPrice.add(roomTotalPrice);

                    // Create room DTO for response
                    BookedRoomDTO bookedRoomDTO = new BookedRoomDTO();
                    bookedRoomDTO.setRoomId(sampleRoom.getId_room());
                    bookedRoomDTO.setRoomTypeId(roomType.getRoom_type_id());
                    bookedRoomDTO.setRoomTypeName(roomType.getType_name());
                    bookedRoomDTO.setNumberOfRooms(roomSelection.getNumberOfRooms());
                    bookedRoomDTO.setNumberBeds(sampleRoom.getNumber_bed());
                    bookedRoomDTO.setPricePerRoom(roomPrice);
                    bookedRoomDTO.setTotalPrice(roomTotalPrice);

                    bookedRooms.add(bookedRoomDTO);

                    // Set room type for the booking (using the first room type if multiple)
                    if (booking.getRoomType() == null) {
                        RoomTypeDTO roomTypeDTO = convertToRoomTypeDTO(roomType);
                        booking.setRoomType(roomTypeDTO);

                        // Set hotel information
                        if (roomType.getHotel() != null) {
                            HotelDTO hotelDTO = convertToHotelDTO(roomType.getHotel());
                            booking.setHotel(hotelDTO);
                        }
                    }
                } catch (Exception e) {
                    logger.error("Error processing room selection: {}", e.getMessage());
                }
            }
        }

        // Set the booked rooms
        booking.setRooms(bookedRooms);

        // Process services
        BigDecimal serviceTotal = BigDecimal.ZERO;
        if (request.getServiceIds() != null && !request.getServiceIds().isEmpty()) {
            Set<Long> serviceSet = new HashSet<>(request.getServiceIds());
            serviceTotal = calculateServicesTotal(booking, serviceSet);
        }

        // Set bill information
        // Multiply room price by the number of days
        BigDecimal roomTotal = totalRoomPrice.multiply(BigDecimal.valueOf(numberOfDays));
        setBillInformation(booking, roomTotal, serviceTotal, numberOfDays);
    }

    private void setRoomAndHotelInfo(BookingResponseDTO booking, Long roomId, BigDecimal roomPrice, int numberOfDays) {
        try {
            Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new EntityNotFoundException("Room not found with id: " + roomId));

            roomPrice = room.getPrice();
            numberOfDays = calculateNumberOfDays(booking.getCheckInDate(), booking.getCheckOutDate());

            RoomType roomType = room.getRoom_type();
            if (roomType != null) {
                booking.setRoomType(convertToRoomTypeDTO(roomType));

                Hotel hotel = roomType.getHotel();
                if (hotel != null) {
                    booking.setHotel(convertToHotelDTO(hotel));
                }
            }
        } catch (Exception e) {
            logger.error("Error setting room and hotel info: {}", e.getMessage());
            throw new RuntimeException("Failed to set room and hotel information", e);
        }
    }

    // Helper method to generate temporary booking ID
    private Long generateTemporaryBookingId() {
        return tempBookingIdCounter++;
    }

    // Helper method to convert Booking entity to BookingResponseDTO
    private BookingResponseDTO convertToResponseDTO(Booking booking) {
        if (booking == null)
            return null;

        BookingResponseDTO dto = new BookingResponseDTO();
        dto.setBookingId(booking.getId_booking());
        dto.setCheckInDate(booking.getCheck_in_date());
        dto.setCheckOutDate(booking.getCheck_out_date());
        dto.setCheckInTime(booking.getCheck_in_time());
        dto.setCheckOutTime(booking.getCheck_out_time());
        dto.setNumberPeople(booking.getNumber_people());
        dto.setStatus(booking.getStatus());
        dto.setContactName(booking.getContact_name());
        dto.setContactEmail(booking.getContact_email());
        dto.setContactPhone(booking.getContact_phone());
        dto.setContactAddress(booking.getContact_address());
        dto.setSpecialRequests(booking.getSpecial_requests());

        // Create list of booked rooms from BookingRoom entities
        List<BookedRoomDTO> bookedRooms = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        if (booking.getBookingRooms() != null && !booking.getBookingRooms().isEmpty()) {
            for (BookingRoom bookingRoom : booking.getBookingRooms()) {
                Room room = bookingRoom.getRoom();
                if (room != null && room.getRoom_type() != null) {
                    RoomType roomType = room.getRoom_type();

                    // Create BookedRoomDTO
                    BookedRoomDTO bookedRoomDTO = new BookedRoomDTO();
                    bookedRoomDTO.setRoomId(room.getId_room());
                    bookedRoomDTO.setRoomTypeId(roomType.getRoom_type_id());
                    bookedRoomDTO.setRoomTypeName(roomType.getType_name());
                    bookedRoomDTO.setNumberOfRooms(bookingRoom.getNumberOfRooms());
                    bookedRoomDTO.setNumberBeds(room.getNumber_bed());
                    bookedRoomDTO.setPricePerRoom(room.getPrice());

                    // Calculate total price for this room
                    BigDecimal roomTotal = room.getPrice()
                            .multiply(BigDecimal.valueOf(bookingRoom.getNumberOfRooms()));
                    bookedRoomDTO.setTotalPrice(roomTotal);

                    // Add to total amount
                    totalAmount = totalAmount.add(roomTotal);

                    // Add to rooms list
                    bookedRooms.add(bookedRoomDTO);

                    // Set room type and hotel info from first room
                    if (dto.getRoomType() == null) {
                        RoomTypeDTO roomTypeDTO = convertToRoomTypeDTO(roomType);
                        dto.setRoomType(roomTypeDTO);

                        if (roomType.getHotel() != null) {
                            HotelDTO hotelDTO = convertToHotelDTO(roomType.getHotel());
                            dto.setHotel(hotelDTO);
                        }
                    }
                }
            }
        }

        // Set the rooms list in the DTO
        dto.setRooms(bookedRooms);
        dto.setTotalAmount(totalAmount);

        // Add services information
        Set<Services> bookingServices = getServicesByBookingId(booking.getId_booking());
        if (bookingServices != null && !bookingServices.isEmpty()) {
            Set<ServiceDTO> serviceDTOs = bookingServices.stream()
                    .map(this::convertToServiceDTO)
                    .collect(Collectors.toSet());
            dto.setServices(serviceDTOs);
        }

        // Add bill information
        Bill bill = billRepository.findByBookingId(booking.getId_booking());
        if (bill != null) {
            BillDTO billDTO = new BillDTO();
            billDTO.setBillId(bill.getBill_id());
            billDTO.setTotal(bill.getTotal_amount());
            billDTO.setDeposit(bill.getDeposit());

            // Calculate service total from actual services
            BigDecimal serviceTotal = BigDecimal.ZERO;
            if (bookingServices != null) {
                serviceTotal = bookingServices.stream()
                        .map(Services::getService_price)
                        .filter(Objects::nonNull)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
            }
            billDTO.setServiceTotal(serviceTotal);

            // Room total is the difference between total and service total
            BigDecimal roomTotal = bill.getTotal_amount().subtract(serviceTotal);
            billDTO.setRoomTotal(roomTotal);

            // Calculate number of days
            if (booking.getCheck_in_date() != null && booking.getCheck_out_date() != null) {
                int days = (int) java.time.temporal.ChronoUnit.DAYS.between(
                        booking.getCheck_in_date(), booking.getCheck_out_date());
                billDTO.setNumberOfDays(Math.max(1, days));
            } else {
                billDTO.setNumberOfDays(1);
            }

            dto.setBill(billDTO);
        } else {
            // Create a minimal bill with booking ID
            BillDTO billDTO = new BillDTO();
            billDTO.setBillId(booking.getId_booking());
            dto.setBill(billDTO);
        }

        // Add user profile information if available
        if (booking.getUser_profile() != null) {
            try {
                // Try to get UserProfileResponse from the service
                Optional<UserProfileResponse> userProfileResponse = userProfileService
                        .findById(booking.getUser_profile().getUser_id());
                if (userProfileResponse.isPresent()) {
                    dto.setUser(userProfileResponse.get());
                } else {
                    // If response not available, create a simple one from entity
                    UserProfileResponse userResponse = new UserProfileResponse();
                    userResponse.setUser_id(booking.getUser_profile().getUser_id());
                    userResponse.setFull_name(booking.getUser_profile().getFull_name());
                    userResponse.setEmail(booking.getUser_profile().getEmail());
                    userResponse.setPhone(booking.getUser_profile().getPhone());
                    dto.setUser(userResponse);
                }
            } catch (Exception e) {
                // Log error but continue without user information
                System.err.println("Error adding user information: " + e.getMessage());
            }
        }

        // Set statusDisplay for UI
        switch (booking.getStatus()) {
            case "PENDING":
                dto.setStatusDisplay("Chờ xác nhận");
                break;
            case "CONFIRMED":
                dto.setStatusDisplay("Đã xác nhận");
                break;
            case "COMPLETED":
                dto.setStatusDisplay("Hoàn tất");
                break;
            case "CANCELLED":
                dto.setStatusDisplay("Đã hủy");
                break;
            case "PAID":
                dto.setStatusDisplay("Đã thanh toán");
                break;
            default:
                dto.setStatusDisplay(booking.getStatus());
        }

        return dto;
    }

    // Helper method to convert List of Booking entities to List of
    // BookingResponseDTOs
    private List<BookingResponseDTO> convertToResponseDTOList(List<Booking> bookings) {
        List<BookingResponseDTO> dtoList = new ArrayList<>();
        for (Booking booking : bookings) {
            dtoList.add(convertToResponseDTO(booking));
        }
        return dtoList;
    }

    // Helper methods to convert entities to DTOs
    private RoomTypeDTO convertToRoomTypeDTO(RoomType roomType) {
        RoomTypeDTO dto = new RoomTypeDTO();
        dto.setId(roomType.getRoom_type_id());
        dto.setName(roomType.getType_name());
        dto.setDescription(roomType.getDescription());

        // Get room price from associated room
        List<Room> rooms = roomRepository.findByRoomType(roomType);
        if (!rooms.isEmpty()) {
            // Get min and max price
            BigDecimal minPrice = rooms.stream()
                    .map(Room::getPrice)
                    .filter(Objects::nonNull)
                    .min(BigDecimal::compareTo)
                    .orElse(BigDecimal.ZERO);

            BigDecimal maxPrice = rooms.stream()
                    .map(Room::getPrice)
                    .filter(Objects::nonNull)
                    .max(BigDecimal::compareTo)
                    .orElse(BigDecimal.ZERO);

            dto.setMinPrice(minPrice);
            dto.setMaxPrice(maxPrice);
            dto.setPrice(minPrice); // Set default price as min price

            // Set occupancy and room counts
            dto.setMaxOccupancy(rooms.stream()
                    .mapToInt(Room::getNumber_bed)
                    .max()
                    .orElse(0));

            dto.setTotalRoomCount(rooms.size());
            dto.setAvailableRoomCount((int) rooms.stream()
                    .filter(r -> "AVAILABLE".equals(r.getStatus()))
                    .count());

            // Convert available rooms to RoomDTO objects
            List<RoomTypeDTO.RoomDTO> availableRooms = rooms.stream()
                    .filter(r -> "AVAILABLE".equals(r.getStatus()))
                    .map(room -> {
                        RoomTypeDTO.RoomDTO roomDTO = new RoomTypeDTO.RoomDTO();
                        roomDTO.setId(room.getId_room());
                        roomDTO.setNumberBed(room.getNumber_bed());
                        roomDTO.setPrice(room.getPrice());
                        roomDTO.setStatus(room.getStatus());
                        return roomDTO;
                    })
                    .collect(Collectors.toList());
            dto.setAvailableRooms(availableRooms);
        }

        // Set average rating if available
        // You may need to implement this based on your rating system
        dto.setAverageRating(4.5); // Example value, implement actual rating calculation

        return dto;
    }

    private HotelDTO convertToHotelDTO(Hotel hotel) {
        HotelDTO dto = new HotelDTO();
        dto.setHotelId(hotel.getHotel_id());
        dto.setName(hotel.getName());
        dto.setAddress(hotel.getAddress());
        dto.setPhone(hotel.getHotline());
        dto.setDescription(hotel.getDescription());

        // Set email from account if available
        if (hotel.getAccount() != null) {
            dto.setEmail(hotel.getAccount().getEmail());
        }

        return dto;
    }

    private ServiceDTO convertToServiceDTO(Services service) {
        ServiceDTO dto = new ServiceDTO();
        dto.setId(service.getService_id());
        dto.setName(service.getService_name());
        dto.setDescription(service.getDescription());
        dto.setPrice(service.getService_price());
        return dto;
    }

    @Override
    public List<BookingResponseDTO> getRecentBookings(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<Booking> bookings = bookingRepository.findRecentBookings(pageable);
        return convertToResponseDTOList(bookings);
    }

    private void setUserInformation(BookingResponseDTO booking, Long userId) {
        try {
            Optional<UserProfile> userProfileEntity = userProfileService.findUserProfileEntityById(userId);

            if (userProfileEntity.isPresent()) {
                UserProfile profile = userProfileEntity.get();
                UserProfileResponse userResponse = new UserProfileResponse();
                userResponse.setUser_id(profile.getUser_id());
                userResponse.setFull_name(profile.getFull_name());
                userResponse.setGender(profile.getGender());
                userResponse.setAddress(profile.getAddress());
                userResponse.setEmail(profile.getEmail());
                userResponse.setPhone(profile.getPhone());
                userResponse.setBirth_date(profile.getBirth_date());
                userResponse.setStatus(profile.getStatus());

                if (profile.getAccount() != null) {
                    userResponse.setUsername(profile.getAccount().getUsername());
                }

                booking.setUser(userResponse);
            } else {
                // Fallback to minimal user info
                UserProfileResponse minimalUser = new UserProfileResponse();
                minimalUser.setUser_id(userId);
                minimalUser.setFull_name("User #" + userId);
                booking.setUser(minimalUser);
            }
        } catch (Exception e) {
            logger.error("Error setting user information: {}", e.getMessage());
            throw new RuntimeException("Failed to set user information", e);
        }
    }

    private Booking createBookingEntity(BookingResponseDTO tempBooking, BookingRequestDTO request,
            Principal principal) {
        Booking booking = new Booking();

        // Set basic booking info
        booking.setCheck_in_date(request.getCheckInDate());
        booking.setCheck_out_date(request.getCheckOutDate());
        booking.setCheck_in_time(request.getCheckInTime());
        booking.setCheck_out_time(request.getCheckOutTime());
        booking.setNumber_people(request.getNumberOfPeople());
        booking.setStatus("PENDING");
        booking.setContact_name(tempBooking.getContactName());
        booking.setContact_email(tempBooking.getContactEmail());
        booking.setContact_phone(tempBooking.getContactPhone());
        booking.setContact_address(tempBooking.getContactAddress());
        booking.setSpecial_requests(tempBooking.getSpecialRequests());

        // Set user
        if (principal != null) {
            Account acc = accountService.getAccountByUsername(principal.getName());
            UserProfile user = userProfileService.findUserProfileByAccoutId(acc.getAccount_id())
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
            booking.setUser_profile(user);
        }

        booking = bookingRepository.save(booking);

        // Handle rooms - SỬA LẠI LOGIC ĐỂ KHÔNG CẦN TÌM PHÒNG TRỐNG
        if (request.getRoomSelections() != null) {
            for (com.tourism.booking.dto.booking.RoomSelectionDTO roomSelection : request.getRoomSelections()) {
                try {
                    // Lấy thông tin RoomType
                    RoomType roomType = roomTypeRepository.findById(roomSelection.getRoomTypeId())
                            .orElseThrow(() -> new EntityNotFoundException(
                                    "Room type not found: " + roomSelection.getRoomTypeId()));

                    // Lấy một phòng bất kỳ thuộc loại này (không cần kiểm tra tính khả dụng)
                    List<Room> rooms = roomRepository.findByRoomTypeRoomTypeId(roomType.getRoom_type_id());
                    if (rooms.isEmpty()) {
                        throw new RuntimeException("No rooms found for type: " + roomType.getType_name());
                    }

                    // Chọn phòng đầu tiên trong danh sách
                    Room room = rooms.get(0);

                    // Tạo BookingRoom
                    BookingRoom bookingRoom = new BookingRoom();
                    bookingRoom.setBooking(booking);
                    bookingRoom.setRoom(room);
                    bookingRoom.setNumberOfRooms(roomSelection.getNumberOfRooms());
                    bookingRoom.setRoomTypeId(roomType.getRoom_type_id());

                    booking.getBookingRooms().add(bookingRoom);

                    logger.info("Added room to booking: roomId={}, roomType={}, numberOfRooms={}",
                            room.getId_room(), roomType.getType_name(), roomSelection.getNumberOfRooms());
                } catch (Exception e) {
                    logger.error("Error adding room to booking: {}", e.getMessage());
                    throw new RuntimeException("Error adding room to booking: " + e.getMessage(), e);
                }
            }
        } else if (tempBooking.getRooms() != null && !tempBooking.getRooms().isEmpty()) {
            // Fallback: Sử dụng thông tin phòng từ tempBooking nếu không có trong request
            for (BookedRoomDTO bookedRoom : tempBooking.getRooms()) {
                try {
                    Room room = roomRepository.findById(bookedRoom.getRoomId())
                            .orElseThrow(
                                    () -> new EntityNotFoundException("Room not found: " + bookedRoom.getRoomId()));

                    BookingRoom bookingRoom = new BookingRoom();
                    bookingRoom.setBooking(booking);
                    bookingRoom.setRoom(room);
                    bookingRoom.setNumberOfRooms(bookedRoom.getNumberOfRooms());
                    bookingRoom.setRoomTypeId(bookedRoom.getRoomTypeId());

                    booking.getBookingRooms().add(bookingRoom);

                    logger.info("Added room from tempBooking: roomId={}, numberOfRooms={}",
                            room.getId_room(), bookedRoom.getNumberOfRooms());
                } catch (Exception e) {
                    logger.error("Error adding room from tempBooking: {}", e.getMessage());
                    throw new RuntimeException("Error adding room from tempBooking: " + e.getMessage(), e);
                }
            }
        }

        return booking;
    }

    private BigDecimal calculateServicesTotal(BookingResponseDTO booking, Set<Long> serviceIds) {
        BigDecimal serviceTotal = BigDecimal.ZERO;
        Set<ServiceDTO> serviceDTOs = new HashSet<>();

        for (Long serviceId : serviceIds) {
            Services service = serviceRepository.findById(serviceId)
                    .orElseThrow(() -> new EntityNotFoundException("Service not found: " + serviceId));

            ServiceDTO serviceDTO = convertToServiceDTO(service);
            serviceDTOs.add(serviceDTO);

            if (service.getService_price() != null) {
                serviceTotal = serviceTotal.add(service.getService_price());
            }
        }

        booking.setServices(serviceDTOs);
        return serviceTotal;
    }

    private void setBillInformation(BookingResponseDTO booking, BigDecimal roomPrice, BigDecimal serviceTotal,
            int numberOfDays) {
        BillDTO bill = new BillDTO();
        BigDecimal roomTotal = roomPrice.multiply(BigDecimal.valueOf(Math.max(1, numberOfDays)));
        bill.setRoomTotal(roomTotal);
        bill.setServiceTotal(serviceTotal);
        BigDecimal total = roomTotal.add(serviceTotal);
        bill.setTotal(total);
        bill.setNumberOfDays(numberOfDays);
        BigDecimal deposit = total.multiply(new BigDecimal("0.3"));
        bill.setDeposit(deposit);
        bill.setBillId(generateTemporaryBookingId());
        booking.setBill(bill);
    }

    private int calculateNumberOfDays(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn != null && checkOut != null) {
            return Math.max(1, (int) java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut));
        }
        return 1;
    }

    @Override
    public Set<Services> getServicesByBookingId(Long bookingId) {
        // First check in temporary storage
        if (temporaryBookings.containsKey(bookingId)) {
            BookingResponseDTO tempBooking = temporaryBookings.get(bookingId);
            return serviceRepository.findAllByServiceIdIn(
                    tempBooking.getServices().stream()
                            .map(ServiceDTO::getId)
                            .collect(Collectors.toSet()));
        }

        // If not in temporary storage, check database
        Booking booking = getBookingEntityById(bookingId);
        return booking.getServices();
    }
}
