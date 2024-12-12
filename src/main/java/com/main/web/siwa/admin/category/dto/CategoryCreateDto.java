package com.main.web.siwa.admin.category.dto;

import com.main.web.siwa.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryCreateDto {
    private Long id;
    private String name;
    private String iconName;
    private Long parentId;

    private List<Category> subCategories;
}
