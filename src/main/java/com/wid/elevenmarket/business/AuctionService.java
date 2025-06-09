package com.wid.elevenmarket.business;

import com.wid.elevenmarket.global.exception.CustomException;
import com.wid.elevenmarket.model.Auction;
import com.wid.elevenmarket.model.Product;
import com.wid.elevenmarket.model.enums.AuctionStatus;
import com.wid.elevenmarket.persistence.AuctionRepository;
import com.wid.elevenmarket.persistence.ProductRepository;
import com.wid.elevenmarket.presentation.dto.auction.req.AuctionRequestDto;
import com.wid.elevenmarket.presentation.dto.auction.req.AuctionUpdateDto;
import com.wid.elevenmarket.presentation.dto.auction.resp.AuctionListResponseDto;
import com.wid.elevenmarket.presentation.dto.auction.resp.AuctionResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly=true)
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final ProductRepository productRepository;

    // 경매 등록
    @Transactional
    public AuctionResponseDto openAuction(AuctionRequestDto auctionRequestDto) {
        try {
            Product savedProduct = productRepository.findById(auctionRequestDto.getProductId()).orElse(null);
            savedProduct.auctionStart(auctionRequestDto.getPrice());
            productRepository.save(savedProduct);
            Auction toSaveAuction = Auction.openAuction(savedProduct, auctionRequestDto.getStartDate(), auctionRequestDto.getEndDate());
            auctionRepository.save(toSaveAuction);
            return new AuctionResponseDto(toSaveAuction);
        } catch (Exception e) {
            throw new CustomException("경매 시작 오류 : " + e, HttpStatus.BAD_REQUEST);
        }
    }

    // 경매 수정
    @Transactional
    public AuctionResponseDto updateAuction(Long auctionId, AuctionUpdateDto auctionUpdateDto) {
        Auction savedAuction = auctionRepository.findById(auctionId).orElseThrow(() -> new CustomException("존재하지 않는 경매 정보에요", HttpStatus.NOT_FOUND));
        savedAuction.updateAuctionInfo(auctionUpdateDto);
        auctionRepository.save(savedAuction);
        return new AuctionResponseDto(savedAuction);
    }

    // 경매 상세 조회
    public AuctionResponseDto readDetailAuctionInfoById(Long auctionId) {
        Auction savedAuction = auctionRepository.findById(auctionId).orElseThrow(() -> new CustomException("존재하지 않는 경매 정보에요", HttpStatus.NOT_FOUND));
        return new AuctionResponseDto(savedAuction);
    }

    // 사용자가 참여한 경매 전체 목록
    public AuctionListResponseDto getUsersAuctionList(Long userId) {
        List<AuctionResponseDto> auctions = auctionRepository.findByBidderId(userId).stream().map(AuctionResponseDto::new).toList();
        return new AuctionListResponseDto(auctions);
    }

    // 전체 경매 목록
    public AuctionListResponseDto getAllAuctionList() {
        List<AuctionResponseDto> auctions = auctionRepository.findAll().stream().map(AuctionResponseDto::new).toList();
        return new AuctionListResponseDto(auctions);
    }

    // 경매 상태 관련 목록
    public AuctionListResponseDto getAuctionListByStatus(AuctionStatus status) {
        List<AuctionResponseDto> auctions = auctionRepository.findByStatus(status).stream().map(AuctionResponseDto::new).toList();
        return new AuctionListResponseDto(auctions);
    }

    // 당첨자별 조회
    public AuctionListResponseDto getAuctionWinnerById(Long winnerId){
        List<AuctionResponseDto> auctions = auctionRepository.findByWinnerId(winnerId).stream().map(AuctionResponseDto::new).toList();
        return new AuctionListResponseDto(auctions);
    }
}
