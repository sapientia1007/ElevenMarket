package com.wid.elevenmarket.presentation.dto.bid.req;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class BidProcessReq {
    public Long userId;
    public Long auctionId;
    public BigDecimal bidPrice;
}
