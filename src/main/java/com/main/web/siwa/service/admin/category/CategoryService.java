package com.main.web.siwa.service.admin.category;

import com.main.web.siwa.dto.admin.category.CategoryCreateDto;
import com.main.web.siwa.dto.admin.category.CategoryResponseDto;
import com.main.web.siwa.dto.admin.category.CategoryUpdateDto;

public interface CategoryService {
    CategoryResponseDto getList(Long parentId);
    CategoryCreateDto create(CategoryCreateDto category);
    CategoryUpdateDto update();
    void delete(Long categoryId);

}
