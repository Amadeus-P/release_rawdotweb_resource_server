package com.main.web.siwa.usecase.ghost.websiteImage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebsiteImageListDto {

    private Long id;
    private Long websiteId;
    private String src;
    private Boolean isDefault;

}
