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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final UsersRepository usersRepository;
    private final StringRedisTemplate redisTemplate;

    private static final String PRODUCT_ALL_IDS_KEY = "product:all:ids";


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

    // 활성화된 상품만 Redis에 저장
    @Transactional
    public void syncRedisProductIds() {
        List<Long> activeProductIds = productRepository.findAllActiveProductIds();
        redisTemplate.delete(PRODUCT_ALL_IDS_KEY);
        if (!activeProductIds.isEmpty()) {
            redisTemplate.opsForSet().add(
                    PRODUCT_ALL_IDS_KEY,
                    activeProductIds.stream().map(String::valueOf).toArray(String[]::new)
            );
        }
    }

    // 메인페이지 랜덤+페이징 조회
    public ProductListResponseDto getRandomProductsWithPaging(Pageable pageable) {
        Set<String> allIds = redisTemplate.opsForSet().members(PRODUCT_ALL_IDS_KEY);
        if (allIds == null || allIds.isEmpty()) {
            syncRedisProductIds();
            allIds =  redisTemplate.opsForSet().members(PRODUCT_ALL_IDS_KEY);
            if (allIds == null || allIds.isEmpty()) {
                return new ProductListResponseDto(Collections.emptyList(),
                        pageable.getPageNumber(), 0, 0);
            }
        }

        List<String> idList = new ArrayList<>(allIds);
        Collections.shuffle(idList);

        // 페이징: offset, limit
        int startIdx = (int) pageable.getOffset();
        int endIdx = Math.min(startIdx + pageable.getPageSize(), idList.size());

        List<Long> pickedIds = idList.subList(startIdx, endIdx)
                .stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());

        List<Product> content = productRepository.findAllById(pickedIds);

        List<ProductResponseDto> responseDtoList = content.stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());

        int totalPages = (int) Math.ceil(idList.size() / (double) pageable.getPageSize());

        return new ProductListResponseDto(
                responseDtoList,
                pageable.getPageNumber(),
                totalPages,
                idList.size()
        );
    }
}
