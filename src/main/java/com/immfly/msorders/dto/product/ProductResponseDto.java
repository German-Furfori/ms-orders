package com.immfly.msorders.dto.product;

import com.immfly.msorders.dto.category.CategoryResponseDto;
import lombok.Data;

@Data
public class ProductResponseDto {

    private Long id;

    private String name;

    private Long price;

    private CategoryResponseDto category;

    private String image;

    private Integer stock;

}
