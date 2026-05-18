package com.project.commerce.controller;

import com.project.commerce.dto.order.OrderCreateRequestDTO;
import com.project.commerce.dto.order.OrderResponseDTO;
import com.project.commerce.dto.payment.PaymentRequestDTO;
import com.project.commerce.service.OrderService;
import com.project.commerce.service.PaymentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderController {

    private final PaymentService paymentService;
    private final OrderService orderService;

    public OrderController(OrderService orderService, PaymentService paymentService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    /**
     * 주문 생성
     * @param request 사용자 ID, 금액
     * @return orderNumber 주문번호
     */
    @PostMapping("/orders")
    public String createOrder(@RequestBody OrderCreateRequestDTO request) { // 사용자의 ID와 금액을 받아 주문을 생성하고 주문번호를 반환.
        String orderNumber = orderService.createOrder(request.getUserId(), request.getTotalPrice());
        return orderNumber;
    }

    /**
     * 결제 진행
     * @param request 주문번호, 금액
     */
    @PostMapping("/payments")
    public void pay(@RequestBody PaymentRequestDTO request) { // 주문번호와 금액을 받아 결제를 진행.
        paymentService.pay(request.getOrderNumber(), request.getAmount());
    }

    /**
     * 주문 조회
     * @param orderNumber 주문번호
     * @return 주문번호, 금액, 주문상태
     */
    @GetMapping("/orders/{orderNumber}")
    public OrderResponseDTO getOrder(@PathVariable(value="orderNumber") String orderNumber) {
        return orderService.getOrder(orderNumber);
    }

    /**
     * 주문 취소
     * @param orderNumber 주문번호
     */
    @PostMapping("/orders/{orderNumber}/cancel")
    public void cancel(@PathVariable(value = "orderNumber") String orderNumber) {
        orderService.cancelOrder(orderNumber);
    }

}
