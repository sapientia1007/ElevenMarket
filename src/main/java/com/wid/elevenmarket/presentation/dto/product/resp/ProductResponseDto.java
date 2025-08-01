package com.wid.elevenmarket.presentation.dto.product.resp;

import com.wid.elevenmarket.model.Product;
import com.wid.elevenmarket.presentation.dto.users.resp.UserResponseDto;
import lombok.Getter;

@Getter
public class ProductResponseDto {
    private Long id;
    private String productName;
    private String description;
    private Long price;
    private UserResponseDto seller;
    private Integer quantity;

    public ProductResponseDto(Product product) {
        this.id = product.getId();
        this.productName = product.getProductName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.seller = new UserResponseDto(product.getSeller());
        this.quantity = product.getQuantity();
    }

    public String testToString() {
        return "ProductResponseDto{"+
                "id= " + id +
                "name= " + productName +
                "description= " + description +
                "price= " + price +
                "seller= " + seller.getUserName() +
                "quantity= " +quantity +
                "}";
    }
}
