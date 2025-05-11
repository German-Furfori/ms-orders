package com.immfly.msorders.dto.category;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class CategoryResponseDto {

    private Long id;

    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private CategoryResponseDto categoryRelated;

}
