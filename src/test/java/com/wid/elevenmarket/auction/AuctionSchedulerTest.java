package com.wid.elevenmarket.auction;

import com.wid.elevenmarket.model.Auction;
import com.wid.elevenmarket.model.Product;
import com.wid.elevenmarket.model.Users;
import com.wid.elevenmarket.model.enums.AuctionStatus;
import com.wid.elevenmarket.persistence.AuctionRepository;
import com.wid.elevenmarket.persistence.ProductRepository;
import com.wid.elevenmarket.persistence.UsersRepository;
import com.wid.elevenmarket.scheduler.AuctionScheduler;
import org.apache.catalina.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AuctionSchedulerTest {

    @Autowired
    private AuctionScheduler auctionScheduler;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private AuctionRepository auctionRepository;

    @Test
    public void testProcessAuctions() {

        LocalDateTime now = LocalDateTime.now();

        Product product = productRepository.findById(1L).orElseThrow();
        Auction auction1 = new Auction( null,null, product,
                LocalDateTime.of(2025, 5, 18, 23, 36, 0),
                LocalDateTime.of(2025, 5, 18, 23, 40, 0),
                AuctionStatus.PENDING,
                new ArrayList<>(), null);

        auctionRepository.save(auction1);
    }
}
