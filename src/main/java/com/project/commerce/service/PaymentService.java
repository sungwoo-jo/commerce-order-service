package com.project.commerce.service;

import com.project.commerce.domain.order.Order;
import com.project.commerce.domain.payment.Payment;
import com.project.commerce.domain.payment.PaymentStatus;
import com.project.commerce.repository.OrderRepository;
import com.project.commerce.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ThreadLocalRandom;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public void pay(String orderNumber, int amount) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        // 결제 완료 처리
        order.pay();

        // 결제 이력 저장
        Payment payment = new Payment(order, amount);

        boolean paymentResult = mockPayApi(amount);

        if (paymentResult) {
            payment.success();
        } else {
            payment.fail();
        }

        paymentRepository.save(payment);
    }

    public boolean mockPayApi(int amount) {
        // 0~99 사이의 랜덤 숫자 생성
        int randomValue = ThreadLocalRandom.current().nextInt(100);

        // 실패 확률 20% 설정 (랜덤 숫자가 20보다 작으면 실패)
        if (randomValue < 20) {
            log.error("PG사 네트워크 장애 발생 - 결제 실패: 금액 {}", amount);
            return false;
        }

        return true;
    }

    @Transactional
    public void refund(Order order) {
        Payment payment = paymentRepository.findByOrderAndStatus(order, PaymentStatus.SUCCESS)
                .orElseThrow(() -> new IllegalArgumentException("취소할 수 있는 성공된 결제 이력이 없습니다."));

        log.info("PG사 결제 취소 요청 전송 - 주문번호: {}, 금액: {}", order.getOrderNumber(), payment.getAmount());

        payment.cancel();
    }
}
