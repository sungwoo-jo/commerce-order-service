package com.project.commerce.service;

import com.project.commerce.domain.order.Order;
import com.project.commerce.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public String createOrder(Long userId, int totalPrice) {
        Order order = new Order(userId, totalPrice);
        orderRepository.save(order);
        return order.getOrderNumber();
    }
}
