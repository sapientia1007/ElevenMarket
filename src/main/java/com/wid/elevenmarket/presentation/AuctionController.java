package com.wid.elevenmarket.presentation;

import com.wid.elevenmarket.business.AuctionService;
import com.wid.elevenmarket.global.response.CommonResponseEntity;
import com.wid.elevenmarket.model.enums.AuctionStatus;
import com.wid.elevenmarket.presentation.dto.auction.req.AuctionRequestDto;
import com.wid.elevenmarket.presentation.dto.auction.req.AuctionUpdateDto;
import com.wid.elevenmarket.presentation.dto.auction.resp.AuctionListResponseDto;
import com.wid.elevenmarket.presentation.dto.auction.resp.AuctionResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.wid.elevenmarket.global.response.CommonResponseEntity.success;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auction")
public class AuctionController {

    private final AuctionService auctionService;

    @PostMapping("/open")
    public CommonResponseEntity<AuctionResponseDto> openAuction(@RequestBody AuctionRequestDto reqDto){
        return success(auctionService.openAuction(reqDto));
    }

    @PatchMapping("/edit/{auctionId}")
    public CommonResponseEntity<AuctionResponseDto> editAuction(@PathVariable Long auctionId, @RequestBody AuctionUpdateDto reqDto){
        return success(auctionService.updateAuction(auctionId, reqDto));
    }

    @GetMapping("/winner/{winnerId}")
    public CommonResponseEntity<AuctionListResponseDto> getWinnerById(@PathVariable Long winnerId){
        return success(auctionService.getAuctionWinnerById(winnerId));
    }

    @GetMapping("/status")
    public CommonResponseEntity<AuctionListResponseDto> getByStatus(@RequestParam AuctionStatus status){
        return success(auctionService.getAuctionListByStatus(status));
    }

    @GetMapping("/read/{auctionId}")
    public CommonResponseEntity<AuctionResponseDto> getAuctionById(@PathVariable Long auctionId){
        return success(auctionService.readDetailAuctionInfoById(auctionId));
    }

    @GetMapping("/user")
    public CommonResponseEntity<AuctionListResponseDto> getAuctionListByUserId(@RequestParam Long userId){
        return success(auctionService.getUsersAuctionList(userId));
    }

    @GetMapping("/all")
    public CommonResponseEntity<AuctionListResponseDto> getAllAuctionList(){
        return success(auctionService.getAllAuctionList());
    }
}
