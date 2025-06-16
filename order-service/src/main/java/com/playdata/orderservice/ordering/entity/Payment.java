package com.playdata.orderservice.ordering.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 주문과의 관계
    @OneToOne
    @JoinColumn(name = "order_id")
    private Ordering ordering;

    //카카오페이에 보낸 UUID 형태의 주문번호 (조회용으로 사용)
    private String partnerOrderId;

    // 결제 고유 번호 (예: 카카오페이 tid)
    private String tid;

    private LocalDateTime paymentDate;

    private boolean success;
}
