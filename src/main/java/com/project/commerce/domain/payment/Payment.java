package com.project.commerce.domain.payment;

import com.project.commerce.domain.order.Order;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 객체 참조
    @JoinColumn(name = "order_id") // FK
    private Order order;
    private int amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    public Payment(Order order, int amount) {
        this.order = order;
        this.amount = amount;
        this.status = PaymentStatus.READY;
    }

    public void success() {
        this.status = PaymentStatus.SUCCESS;
    }

    public void fail() {
        this.status = PaymentStatus.FAIL;
    }

    public void cancel() {
        if (this.status == PaymentStatus.CANCELLED) {
            throw new IllegalStateException("이미 취소 처리된 결제 건입니다.");
        }
        if (this.status == PaymentStatus.FAIL) {
            throw new IllegalStateException("결제 실패 건은 취소할 수 없습니다.");
        }

        this.status = PaymentStatus.CANCELLED;
    }
}
