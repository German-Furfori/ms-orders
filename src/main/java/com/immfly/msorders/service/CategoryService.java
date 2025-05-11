package com.immfly.msorders.service;

import com.immfly.msorders.dto.category.CategoryPagesResponseDto;
import org.springframework.data.domain.Pageable;

public interface CategoryService {

    CategoryPagesResponseDto findAll(Pageable pageable);

}
