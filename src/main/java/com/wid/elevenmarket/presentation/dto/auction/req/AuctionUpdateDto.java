package com.wid.elevenmarket.presentation.dto.auction.req;

import com.wid.elevenmarket.presentation.dto.auction.resp.ProductInfoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuctionUpdateDto {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private ProductInfoDto product;
}
