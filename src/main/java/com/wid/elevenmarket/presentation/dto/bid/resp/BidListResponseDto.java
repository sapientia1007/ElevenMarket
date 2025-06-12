package com.wid.elevenmarket.presentation.dto.bid.resp;

import lombok.Getter;

import java.util.List;

@Getter
public class BidListResponseDto {
    private List<BidResponseDto> bids;

    public BidListResponseDto(List<BidResponseDto> bids) {
        this.bids = bids;
    }
}
