package com.wid.elevenmarket.persistence;

import com.wid.elevenmarket.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, Long> {
}
