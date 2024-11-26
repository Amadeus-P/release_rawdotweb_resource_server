package com.main.web.siwa.service.admin.category;

import com.main.web.siwa.dto.admin.category.CategoryListDto;
import com.main.web.siwa.dto.admin.category.CategoryResponseDto;
import com.main.web.siwa.entity.Category;
import com.main.web.siwa.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DefaultCategoryService implements CategoryService {

    private final CategoryRepository categoryRepository;

    public DefaultCategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryResponseDto getList(Long parentId) {

        List<Category> categories;
        List<CategoryListDto> categoryListDtos = new ArrayList<>();

        if(parentId == null) { // 대분류
            categories = categoryRepository.findAllByParentIdIsNull();
        } else { // 나머지
            categories = categoryRepository.findAllByParentId(parentId);
        }
        
        for(Category c : categories) {
            System.out.println("c.getSubCategories()" + c.getSubCategories());
            CategoryListDto categoryListDto = CategoryListDto
                    .builder()
                    .id(c.getId())
                    .name(c.getName())
                    .parentId(c.getParentId())
                    .iconName(c.getIconName())
//                    .subCategories(c.getSubCategories())
                    .build();
            categoryListDtos.add(categoryListDto);
        }

        return CategoryResponseDto
                .builder()
                .categoryListDtos(categoryListDtos)
                .build();
    }//getList

}
