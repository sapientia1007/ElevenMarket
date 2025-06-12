package com.wid.elevenmarket.model;

import com.wid.elevenmarket.global.entity.BaseTimeEntity;
import com.wid.elevenmarket.model.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Orders extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    private Users buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private Long priceAtPurchase;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public static Orders createOrder(Users buyer, Product product, Long priceAtPurchase) {
        return new Orders(null, buyer, product, priceAtPurchase, OrderStatus.RPOGRESS);
    }

    public void changeStatusOrder(OrderStatus status) {
        this.status = status;
    }
}
