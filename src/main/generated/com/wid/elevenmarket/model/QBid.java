package com.wid.elevenmarket.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBid is a Querydsl query type for Bid
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBid extends EntityPathBase<Bid> {

    private static final long serialVersionUID = -279669808L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBid bid = new QBid("bid");

    public final com.wid.elevenmarket.global.entity.QBaseTimeEntity _super = new com.wid.elevenmarket.global.entity.QBaseTimeEntity(this);

    public final QAuction auction;

    public final DateTimePath<java.time.LocalDateTime> bidDate = createDateTime("bidDate", java.time.LocalDateTime.class);

    public final QUsers bidder;

    public final NumberPath<java.math.BigDecimal> bidPrice = createNumber("bidPrice", java.math.BigDecimal.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<com.wid.elevenmarket.model.enums.BidStatus> status = createEnum("status", com.wid.elevenmarket.model.enums.BidStatus.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateAt = _super.updateAt;

    public QBid(String variable) {
        this(Bid.class, forVariable(variable), INITS);
    }

    public QBid(Path<? extends Bid> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBid(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBid(PathMetadata metadata, PathInits inits) {
        this(Bid.class, metadata, inits);
    }

    public QBid(Class<? extends Bid> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.auction = inits.isInitialized("auction") ? new QAuction(forProperty("auction"), inits.get("auction")) : null;
        this.bidder = inits.isInitialized("bidder") ? new QUsers(forProperty("bidder")) : null;
    }

}

