package com.project.commerce.service;

import com.project.commerce.domain.order.Order;
import com.project.commerce.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("성공: 올바른 정보로 주문을 생성하면 주문번호가 반환되어야 한다")
    void createOrder_Success() {
        // Given
        Long userId = 1L;
        int totalPrice = 100000;

        // When
        String orderNumber = orderService.createOrder(userId, totalPrice);

        // Then
        assertThat(orderNumber).isNotNull();

        Order savedOrder = orderRepository.findAll().get(0);
        assertThat(savedOrder.getOrderNumber()).isEqualTo(orderNumber);
        assertThat(savedOrder.getTotalPrice()).isEqualTo(totalPrice);
    }

    @Test
    @DisplayName("실패: 금액이 0원 이하이면 예외가 발생해야 한다")
    void createOrder_fail_InvalidPrice() throws InterruptedException {
        // Given
        Long userId = 1L;
        int invalidPrice = -100;

        // When & Then
        assertThatThrownBy(() -> orderService.createOrder(userId, invalidPrice))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("결제 금액이 정상적이지 않습니다.");
    }
}
