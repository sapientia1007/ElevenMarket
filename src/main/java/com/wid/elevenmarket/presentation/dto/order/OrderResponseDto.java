package com.wid.elevenmarket.presentation.dto.order;

import com.wid.elevenmarket.model.Orders;
import com.wid.elevenmarket.model.Product;
import com.wid.elevenmarket.model.Users;
import lombok.Getter;

@Getter
public class OrderResponseDto {
    private Long id;
    private Long priceAtPurchase;
    private BuyerResponse buyer;
    private ProductResponse product;

    public OrderResponseDto(Orders orders) {
        this.id = orders.getId();
        this.priceAtPurchase = orders.getPriceAtPurchase();
        this.buyer = new BuyerResponse(orders.getBuyer());
        this.product = new ProductResponse(orders.getProduct());
    }

    @Getter
    public static class BuyerResponse {
        private Long id;
        private String userName;
        private String email;

        public BuyerResponse(Users buyer) {
            this.id = buyer.getId();
            this.userName = buyer.getUserName();
            this.email = buyer.getEmail();
        }
    }

    @Getter
    public static class ProductResponse {
        private Long id;
        private String productName;

        public ProductResponse(Product product) {
            this.id = product.getId();
            this.productName = product.getProductName();
        }
    }
}
