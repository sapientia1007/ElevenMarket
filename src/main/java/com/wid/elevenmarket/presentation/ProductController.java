package com.wid.elevenmarket.presentation;

import com.wid.elevenmarket.business.ProductService;
import com.wid.elevenmarket.global.response.CommonResponseEntity;
import com.wid.elevenmarket.presentation.dto.product.req.ProductRequestDto;
import com.wid.elevenmarket.presentation.dto.product.req.ProductUpdateDto;
import com.wid.elevenmarket.presentation.dto.product.resp.ProductListResponseDto;
import com.wid.elevenmarket.presentation.dto.product.resp.ProductResponseDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import static com.wid.elevenmarket.global.response.CommonResponseEntity.success;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/enroll")
    public CommonResponseEntity<ProductResponseDto> enrollProduct(@RequestBody ProductRequestDto productRequestDto) {
        return success(productService.enrollProduct(productRequestDto));
    }

    @PatchMapping("/edit/{productId}")
    public CommonResponseEntity<ProductResponseDto> editProduct(@PathVariable Long productId, @RequestBody ProductUpdateDto productUpdateDto) {
        return success(productService.editProductInfo(productId, productUpdateDto));
    }

    @GetMapping("/{productId}")
    public CommonResponseEntity<ProductResponseDto> getProductInfo(@PathVariable Long productId) {
        return success(productService.getProductInfo(productId));
    }

    @PatchMapping("/delete/{productId}")
    public CommonResponseEntity<ProductResponseDto> deActiveProduct(@PathVariable Long productId) {
        return success(productService.deActiveProduct(productId));
    }

    @GetMapping("/search")
    public CommonResponseEntity<ProductListResponseDto> searchProduct(@RequestParam String keyword,
                                                                      @PageableDefault(size = 10, sort = "product_id", direction = Sort.Direction.DESC) Pageable pageable) {
        return success(productService.getProductsByKeyword(keyword, pageable));
    }

    @GetMapping("/random-main")
    public CommonResponseEntity<ProductListResponseDto> getRandomProducts(HttpSession session,
                                                                          @PageableDefault(size = 3) Pageable pageable) {
        return success(productService.getRandomProductsWithPaging(session.getId(), pageable));
    }

    @GetMapping("/random-querydsl")
    public CommonResponseEntity<ProductListResponseDto> getRandomProductsByQueryDsl(@PageableDefault(size = 3) Pageable pageable) {
        return success(productService.getRandomProductsWithPagingQuerydsl(pageable));
    }
}