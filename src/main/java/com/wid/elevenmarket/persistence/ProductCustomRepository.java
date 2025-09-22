package com.wid.elevenmarket.persistence;

import com.wid.elevenmarket.model.Product;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductCustomRepository {
    List<Product> findProductListByRandom(Pageable pageable);
}
