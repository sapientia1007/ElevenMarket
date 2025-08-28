package com.wid.elevenmarket.presentation;

import com.wid.elevenmarket.business.BidService;
import com.wid.elevenmarket.global.response.CommonResponseEntity;
import com.wid.elevenmarket.presentation.dto.bid.req.BidProcessRequestDto;
import com.wid.elevenmarket.presentation.dto.bid.resp.BidListResponseDto;
import com.wid.elevenmarket.presentation.dto.bid.resp.BidResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.wid.elevenmarket.global.response.CommonResponseEntity.success;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bid")
public class BidController {

    private final BidService bidService;

    @PostMapping("/process")
    public CommonResponseEntity<BidResponseDto> processBid(@RequestBody BidProcessRequestDto bidProcessRequestDto) {
        return success(bidService.processBid(bidProcessRequestDto));
    }

    @PatchMapping("/cancel/{bidId}")
    public CommonResponseEntity<BidResponseDto> cancelBid(@PathVariable("bidId") Long bidId) {
        return success(bidService.cancelBid(bidId));
    }

    @GetMapping("/user/{userId}")
    public CommonResponseEntity<BidListResponseDto> getBidByUser(@PathVariable("userId") Long userId) {
        return success(bidService.getBidListByUserId(userId));
    }

    @GetMapping("/auction/{auctionId}")
    public CommonResponseEntity<BidListResponseDto> getBidByAuction(@PathVariable("auctionId") Long auctionId) {
        return success(bidService.getBidListByAuctionId(auctionId));
    }
}
