package com.main.web.siwa.member.profile.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileListDto {
    private Long id;
    private String username;
    private String password;
    private String profileImage;
    private String profileName;
    private Instant regDate;
}
