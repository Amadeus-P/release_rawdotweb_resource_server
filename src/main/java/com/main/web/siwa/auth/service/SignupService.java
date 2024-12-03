package com.main.web.siwa.auth.service;

import com.main.web.siwa.auth.dto.SignupRequestDto;
import com.main.web.siwa.auth.dto.SignupResponseDto;

public interface SignupService {

    SignupResponseDto create(SignupRequestDto requestDto);
}
