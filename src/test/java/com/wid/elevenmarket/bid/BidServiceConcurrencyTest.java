package com.wid.elevenmarket.bid;

import com.wid.elevenmarket.business.BidService;
import com.wid.elevenmarket.model.Auction;
import com.wid.elevenmarket.model.Product;
import com.wid.elevenmarket.model.Users;
import com.wid.elevenmarket.model.enums.AuctionStatus;
import com.wid.elevenmarket.persistence.AuctionRepository;
import com.wid.elevenmarket.persistence.BidRepository;
import com.wid.elevenmarket.persistence.ProductRepository;
import com.wid.elevenmarket.persistence.UsersRepository;
import com.wid.elevenmarket.presentation.dto.bid.req.BidProcessReqDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BidServiceConcurrencyTest {

    @Autowired
    private BidService bidService;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private AuctionRepository auctionRepository;
    @Autowired
    private BidRepository bidRepository;
    @Autowired
    private ProductRepository productRepository;

    @Test
    void testBidConcurrencyWithRedisLock() throws InterruptedException {
        // 테스트용 사용자, 상품, 경매 생성
        Users user0 = usersRepository.save(new Users(null, "user0", "user0@example.com", "1234"));
        Users user1 = usersRepository.save(new Users(null, "user1", "user1@example.com", "1234"));
        Users user2 = usersRepository.save(new Users(null, "user2", "user2@example.com", "1234"));
        Product product = new Product(null, "Test Product", "Test Description", 1000L, null, false, new ArrayList<>(), user0, 100, null);
        productRepository.save(product);
        LocalDateTime startDate = LocalDateTime.now().minusMinutes(1);
        LocalDateTime endDate = LocalDateTime.now().plusHours(1);
        Auction auction = Auction.openAuction(product, startDate, endDate);
        auction.changeStatus(AuctionStatus.LIVE);
        auctionRepository.save(auction);

        int threadCount = 5;
        CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; i++) {
            final Long userId = i % 2 == 0 ? user1.getId() : user2.getId();
            executor.submit(() -> {
                try {
                    BigDecimal bidPrice = BigDecimal.valueOf(1000 + (int)(Math.random() * 1000));
                    bidService.processBid(BidProcessReqDto.builder().userId(userId).auctionId(auction.getId()).bidPrice(bidPrice).build());
                    System.out.println("입찰 성공: " + userId + ", " + bidPrice);
                } catch (Exception e) {
                    System.out.println("입찰 실패: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        System.out.println("경매 " + auction.getId() + "의 입찰 내역: " + bidRepository.findByAuctionId(auction.getId()));
    }
}