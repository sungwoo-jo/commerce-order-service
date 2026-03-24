package com.project.commerce.service;

import com.project.commerce.domain.order.Order;
import com.project.commerce.domain.order.OrderStatus;
import com.project.commerce.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderServiceTest {
    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("주문 저장 시 주문번호가 자동 생성되고 기본 상태는 READY여야 한다")
    void saveOrderTest() {
        // 1. 테스트할 데이터 준비
        Long userId = 1L;
        int totalPrice = 2100000000;
        Order order = new Order(userId, totalPrice);

        // 2. DB에 저장
        Order savedOrder = orderRepository.save(order);

        // 3. 검증
        assertThat(savedOrder.getId()).isNotNull();
        assertThat(savedOrder.getOrderNumber()).isNotNull();
        assertThat(savedOrder.getOrderNumber().startsWith("26"));

        assertThat(savedOrder.getStatus()).isEqualTo(OrderStatus.READY);

        Order foundOrder = orderRepository.findById(savedOrder.getId()).orElseThrow();
        assertThat(foundOrder.getUserId()).isEqualTo(userId);
    }
}
