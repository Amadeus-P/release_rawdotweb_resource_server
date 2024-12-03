package com.main.web.siwa.member.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileResponseDto {

    private Long totalCount;
    private Long totalPages;
    private Boolean nextPage;
    private Boolean prevPage;
    private List<ProfileListDto> profileListDto;
    private List<Integer> pages;
}
