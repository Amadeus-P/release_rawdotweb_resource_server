package com.main.web.siwa.action.service;

import com.main.web.siwa.action.dto.ActionDto;
import com.main.web.siwa.action.dto.ActionResponseDto;

public interface ActionService {
    void setStatusAction(ActionDto actionDto);

    ActionResponseDto getStatusAction(Long memberId);

}
