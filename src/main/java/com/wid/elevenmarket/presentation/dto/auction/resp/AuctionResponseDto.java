package com.wid.elevenmarket.presentation.dto.auction.resp;

import com.wid.elevenmarket.model.Auction;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AuctionResponseDto {
    private Long id;
    private ProductSummaryRespDto product;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public AuctionResponseDto(Auction auction) {
        this.id = auction.getId();
        this.product = new ProductSummaryRespDto(auction.getProduct());
        this.startDate = auction.getStartDate();
        this.endDate = auction.getEndDate();
    }

    public String testToString() {
        return "AuctionResponseDto{" +
                "id=" + id +
                ", product=" + product.getProductName() +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
