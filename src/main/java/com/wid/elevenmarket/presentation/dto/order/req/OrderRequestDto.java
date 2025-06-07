package com.wid.elevenmarket.presentation.dto.order.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequestDto {
    private Long productId;
    private Long buyerId;
    private int orderQuantity;
}
