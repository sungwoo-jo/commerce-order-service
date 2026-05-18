package com.project.commerce.service;

import com.project.commerce.domain.order.Order;
import com.project.commerce.domain.order.OrderStatus;
import com.project.commerce.dto.order.OrderResponseDTO;
import com.project.commerce.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final PaymentService paymentService;

    @Transactional
    public String createOrder(Long userId, int totalPrice) {
        Order order = new Order(userId, totalPrice);
        orderRepository.save(order);
        return order.getOrderNumber();
    }

    public OrderResponseDTO getOrder(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber)
                .map(OrderResponseDTO::new)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문번호입니다: " + orderNumber));
    }

    @Transactional
    public void cancelOrder(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문번호입니다: " + orderNumber));

        if (order.getStatus() == OrderStatus.PAID) {
            paymentService.refund(order);
        }

        // 주문 상태 변경
        order.cancel();
    }
}
