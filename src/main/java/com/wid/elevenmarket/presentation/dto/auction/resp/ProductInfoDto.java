package com.wid.elevenmarket.presentation.dto.auction.resp;

import com.wid.elevenmarket.model.Product;
import lombok.Getter;

@Getter
public class ProductInfoDto {
    private Long id;
    private String productName;
    private String description;
    private Long price;
    private Long auctionStartPrice;

    public ProductInfoDto(Product product) {
        this.id = product.getId();
        this.productName = product.getProductName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.auctionStartPrice = product.getAuctionStartPrice();
    }
}
