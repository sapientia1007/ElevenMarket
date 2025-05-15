package com.wid.elevenmarket.persistence;

import com.wid.elevenmarket.model.Auction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
}
