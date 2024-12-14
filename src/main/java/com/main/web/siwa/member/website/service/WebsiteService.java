package com.main.web.siwa.member.website.service;

import com.main.web.siwa.member.website.dto.*;
import org.springframework.web.multipart.MultipartFile;

public interface WebsiteService {
    WebsiteResponseDto getList(Integer page,Integer size, String title, Long categoryId);
    WebsiteResponseDto getList(WebsiteSearchDto websiteSearchDto);

    WebsiteListDto getById(Long id);
    WebsiteCreateDto create(WebsiteCreateDto websiteCreateDto, MultipartFile image);
    WebsiteListDto update(WebsiteListDto websiteListDto, MultipartFile newImage);
    void delete(Long id);
}
