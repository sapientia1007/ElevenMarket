package com.wid.elevenmarket.model;

import com.wid.elevenmarket.global.entity.BaseTimeEntity;
import com.wid.elevenmarket.presentation.dto.product.req.ProductUpdateDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="product_id")
    private Long id;

    @Column(nullable = false)
    private String productName;

    private String description;

    @Column(nullable = false)
    private Long price;

    private Long auctionStartPrice;

    @Column(nullable = false)
    private boolean isAuction;

    @OneToMany(mappedBy = "product")
    private List<Auction> auctions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private Users seller;

    @Column(nullable = false)
    private Integer quantity;

    @Column
    private LocalDate isDeleted;

    public void markAsDeleted() {
        this.isDeleted = LocalDate.now();
    }

    public void changeStock(int orderQuantity) {
        if (this.quantity < orderQuantity) {
            throw new IllegalStateException("재고가 부족합니다. 현재 재고: " + this.quantity);
        }
        this.quantity -= orderQuantity;
    }

    public void auctionStart(Long price) {
        this.isAuction = true;
        this.auctionStartPrice = price != null ? price : this.price;
    }

    public static Product enrollProduct(String productName, String description, Long price,
                                        Users seller, Integer quantity) {
        return new Product(null, productName, description, price, null, false, new ArrayList<>(), seller, quantity, null);
    }

    public void updateProductInfo(ProductUpdateDto productUpdateDto) {
        this.productName = productUpdateDto.getProductName();
        this.description = productUpdateDto.getDescription();
        this.price = productUpdateDto.getPrice();
        this.quantity = productUpdateDto.getQuantity();
    }

}
