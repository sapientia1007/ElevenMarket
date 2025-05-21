package com.wid.elevenmarket.business;

import com.wid.elevenmarket.model.Auction;
import com.wid.elevenmarket.model.Bid;
import com.wid.elevenmarket.model.Users;
import com.wid.elevenmarket.persistence.*;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BidService {

    private final RedissonClient redissonClient;
    private final AuctionRepository auctionRepository;
    private final BidRepository bidRepository;
    private final UsersRepository usersRepository;

    public void processBid(Long userId, Long auctionId, BigDecimal bidPrice) {
        String lockKey = "auction:bid:lock:" + auctionId;
        RLock lock = redissonClient.getLock(lockKey);
        boolean locked = false;

        try {
            locked = lock.tryLock(3, 6, TimeUnit.SECONDS);
            if (!locked) {
                throw new IllegalStateException("다른 사용자가 입찰 중입니다.");
            }

            // 경매 상태 확인
            Auction savedAuction = auctionRepository.findById(auctionId).orElseThrow(() -> new IllegalStateException("경매가 존재하지 않습니다."));
            if (!savedAuction.getStatus().equals("LIVE")) {
                throw new IllegalStateException("진행 중인 경매가 아닙니다.");
            }

            // 입찰 저장
            Users savedUser = usersRepository.findById(userId).orElseThrow(() -> new IllegalStateException("존재하지 않는 회원입니다"));
            Bid newBid = Bid.createBid(savedUser, bidPrice, LocalDateTime.now(), savedAuction);
            bidRepository.save(newBid);

        } catch (InterruptedException e) {
            throw new RuntimeException("락 획득 실패 " + e);
        }
    }

    // 최고가 조회 - 낙찰가 확정

    // 낙찰 실패 - END 상태로 전환
}
