package com.wid.elevenmarket.order;

import com.wid.elevenmarket.global.exception.CustomException;
import com.wid.elevenmarket.model.Product;
import com.wid.elevenmarket.model.Users;
import com.wid.elevenmarket.persistence.OrderRepository;
import com.wid.elevenmarket.persistence.ProductRepository;
import com.wid.elevenmarket.persistence.UsersRepository;
import com.wid.elevenmarket.business.OrderService;
import com.wid.elevenmarket.presentation.dto.order.req.OrderRequestDto;
import com.wid.elevenmarket.presentation.dto.order.resp.OrderResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class OrderServiceTest {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private OrderService orderService;

    @Test
    void testProcessOrder() {

        List<Long> usersIds = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Users user = new Users(
                    null,
                    "user" + i,
                    "user" + i + "@example.com",
                    "1234",
                    "010-0000-" + String.format("%04d", i),
                    1
            );
            usersRepository.save(user);
            usersIds.add(user.getId());
        }

        Users owner = usersRepository.findById(usersIds.get(0)).orElseThrow();

        Product product = new Product(null, "TestProduct", "Test Description",
                1000L, 500L, false, new ArrayList<>(), owner, 100, null);

        productRepository.save(product);

        Product savedProduct = productRepository.findById(product.getId()).orElseThrow();
        Long firstUser = usersIds.get(1);
        Users savedUser = usersRepository.findById(firstUser).orElseThrow(() -> new CustomException("존재하지 않는 사용자에요", HttpStatus.NOT_FOUND));

        // 실제 주문 처리 메소드 호출 : 1개 주문
        OrderRequestDto orderRequestDto = new OrderRequestDto(savedProduct.getId(), savedUser.getId(), 1);
        OrderResponseDto savedOrder = orderService.processOrder(orderRequestDto);

        // 상품의 재고 감소 확인
        Product updatedProduct = productRepository.findById(savedProduct.getId()).orElseThrow();
        assertEquals(99, updatedProduct.getQuantity());  // 재고가 1 감소해야 함

        // 주문 객체 확인
        assertNotNull(savedOrder);
        assertEquals(savedUser.getId(), savedOrder.getBuyer().getId());
        assertEquals(savedProduct.getId(), savedOrder.getProduct().getId());
        assertEquals(savedProduct.getPrice(), savedOrder.getPriceAtPurchase());
    }

    // 동시성 테스트
    @Test
    void testOrderConcurrency() throws InterruptedException {

        List<Long> usersIds = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Users user = new Users(
                    null,
                    "user" + i,
                    "user" + i + "@example.com",
                    "1234",
                    "010-0000-" + String.format("%04d", i),
                    1
            );
            usersRepository.save(user);
            usersIds.add(user.getId());
        }

        Users owner = usersRepository.findById(usersIds.get(0)).orElseThrow();

        // 재고가 100개인 상품 생성
        Product product = new Product(null, "TestProduct", "Test Description",
                1000L, 500L, false, new ArrayList<>(), owner, 100, null);
        productRepository.save(product);

        int totalOrders = 100;
        CountDownLatch countDownLatch = new CountDownLatch(totalOrders);
        ExecutorService executorService = Executors.newFixedThreadPool(totalOrders);

        AtomicInteger successfulOrders = new AtomicInteger(0);
        AtomicInteger failedOrders = new AtomicInteger(0);
        AtomicLong totalExecutionTime = new AtomicLong(0);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < totalOrders; i++) {
            final int userIndex = i;
            executorService.submit(() -> {
                long threadStartTime = System.currentTimeMillis();
                try {
                    // 각 사용자가 1개씩 주문
                    OrderRequestDto orderRequestDto = new OrderRequestDto(product.getId(), usersIds.get(userIndex), 1);
                    orderService.processOrder(orderRequestDto);
                    successfulOrders.incrementAndGet();
                } catch (Exception e) {
                    failedOrders.incrementAndGet();
                    System.err.println("주문 실패: " + e.getMessage());
                } finally {
                    long threadEndTime = System.currentTimeMillis();
                    totalExecutionTime.addAndGet(threadEndTime - threadStartTime);
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await(); // 모두 끝날때까지 대기
        executorService.shutdown();
        long endTime = System.currentTimeMillis();
        long totalTestTime = endTime - startTime;

        System.out.println("--- 동시성 테스트 결과 ---");
        System.out.println("총 주문 시도: " + totalOrders);
        System.out.println("성공한 주문: " + successfulOrders.get());
        System.out.println("실패한 주문: " + failedOrders.get());
        System.out.printf("성공률: %.2f%%\n", (double) successfulOrders.get() / totalOrders * 100);
        System.out.println("최종 재고: " + productRepository.findById(product.getId()).orElseThrow().getQuantity());
        System.out.println("총 테스트 시간: " + totalTestTime + "ms");
        System.out.printf("평균 응답 시간: %.2fms\n", (double)totalExecutionTime.get() / totalOrders);

        assertEquals(0, productRepository.findById(product.getId()).orElseThrow().getQuantity());
        assertEquals(100, successfulOrders.get());
    }
}
