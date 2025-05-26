package com.wid.elevenmarket.persistence;

import com.wid.elevenmarket.model.Product;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p where p.id = :id")
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "3000")}) // 3초 대기
    Optional<Product> findProductByIdWithPessimisticLock(@Param("id") Long id);
}
