package com.main.web.siwa.service.member;

import com.main.web.siwa.dto.auth.SignupRequestDto;
import com.main.web.siwa.dto.auth.SignupResponseDto;
import com.main.web.siwa.dto.member.MemberListDto;
import com.main.web.siwa.dto.member.MemberResponseDto;
import com.main.web.siwa.dto.member.MemberSearchDto;

public interface MemberService {
    SignupResponseDto create(SignupRequestDto requestDto);

    MemberListDto getById(Long memberId);

    MemberResponseDto getList(MemberSearchDto memberSearchDto);

    void delete(Long memberId);

    MemberListDto update(MemberListDto memberListDto);
}
