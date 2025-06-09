package com.wid.elevenmarket.presentation.dto.auction.resp;

import lombok.Getter;

import java.util.List;

@Getter
public class AuctionListResponseDto {
    private List<AuctionResponseDto> auctions;

    public AuctionListResponseDto(List<AuctionResponseDto> auctions) {
        this.auctions = auctions;
    }
}
