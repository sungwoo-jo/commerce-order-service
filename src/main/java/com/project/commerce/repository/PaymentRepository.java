package com.project.commerce.repository;

import com.project.commerce.domain.order.Order;
import com.project.commerce.domain.payment.Payment;
import com.project.commerce.domain.payment.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // 주문(Order) 객체와 결제 상태(SUCCESS)를 기준으로 결제 내역을 확인한다.
    Optional<Payment> findByOrderAndStatus(Order order, PaymentStatus status);
}