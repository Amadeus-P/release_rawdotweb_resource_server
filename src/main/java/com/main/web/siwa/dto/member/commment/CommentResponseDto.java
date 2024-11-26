package com.main.web.siwa.dto.member.commment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponseDto {

    private Long totalCount;
    private Long totalPages;
    private List<CommentListDto> commentListDtos;
    private List<Integer> pages;
}
