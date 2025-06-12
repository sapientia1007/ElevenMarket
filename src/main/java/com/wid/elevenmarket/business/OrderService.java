package com.wid.elevenmarket.business;

import com.wid.elevenmarket.global.exception.CustomException;
import com.wid.elevenmarket.model.Orders;
import com.wid.elevenmarket.model.Product;
import com.wid.elevenmarket.model.Users;
import com.wid.elevenmarket.model.enums.OrderStatus;
import com.wid.elevenmarket.persistence.OrderRepository;
import com.wid.elevenmarket.persistence.ProductRepository;
import com.wid.elevenmarket.persistence.UsersRepository;
import com.wid.elevenmarket.presentation.dto.order.resp.OrderListResponseDto;
import com.wid.elevenmarket.presentation.dto.order.resp.OrderResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly=true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UsersRepository usersRepository;

    // 주문 처리 - 결제 처리 진행 전
    @Transactional
    public OrderResponseDto processOrder(Long userId, Long productId, int orderQuantity) {
        // 조회
        Product savedProduct = productRepository.findProductByIdWithPessimisticLock(productId).orElseThrow(() -> new CustomException("존재하지 않는 상품이에요", HttpStatus.NOT_FOUND));
        Users savedUser = usersRepository.findById(userId).orElseThrow(() -> new CustomException("존재하지 않는 사용자에요", HttpStatus.NOT_FOUND));

        //  주문 생성
        Orders savedOrder = Orders.createOrder(savedUser, savedProduct, savedProduct.getPrice());
        orderRepository.save(savedOrder);

        // 상품 재고 개수 감소
        savedProduct.decreaseStock(orderQuantity);

        Orders order = orderRepository.findById(savedOrder.getId())
                .orElseThrow(() -> new CustomException("존재하지 않는 주문이에요", HttpStatus.NOT_FOUND));

        return new OrderResponseDto(order);
    }

    // 주문 상세 정보 조회
    public OrderResponseDto getOrderDetailInfoById(Long orderId) {
        Orders savedOrder = orderRepository.findById(orderId).orElseThrow(() -> new CustomException("존재하지 않은 주문번호에요", HttpStatus.NOT_FOUND));
        return new OrderResponseDto(savedOrder);
    }

    // 주문 목록 조회
    public OrderListResponseDto getOrderListByUserId(Long userId){
        List<Orders> savedOrders = orderRepository.findByBuyerId(userId);
        List<OrderResponseDto> ordersDtos = savedOrders.stream().map(OrderResponseDto::new).toList();
        return new OrderListResponseDto(ordersDtos);
    }

    // 주문 취소
    @Transactional
    public OrderResponseDto cancelOrder(Long orderId){
        Orders savedOrder = orderRepository.findById(orderId).orElseThrow(() -> new CustomException("존재하지 않는 주문번호에요", HttpStatus.NOT_FOUND));
        savedOrder.changeStatusOrder(OrderStatus.CANCELLED);
        return new OrderResponseDto(savedOrder);
    }
}
