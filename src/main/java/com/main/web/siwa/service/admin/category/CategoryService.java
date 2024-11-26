package com.main.web.siwa.service.admin.category;

import com.main.web.siwa.dto.admin.category.CategoryResponseDto;

public interface CategoryService {
    CategoryResponseDto getList(Long parentId);

}
