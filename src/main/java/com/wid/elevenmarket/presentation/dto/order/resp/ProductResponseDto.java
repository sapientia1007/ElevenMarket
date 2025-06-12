package com.wid.elevenmarket.presentation.dto.order.resp;

import com.wid.elevenmarket.model.Product;
import lombok.Getter;

@Getter
public class ProductResponseDto {
    private Long id;
    private String productName;
    private String productDescription;

    public ProductResponseDto(Product product) {
        this.id = product.getId();
        this.productName = product.getProductName();
        this.productDescription = product.getDescription();
    }
}
