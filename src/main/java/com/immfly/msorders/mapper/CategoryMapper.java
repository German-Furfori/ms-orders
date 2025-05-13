package com.immfly.msorders.mapper;

import com.immfly.msorders.dto.category.CategoryResponseDto;
import com.immfly.msorders.entity.Category;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    List<CategoryResponseDto> categoryPagesToCategoryResponse(Page<Category> categories);

    CategoryResponseDto categoryToCategoryResponse(Category category);
}
