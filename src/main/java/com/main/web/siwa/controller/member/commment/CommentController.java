package com.main.web.siwa.controller.member.commment;

import com.main.web.siwa.utility.GetAuthMemberId;
import com.main.web.siwa.dto.member.commment.CommentCreateDto;
import com.main.web.siwa.dto.member.commment.CommentListDto;
import com.main.web.siwa.dto.member.commment.CommentResponseDto;
import com.main.web.siwa.service.member.commment.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("{websiteId}")
    public ResponseEntity<CommentResponseDto> getList(
            @ModelAttribute CommentListDto commentListDto,
            @PathVariable(value = "websiteId") Long websiteId
    ) {
        CommentResponseDto responseDto = commentService.getList(commentListDto, websiteId);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
    @GetMapping("{commentId}")
    public ResponseEntity<CommentListDto> getOne(
            @PathVariable(value = "commentId", required = true) Long commentId
    ) {
        return new ResponseEntity<>(commentService.getById(commentId),HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<?> create(
            @ModelAttribute CommentCreateDto commentCreateDto
    ) {
        try {
            // JWT 인증필터에서 가져온 회원 아이디
            GetAuthMemberId authenticatedId = new GetAuthMemberId();
            Long memberId = authenticatedId.getAuthMemberId();
            commentCreateDto.setMemberId(memberId);

            System.out.println("memberId: " + memberId);
            System.out.println("memberId: " +  commentCreateDto.getMemberId());
            System.out.println("content: " +  commentCreateDto.getContent());
            System.out.println("websiteId: " +  commentCreateDto.getWebsiteId());

            commentService.create(commentCreateDto);

            // 클라이언트에게 응답할 땐 데이터 타입을 맞춰야한다.(JSON), 클라이언트와 같은 응답메세지로
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "댓글 등록 성공"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "댓글 등록 중 오류가 발생했습니다."));
        }
    }
    @PutMapping("{commentId}")
    public ResponseEntity<CommentListDto> update(
            CommentListDto commentListDto,
            @PathVariable(value = "commentId", required = true) Long commentId
    ) {
        commentListDto.setId(commentId);
        return new ResponseEntity<>(commentService.update(commentListDto), HttpStatus.OK);
    }
    @DeleteMapping("{commentId}")
    public ResponseEntity<String> delete(
            @PathVariable(value = "commentId", required = true) Long commentId
    ) {
        commentService.delete(commentId);
        return new ResponseEntity<>("웹사이트가 삭제되었습니다.", HttpStatus.OK);
    }
}
