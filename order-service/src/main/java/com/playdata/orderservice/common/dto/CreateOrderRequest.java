package com.playdata.orderservice.common.dto;

import com.playdata.orderservice.ordering.dto.OrderingSaveReqDto;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {
    private String email;
    private List<OrderingSaveReqDto> dtoList;
}