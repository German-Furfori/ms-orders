package com.immfly.msorders.service.impl;

import com.immfly.msorders.dto.product.ProductPagesResponseDto;
import com.immfly.msorders.dto.product.ProductResponseDto;
import com.immfly.msorders.entity.Product;
import com.immfly.msorders.mapper.ProductMapper;
import com.immfly.msorders.repository.ProductRepository;
import com.immfly.msorders.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.NoSuchElementException;

import static com.immfly.msorders.constants.ErrorMessages.NON_EXISTING_PRODUCT;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    @Override
    public ProductPagesResponseDto findAll(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        return this.productPagesToProductPagesResponseDto(products);
    }

    @Override
    public ProductResponseDto findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(String.format(NON_EXISTING_PRODUCT, id)));

        return productMapper.productToProductResponse(product);
    }

    private ProductPagesResponseDto productPagesToProductPagesResponseDto(Page<Product> products) {
        ProductPagesResponseDto productPagesResponseDto = new ProductPagesResponseDto();
        productPagesResponseDto.setTotal(products.getTotalElements());
        productPagesResponseDto.setCount((long) products.getNumberOfElements());
        productPagesResponseDto.setResults(productMapper.productPagesToProductResponse(products));

        return productPagesResponseDto;
    }

}
