package com.main.web.siwa.ghost.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebsiteResponseDto {


    private Long websiteTotalCount;
    private Long websiteTotalPages;
    private Boolean nextPage;
    private Boolean prevPage;
    private List<WebsiteListDto> websiteListDtos;
    private List<Integer> pages;
}
