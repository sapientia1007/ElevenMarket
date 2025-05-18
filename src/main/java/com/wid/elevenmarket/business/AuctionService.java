package com.wid.elevenmarket.business;

import com.wid.elevenmarket.persistence.AuctionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly=true)
public class AuctionService {

    private final AuctionRepository auctionRepository;

    // 경매 등록


    // 경매 시작 (스케줄링)


    // 경매 종료 및 낙찰 확정
}
