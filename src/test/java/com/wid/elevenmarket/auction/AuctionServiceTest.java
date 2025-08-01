package com.wid.elevenmarket.auction;

import com.wid.elevenmarket.business.AuctionService;
import com.wid.elevenmarket.model.Auction;
import com.wid.elevenmarket.model.Product;
import com.wid.elevenmarket.model.Users;
import com.wid.elevenmarket.model.enums.AuctionStatus;
import com.wid.elevenmarket.persistence.AuctionRepository;
import com.wid.elevenmarket.persistence.ProductRepository;
import com.wid.elevenmarket.persistence.UsersRepository;
import com.wid.elevenmarket.presentation.dto.auction.req.AuctionUpdateDto;
import com.wid.elevenmarket.presentation.dto.auction.resp.AuctionResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AuctionServiceTest {

    @Autowired
    private AuctionService auctionService;
    @Autowired
    private AuctionRepository auctionRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private ProductRepository productRepository;

    // 경매등록
    @Test
    void testOpenAuction() {

        LocalDateTime now = LocalDateTime.now();
        Users user = new Users(null, "user", "user@example.com", "1234");
        usersRepository.save(user);

        Product productToSave = new Product(null, "productName", "description", 1000L, null, false, new ArrayList<>(), user, 10, null);
        productRepository.save(productToSave);
        Auction auctionToSave = new Auction(null, null, productToSave, now.minusMinutes(1), now.plusMinutes(5), AuctionStatus.PENDING, new ArrayList<>(), null, null);
        auctionRepository.save(auctionToSave);

        AuctionResponseDto savedAuctionInfo = new AuctionResponseDto(auctionToSave);
        System.out.println(savedAuctionInfo);
    }

    // 경매 수정
    @Test
    @Transactional
    void testEditAuction() {

        LocalDateTime now = LocalDateTime.now();
        Users user = new Users(null, "user", "user@example.com", "1234");
        usersRepository.save(user);

        Product productToSave = new Product(null, "productName", "description", 1000L, null, false, new ArrayList<>(), user, 10, null);
        productRepository.save(productToSave);
        Auction auctionToSave = new Auction(null, null, productToSave, now.minusMinutes(1), now.plusMinutes(5), AuctionStatus.PENDING, new ArrayList<>(), null, null);
        Auction savedAuction = auctionRepository.save(auctionToSave);
        Long auctionId = savedAuction.getId();

        // 저장 후 다시 조회
        Auction savedAuctionEntity = auctionRepository.findById(auctionId).orElseThrow();
        AuctionResponseDto savedAuctionInfo = new AuctionResponseDto(savedAuctionEntity);
        System.out.println("=============수정 전 : " + savedAuctionInfo.testToString());

        AuctionUpdateDto auctionUpdateDto = new AuctionUpdateDto(now.minusDays(1), now.plusDays(2));
        auctionService.updateAuction(auctionId, auctionUpdateDto);

        // 수정 후 다시 조회
        Auction afterUpdateInfo = auctionRepository.findById(auctionId).orElseThrow();
        AuctionResponseDto savedAuctionUpdateInfo = new AuctionResponseDto(afterUpdateInfo);
        System.out.println("==========수정 후 : " + savedAuctionUpdateInfo.testToString());
    }

}
