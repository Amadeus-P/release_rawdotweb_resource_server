package com.main.web.siwa.websiteImage.service;

import com.main.web.siwa.entity.Website;
import com.main.web.siwa.entity.WebsiteImage;
import com.main.web.siwa.websiteImage.dto.WebsiteImageCreateDto;
import com.main.web.siwa.websiteImage.dto.WebsiteImageListDto;
import com.main.web.siwa.repository.WebsiteImageRepository;
import com.main.web.siwa.repository.WebsiteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service("websiteImageService")
public class DefaultWebsiteImageService implements WebsiteImageService {

    private WebsiteRepository websiteRepository;
    private WebsiteImageRepository websiteImageRepository;
    private ModelMapper modelMapper;

    public DefaultWebsiteImageService(
            WebsiteRepository websiteRepository,
            WebsiteImageRepository websiteImageRepository,
            ModelMapper modelMapper) {
        this.websiteRepository = websiteRepository;
        this.websiteImageRepository = websiteImageRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<WebsiteImageListDto> getList() {
        List<WebsiteImage> images = websiteImageRepository.findAll();
        return images
                .stream()
                .map(websiteImage
                        -> modelMapper.map(websiteImage, WebsiteImageListDto.class))
                .toList();

    }

    @Override
    public List<WebsiteImageListDto> getById(Long websiteId) {
        // 웹사이트 먼저 가져오기
        Website website = websiteRepository.findById(websiteId)
                .orElseThrow(() -> new RuntimeException("웹사이트가 없는 것 같아요.."));

        // 그러면 웹사이트의 이미지 가져올 준비 끝
        List<WebsiteImage> images = websiteImageRepository.findAllByWebsite(website);

        return images
                .stream()
                .map((WebsiteImage websiteImage)
                        -> modelMapper
                        .map(websiteImage, WebsiteImageListDto.class))
                .toList();
    }

    @Override
    public WebsiteImageCreateDto create(List<MultipartFile> images) {
        // 이미지 등록
        List<WebsiteImageListDto> imageListDtos = new ArrayList<>();
        Website website = new Website();

        // 프로젝트의 홈 디렉토리에서 서버 데이터를 클라이언트가 받기 위해서
        // 클라이언트에게 공개된 홈 디렉토리를 구해주는 코드임
        Path location = Paths.get(System.getProperty("user.dir"), "uploads", "website", "images");
        System.out.println("==============location:" + location.toString());

//        File file = new File(location.toString());
//        if(file != null && !file.isEmpty()) {
//            // 고유 파일명 생성
//            String originalFilename = file.getOriginalFilename();
//            String fileName = originalFilename;
//            int count = 1;
//        }

        for (MultipartFile image : images) {
            // 이미지 경로

            // Entity(JPA -> DB)에 저장
            WebsiteImage websiteImage = WebsiteImage
                    .builder()
                    .src(image.getOriginalFilename())
                    .website(website)
//                    .isDefault() // 컬렉션의 가장 첫 번째 이미지를 대표 이미지로.
                    .build();

            WebsiteImage createdWebsiteImage = websiteImageRepository.save(websiteImage);

            // 흐름상 데이터가 새로 추가되면 조회하는게 일반적이므로 등록한 데이터를 getList에게 전달하기 위한 DTO 객체
            // 객체 5원칙에 벗어나긴 하지만 지키고 싶으면 getList()로 옮기면 된다.
            imageListDtos.add(modelMapper.map(createdWebsiteImage, WebsiteImageListDto.class));
        }
        return null;
    }
}
