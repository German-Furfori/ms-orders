package com.immfly.msorders.service;

import com.immfly.msorders.dto.category.CategoryPagesResponseDto;
import com.immfly.msorders.dto.category.CategoryResponseDto;
import org.springframework.data.domain.Pageable;

public interface CategoryService {

    CategoryPagesResponseDto findAll(Pageable pageable);

    CategoryResponseDto findById(Long id);
}
