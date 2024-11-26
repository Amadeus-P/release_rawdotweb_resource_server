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
public class CategoryDetailDto {
    private Long id;
    private String name;
    private String korName;
    private String engName;
    private Long parentId;
    private String iconName;

    private List<Category> subCategories;
}
