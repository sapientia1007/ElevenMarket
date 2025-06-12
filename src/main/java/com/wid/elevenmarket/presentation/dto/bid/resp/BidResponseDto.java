package com.wid.elevenmarket.presentation.dto.bid.resp;

import com.wid.elevenmarket.model.Bid;
import com.wid.elevenmarket.presentation.dto.auction.resp.AuctionResponseDto;
import com.wid.elevenmarket.presentation.dto.users.resp.UserResponseDto;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class BidResponseDto {
    private Long id;
    private UserResponseDto bidder;
    private BigDecimal bidPrice;
    private LocalDateTime bidDate;
    private AuctionResponseDto auction;

    public BidResponseDto(Bid bid) {
        this.id = bid.getId();
        this.bidder = new UserResponseDto(bid.getBidder());
        this.bidPrice = bid.getBidPrice();
        this.bidDate = bid.getBidDate();
        this.auction = new AuctionResponseDto(bid.getAuction());
    }
}
