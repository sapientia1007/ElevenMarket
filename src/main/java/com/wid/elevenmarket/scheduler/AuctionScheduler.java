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

        int updatedCount = auctionRepository.updateStatusFromPendingToLive(now);
        System.out.println("경매 시작 상태 변경 건수: " + updatedCount);
    }

    @Scheduled(fixedRate = 10_000)
    @Transactional
    public void endAuctions() {

        LocalDateTime now = LocalDateTime.now();

        int updatedCount = auctionRepository.updateStatusFromLiveToEnd(now);
        System.out.println("경매 종료 처리 건수: " + updatedCount);

        List<Auction> AuctionsEnd = auctionRepository.findByStatus(AuctionStatus.END);

        for (Auction auction : AuctionsEnd) {
            if (bidRepository.findHighestBidByAuctionId(auction).isPresent()) {
                Bid highestBid = bidRepository.findHighestBidByAuctionId(auction).orElseThrow();
                auction.setAuctionWinner(highestBid.getBidder());
                auction.changeStatus(AuctionStatus.WINNER);
                System.out.println("경매 ID "+auction.getId()+"  처리 성공: ");
            } else {
                auction.changeStatus(AuctionStatus.FAILED);
                System.out.println("경매 ID "+auction.getId()+"  처리 실패: ");
            }
            auctionRepository.save(auction);
        }
    }

}