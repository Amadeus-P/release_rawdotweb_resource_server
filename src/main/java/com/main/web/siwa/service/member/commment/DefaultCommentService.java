package com.main.web.siwa.service.member.commment;

import com.main.web.siwa.entity.Comment;
import com.main.web.siwa.dto.member.commment.CommentCreateDto;
import com.main.web.siwa.dto.member.commment.CommentListDto;
import com.main.web.siwa.dto.member.commment.CommentResponseDto;
import com.main.web.siwa.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultCommentService implements CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final WebsiteRepository websiteRepository;
    private final LikeRepository likeRepository;
    private final DislikeRepository dislikeRepository;
    private final BookmarkRepository bookmarkRepository;
    private final ModelMapper modelMapper;

    public DefaultCommentService(CommentRepository commentRepository,
                                 MemberRepository memberRepository,
                                 WebsiteRepository websiteRepository,
                                 LikeRepository likeRepository,
                                 DislikeRepository dislikeRepository,
                                 BookmarkRepository bookmarkRepository,
                                 ModelMapper modelMapper
    ) {
        this.commentRepository = commentRepository;
        this.memberRepository = memberRepository;
        this.websiteRepository = websiteRepository;
        this.likeRepository = likeRepository;
        this.dislikeRepository = dislikeRepository;
        this.bookmarkRepository = bookmarkRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void delete(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    public CommentListDto update(CommentListDto commentListDto) {
        Comment comment = commentRepository
                .findById(commentListDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다. id = " + commentListDto.getId()));

        if(commentListDto.getContent() != null)
            comment.setContent(commentListDto.getContent());

        Comment updatedComment = commentRepository.save(comment);

        return modelMapper.map(updatedComment, CommentListDto.class);
    }

    @Override
    public CommentCreateDto create(CommentCreateDto commentCreateDto) {
        Comment createdComment = commentRepository.save(modelMapper.map(commentCreateDto, Comment.class));
        return modelMapper.map(createdComment, CommentCreateDto.class);
    }

    @Override
    public CommentListDto getById(Long commentId) {
        Comment comment = commentRepository
                .findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다. commentId = " + commentId));
        return modelMapper.map(comment, CommentListDto.class);
    }

    @Override
    public CommentResponseDto getList(CommentListDto commentListDto, Long websiteId) {

        List<Comment> commentList = commentRepository.findAllByWebsiteId(websiteId);
        CommentResponseDto listDto = modelMapper.map(commentList, CommentResponseDto.class);

        return listDto;
    }
}
