package com.immfly.msorders.controller;

import com.immfly.msorders.dto.category.CategoryPagesResponseDto;
import com.immfly.msorders.dto.product.ProductPagesResponseDto;
import com.immfly.msorders.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(value = HttpStatus.OK)
    public ProductPagesResponseDto getProducts(Pageable pageable) {
        log.debug("[Products] getProducts request: [{}]", pageable);
        ProductPagesResponseDto productPages = productService.findAll(pageable);
        log.debug("[Products] getProducts response: [{}]", productPages.getCount());
        return productPages;
    }

}
