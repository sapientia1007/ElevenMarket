package com.wid.elevenmarket.persistence;

import com.wid.elevenmarket.model.Product;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p where p.id = :id")
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "3000")}) // 3초 대기
    Optional<Product> findProductByIdWithPessimisticLock(@Param("id") Long id);

    @Modifying
    @Query("DELETE FROM Product p WHERE p.id IN :productIds")
    void deleteByIds(@Param("productIds") List<Long> productIds);

    @Query("SELECT p FROM Product p WHERE p.isDeleted <= :date")
    Page<Product> findByIsDeletedBefore(LocalDate date, Pageable pageable);

    @Query(
            value = "SELECT * FROM product WHERE MATCH(product_name, description) AGAINST(:keyword IN BOOLEAN MODE)",
            countQuery = "SELECT COUNT(*) FROM product WHERE MATCH(product_name, description) AGAINST(:keyword IN BOOLEAN MODE)",
            nativeQuery = true
    )
    Page<Product> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.productName LIKE %:keyword% OR p.description LIKE %:keyword%")
    Page<Product> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p.id FROM Product p WHERE p.isDeleted IS NULL")
    List<Long> findAllActiveProductIds();

}
