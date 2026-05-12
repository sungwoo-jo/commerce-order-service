package com.project.commerce.repository;

import com.project.commerce.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // 데이터가 잘못 들어온 경우 null이 발생하는 것을 방지하기 위해 예외를 던질 수 있도록 Optional을 사용
    Optional<Order> findByOrderNumber(String orderNumber);
}