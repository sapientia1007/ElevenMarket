package com.wid.elevenmarket.presentation.dto.product.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductUpdateReqDto {
    private String productName;
    private String description;
    private Long price;
    private Integer quantity;
}
