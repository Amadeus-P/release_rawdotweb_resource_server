package com.main.web.siwa.service.member.commment;

import com.main.web.siwa.dto.member.commment.CommentCreateDto;
import com.main.web.siwa.dto.member.commment.CommentListDto;
import com.main.web.siwa.dto.member.commment.CommentResponseDto;

public interface CommentService {

    void delete(Long commentId);

    CommentListDto update(CommentListDto commentListDto);

    CommentCreateDto create(CommentCreateDto commentCreateDto);

    CommentListDto getById(Long commentId);

    CommentResponseDto getList(CommentListDto commentListDto, Long websiteId);
}
