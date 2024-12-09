package com.main.web.siwa.action.controller;

import com.main.web.siwa.action.dto.ActionDto;
import com.main.web.siwa.action.dto.ActionResponseDto;
import com.main.web.siwa.action.dto.WebsiteLikeRateDto;
import com.main.web.siwa.action.service.ActionService;
import com.main.web.siwa.utility.GetAuthMemberId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("actions")
public class ActionController {

    private final ActionService actionService;

    public ActionController(ActionService actionService) {
        this.actionService = actionService;
    }
    @GetMapping("website/rating")
    public  ResponseEntity<?> calculateWebsiteRatings(
            @ModelAttribute ActionResponseDto responseDto
    ) {
        try {
            System.out.println("responseDto.getActionDtos()" + responseDto.getActionDtos());
            // 서비스 레이어 호출
            WebsiteLikeRateDto resultDto = actionService.calculateWebsiteRatings(responseDto.getActionDtos());
            System.out.println("=================resultDto" + resultDto);

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
            ActionResponseDto responseDto = actionService.getWebsiteActionStatus(websiteId);
            return ResponseEntity.ok(responseDto); // 상태 반환
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "웹사이트 액션 정보 조회 중 오류가 발생했습니다."));
        }
    }

    @GetMapping("member/{memberId}")
    public ResponseEntity<?> getMemberActionStatus(
            @PathVariable(value = "memberId", required = true) Long memberId
    ) {
        try {
            // 서비스 레이어 호출
            ActionResponseDto responseDto = actionService.getMemberActionStatus(memberId);
            return ResponseEntity.ok(responseDto); // 상태 반환
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "상태 확인 중 오류가 발생했습니다."));
        }
    }

    @PostMapping()
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
            actionService.setStatusAction(actionDto);
            String message = actionDto.getIsAdded() ? "추가되었습니다." : "취소되었습니다.";
            return ResponseEntity.ok(Map.of("message", message));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "상호작용 처리 중 오류가 발생했습니다."));
        }
    }
}
