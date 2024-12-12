package com.main.web.siwa.member.profile.controller;

import com.main.web.siwa.member.profile.dto.ProfileResponseDto;
import com.main.web.siwa.member.profile.dto.ProfileListDto;
import com.main.web.siwa.member.profile.dto.ProfileSearchDto;
import com.main.web.siwa.member.profile.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("member/profile")
public class ProfileController {
    private ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public ResponseEntity<ProfileResponseDto> getList(
            @ModelAttribute ProfileSearchDto profileSearchDto
    ) {
        if(profileSearchDto.getPage() == null || profileSearchDto.getSize() < 1) {
            profileSearchDto.setPage(1);
        }
        ProfileResponseDto responseDto = profileService.getList(profileSearchDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK); // 페이지 정보, 웹사이트 정보, 카테고리 정보
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<ProfileListDto> getOne(
            @PathVariable(value = "memberId", required = true) Long memberId
    ) {

        System.out.println("========================profileService.getById(memberId)" + profileService.getById(memberId));
        return new ResponseEntity<>(profileService.getById(memberId),HttpStatus.OK);
    }

    @PutMapping("/{memberId}")
    public ResponseEntity<ProfileListDto> update(
            ProfileListDto profileListDto,
            @PathVariable(value = "memberId", required = true) Long memberId
    ) {
        profileListDto.setId(memberId);
        return new ResponseEntity<>(profileService.update(profileListDto), HttpStatus.OK);
    }

    // 1개 삭제(무조건 개별 삭제할 것)
    @DeleteMapping("{memberId}")
    public ResponseEntity<String> delete(
            @PathVariable(value = "memberId", required = true) Long memberId
    ) {
        profileService.delete(memberId);
        return new ResponseEntity<>("웹사이트가 삭제되었습니다.", HttpStatus.OK);
    }
}
