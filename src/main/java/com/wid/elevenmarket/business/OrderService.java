package com.wid.elevenmarket.business;

import com.wid.elevenmarket.global.exception.CustomException;
import com.wid.elevenmarket.model.Orders;
import com.wid.elevenmarket.model.Product;
import com.wid.elevenmarket.model.Users;
import com.wid.elevenmarket.persistence.OrderRepository;
import com.wid.elevenmarket.persistence.ProductRepository;
import com.wid.elevenmarket.persistence.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly=true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UsersRepository usersRepository;

    // 주문 처리
    @Transactional
    public Orders processOrder(Long userId, Long productId) {
        // 조회
        Product savedProduct = productRepository.findById(productId).orElseThrow(() -> new CustomException("존재하지 않는 상품이에요", HttpStatus.NOT_FOUND));
        Users savedUser = usersRepository.findById(userId).orElseThrow(() -> new CustomException("존재하지 않는 사용자에요", HttpStatus.NOT_FOUND));

        //  주문 생성
        Orders savedOrder = Orders.createOrder(savedUser, savedProduct, savedProduct.getPrice());
        orderRepository.save(savedOrder);

        // 상품 재고 개수 감소
        savedProduct.decreaseStock();
        productRepository.findById(productId).orElseThrow(() -> new CustomException("존재하지 않는 상품이에요", HttpStatus.NOT_FOUND));
        return savedOrder;
    }

}
