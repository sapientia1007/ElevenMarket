package com.wid.elevenmarket.business;

import com.wid.elevenmarket.global.exception.CustomException;
import com.wid.elevenmarket.model.Product;
import com.wid.elevenmarket.model.Users;
import com.wid.elevenmarket.persistence.ProductRepository;
import com.wid.elevenmarket.persistence.UsersRepository;
import com.wid.elevenmarket.presentation.dto.product.req.ProductRequestDto;
import com.wid.elevenmarket.presentation.dto.product.req.ProductUpdateReqDto;
import com.wid.elevenmarket.presentation.dto.product.resp.ProductListResponseDto;
import com.wid.elevenmarket.presentation.dto.product.resp.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final UsersRepository usersRepository;

    // 상품 등록
    @Transactional
    public ProductResponseDto enrollProduct(ProductRequestDto productRequestDto) {
        Users seller = usersRepository.findById(productRequestDto.getSellerId()).orElseThrow(() -> new CustomException("존재하지 않는 판매자예요", HttpStatus.NOT_FOUND));
        Product toSaveProduct = Product.enrollProduct(productRequestDto.getName(), productRequestDto.getDescription(), productRequestDto.getPrice(), seller, productRequestDto.getQuantity());
        productRepository.save(toSaveProduct);
        return new ProductResponseDto(toSaveProduct);
    }

    // 상품 수정
    @Transactional
    public ProductResponseDto editProductInfo(Long productId, ProductUpdateReqDto productUpdateReqDto) {
        Product savedProduct = productRepository.findById(productId).orElseThrow(() -> new CustomException("존재하지 않는 제품이에요", HttpStatus.NOT_FOUND));
        savedProduct.updateProductInfo(productUpdateReqDto);
        productRepository.save(savedProduct);
        return new ProductResponseDto(savedProduct);
    }

    // 상품 조회
    public ProductResponseDto getProductInfo(Long productId) {
        Product savedProduct = productRepository.findById(productId).orElseThrow(() -> new CustomException("존재하지 않는 제품이에요", HttpStatus.NOT_FOUND));
        return new ProductResponseDto(savedProduct);
    }

    // 상품 논리적 삭제
    @Transactional
    public ProductResponseDto deActiveProduct(Long productId) {
        Product savedProduct = productRepository.findById(productId).orElseThrow(() -> new CustomException("존재하지 않는 제품이에요", HttpStatus.NOT_FOUND));
        savedProduct.markAsDeleted();
        productRepository.save(savedProduct);
        return new ProductResponseDto(savedProduct);
    }

    // 상품 검색 - 키워드
    public ProductListResponseDto getProductsByKeyword(String keyword, Pageable pageable) {
        Page<ProductResponseDto> savedProducts = productRepository.findByKeyword(keyword, pageable).map(ProductResponseDto::new);
        return new ProductListResponseDto(savedProducts.getContent(), savedProducts.getNumber(), savedProducts.getTotalPages(), savedProducts.getTotalElements());
    }

}
