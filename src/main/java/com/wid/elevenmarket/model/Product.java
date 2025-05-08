package com.wid.elevenmarket.model;

import com.wid.elevenmarket.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

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

    private boolean isAuction;

    private LocalDateTime auctionEndTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private Users seller;

    @Column(nullable = false)
    private Integer quantity;

    public void decreaseStock(int orderQuantity) {
        if (this.quantity < orderQuantity) {
            throw new IllegalStateException("재고가 부족합니다. 현재 재고: " + this.quantity);
        }
        this.quantity -= orderQuantity;
    }
}
