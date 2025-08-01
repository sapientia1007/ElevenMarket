package com.wid.elevenmarket.business;

import com.wid.elevenmarket.global.exception.CustomException;
import com.wid.elevenmarket.model.Auction;
import com.wid.elevenmarket.model.Bid;
import com.wid.elevenmarket.model.Users;
import com.wid.elevenmarket.model.enums.AuctionStatus;
import com.wid.elevenmarket.model.enums.BidStatus;
import com.wid.elevenmarket.persistence.*;
import com.wid.elevenmarket.presentation.dto.bid.req.BidProcessReqDto;
import com.wid.elevenmarket.presentation.dto.bid.resp.BidListResponseDto;
import com.wid.elevenmarket.presentation.dto.bid.resp.BidResponseDto;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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
    public BidResponseDto processBid(BidProcessReqDto bidProcessReqDto) {
        Long userId = bidProcessReqDto.getUserId();
        Long auctionId = bidProcessReqDto.getAuctionId();
        BigDecimal bidPrice = bidProcessReqDto.getBidPrice();
        String lockKey = "auction:bid:lock:" + auctionId;
        RLock lock = redissonClient.getLock(lockKey);
        boolean locked = false;

        try {
            locked = lock.tryLock(5, 30, TimeUnit.SECONDS);
            if (!locked) {
                throw new CustomException("다른 사용자가 입찰 중입니다.", HttpStatus.CONFLICT);
            }

            System.out.println("락 획득: 경매 " + auctionId + " / 사용자 " + userId);

            // 경매 상태 확인
            Auction savedAuction = auctionRepository.findById(auctionId).orElseThrow(() -> new CustomException("경매가 존재하지 않습니다.", HttpStatus.NOT_FOUND));
            if (savedAuction.getStatus() != AuctionStatus.LIVE) {
                throw new CustomException("진행 중인 경매가 아닙니다.", HttpStatus.NOT_FOUND);
            }
            Users savedUser = usersRepository.findById(userId).orElseThrow(() -> new CustomException("존재하지 않는 회원입니다", HttpStatus.NOT_FOUND));

            // 최고가 조회
            Bid currentHighestBid = bidRepository.findHighestBidByAuctionId(savedAuction.getId())
                    .orElse(null);

            Bid newBid = Bid.createBid(savedUser, bidPrice, LocalDateTime.now(), savedAuction);
            bidRepository.save(newBid);
            if (currentHighestBid == null) {
                return new BidResponseDto(newBid);
            }
            if (bidPrice.compareTo(currentHighestBid.getBidPrice()) <= 0) {
                throw new CustomException("현재 최고가보다 높은 금액만 입찰 가능합니다", HttpStatus.BAD_REQUEST);
            }
            if (bidPrice.compareTo(currentHighestBid.getBidPrice()) > 0) {
                updateHighestBid(auctionId, newBid, currentHighestBid);
            }
            return new BidResponseDto(newBid);
        } catch (InterruptedException e) {
            throw new CustomException("락 획득 실패 " + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        finally {
            if (locked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    // 입찰 취소
    @Transactional
    public BidResponseDto cancelBid(Long bidId) {
        Bid savedBid = bidRepository.findById(bidId).orElseThrow(() -> new CustomException("존재하지 않는 입찰 정보에요", HttpStatus.NOT_FOUND));
        savedBid.changeStatusBid(BidStatus.CANCELLED);
        return new BidResponseDto(savedBid);
    }

    // 최고가 갱신
    @Transactional
    public void updateHighestBid(Long auctionId, Bid newHighestBid, Bid currentHighestBid) {
        Auction savedAuction = auctionRepository.findById(auctionId).orElseThrow(() -> new CustomException("존재하지 않은 경매에요", HttpStatus.NOT_FOUND));

        currentHighestBid.changeStatusBid(BidStatus.DISQUALIFIED);
        bidRepository.save(currentHighestBid);

        BigDecimal newBidPrice = newHighestBid.getBidPrice();
        BigDecimal currentBidPrice = currentHighestBid.getBidPrice();

        if (newBidPrice.compareTo(currentBidPrice) > 0) {
            savedAuction.updateHighestBid(newHighestBid);
            auctionRepository.save(savedAuction);
        }
    }

    // 사용자별 입찰 목록
    public BidListResponseDto getBidListByUserId(Long userId) {
        List<BidResponseDto> savedBidsByUserId = bidRepository.findBidsByBidder(userId).stream().map(BidResponseDto::new).toList();
        return new BidListResponseDto(savedBidsByUserId);
    }

    // 경매별 입찰 목록
    public BidListResponseDto getBidListByAuctionId(Long auctionId) {
        List<BidResponseDto> savedBidsByAuctionId = bidRepository.findByAuctionId(auctionId).stream().map(BidResponseDto::new).toList();
        return new BidListResponseDto(savedBidsByAuctionId);
    }
}
