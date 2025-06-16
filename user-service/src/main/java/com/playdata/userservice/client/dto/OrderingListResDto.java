package com.playdata.userservice.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderingListResDto {

    // 하나의 주문에 대한 내용
    private Long id;
    private String userEmail;
    private Long userId;
    private OrderStatus orderStatus;
    private Long productId;
    private String productName;
    private LocalDate orderDate;
    private String category;
    private String filePath;
}
