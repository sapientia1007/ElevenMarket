package com.wid.elevenmarket.model.enums;

public enum AuctionStatus {
    PENDING,    // 입찰 진행 중
    WINNER,     // 낙찰됨
    CANCELLED,  // 입찰 취소됨
    FAILED      // 입찰 실패(예: 입찰 조건 미달 등)
}