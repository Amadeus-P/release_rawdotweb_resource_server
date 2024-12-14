package com.main.web.siwa.action.service;

import com.main.web.siwa.action.dto.MemberActionDto;
import com.main.web.siwa.action.dto.MemberActionResponseDto;
import com.main.web.siwa.action.dto.WebsiteActionDto;
import com.main.web.siwa.action.dto.WebsiteActionResponseDto;

import java.util.List;

public interface ActionService {
    void setStatusAction(MemberActionDto memberActionDto);

    MemberActionResponseDto getMemberActionStatus();
    WebsiteActionDto getOneWebsiteActionStatus(Long websiteId);
    WebsiteActionResponseDto getWebsiteListActionStatus(List<Long> websiteIds);

}
