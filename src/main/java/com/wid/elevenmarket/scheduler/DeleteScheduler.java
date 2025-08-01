package com.wid.elevenmarket.scheduler;

import com.wid.elevenmarket.model.Auction;
import com.wid.elevenmarket.model.Product;
import com.wid.elevenmarket.persistence.AuctionRepository;
import com.wid.elevenmarket.persistence.BidRepository;
import com.wid.elevenmarket.persistence.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeleteScheduler {

    private final ProductRepository productRepository;
    private final AuctionRepository auctionRepository;
    private final BidRepository bidRepository;

    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteProductAndRelatedInChunks() {
        LocalDate oneWeekAgo = LocalDate.now().minusDays(7);
        int chunkSize = 100;
        Pageable pageable = PageRequest.of(0, chunkSize);
        Page<Product> page;

        do {
            page = productRepository.findByIsDeletedBefore(oneWeekAgo, pageable);
            List<Product> products = page.getContent();

            // 상품 ID 목록 추출
            List<Long> productIds = products.stream()
                    .map(Product::getId)
                    .collect(Collectors.toList());

            // 연관 경매 ID 목록 추출 (BatchSize로 지연로딩 최적화)
            List<Long> auctionIds = products.stream()
                    .flatMap(p -> p.getAuctions().stream())
                    .map(Auction::getId)
                    .collect(Collectors.toList());

            // 벌크 삭제: 입찰 -> 경매 -> 상품 순서
            if (!auctionIds.isEmpty()) {
                bidRepository.deleteByAuctionIds(auctionIds);
            }
            if (!productIds.isEmpty()) {
                auctionRepository.deleteByProductIds(productIds);
                productRepository.deleteByIds(productIds);
            }

            pageable = pageable.next();
        } while (page.hasNext());
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteAuctionAndRelatedInChunks() {
        LocalDate oneWeekAgo = LocalDate.now().minusDays(7);
        int chunkSize = 100;
        Pageable pageable = PageRequest.of(0, chunkSize);
        Page<Auction> page;

        do {
            page = auctionRepository.findByIsDeletedBefore(oneWeekAgo, pageable);
            List<Auction> auctions = page.getContent();

            // 경매 ID 목록 추출
            List<Long> auctionIds = auctions.stream()
                    .map(Auction::getId)
                    .collect(Collectors.toList());

            // 벌크 삭제: 입찰 -> 경매 순서
            if (!auctionIds.isEmpty()) {
                bidRepository.deleteByAuctionIds(auctionIds);
                auctionRepository.deleteByIds(auctionIds);
            }

            pageable = pageable.next();
        } while (page.hasNext());
    }

}