package com.main.web.siwa.service.ghost.website;

import com.main.web.siwa.dto.ghost.website.WebsiteListDto;
import com.main.web.siwa.dto.ghost.website.WebsiteResponseDto;
import com.main.web.siwa.dto.ghost.website.WebsiteSearchDto;

public interface WebsiteService {
    WebsiteResponseDto getList(Integer page, String title, Long categoryId);
    WebsiteResponseDto getList(WebsiteSearchDto websiteSearchDto);

    WebsiteListDto getById(Long id);

}
