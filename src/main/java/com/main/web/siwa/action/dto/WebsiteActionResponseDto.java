package com.main.web.siwa.action.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebsiteActionResponseDto {
    private List<WebsiteActionDto> websiteActionDtos;
}
