package com.wid.elevenmarket.presentation.dto.product.resp;

import lombok.Getter;

import java.util.List;

@Getter
public class ProductListResponseDto {
    private List<ProductResponseDto> product;
    private int currentPage;
    private int totalPages;
    private long totalElements;

    public ProductListResponseDto(List<ProductResponseDto> product, int currentPage, int totalPages, long totalElements) {
        this.product = product;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }
}