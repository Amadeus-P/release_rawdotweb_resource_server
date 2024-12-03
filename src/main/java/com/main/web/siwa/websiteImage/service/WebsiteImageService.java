package com.main.web.siwa.websiteImage.service;

import com.main.web.siwa.websiteImage.dto.WebsiteImageCreateDto;
import com.main.web.siwa.websiteImage.dto.WebsiteImageListDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface WebsiteImageService {
    List<WebsiteImageListDto> getList();
    List<WebsiteImageListDto> getById(Long id);
    WebsiteImageCreateDto create(List<MultipartFile> images);
}
