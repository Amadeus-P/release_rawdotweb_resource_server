package com.main.web.siwa.controller.admin.category;

import com.main.web.siwa.dto.admin.category.CategoryResponseDto;
import com.main.web.siwa.service.admin.category.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("categoryController")
@RequestMapping("categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<CategoryResponseDto> getMainCategoryList(
            @RequestParam(value = "parentId", required = false) Long parentId
    ) {
        CategoryResponseDto responseDto = categoryService.getList(parentId);
        System.out.println("responseDto: " + responseDto);
        System.out.println("parentId: " + parentId);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

}
