package com.wid.elevenmarket.business;

import com.wid.elevenmarket.global.exception.CustomException;
import com.wid.elevenmarket.model.Auction;
import com.wid.elevenmarket.model.Product;
import com.wid.elevenmarket.persistence.AuctionRepository;
import com.wid.elevenmarket.persistence.ProductRepository;
import com.wid.elevenmarket.presentation.dto.auction.AuctionRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly=true)
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void openAuction(AuctionRequestDto auctionRequestDto) {
        try {
            Product savedProduct = productRepository.findById(auctionRequestDto.getProductId()).orElse(null);
            Auction toSaveAuction = Auction.openAuction(savedProduct, auctionRequestDto.getStartDate(), auctionRequestDto.getEndDate());
            auctionRepository.save(toSaveAuction);
        } catch (Exception e) {
            throw new CustomException("경매 시작 오류 : " + e, HttpStatus.BAD_REQUEST);
        }
    }
}
