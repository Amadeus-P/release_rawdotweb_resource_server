package com.main.web.siwa.dto.member.website;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebsiteCreateDto {
    private Long id;
    private String title;
    private String url;
    private Instant regDate;

    private Long memberId; // 서버에서 인증된 회원 아이디를 넣음(jwt 인증필터, SCH에서 꺼내야함)
    private Long categoryId;
}
