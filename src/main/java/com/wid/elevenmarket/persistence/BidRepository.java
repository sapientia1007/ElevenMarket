package com.wid.elevenmarket.persistence;

import com.wid.elevenmarket.model.Auction;
import com.wid.elevenmarket.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid, Long> {

    @Query("SELECT b FROM Bid b where b.auction = :auction AND b.bidPrice = (SELECT MAX(b2.bidPrice) FROM Bid b2 WHERE b2.auction = :auction)")
    Optional<Bid> findHighestBidByAuctionId(@Param("auction")Auction auction);

}
