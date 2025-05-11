package com.immfly.msorders.dto.category;

import lombok.Data;

@Data
public class CategoryResponseDto {

    private Long id;

    private String name;

    private CategoryResponseDto categoryRelated;

}
