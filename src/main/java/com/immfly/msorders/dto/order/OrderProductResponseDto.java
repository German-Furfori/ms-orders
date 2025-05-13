package com.immfly.msorders.dto.order;

import com.immfly.msorders.dto.product.ProductResponseDto;
import lombok.Data;

@Data
public class OrderProductResponseDto {

    private Long id;

    private ProductResponseDto product;

    private Integer quantity;

}
