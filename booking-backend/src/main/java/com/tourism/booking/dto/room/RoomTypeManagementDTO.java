package com.tourism.booking.dto.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomTypeManagementDTO {
    private Long roomTypeId;
    private String typeName;
    private String description;
    private String roomImage;
    private int totalRoomsInType; // Tổng số phòng thuộc loại này
    private int bookedRoomsCount; // Số phòng đã đặt
    private int availableRoomsCount; // Số phòng còn trống
    private BigDecimal averagePrice; // Giá trung bình của loại phòng
    private List<RoomDetailDTO> rooms; // Chi tiết các phòng thuộc loại này
}