package com.wid.elevenmarket.bid;

import com.wid.elevenmarket.business.AuctionService;
import com.wid.elevenmarket.business.BidService;
import com.wid.elevenmarket.business.OrderService;
import com.wid.elevenmarket.model.Product;
import com.wid.elevenmarket.model.Users;
import com.wid.elevenmarket.persistence.ProductRepository;
import com.wid.elevenmarket.persistence.UsersRepository;
import com.wid.elevenmarket.presentation.dto.auction.req.AuctionRequestDto;
import com.wid.elevenmarket.presentation.dto.bid.req.BidProcessRequestDto;
import com.wid.elevenmarket.scheduler.AuctionScheduler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BidServiceTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private BidService bidService;
    @Autowired
    private AuctionService auctionService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private AuctionScheduler auctionScheduler;

    private List<Long> usersIds;
    private List<Long> auctionIds;

    @BeforeEach
    void setUp() {

        productRepository.deleteAll();
        usersRepository.deleteAll();
        usersIds = new ArrayList<>();
        auctionIds = new ArrayList<>();

        // 사용자 20명 생성
        for (int i = 0; i < 20; i++) {
            Users user = new Users(null, "user" + i, "user" + i + "_" + System.nanoTime() + "@example.com", "1234");
            usersRepository.save(user);
            usersIds.add(user.getId());
        }

        // 상품 및 경매 2개 생성
        Users owner = usersRepository.findById(usersIds.get(0)).orElseThrow();
        Product product1 = new Product(null, "TestProduct1", "Desc", 1000L, 500L, false, new ArrayList<>(), owner, 100, null);
        productRepository.save(product1);
        Product product2 = new Product(null, "TestProduct2", "Desc", 1000L, 500L, false, new ArrayList<>(), owner, 100, null);
        productRepository.save(product2);

        LocalDateTime now = LocalDateTime.now();
        auctionService.openAuction(new AuctionRequestDto(1L,
                now.minusMinutes(1), now.plusMinutes(5), 5000L));
        auctionService.openAuction(new AuctionRequestDto(2L,
                now.minusMinutes(1), now.plusMinutes(8), 5000L));

        auctionIds.add(1L);
        auctionIds.add(2L);

        // 스케줄러를 직접 호출해서 경매 상태를 LIVE로 변경
        auctionScheduler.processAuctions();
    }

    @Test
    void testRedistConnection() {
        redisTemplate.opsForValue().set("testKey", "Hello, Redis!");
        String value = redisTemplate.opsForValue().get("testKey");
        System.out.println("Redis Value: " + value);
    }

    @Test
    void testBidConcurrency() throws InterruptedException {

        int threadCount = 15;
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        Random random = new Random();

        for (int i = 1; i < 1 + threadCount; i++) {
            final Long userId = usersIds.get(i);
            executorService.submit(() -> {
                try {
                    Long auctionId = auctionIds.get(random.nextInt(auctionIds.size()));
                    BigDecimal price = BigDecimal.valueOf(1000 + random.nextInt(1000));
                    bidService.processBid(BidProcessRequestDto.builder().userId(userId).auctionId(auctionId).bidPrice(price).build());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();

        // 결과 검증: 최고가, 입찰 건수, 낙찰자 등
    }
}
