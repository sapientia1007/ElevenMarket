package com.wid.elevenmarket.model;

import com.wid.elevenmarket.global.entity.BaseTimeEntity;
import com.wid.elevenmarket.model.enums.AuctionStatus;
import com.wid.elevenmarket.presentation.dto.auction.req.AuctionUpdateDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Auction extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auction_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winner_id")
    private Users winner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    private AuctionStatus status;

    @OneToMany(mappedBy = "auction")
    private List<Bid> bids = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "highest_bid_id")
    private Bid highestBid;

    @Column
    private LocalDate isDeleted;

    public void markAsDeleted() {
        this.isDeleted = LocalDate.now();
    }

    public void changeStatus(AuctionStatus newStatus) {
        this.status = newStatus;
    }

    public void setAuctionWinner(Users winner) {
        this.winner = winner;
    }

    public void updateHighestBid(Bid newHighestBid) { this.highestBid = newHighestBid; }

    public void updateAuctionInfo(AuctionUpdateDto auctionUpdateDto) {
        this.startDate = auctionUpdateDto.getStartTime();
        this.endDate = auctionUpdateDto.getEndTime();
    }

    public static Auction openAuction(Product product, LocalDateTime startDate, LocalDateTime endDate) {
        Auction auctionTosave = new Auction(null, null, product, startDate, endDate, AuctionStatus.PENDING, new ArrayList<>(), null, null);
        product.getAuctions().add(auctionTosave);
        return auctionTosave;
    }
}
