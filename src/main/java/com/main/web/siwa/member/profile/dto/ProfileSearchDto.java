package com.main.web.siwa.member.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileSearchDto {
    private Integer page;
    private Integer size;
    private String keyWord;
    private Long memberId;
}
