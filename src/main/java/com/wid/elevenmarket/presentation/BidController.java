package com.wid.elevenmarket.presentation;

import com.wid.elevenmarket.business.BidService;
import com.wid.elevenmarket.global.response.CommonResponseEntity;
import com.wid.elevenmarket.presentation.dto.bid.resp.BidResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.wid.elevenmarket.global.response.CommonResponseEntity.success;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bid")
public class BidController {

    private final BidService bidService;

    @PatchMapping("/cancel/{bidId}")
    public CommonResponseEntity<BidResponseDto> cancelBid(@PathVariable("bidId") Long bidId) {
        return success(bidService.cancelBid(bidId));
    }
}
