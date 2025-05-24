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
    List<Auction> findByStatusAndStartDateLessThan(AuctionStatus status, LocalDateTime startDate);
    List<Auction> findByEndDateLessThanAndWinnerIsNotNull(LocalDateTime endDate);
    List<Auction> findByEndDateLessThanAndWinnerIsNull(LocalDateTime endDate);
    List<Auction> findByStatus(AuctionStatus status);

    @Modifying
    @Query("UPDATE Auction a SET a.status = :newStatus WHERE a.status = :currentStatus AND a.startDate <= :now")
    int updateStatusFromPendingToLive(@Param("newStatus") AuctionStatus newStatus,
                         @Param("currentStatus") AuctionStatus currentStatus,
                         @Param("now") LocalDateTime now);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Auction a SET a.status = 'END' WHERE a.endDate <= :now AND a.status <> 'END'")
    int updateStatusFromLiveToEnd(@Param("now") LocalDateTime now);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Auction a SET a.status = 'FAILED' WHERE a.winner IS NULL AND a.endDate <= :now AND a.status <> 'FAILED'")
    int updateStatusFromLiveToEndWithoutWinner(@Param("now") LocalDateTime now);
}
