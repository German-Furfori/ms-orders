package com.immfly.msorders.dto.category;

import lombok.Data;

import java.util.List;

@Data
public class CategoryPagesResponseDto {

    private List<CategoryResponseDto> results;

    private Long count;

    private Long total;

}
