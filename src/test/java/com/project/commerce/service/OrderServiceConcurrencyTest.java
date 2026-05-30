package com.project.commerce.service;

import com.project.commerce.domain.item.Item;
import com.project.commerce.repository.ItemRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class OrderServiceConcurrencyTest {

    @Autowired private OrderService orderService;
    @Autowired private ItemRepository itemRepository;

    private Long savedItemId;

    @BeforeEach
    public void before() {
        // 테스트 시작 전, 재고가 100개인 뉴발란스 993 상품을 DB에 저장
        Item item = new Item("뉴발란스 993 280", 259000, 100);
        savedItemId = itemRepository.save(item).getId();
    }

    @AfterEach
    public void after() {
        // 멀티스레드 환경에서는 @Transactional 롤백이 잘 안 먹히므로 직접 지워줌
        itemRepository.deleteAll();
    }

    @Test
    @DisplayName("동시성 제어: 100명이 동시에 1개씩 주문하면 재고가 정확히 0이 되어야 한다.")
    public void order100AtTheSameTime() throws InterruptedException {
        int threadCount = 100;

        // 멀티스레드를 생성하는 자바 API
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        // 모든 스레드의 작업이 끝날 때까지 기다려주는 도구
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    // 100명이 동시에 주문 메서드 호출!
                    orderService.createOrder(1L, savedItemId, 1);
                } finally {
                    latch.countDown(); // 작업 완료 시 카운트 감소
                }
            });
        }

        latch.await(); // 100개의 요청이 모두 끝날 때까지 메인 스레드 대기

        // 검증: 재고가 0개인지 확인!
        Item findItem = itemRepository.findById(savedItemId).orElseThrow();
        System.out.println("최종 남은 재고: " + findItem.getStockQuantity());

        assertThat(findItem.getStockQuantity()).isEqualTo(0);
    }
}