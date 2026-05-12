package com.project.commerce.controller;

import com.project.commerce.dto.order.OrderCreateRequest;
import com.project.commerce.dto.payment.PaymentRequest;
import com.project.commerce.service.OrderService;
import com.project.commerce.service.PaymentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class OrderController {

    private final PaymentService paymentService;
    private final OrderService orderService;

    public OrderController(OrderService orderService, PaymentService paymentService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    @PostMapping("/orders")
    public String order(@RequestBody OrderCreateRequest request) { // 사용자의 ID와 금액을 받아 주문을 생성하고 주문번호를 반환.
        String orderNumber = orderService.createOrder(request.getUserId(), request.getTotalPrice());
        return orderNumber;
    }

    @PostMapping("/payments")
    public void pay(@RequestBody PaymentRequest request) { // 주문번호와 금액을 받아 결제를 진행.
        paymentService.pay(request.getOrderNumber(), request.getAmount());
    }
}
