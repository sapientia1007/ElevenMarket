package com.wid.elevenmarket.presentation;

import com.wid.elevenmarket.business.OrderService;
import com.wid.elevenmarket.global.response.CommonResponseEntity;
import com.wid.elevenmarket.presentation.dto.order.OrderRequestDto;
import com.wid.elevenmarket.presentation.dto.order.OrderResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.wid.elevenmarket.global.response.CommonResponseEntity.success;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/order")
    public CommonResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto orderRequestDto) {
        return success(orderService.processOrder(orderRequestDto.getBuyerId(), orderRequestDto.getProductId(), orderRequestDto.getOrderQuantity()));
    }
}
