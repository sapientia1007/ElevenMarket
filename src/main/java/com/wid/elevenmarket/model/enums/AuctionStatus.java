package com.wid.elevenmarket.model.enums;

public enum AuctionStatus {
    PENDING,    // 경매 시작 전
    LIVE, // 경매 진행 중
    WINNER,     // 낙찰됨 (경매 종료)
    CANCELLED,  // 입찰 취소됨
    FAILED      // 입찰 실패
}