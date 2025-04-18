package com.main.web.siwa.member.website.dto;

import com.main.web.siwa.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebsiteUpdateDto {
    private Long id;
    private String title;
    private String url;
    private Instant regDate;

    // 카테고리 정보
    private Category category;
}
