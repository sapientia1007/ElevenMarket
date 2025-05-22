package com.wid.elevenmarket.scheduler;

import com.wid.elevenmarket.model.Auction;
import com.wid.elevenmarket.model.enums.AuctionStatus;
import com.wid.elevenmarket.persistence.AuctionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuctionScheduler {
    private final AuctionRepository auctionRepository;


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

        int updatedCount = auctionRepository.updateStatusFromLiveToEndWithWinner(now);
        System.out.println("낙찰자 있는 경매 종료 처리 건수: " + updatedCount);
    }

    @Scheduled(fixedRate = 10_000)
    @Transactional
    public void cancelWithoutWinnerAuctions() {
        LocalDateTime now = LocalDateTime.now();

        int updatedCount = auctionRepository.updateStatusFromLiveToEndWitoutWinner(now);
        System.out.println("낙찰자 없는 경매 실패 처리 건수: " + updatedCount);
    }
}