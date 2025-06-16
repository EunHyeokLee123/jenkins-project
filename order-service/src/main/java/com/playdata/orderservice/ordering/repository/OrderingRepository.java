package com.playdata.orderservice.ordering.repository;

import com.playdata.orderservice.ordering.entity.Ordering;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderingRepository extends JpaRepository<Ordering, Long> {

    List<Ordering> findByUserId(Long userId);

    // 주문 테이블에서 여러 productId에 대한 주문 내역 찾기
    List<Ordering> findByProductIdIn(List<Long> productIds);

}
