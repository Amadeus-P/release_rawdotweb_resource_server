package com.main.web.siwa.dto.member.commment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentCreateDto {
    private Long id;
    private String content;
    private Instant regDate;

    private Long memberId;
    private Long websiteId;
}
