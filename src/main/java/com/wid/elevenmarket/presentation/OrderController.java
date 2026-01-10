package com.wid.elevenmarket.presentation;

import com.wid.elevenmarket.business.OrderService;
import com.wid.elevenmarket.global.response.CommonResponseEntity;
import com.wid.elevenmarket.presentation.dto.order.req.OrderRequestDto;
import com.wid.elevenmarket.presentation.dto.order.resp.OrderListResponseDto;
import com.wid.elevenmarket.presentation.dto.order.resp.OrderResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.wid.elevenmarket.global.response.CommonResponseEntity.success;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/process")
    public CommonResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto orderRequestDto) {
        return success(orderService.processOrder(orderRequestDto));
    }

    @GetMapping("/get/{orderId}")
    public CommonResponseEntity<OrderResponseDto> getOrder(@PathVariable Long orderId) {
        return success(orderService.getOrderDetailInfoById(orderId));
    }

    @GetMapping("/get/all/{userId}")
    public CommonResponseEntity<OrderListResponseDto> getOrderList(@PathVariable Long userId) {
        return success(orderService.getOrderListByUserId(userId));
    }

    @PatchMapping("/cancel/{orderId}")
    public CommonResponseEntity<OrderResponseDto> cancelOrder(@PathVariable Long orderId) {
        return success(orderService.cancelOrder(orderId));
    }
}
