package com.wid.elevenmarket.presentation.dto.order;

import com.wid.elevenmarket.model.Orders;
import com.wid.elevenmarket.model.Product;
import com.wid.elevenmarket.model.Users;
import lombok.Getter;

@Getter
public class OrderResponseDto {
    private Long id;
    private Long priceAtPurchase;
    private BuyerResponseDto buyer;
    private ProductResponseDto product;

    public OrderResponseDto(Orders orders) {
        this.id = orders.getId();
        this.priceAtPurchase = orders.getPriceAtPurchase();
        this.buyer = new BuyerResponseDto(orders.getBuyer());
        this.product = new ProductResponseDto(orders.getProduct());
    }
}
