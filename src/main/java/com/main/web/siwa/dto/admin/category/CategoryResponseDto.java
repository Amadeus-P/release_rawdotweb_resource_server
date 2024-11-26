package com.main.web.siwa.dto.admin.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponseDto {

//    private Long CategoryTotalCount;
    private List<CategoryListDto> categoryListDtos;

}
