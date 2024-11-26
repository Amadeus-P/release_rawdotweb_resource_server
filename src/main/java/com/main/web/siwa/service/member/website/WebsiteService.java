package com.main.web.siwa.service.member.website;

import com.main.web.siwa.dto.member.website.*;
import org.springframework.web.multipart.MultipartFile;

public interface WebsiteService {
    WebsiteResponseDto getList(Integer page, String title, Long categoryId);
    WebsiteResponseDto getList(WebsiteSearchDto websiteSearchDto);

    WebsiteListDto getById(Long id);
    WebsiteCreateDto create(WebsiteCreateDto websiteCreateDto, MultipartFile image);
    WebsiteListDto update(WebsiteListDto websiteListDto, MultipartFile newImage);
    void delete(Long id);

    void setStatusAction(ActionDto actionDto);

    ActionResponseDto getStatusAction(Long memberId);

//    void
}
