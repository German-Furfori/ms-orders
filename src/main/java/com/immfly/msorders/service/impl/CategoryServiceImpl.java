package com.immfly.msorders.service.impl;

import com.immfly.msorders.dto.category.CategoryPagesResponseDto;
import com.immfly.msorders.entity.Category;
import com.immfly.msorders.mapper.CategoryMapper;
import com.immfly.msorders.repository.CategoryRepository;
import com.immfly.msorders.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    @Override
    public CategoryPagesResponseDto findAll(Pageable pageable) {
        Page<Category> categories = categoryRepository.findAll(pageable);
        return this.categoryPagesToCategoryPagesResponseDto(categories);
    }

    private CategoryPagesResponseDto categoryPagesToCategoryPagesResponseDto(Page<Category> categories) {
        CategoryPagesResponseDto categoryPagesResponseDto = new CategoryPagesResponseDto();
        categoryPagesResponseDto.setTotal(categories.getTotalElements());
        categoryPagesResponseDto.setCount((long) categories.getNumberOfElements());
        categoryPagesResponseDto.setResults(categoryMapper.categoryPagesToCategoryResponse(categories));

        return categoryPagesResponseDto;
    }

}
