package com.main.web.siwa.action.service;

import com.main.web.siwa.action.dto.ActionDto;
import com.main.web.siwa.action.dto.ActionResponseDto;
import com.main.web.siwa.action.dto.WebsiteLikeRateDto;

import java.util.List;

public interface ActionService {
    void setStatusAction(ActionDto actionDto);

    ActionResponseDto getMemberActionStatus(Long memberId);

    ActionResponseDto getWebsiteActionStatus(Long websiteId);

    WebsiteLikeRateDto calculateWebsiteRatings(List<ActionDto> actionDtos);
}
