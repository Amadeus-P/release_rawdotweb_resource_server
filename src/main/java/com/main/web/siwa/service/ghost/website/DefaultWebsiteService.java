package com.main.web.siwa.service.ghost.website;

import com.main.web.siwa.entity.Website;
import com.main.web.siwa.entity.WebsiteImage;
import com.main.web.siwa.dto.ghost.website.WebsiteListDto;
import com.main.web.siwa.dto.ghost.website.WebsiteResponseDto;
import com.main.web.siwa.dto.ghost.website.WebsiteSearchDto;
import com.main.web.siwa.dto.websiteImage.WebsiteImageListDto;
import com.main.web.siwa.repository.WebsiteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service("websiteService")
public class DefaultWebsiteService implements WebsiteService {

    private WebsiteRepository websiteRepository;
    private ModelMapper modelMapper;

    public DefaultWebsiteService(WebsiteRepository websiteRepository
            , ModelMapper modelMapper){
        this.websiteRepository = websiteRepository;
        this.modelMapper = modelMapper;
    }


    // 전달 받은 데이터를
    @Override
    public WebsiteResponseDto getList(WebsiteSearchDto websiteSearchDto) {
        return getList(websiteSearchDto.getPage(), websiteSearchDto.getKeyWord(), websiteSearchDto.getCategoryId());
    }

    // 여기서 꺼내 쓰기
    @Override
    public WebsiteResponseDto getList(Integer page, String keyWord, Long categoryId) {
        System.out.println(categoryId);
        System.out.println(keyWord);
        System.out.println(page);

        int size = 30; // 페이지당 보여줄 웹사이트 개수
        if (page == null || page < 1) {
            page = 1;
        }

        Sort sort = Sort.by("regDate").descending();
        Pageable pageable = PageRequest.of(page-1, size, sort); // 0
        Page<Website> websitePage = websiteRepository.findAll(keyWord, categoryId, page, size);

        // Entity를 DTO로 바꾸기 > Stream
        List<WebsiteListDto> websiteListDtos = websitePage
                .getContent()
                .stream()
                .map((Website website) -> {
                    WebsiteListDto websiteListDto = modelMapper.map(website, WebsiteListDto.class);

                    // 이미지, null이 아닐 경우에만 매핑
                    List<WebsiteImageListDto> imageListDtos =
                            website.getImages() != null ?
                                    website.getImages()
                                            .stream()
                                            .map((WebsiteImage image) -> modelMapper.map(image, WebsiteImageListDto.class))
                                            .toList() : new ArrayList<>();
                    websiteListDto.setImages(imageListDtos);

                    return websiteListDto;
                })
                .toList();

        // 조회된 모든 웹사이트 갯수
        long websiteTotalCount = websitePage.getTotalElements();
        long websiteTotalPages = websitePage.getTotalPages();
        boolean nextPage = websitePage.hasNext();
        boolean previousPage = websitePage.hasPrevious();

        // 현재 페이지
        page = (page == null) ? 1 : page;

        // 페이지 구간
        Integer offset = (page -1) % 5;

        // 현재 페이지에서 다른 페이지 구하기
        Integer startNum = page - offset;
        List<Integer> pages = IntStream
                .range(startNum, startNum+5)
                .boxed().toList();

        return WebsiteResponseDto
                .builder()
                .websiteListDtos(websiteListDtos)
                .websiteTotalCount(websiteTotalCount)
                .websiteTotalPages(websiteTotalPages)
                .nextPage(nextPage)
                .prevPage(previousPage)
                .pages(pages)

                .build();
    }
    // GET + id
    @Override
    public WebsiteListDto getById(Long id) {
       Website website =  websiteRepository
               .findById(id)
               .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 웹사이트입니다. id = " + id));
        return modelMapper.map(website, WebsiteListDto.class);
    }
}
