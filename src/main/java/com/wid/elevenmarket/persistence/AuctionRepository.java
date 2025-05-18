package com.wid.elevenmarket.persistence;

import com.wid.elevenmarket.model.Auction;
import com.wid.elevenmarket.model.enums.AuctionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
    List<Auction> findByStatusAndStartDateLessThan(AuctionStatus status, LocalDateTime startDate);
}
