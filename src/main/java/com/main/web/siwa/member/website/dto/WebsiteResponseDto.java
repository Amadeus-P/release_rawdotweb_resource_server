package com.main.web.siwa.member.website.dto;

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

    private Long totalCount;
    private Long totalPages;
    private Boolean nextPage;
    private Boolean prevPage;
    private List<WebsiteListDto> websiteListDtos;
    private List<Integer> pages;
}
