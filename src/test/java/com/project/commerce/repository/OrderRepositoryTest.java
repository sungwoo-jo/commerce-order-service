package com.project.commerce.repository;

import com.project.commerce.domain.order.Order;
import com.project.commerce.domain.order.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("주문 저장 및 조회 테스트")
    void saveAndFindOrder() {
        // 1. 테스트할 데이터 준비
        Order order = new Order(1L, 50000); // 1, 5만원

        // 2. DB에 저장
        Order savedOrder = orderRepository.save(order);

        // 3. 저장된 데이터가 내가 넣은 데이터와 동일한가?
        Order foundOrder = orderRepository.findById(savedOrder.getId()).orElse(null);

        assertThat(foundOrder).isNotNull();
        assertThat(foundOrder.getUserId()).isEqualTo(1L);
        assertThat(foundOrder.getTotalPrice()).isEqualTo(50000);
        assertThat(foundOrder.getStatus()).isEqualTo(OrderStatus.READY);
    }
}
