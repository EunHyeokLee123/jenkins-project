package com.playdata.evalservice.eval.entity;


import com.playdata.evalservice.common.entity.BaseTimeEntity;
import com.playdata.evalservice.eval.dto.EvalResDto;
import jakarta.persistence.*;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Setter
@Table(name="tbl_eval")
public class Eval extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long evalId;

    @JoinColumn
    private Long userId;

    @JoinColumn
    private Long productId;

    private String content;

    private double rating;

    public EvalResDto fromEntity() {
        return EvalResDto.builder()
                .evalId(evalId)
                .rating(rating)
                .content(content)
                .productId(productId)
                .userId(userId)
                .build();
    }

}
