package com.playdata.orderservice.ordering.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@ToString
@Table(name = "order_tbl")
public class Ordering {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 생성
    @Column(name = "order_id")
    private Long id;

    @JoinColumn
    private Long userId;

    private String userEmail;

    private Long productId;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private OrderStatus orderStatus = OrderStatus.ORDERED;

    private LocalDate orderDate;

    public void updateStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

//    @OneToOne(mappedBy = "ordering", cascade = CascadeType.ALL)
//    private Payment payment;

}
