package com.wid.elevenmarket.presentation.dto.auction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuctionRequestDto {
    Long productId;
    LocalDateTime startDate;
    LocalDateTime endDate;
}
