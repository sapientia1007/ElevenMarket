package com.wid.elevenmarket.presentation.dto.order.resp;

import com.wid.elevenmarket.model.Product;
import lombok.Getter;

@Getter
public class ProductSummaryResponseDto {
    private Long id;
    private String productName;
    private String productDescription;

    public ProductSummaryResponseDto(Product product) {
        this.id = product.getId();
        this.productName = product.getProductName();
        this.productDescription = product.getDescription();
    }
}
