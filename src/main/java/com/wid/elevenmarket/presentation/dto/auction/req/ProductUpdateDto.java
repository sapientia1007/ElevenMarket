package com.wid.elevenmarket.presentation.dto.auction.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductUpdateDto {
    private String productName;
    private String description;
    private Long price;
    private Long auctionStartPrice;
    private Integer quantity;
}
