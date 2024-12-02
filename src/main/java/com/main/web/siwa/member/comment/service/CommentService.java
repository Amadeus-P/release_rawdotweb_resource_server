package com.main.web.siwa.member.comment.service;

import com.main.web.siwa.member.comment.dto.CommentCreateDto;
import com.main.web.siwa.member.comment.dto.CommentListDto;
import com.main.web.siwa.member.comment.dto.CommentResponseDto;

public interface CommentService {

    void delete(Long commentId);

    CommentListDto update(CommentListDto commentListDto);

    CommentCreateDto create(CommentCreateDto commentCreateDto);

    CommentListDto getById(Long commentId);

    CommentResponseDto getList(CommentListDto commentListDto, Long websiteId);
}
