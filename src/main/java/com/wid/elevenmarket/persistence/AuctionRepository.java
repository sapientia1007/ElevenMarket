package com.wid.elevenmarket.persistence;

import com.wid.elevenmarket.model.Auction;
import com.wid.elevenmarket.model.enums.AuctionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AuctionRepository extends JpaRepository<Auction, Long> {

    @Modifying
    @Query("UPDATE Auction a SET a.status = 'LIVE' WHERE a.status = 'PENDING' AND a.startDate <= :now")
    int updateStatusFromPendingToLive(@Param("now") LocalDateTime now);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Auction a SET a.status = 'END' WHERE a.endDate <= :now AND a.status = 'LIVE'")
    int updateStatusFromLiveToEnd(@Param("now") LocalDateTime now);

    @Query("SELECT a FROM Auction a WHERE a.status = :status")
    List<Auction> findByStatus(@Param("status") AuctionStatus status);

    @Query("SELECT a FROM Auction a WHERE a.winner.id = :winnerId")
    List<Auction> findByWinnerId(@Param("winnerId") Long winnerId);

    @Query("SELECT a from Auction a WHERE a.id IN (SELECT b.auction.id from Bid b where b.bidder.id = :userId)")
    List<Auction> findByBidderId(@Param("userId") Long userId);
}
