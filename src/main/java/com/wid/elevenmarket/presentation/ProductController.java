package com.wid.elevenmarket.presentation;

import com.wid.elevenmarket.business.ProductService;
import com.wid.elevenmarket.global.response.CommonResponseEntity;
import com.wid.elevenmarket.presentation.dto.product.req.ProductRequestDto;
import com.wid.elevenmarket.presentation.dto.product.req.ProductUpdateReqDto;
import com.wid.elevenmarket.presentation.dto.product.resp.ProductResponseDto;
import lombok.RequiredArgsConstructor;
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
    public CommonResponseEntity<ProductResponseDto> editProduct(@PathVariable Long productId, @RequestBody ProductUpdateReqDto productUpdateReqDto) {
        return success(productService.editProductInfo(productId, productUpdateReqDto));
    }

    @GetMapping("/{productId}")
    public CommonResponseEntity<ProductResponseDto> getProductInfo(@PathVariable Long productId) {
        return success(productService.getProductInfo(productId));
    }

    @PatchMapping("/delete/{productId}")
    public CommonResponseEntity<ProductResponseDto> deActiveProduct(@PathVariable Long productId) {
        return success(productService.deActiveProduct(productId));
    }
}
