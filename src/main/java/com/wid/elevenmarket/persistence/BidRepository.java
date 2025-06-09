package com.wid.elevenmarket.persistence;

import com.wid.elevenmarket.model.Auction;
import com.wid.elevenmarket.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid, Long> {

    @Query("SELECT b FROM Bid b WHERE b.auction = :auction AND b.status = 'ACTIVE' AND  b.bidPrice = (SELECT MAX(b2.bidPrice) FROM Bid b2 WHERE b2.auction = :auction)")
    Optional<Bid> findHighestBidByAuctionId(@Param("auction")Auction auction);

    @Query("SELECT b FROM Bid b WHERE b.bidder.id = :userId AND b.status = 'ACTIVE'")
    List<Bid> findBidsByBidder(@Param("userId") Long userId);

    @Query("SELECT b from Bid b WHERE b.auction.id = :auctionId AND b.status = 'ACTIVE'")
    List<Bid> findByAuctionId(@Param("auctionId") Long auctionId);
}
