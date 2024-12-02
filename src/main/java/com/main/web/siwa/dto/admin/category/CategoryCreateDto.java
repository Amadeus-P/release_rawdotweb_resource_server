package com.main.web.siwa.dto.admin.category;

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
    private String korName;
    private String engName;
    private String iconName;
    private Long parentId;

    private List<Category> subCategories;
}
