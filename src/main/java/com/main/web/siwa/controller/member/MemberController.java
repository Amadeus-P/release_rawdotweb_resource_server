package com.main.web.siwa.controller.member;

import com.main.web.siwa.dto.member.MemberListDto;
import com.main.web.siwa.dto.member.MemberResponseDto;
import com.main.web.siwa.dto.member.MemberSearchDto;
import com.main.web.siwa.service.member.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
public class MemberController {
    private MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<MemberResponseDto> getList(
            @ModelAttribute MemberSearchDto memberSearchDto
    ) {
        if(memberSearchDto.getPage() == null || memberSearchDto.getSize() < 1) {
            memberSearchDto.setPage(1);
        }
        MemberResponseDto responseDto = memberService.getList(memberSearchDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK); // 페이지 정보, 웹사이트 정보, 카테고리 정보
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberListDto> getOne(
            @PathVariable(value = "memberId", required = true) Long memberId
    ) {
        return new ResponseEntity<>(memberService.getById(memberId),HttpStatus.OK);
    }

    @PutMapping("/{memberId}")
    public ResponseEntity<MemberListDto> update(
            MemberListDto memberListDto,
            @PathVariable(value = "memberId", required = true) Long memberId
    ) {
        memberListDto.setId(memberId);
        return new ResponseEntity<>(memberService.update(memberListDto), HttpStatus.OK);
    }

    // 1개 삭제(무조건 개별 삭제할 것)
    @DeleteMapping("{memberId}")
    public ResponseEntity<String> delete(
            @PathVariable(value = "memberId", required = true) Long memberId
    ) {
        memberService.delete(memberId);
        return new ResponseEntity<>("웹사이트가 삭제되었습니다.", HttpStatus.OK);
    }
}
