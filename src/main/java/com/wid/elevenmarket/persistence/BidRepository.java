package com.wid.elevenmarket.persistence;

import com.wid.elevenmarket.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid, Long> {

    @Query("SELECT b FROM Bid b WHERE b.auction.id = :auctionId AND b.status = 'ACTIVE' AND  b.bidPrice = (SELECT MAX(b2.bidPrice) FROM Bid b2 WHERE b2.auction.id = :auctionId)")
    Optional<Bid> findHighestBidByAuctionId(@Param("auctionId") Long auctionId);

    @Query("SELECT b FROM Bid b WHERE b.bidder.id = :userId AND b.status = 'ACTIVE'")
    List<Bid> findBidsByBidder(@Param("userId") Long userId);

    @Query("SELECT b from Bid b WHERE b.auction.id = :auctionId AND b.status = 'ACTIVE'")
    List<Bid> findByAuctionId(@Param("auctionId") Long auctionId);

    @Modifying
    @Query("DELETE FROM Bid b WHERE b.auction.id IN :auctionIds")
    void deleteByAuctionIds(@Param("auctionIds") List<Long> auctionIds);
}
