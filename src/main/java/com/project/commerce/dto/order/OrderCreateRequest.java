package com.project.commerce.dto.order;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderCreateRequest {
    private Long userId; // 전달받은 사용자 id
    private int totalPrice; // 전달받은 금액
}
