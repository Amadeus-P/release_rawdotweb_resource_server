package com.main.web.siwa.member.profile.service;

import com.main.web.siwa.member.profile.dto.ProfileListDto;
import com.main.web.siwa.member.profile.dto.ProfileResponseDto;
import com.main.web.siwa.member.profile.dto.ProfileSearchDto;

public interface ProfileService {

    ProfileListDto getById(Long memberId);

    ProfileResponseDto getList(ProfileSearchDto profileSearchDto);

    void delete(Long memberId);

    ProfileListDto update(ProfileListDto profileListDto);
}
