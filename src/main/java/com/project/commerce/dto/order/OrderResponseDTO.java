package com.project.commerce.dto.order;

import com.project.commerce.domain.order.Order;
import lombok.Getter;

@Getter
public class OrderResponseDTO {
    private String orderNumber;
    private int totalPrice;
    private String status;

    public OrderResponseDTO(Order order) {
        this.orderNumber = order.getOrderNumber();
        this.totalPrice = order.getTotalPrice();
        this.status = order.getStatus().name();
    }
}
