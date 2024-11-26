package com.main.web.siwa.dto.member.website;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActionDto {
    private Long memberId;
    private Long websiteId;
    private String action;
    private Boolean isAdded;
}
