package com.main.web.siwa.usecase.ghost.websiteImage.controller;

import com.main.web.siwa.usecase.ghost.websiteImage.dto.WebsiteImageListDto;
import com.main.web.siwa.usecase.ghost.websiteImage.service.WebsiteImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("websiteImageController")
@RequestMapping("websiteImage")
public class WebsiteImageController {

    private WebsiteImageService websiteImageService;

    public WebsiteImageController(WebsiteImageService websiteImageService) {
        this.websiteImageService = websiteImageService;
    }


    @GetMapping()
    public ResponseEntity<List<WebsiteImageListDto> > getList() {
        return new ResponseEntity<>(websiteImageService.getList(), HttpStatus.OK);
    }
    @GetMapping("{websiteId}")
    public ResponseEntity<List<WebsiteImageListDto>> getById(
            @RequestParam() Long websiteId
            ){
        return new ResponseEntity<>(websiteImageService.getById(websiteId), HttpStatus.OK);

    }

}
