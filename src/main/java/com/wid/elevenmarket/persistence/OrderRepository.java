package com.wid.elevenmarket.persistence;

import com.wid.elevenmarket.model.Orders;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Orders, Long> {

    @EntityGraph(attributePaths = {"buyer"})
    Optional<Orders> findById(Long id);

    @Query("SELECT o FROM Orders o JOIN FETCH o.buyer JOIN FETCH o.product WHERE o.id = :id")
    Optional<Orders> findByIdWithBuyerAndProduct(@Param("id") Long id);

    @Query("SELECT o from Orders o WHERE o.buyer.id = :buyerId")
    List<Orders> findByBuyerId(@Param("buyerId") Long buyerId);
}
