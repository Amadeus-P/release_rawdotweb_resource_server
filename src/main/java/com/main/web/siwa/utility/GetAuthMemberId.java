package com.main.web.siwa.utility;

import com.main.web.siwa.entity.SiwaUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class GetAuthMemberId {
    public Long getAuthMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getPrincipal() instanceof SiwaUserDetails) {
            SiwaUserDetails userDetails = (SiwaUserDetails) authentication.getPrincipal();
            return userDetails.getId();
        } else {
            throw new IllegalStateException("인증되지 않은 사용자입니다.");
        }
    }
}
