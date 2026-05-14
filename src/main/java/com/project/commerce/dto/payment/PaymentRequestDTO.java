package com.project.commerce.dto.payment;

import lombok.Getter;

@Getter
public class PaymentRequestDTO {
    private String orderNumber; // 전달받은 주문번호
    private int amount; // 전달받은 금액
}
