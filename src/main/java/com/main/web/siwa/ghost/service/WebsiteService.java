package com.main.web.siwa.ghost.service;

import com.main.web.siwa.ghost.dto.WebsiteListDto;
import com.main.web.siwa.ghost.dto.WebsiteResponseDto;
import com.main.web.siwa.ghost.dto.WebsiteSearchDto;

public interface WebsiteService {
    WebsiteResponseDto getList(Integer page, String title, Long categoryId);
    WebsiteResponseDto getList(WebsiteSearchDto websiteSearchDto);

    WebsiteListDto getById(Long id);

}
