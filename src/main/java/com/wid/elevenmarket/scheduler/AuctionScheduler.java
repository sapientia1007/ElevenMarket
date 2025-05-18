package com.wid.elevenmarket.scheduler;

import com.wid.elevenmarket.model.Auction;
import com.wid.elevenmarket.model.enums.AuctionStatus;
import com.wid.elevenmarket.persistence.AuctionRepository;
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

    @Scheduled(fixedRate = 10_000)
    @Transactional
    public void processAuctions() {
        LocalDateTime now = LocalDateTime.now();
        List<Auction> auctionToStart = auctionRepository.findByStatusAndStartDateLessThan(AuctionStatus.PENDING, now);

        for (Auction auction : auctionToStart) {
            auction.changeStatus(AuctionStatus.LIVE);
            auctionRepository.save(auction);

            Auction savedAuction = auctionRepository.findById(auction.getId()).orElseThrow();
        }
    }
}

