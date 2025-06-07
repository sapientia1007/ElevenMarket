package com.wid.elevenmarket.presentation.dto.order.resp;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderListResponseDto {
    private List<OrderResponseDto> orders;

    public OrderListResponseDto(List<OrderResponseDto> orders) {
        this.orders = orders;
    }
}
