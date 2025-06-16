package com.playdata.evalservice.eval.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductRatingAvgDto<T> {
    private Long productId;
    private Double averageRating;
}

