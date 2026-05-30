package com.project.commerce.service;

import com.project.commerce.domain.item.Item;
import com.project.commerce.domain.order.Order;
import com.project.commerce.domain.order.OrderStatus;
import com.project.commerce.domain.payment.Payment;
import com.project.commerce.domain.payment.PaymentStatus;
import com.project.commerce.repository.OrderRepository;
import com.project.commerce.repository.PaymentRepository;
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

    @Autowired private OrderService orderService;
    @Autowired private OrderRepository orderRepository;
    @Autowired private PaymentRepository paymentRepository;

    @Test
    @DisplayName("성공: 올바른 정보로 주문을 생성하면 주문번호가 반환되어야 한다")
    void createOrder_Success() {
        // Given
        Long userId = 1L;
        Long itemId = 1L;
        int totalPrice = 100000;

        // When
        String orderNumber = orderService.createOrder(userId, itemId, totalPrice);

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
        Long itemId = 1L;
        int invalidPrice = -100;

        // When & Then
        assertThatThrownBy(() -> orderService.createOrder(userId, itemId, invalidPrice))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("결제 금액이 정상적이지 않습니다.");
    }

    @Test
    @DisplayName("결제 완료된 주문을 취소하면, 주문과 결제 상태가 모두 CANCELLED로 변경된다.")
    void cancelOrder_Success() {
        // 1. Given(준비): 결제 완료된 주문 상황을 강제로 만든다.
        Item item = new Item();
        Order order = new Order(1L, item, 1);
        order.pay();
        orderRepository.save(order);

        Payment payment = new Payment(order, 10000);
        payment.success();
        paymentRepository.save(payment);

        // 2. When(실행): 서비스의 취소 로직을 호출한다.
        orderService.cancelOrder(order.getOrderNumber());

        // 3. Then(검증): 상태가 올바르게 변했는지 확인한다.
        Order canceledOrder = orderRepository.findByOrderNumber(order.getOrderNumber()).get();
        assertThat(canceledOrder.getStatus()).isEqualTo(OrderStatus.CANCELLED);

        Payment canceledPayment = paymentRepository.findByOrderAndStatus(canceledOrder, PaymentStatus.CANCELLED).get();
        assertThat(canceledPayment.getStatus()).isEqualTo(PaymentStatus.CANCELLED);
    }

    @Test
    @DisplayName("이미 취소된 주문을 다시 취소하려고 하면 예외가 발생해야 한다.")
    void cancelOrder_ALreadyCanceled_ThrowsException() {
        // 1. Given(준비): 이미 취소된 주문을 만든다.
        Item item = new Item();
        Order order = new Order(1L, item, 1);
        order.cancel();
        orderRepository.save(order);

        // 2. When & 3. Then (실행 및 검증): 예외가 터지는지 확인한다.
        // assertThatThrownBy: 이 블록 안의 코드를 실행했을 때 에러가 터져야 성공으로 간주한다.
        assertThatThrownBy(() -> orderService.cancelOrder(order.getOrderNumber()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 취소된 주문입니다.");
    }
}
