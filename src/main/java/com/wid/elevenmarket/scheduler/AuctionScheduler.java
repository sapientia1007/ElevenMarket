package com.wid.elevenmarket.scheduler;

import com.wid.elevenmarket.model.Auction;
import com.wid.elevenmarket.model.Bid;
import com.wid.elevenmarket.model.enums.AuctionStatus;
import com.wid.elevenmarket.persistence.AuctionRepository;
import com.wid.elevenmarket.persistence.BidRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuctionScheduler {

    private final AuctionRepository auctionRepository;
    private final BidRepository bidRepository;

    @Scheduled(fixedRate = 10_000)
    @Transactional
    public void processAuctions() {

        LocalDateTime now = LocalDateTime.now();

        int updatedCount = auctionRepository.updateStatusFromPendingToLive(
                AuctionStatus.LIVE, AuctionStatus.PENDING, now);
        System.out.println("경매 시작 상태 변경 건수: " + updatedCount);
    }

    @Scheduled(fixedRate = 10_000)
    @Transactional
    public void endWithWinnerAuctions() {

        LocalDateTime now = LocalDateTime.now();

        int updatedCount = auctionRepository.updateStatusFromLiveToEnd(now);
        System.out.println("경매 종료 처리 건수: " + updatedCount);
        List<Auction> AuctionsEnd = auctionRepository.findByStatus(AuctionStatus.END);

        for (Auction auction : AuctionsEnd) {
            try {
                Bid highestBid = bidRepository.findHighestBidByAuctionId(auction).orElseThrow();
                auction.setAuctionWinner(highestBid.getBidder());
                auction.changeStatus(AuctionStatus.WINNER);
            } catch (Exception e) {
                System.out.println("경매 ID "+auction.getId()+"  처리 실패: " + e.getMessage());
                throw e;
            }
        }
    }

    @Scheduled(fixedRate = 10_000)
    @Transactional
    public void cancelWithoutWinnerAuctions() {
        LocalDateTime now = LocalDateTime.now();

        int updatedCount = auctionRepository.updateStatusFromLiveToEndWithoutWinner(now);
        System.out.println("낙찰자 없는 경매 실패 처리 건수: " + updatedCount);
    }
}