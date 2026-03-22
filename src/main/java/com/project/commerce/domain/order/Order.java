package com.project.commerce.domain.order;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private int totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public Order(Long userId, int totalPrice) {
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.status = OrderStatus.READY;
    }

    public void pay() {
        this.status = OrderStatus.PAID;
    }
}
