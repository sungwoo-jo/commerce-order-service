package com.project.commerce.repository;

import com.project.commerce.domain.item.Item;
import com.project.commerce.domain.order.Order;
import com.project.commerce.domain.order.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("주문 저장 및 조회 테스트")
    void saveAndFindOrder() {
        // 1. 테스트할 데이터 준비
        Item item = new Item();
        Order order = new Order(1L, item, 1); // userId 1, 총 금액 5만원

        // 2. DB에 저장
        Order savedOrder = orderRepository.save(order);

        // 3. 저장된 데이터가 내가 넣은 데이터와 동일한가?
        Order foundOrder = orderRepository.findById(savedOrder.getId()).orElse(null);

        assertThat(foundOrder).isNotNull();
        assertThat(foundOrder.getUserId()).isEqualTo(1L);
        assertThat(foundOrder.getTotalPrice()).isEqualTo(50000);
        assertThat(foundOrder.getStatus()).isEqualTo(OrderStatus.READY);
    }

    @Test
    @DisplayName("주문 저장 시 주문번호가 자동 생성되고 기본 상태는 READY여야 한다")
    void saveOrderTest() {
        // 1. 테스트할 데이터 준비
        Long userId = 1L;
        Item item = new Item();
        int totalPrice = 2100000000;
        Order order = new Order(userId, item, 1);

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
