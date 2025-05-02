package com.wid.elevenmarket.order;

import com.wid.elevenmarket.model.Orders;
import com.wid.elevenmarket.model.Product;
import com.wid.elevenmarket.model.Users;
import com.wid.elevenmarket.persistence.OrderRepository;
import com.wid.elevenmarket.persistence.ProductRepository;
import com.wid.elevenmarket.persistence.UsersRepository;
import com.wid.elevenmarket.business.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional  // DB 상태 롤백
public class OrderServiceTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private OrderService orderService;

    private Product product;
    private Users user;

    @BeforeEach
    void setUp() {
        // 매번 테스트 전에 새로운 사용자 및 상품 객체 준비
        user = new Users(null, "TestUser", "testuser@example.com", "1234");
        product = new Product(null, "TestProduct", "Test Description", 1000L, 500L, false, null, user, 10);

        // 테스트 DB에 저장
        usersRepository.save(user);
        productRepository.save(product);
    }

    @Test
    void testProcessOrder() {
        Product savedProduct = productRepository.findById(product.getId()).orElseThrow();
        Users savedUser = usersRepository.findById(user.getId()).orElseThrow();

        // 실제 주문 처리 메소드 호출
        Orders savedOrder = orderService.processOrder(savedUser.getId(), savedProduct.getId());

        // 상품의 재고 감소 확인
        Product updatedProduct = productRepository.findById(savedProduct.getId()).orElseThrow();
        assertEquals(9, updatedProduct.getQuantity());  // 재고가 1 감소해야 함

        // 주문 객체 확인
        assertNotNull(savedOrder);
        assertEquals(savedUser, savedOrder.getBuyer());
        assertEquals(savedProduct, savedOrder.getProduct());
        assertEquals(savedProduct.getPrice(), savedOrder.getPriceAtPurchase());
    }
}