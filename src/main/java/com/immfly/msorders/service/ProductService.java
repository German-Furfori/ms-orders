package com.immfly.msorders.service;

import com.immfly.msorders.dto.product.ProductPagesResponseDto;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    ProductPagesResponseDto findAll(Pageable pageable);

}
