package com.main.web.siwa.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupResponseDto {
    private Long id;
    private String username;
    private String profileName;
    private String profileImage;

}