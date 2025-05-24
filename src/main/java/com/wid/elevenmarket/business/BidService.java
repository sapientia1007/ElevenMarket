package com.wid.elevenmarket.business;

import com.wid.elevenmarket.global.exception.CustomException;
import com.wid.elevenmarket.model.Auction;
import com.wid.elevenmarket.model.Bid;
import com.wid.elevenmarket.model.Users;
import com.wid.elevenmarket.model.enums.AuctionStatus;
import com.wid.elevenmarket.persistence.*;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.http.HttpStatus;
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

    @Transactional
    public void processBid(Long userId, Long auctionId, BigDecimal bidPrice) {
        String lockKey = "auction:bid:lock:" + auctionId;
        RLock lock = redissonClient.getLock(lockKey);
        boolean locked = false;

        try {
            locked = lock.tryLock(3, 6, TimeUnit.SECONDS);
            if (!locked) {
                throw new CustomException("다른 사용자가 입찰 중입니다.", HttpStatus.CONFLICT);
            }
            // 경매 상태 확인
            Auction savedAuction = auctionRepository.findById(auctionId).orElseThrow(() -> new CustomException("경매가 존재하지 않습니다.", HttpStatus.NOT_FOUND));
            if (savedAuction.getStatus() != AuctionStatus.LIVE) {
                throw new CustomException("진행 중인 경매가 아닙니다.", HttpStatus.NOT_FOUND);
            }
            Users savedUser = usersRepository.findById(userId).orElseThrow(() -> new CustomException("존재하지 않는 회원입니다", HttpStatus.NOT_FOUND));

            // 최고가 조회
            Bid highestBid = bidRepository.findHighestBidByAuctionId(savedAuction)
                    .orElse(null);
            if (highestBid != null && bidPrice.compareTo(highestBid.getBidPrice()) <= 0) {
                throw new CustomException("현재 최고가보다 높은 금액만 입찰 가능합니다", HttpStatus.BAD_REQUEST);
            }

            // 입찰 저장
            Bid newBid = Bid.createBid(savedUser, bidPrice, LocalDateTime.now(), savedAuction);
            bidRepository.save(newBid);
        } catch (InterruptedException e) {
            throw new CustomException("락 획득 실패 " + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        finally {
            if (locked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

}
