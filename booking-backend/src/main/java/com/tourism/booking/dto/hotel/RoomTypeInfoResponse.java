package com.tourism.booking.dto.hotel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class RoomTypeInfoResponse {
//    String typeName;
//    double price;
//}

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomTypeInfoResponse {
    private Long roomTypeId;
    private String typeName;
    private String description;
    private String roomImage;
    private int numberRoom;
    private BigDecimal averagePrice;
    private List<RoomInfoResponse> rooms;
}
