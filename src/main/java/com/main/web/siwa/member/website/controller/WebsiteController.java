package com.main.web.siwa.member.website.controller;

import com.main.web.siwa.member.website.dto.*;
import com.main.web.siwa.utility.GetAuthMemberId;
import com.main.web.siwa.member.website.service.WebsiteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController("memberWebsiteController")
@RequestMapping("member/websites")
public class WebsiteController {

    private final WebsiteService websiteService;

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
        if(websiteSearchDto.getKeyWord() == null) {
            websiteSearchDto.setKeyWord("");
        }

        System.out.println("==============조회 검색 컨트롤러 요청");
        System.out.println("websiteSearchDto.getPage()" + websiteSearchDto.getPage());
        System.out.println("검색 키워드" + websiteSearchDto.getKeyWord());

        WebsiteResponseDto responseDto = websiteService.getList(websiteSearchDto);
            return new ResponseEntity<>(responseDto, HttpStatus.OK); // 페이지 정보, 웹사이트 정보, 카테고리 정보
    }
    // GET + ID
    @GetMapping("{websiteId}")
    public ResponseEntity<WebsiteListDto> getOne(
            @PathVariable(value = "websiteId", required = true) Long websiteId
    ) {
        return new ResponseEntity<>(websiteService.getById(websiteId),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(
            @ModelAttribute WebsiteCreateDto websiteCreateDto,
            @RequestParam("img")MultipartFile image
    ) {
        try {
            // JWT 인증필터에서 가져온 회원 아이디
            GetAuthMemberId authenticatedId = new GetAuthMemberId();
            Long memberId = authenticatedId.getAuthMemberId();
            websiteCreateDto.setMemberId(memberId);

            System.out.println("memberId: " + memberId);
            System.out.println("memberId: " +  websiteCreateDto.getMemberId());
            System.out.println("title: " +  websiteCreateDto.getTitle());
            System.out.println("url: " + websiteCreateDto.getUrl());
            System.out.println("categoryId: " + websiteCreateDto.getCategoryId());
            System.out.println("Image: " + (image != null ? image.getOriginalFilename() : "No image"));

            websiteService.create(websiteCreateDto, image);

            // 클라이언트에게 응답할 땐 데이터 타입을 맞춰야한다.(JSON), 클라이언트와 같은 응답메세지로
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "웹사이트 등록 성공"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "웹사이트 등록 중 오류가 발생했습니다."));
        }
    }

    @PutMapping("{websiteId}")
    public ResponseEntity<WebsiteListDto> update(
            WebsiteListDto websiteListDto,
            @RequestParam("img")MultipartFile newImage,
            @PathVariable(value = "websiteId", required = true) Long websiteId
    ) {
        websiteListDto.setId(websiteId);
        return new ResponseEntity<>(websiteService.update(websiteListDto, newImage), HttpStatus.OK);
    }
    @GetMapping("/actions")
    public ResponseEntity<?> getStatusAction(
            @RequestParam("memberId") Long memberId
    ) {
        try {
            // 서비스 레이어 호출
            ActionResponseDto responseDto = websiteService.getStatusAction(memberId);
            return ResponseEntity.ok(responseDto); // 상태 반환
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "상태 확인 중 오류가 발생했습니다."));
        }
    }

    @PostMapping("/actions")
    public ResponseEntity<?> setStatusAction(
            @RequestBody ActionDto actionDto
    ) {
        try {
            GetAuthMemberId authenticatedId = new GetAuthMemberId();
            Long memberId = authenticatedId.getAuthMemberId();

            System.out.println("actionDto.getWebsiteId() " + actionDto.getWebsiteId());
            System.out.println("actionDto.getMemberId() " + actionDto.getMemberId());
            System.out.println("actionDto.getAction() " + actionDto.getAction());
            System.out.println("actionDto.getIsAdded() " + actionDto.getIsAdded());

            actionDto.setMemberId(memberId);
            websiteService.setStatusAction(actionDto);
            String message = actionDto.getIsAdded() ? "추가되었습니다." : "취소되었습니다.";
            return ResponseEntity.ok(Map.of("message", message));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "상호작용 처리 중 오류가 발생했습니다."));
        }
    }

    // 웹 사이트 1개 삭제(무조건 wid로 개별 삭제할 것)
    @DeleteMapping("{websiteId}")
    public ResponseEntity<String> delete(
            @PathVariable(value = "websiteId", required = true) Long websiteId
    ) {
        websiteService.delete(websiteId);
        return new ResponseEntity<>("웹사이트가 삭제되었습니다.", HttpStatus.OK);
    }
}
