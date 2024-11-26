package com.main.web.siwa.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SigninResponseDto {
    private Long memberId;
    private String profileName;
    private String profileImage;
    private String token;
}
