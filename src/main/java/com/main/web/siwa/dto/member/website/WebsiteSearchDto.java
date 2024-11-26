package com.main.web.siwa.dto.member.website;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebsiteSearchDto {
    private Integer page;
    private Integer size;
    private String keyWord;
    private Long categoryId;
}
