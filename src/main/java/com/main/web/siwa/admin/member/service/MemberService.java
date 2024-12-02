package com.main.web.siwa.admin.member.service;

import com.main.web.siwa.auth.dto.SignupRequestDto;
import com.main.web.siwa.auth.dto.SignupResponseDto;
import com.main.web.siwa.admin.member.dto.MemberListDto;
import com.main.web.siwa.admin.member.dto.MemberResponseDto;
import com.main.web.siwa.admin.member.dto.MemberSearchDto;

public interface MemberService {

    MemberListDto getById(Long memberId);

    MemberResponseDto getList(MemberSearchDto memberSearchDto);

    void delete(Long memberId);

    MemberListDto update(MemberListDto memberListDto);
}
