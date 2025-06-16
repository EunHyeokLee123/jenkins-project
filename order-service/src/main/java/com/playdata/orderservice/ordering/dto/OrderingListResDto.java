package com.playdata.orderservice.ordering.dto;

import com.playdata.orderservice.ordering.entity.OrderStatus;
import com.playdata.orderservice.ordering.entity.Ordering;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
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
    private boolean active;

}
