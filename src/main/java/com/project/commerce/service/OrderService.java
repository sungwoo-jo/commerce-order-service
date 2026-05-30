package com.project.commerce.service;

import com.project.commerce.domain.item.Item;
import com.project.commerce.domain.order.Order;
import com.project.commerce.domain.order.OrderStatus;
import com.project.commerce.dto.order.OrderResponseDTO;
import com.project.commerce.repository.ItemRepository;
import com.project.commerce.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final PaymentService paymentService;
    private final ItemRepository itemRepository;

    @Transactional
    public String createOrder(Long userId, Long itemId, int count) {
//        Item item = itemRepository.findById(itemId)
        Item item = itemRepository.findByIdWithPessimisticLock(itemId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        item.removeStock(count);

        Order order = new Order(userId, item, count);
        orderRepository.save(order);

        return order.getOrderNumber();
    }

    public OrderResponseDTO getOrder(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber)
                .map(OrderResponseDTO::new)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문번호입니다: " + orderNumber));
    }

    @Transactional
    public void cancelOrder(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문번호입니다: " + orderNumber));

        if (order.getStatus() == OrderStatus.PAID) {
            paymentService.refund(order);
        }

        Item item = order.getItem();
        item.addStock(order.getCount());

        // 주문 상태 변경
        order.cancel();
    }
}
