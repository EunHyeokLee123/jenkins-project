package com.playdata.evalservice.eval.dto;


import lombok.*;

@Getter @Setter
@ToString @Builder
@Data
@AllArgsConstructor
public class EvalRateLenDto {

    private Long prodId;
    private Long evalCount;
    private double averageRating;

}
