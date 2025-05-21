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

}
