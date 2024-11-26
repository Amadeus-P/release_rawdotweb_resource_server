package com.main.web.siwa.dto.member.website;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActionResponseDto {
    List<ActionDto> actionDtos;
}
