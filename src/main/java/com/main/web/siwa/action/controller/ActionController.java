package com.main.web.siwa.action.controller;

import com.main.web.siwa.action.dto.MemberActionDto;
import com.main.web.siwa.action.dto.MemberActionResponseDto;
import com.main.web.siwa.action.dto.WebsiteActionDto;
import com.main.web.siwa.action.dto.WebsiteActionResponseDto;
import com.main.web.siwa.action.service.ActionService;
import com.main.web.siwa.utility.GetAuthMemberId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("actions")
public class ActionController {

    private final ActionService actionService;

    public ActionController(ActionService actionService) {
        this.actionService = actionService;
    }
    @PostMapping()
    public ResponseEntity<?> setStatusAction(
            @RequestBody MemberActionDto memberActionDto
    ) {
        try {
            GetAuthMemberId authenticatedId = new GetAuthMemberId();
            Long memberId = authenticatedId.getAuthMemberId();

            System.out.println("=========/actions=============");
            System.out.println("actionDto.getWebsiteId() " + memberActionDto.getWebsiteId());
            System.out.println("actionDto.getMemberId() " + memberActionDto.getMemberId());
            System.out.println("actionDto.getAction() " + memberActionDto.getAction());
            System.out.println("actionDto.getIsAdded() " + memberActionDto.getIsAdded());

            memberActionDto.setMemberId(memberId);
            actionService.setStatusAction(memberActionDto);
            String message = memberActionDto.getIsAdded() ? "추가되었습니다." : "취소되었습니다.";
            return ResponseEntity.ok(Map.of("message", message));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "상호작용 처리 중 오류가 발생했습니다."));
        }
    }
    @GetMapping("website")
    public  ResponseEntity<?> getAllActionStatus(
            @RequestParam List<Long> websiteIds

    ) {
        // 여러 개의 웹사이트 아이디를 어떻게 처리?
        try {
            System.out.println("=========/actions/website=============");
            System.out.println("============================websiteIds " + websiteIds);
            // 서비스 레이어 호출
            System.out.println("actionService.getMemberAllActionStatus(websiteIds): " + actionService.getWebsiteListActionStatus(websiteIds));
            WebsiteActionResponseDto resultDto = actionService.getWebsiteListActionStatus(websiteIds);
            System.out.println("=================resultDto: " + resultDto);

            return ResponseEntity.ok(resultDto); // 상태 반환
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "웹사이트 상태를 불러오는 중 오류가 발생했습니다."));
        }
    }
    @GetMapping("website/{websiteId}")
    public ResponseEntity<?> getWebsiteActionStatus(
            @PathVariable(value = "websiteId", required = true) Long websiteId
    ) {
        try {
            System.out.println("=========/actions/website/{websiteId}=============");
            WebsiteActionDto responseDto = actionService.getOneWebsiteActionStatus(websiteId);

            return ResponseEntity.ok(responseDto); // 상태 반환
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "웹사이트 액션 정보 조회 중 오류가 발생했습니다."));
        }
    }

    @GetMapping("member")
    public ResponseEntity<?> getMemberActionStatus() {
        try {
            // 서비스 레이어 호출
            MemberActionResponseDto responseDto = actionService.getMemberActionStatus();
            System.out.println("===================member/{memberId}==================");
            System.out.println("responseDto: " + responseDto);
            return ResponseEntity.ok(responseDto); // 상태 반환
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "상태 확인 중 오류가 발생했습니다."));
        }
    }
}
