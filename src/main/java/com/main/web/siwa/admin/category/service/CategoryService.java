package com.main.web.siwa.admin.category.service;

import com.main.web.siwa.admin.category.dto.CategoryCreateDto;
import com.main.web.siwa.admin.category.dto.CategoryResponseDto;
import com.main.web.siwa.admin.category.dto.CategoryUpdateDto;

public interface CategoryService {
    CategoryResponseDto getList(Long parentId);
    CategoryCreateDto create(CategoryCreateDto category);
    CategoryUpdateDto update();
    void delete(Long categoryId);

}
