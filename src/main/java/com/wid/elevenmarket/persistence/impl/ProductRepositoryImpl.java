package com.wid.elevenmarket.persistence.impl;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wid.elevenmarket.model.Product;
import com.wid.elevenmarket.model.QProduct;
import com.wid.elevenmarket.persistence.ProductCustomRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductCustomRepository {
    private final JPAQueryFactory queryFactory;


    public ProductRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }


    @Override
    public List<Product> findProductListByRandom(Pageable pageable) {

        QProduct product = QProduct.product;

        return queryFactory.select(product)
                .from(product)
                .orderBy(Expressions.numberTemplate(Double.class, "rand()").asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
