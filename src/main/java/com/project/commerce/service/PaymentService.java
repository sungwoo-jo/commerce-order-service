package com.project.commerce.service;

import com.project.commerce.domain.order.Order;
import com.project.commerce.domain.payment.Payment;
import com.project.commerce.repository.OrderRepository;
import com.project.commerce.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public void pay(String orderNumber, int amount) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        // 결제 완료 처리
        order.pay();

        // 결제 이력 저장
        Payment payment = new Payment(order, amount);
        paymentRepository.save(payment);
    }
}
