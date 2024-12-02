package com.main.web.siwa.service.member.website;

import com.main.web.siwa.dto.ghost.website.WebsiteListDto;
import com.main.web.siwa.dto.ghost.website.WebsiteResponseDto;
import com.main.web.siwa.dto.ghost.website.WebsiteSearchDto;
import com.main.web.siwa.service.ghost.website.WebsiteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

    @RestController("websiteController")
    @RequestMapping("websites")
    public class WebsiteController {

        private WebsiteService websiteService;

        public WebsiteController(WebsiteService websiteService) {
            this.websiteService = websiteService;
        }

        @GetMapping
        public ResponseEntity<WebsiteResponseDto> getList(
                @ModelAttribute WebsiteSearchDto websiteSearchDto
        ) {
            if(websiteSearchDto.getPage() == null || websiteSearchDto.getSize() < 1) {
                websiteSearchDto.setPage(1);
            }
            System.out.println("websiteSearchDto.getCategory()" + websiteSearchDto.getCategoryId());
            System.out.println("websiteSearchDto.getKeyWord()" + websiteSearchDto.getKeyWord());
            System.out.println("websiteSearchDto.getPage()" + websiteSearchDto.getPage());
            System.out.println("websiteSearchDto.getSize()" + websiteSearchDto.getSize());
            WebsiteResponseDto responseDto = websiteService.getList(websiteSearchDto);
            return new ResponseEntity<>(responseDto, HttpStatus.OK); // 페이지 정보, 웹사이트 정보, 카테고리 정보
        }
    // GET + ID + 추천 웹사이트
    @GetMapping("/{websiteId}")
    public ResponseEntity<WebsiteListDto> getOne(
            @PathVariable(value = "websiteId", required = true) Long websiteId
    ) {
        return new ResponseEntity<>(websiteService.getById(websiteId),HttpStatus.OK);
    }
}
