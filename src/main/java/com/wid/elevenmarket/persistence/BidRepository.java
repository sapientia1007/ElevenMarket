package com.wid.elevenmarket.persistence;

import com.wid.elevenmarket.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidRepository extends JpaRepository<Bid, Long> {
}
