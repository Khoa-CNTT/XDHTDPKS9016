package com.tourism.booking.dto.booking;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tourism.booking.dto.user.UserProfileResponse;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import java.math.BigDecimal;
import java.util.List;

@Data
public class BookingResponseDTO {
    private Long bookingId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private int numberPeople;
    private String status;

    // Thông tin khách sạn
    private HotelDTO hotel;

    // Danh sách phòng đã đặt (thay thế cho roomType vì đã có thông tin chi tiết
    // hơn)
    private List<BookedRoomDTO> rooms;

    // Thông tin dịch vụ
    private Set<ServiceDTO> services;

    // Thông tin hóa đơn
    private BillDTO bill;

    // Thông tin người dùng
    private UserProfileResponse user;

    // Thông tin liên hệ khi đặt phòng
    private String contactName;
    private String contactEmail;
    private String contactPhone;
    private String contactAddress;
    private String specialRequests;

    // Hiển thị trạng thái cho UI
    private String statusDisplay;

    // Trường roomType được giữ lại với @JsonIgnore để tránh ảnh hưởng đến code
    // nghiệp vụ hiện tại
    // nhưng không hiển thị trong response API
    @JsonIgnore
    private RoomTypeDTO roomType;

    // Các trường không cần thiết được đánh dấu JsonIgnore để không hiển thị trong
    // response
    @JsonIgnore
    private List<String> servicesList;

    @JsonIgnore
    private BigDecimal totalAmount;

    @JsonIgnore
    private Long userId;
}