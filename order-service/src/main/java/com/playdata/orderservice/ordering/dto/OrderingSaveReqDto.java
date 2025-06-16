package com.playdata.orderservice.ordering.dto;


import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrderingSaveReqDto {
    private Long productId;
}
