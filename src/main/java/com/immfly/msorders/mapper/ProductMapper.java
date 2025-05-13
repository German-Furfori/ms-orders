package com.immfly.msorders.mapper;

import com.immfly.msorders.dto.product.ProductResponseDto;
import com.immfly.msorders.entity.Product;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    List<ProductResponseDto> productPagesToProductResponse(Page<Product> products);

    ProductResponseDto productToProductResponse(Product product);

}
