package com.project.commerce.dto.order;

import lombok.Getter;

@Getter
public class OrderCreateRequestDTO {
    private Long userId; // 전달받은 사용자 id
    private int totalPrice; // 전달받은 금액
}
