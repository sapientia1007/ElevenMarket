package com.wid.elevenmarket.persistence;

import com.wid.elevenmarket.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
