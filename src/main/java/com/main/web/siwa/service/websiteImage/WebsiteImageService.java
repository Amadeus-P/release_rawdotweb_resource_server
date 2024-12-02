package com.main.web.siwa.service.websiteImage;

import com.main.web.siwa.dto.websiteImage.WebsiteImageCreateDto;
import com.main.web.siwa.dto.websiteImage.WebsiteImageListDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface WebsiteImageService {
    List<WebsiteImageListDto> getList();
    List<WebsiteImageListDto> getById(Long id);
    WebsiteImageCreateDto create(List<MultipartFile> images);
}
