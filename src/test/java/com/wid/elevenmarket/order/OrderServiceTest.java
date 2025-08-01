package com.wid.elevenmarket.order;

import com.wid.elevenmarket.global.exception.CustomException;
import com.wid.elevenmarket.model.Product;
import com.wid.elevenmarket.model.Users;
import com.wid.elevenmarket.persistence.OrderRepository;
import com.wid.elevenmarket.persistence.ProductRepository;
import com.wid.elevenmarket.persistence.UsersRepository;
import com.wid.elevenmarket.business.OrderService;
import com.wid.elevenmarket.presentation.dto.order.resp.OrderResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

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

        for (int i=0; i<10; i++) {
            Users user = new Users(null, "user" + i, "user" + i + "@example.com", "1234");
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
        OrderResponseDto savedOrder = orderService.processOrder(savedUser.getId(), savedProduct.getId(), 1);

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

        for (int i=0; i<10; i++) {
            Users user = new Users(null, "user" + i, "user" + i + "@example.com", "1234");
            usersRepository.save(user);
            usersIds.add(user.getId());
        }

        Users owner = usersRepository.findById(usersIds.get(0)).orElseThrow();

        Product product = new Product(null, "TestProduct", "Test Description",
                1000L, 500L, false, new ArrayList<>(), owner, 100, null);

        productRepository.save(product);

        int threadCount = 10;
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        // 10명이 동시에 10개 주문
        for (int i = 0; i < threadCount; i++) {
            final Long userId = usersIds.get(i);
            executorService.submit(() -> {
                try {
                    orderService.processOrder(userId, product.getId(), 10);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await(); // 모두 끝날때까지 대기

        Product savedProduct = productRepository.findById(product.getId()).orElseThrow();
        System.out.println("최종 재고 = " + savedProduct.getQuantity());
        assertEquals(0, savedProduct.getQuantity());
    }
}