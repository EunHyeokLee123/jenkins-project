package com.playdata.orderservice.ordering.dto;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ProdDetailResDto {
    private Long productId;
    private String productName;
    private String prodDesc;
    private int price;
    private String category;
    private String filePath;
    private boolean active;
    private Long userId;
    //active, userid
}
