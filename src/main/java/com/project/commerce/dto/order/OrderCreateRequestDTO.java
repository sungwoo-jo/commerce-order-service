package com.project.commerce.dto.order;

import lombok.Getter;

@Getter
public class OrderCreateRequestDTO {
    private Long userId; // 전달받은 사용자 id
    private Long itemId; // 전달받은 상품 id
    private int count;  // 전달받은 수량
}
