package com.playdata.evalservice.eval.dto;

import com.playdata.evalservice.eval.entity.Eval;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class EvalResDto {

    private Long evalId;

    private Long userId;

    private Long productId;

    private String content;

    private double rating;

}
