package com.playdata.evalservice.eval.dto;

import com.playdata.evalservice.eval.entity.Eval;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString @Builder
public class EvalSaveReqDto {

    private Long productId;

    private String content;

    private double rating;


    public Eval toEntity(Long userId){
        return Eval.builder()
                .userId(userId)
                .productId(productId)
                .content(content)
                .rating(rating)
                .build();
    }
}
