package com.immfly.msorders.dto.product;

import lombok.Data;
import java.util.List;

@Data
public class ProductPagesResponseDto {

    private List<ProductResponseDto> results;

    private Long count;

    private Long total;

}
