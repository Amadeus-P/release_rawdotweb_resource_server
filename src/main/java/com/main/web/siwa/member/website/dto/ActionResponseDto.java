package com.main.web.siwa.member.website.dto;

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
