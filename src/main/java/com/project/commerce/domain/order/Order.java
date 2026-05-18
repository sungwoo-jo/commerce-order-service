package com.project.commerce.domain.order;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private int totalPrice;

    @Column(unique = true, nullable = false)
    private String orderNumber;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public Order(Long userId, int totalPrice) {
        validateUserId(userId);
        validatePrice(totalPrice);
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.status = OrderStatus.READY;
        this.orderNumber = generateOrderNumber();
    }

    private void validateUserId(Long userId) {
        if (userId < 0) {
            throw new NumberFormatException("정상적인 회원 번호가 아닙니다.");
        }
    }

    public void pay() {
        this.status = OrderStatus.PAID;
    }

    public void cancel() {
        // 이미 취소된 경우 처리
        if (this.status == OrderStatus.CANCELLED) {
            throw new IllegalStateException("이미 취소된 주문입니다.");
        }

        this.status = OrderStatus.CANCELLED;
    }

    /**
     * 주문번호 생성
     */
    private String generateOrderNumber() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss"));

        int randomSuffix = ThreadLocalRandom.current().nextInt(100, 1000);

        // 예시: 260324115321874
        return timestamp + randomSuffix;
    }

    private void validatePrice(int totalPrice) {
        if (totalPrice <= 0 || totalPrice > 2100000000) {
            throw new IllegalArgumentException("결제 금액이 정상적이지 않습니다. 금액을 다시 확인해주세요.");
        }
    }
}
