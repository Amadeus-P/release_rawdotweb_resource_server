package com.main.web.siwa.admin.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberResponseDto {

    private Long totalCount;
    private Long totalPages;
    private Boolean nextPage;
    private Boolean prevPage;
    private List<MemberListDto> memberListDto;
    private List<Integer> pages;
}
