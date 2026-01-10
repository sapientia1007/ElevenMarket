package com.wid.elevenmarket.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAuction is a Querydsl query type for Auction
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAuction extends EntityPathBase<Auction> {

    private static final long serialVersionUID = 671241110L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAuction auction = new QAuction("auction");

    public final com.wid.elevenmarket.global.entity.QBaseTimeEntity _super = new com.wid.elevenmarket.global.entity.QBaseTimeEntity(this);

    public final ListPath<Bid, QBid> bids = this.<Bid, QBid>createList("bids", Bid.class, QBid.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final DateTimePath<java.time.LocalDateTime> endDate = createDateTime("endDate", java.time.LocalDateTime.class);

    public final QBid highestBid;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DatePath<java.time.LocalDate> isDeleted = createDate("isDeleted", java.time.LocalDate.class);

    public final QProduct product;

    public final DateTimePath<java.time.LocalDateTime> startDate = createDateTime("startDate", java.time.LocalDateTime.class);

    public final EnumPath<com.wid.elevenmarket.model.enums.AuctionStatus> status = createEnum("status", com.wid.elevenmarket.model.enums.AuctionStatus.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateAt = _super.updateAt;

    public final QUsers winner;

    public QAuction(String variable) {
        this(Auction.class, forVariable(variable), INITS);
    }

    public QAuction(Path<? extends Auction> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAuction(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAuction(PathMetadata metadata, PathInits inits) {
        this(Auction.class, metadata, inits);
    }

    public QAuction(Class<? extends Auction> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.highestBid = inits.isInitialized("highestBid") ? new QBid(forProperty("highestBid"), inits.get("highestBid")) : null;
        this.product = inits.isInitialized("product") ? new QProduct(forProperty("product"), inits.get("product")) : null;
        this.winner = inits.isInitialized("winner") ? new QUsers(forProperty("winner")) : null;
    }

}

