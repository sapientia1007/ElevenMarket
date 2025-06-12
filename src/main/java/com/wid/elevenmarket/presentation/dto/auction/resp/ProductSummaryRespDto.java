package com.wid.elevenmarket.presentation.dto.auction.resp;

import com.wid.elevenmarket.model.Product;
import lombok.Getter;

@Getter
public class ProductSummaryRespDto {
    private Long id;
    private String productName;
    private String description;

    public ProductSummaryRespDto(Product product) {
        this.id = product.getId();
        this.productName = product.getProductName();
        this.description = product.getDescription();
    }
}
