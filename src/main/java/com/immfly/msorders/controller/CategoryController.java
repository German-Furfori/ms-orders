package com.immfly.msorders.controller;

import com.immfly.msorders.dto.category.CategoryPagesResponseDto;
import com.immfly.msorders.dto.category.CategoryResponseDto;
import com.immfly.msorders.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(value = HttpStatus.OK)
    public CategoryPagesResponseDto findAllCategories(Pageable pageable) {
        log.debug("[Categories] findAllCategories request: [{}]", pageable);
        CategoryPagesResponseDto categoryPages = categoryService.findAll(pageable);
        log.debug("[Categories] findAllCategories response: [{}]", categoryPages.getCount());
        return categoryPages;
    }

    @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(value = HttpStatus.OK)
    public CategoryResponseDto findCategoryById(@PathVariable Long id) {
        log.debug("[Categories] findCategoryById request: [{}]", id);
        CategoryResponseDto categoryResponse = categoryService.findById(id);
        log.debug("[Categories] findCategoryById response: [{}]", categoryResponse);
        return categoryResponse;
    }

}
