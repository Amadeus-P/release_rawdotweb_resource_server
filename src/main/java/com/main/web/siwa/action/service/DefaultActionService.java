package com.main.web.siwa.action.service;

import com.main.web.siwa.action.dto.ActionDto;
import com.main.web.siwa.action.dto.ActionResponseDto;
import com.main.web.siwa.entity.*;
import com.main.web.siwa.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DefaultActionService implements ActionService{

    private final WebsiteRepository websiteRepository;
    private final MemberRepository memberRepository;
    private final LikeRepository likeRepository;
    private final DislikeRepository dislikeRepository;
    private final BookmarkRepository bookmarkRepository;
    private final ModelMapper modelMapper;

    public DefaultActionService(
            WebsiteRepository websiteRepository,
            MemberRepository memberRepository,
            LikeRepository likeRepository,
            DislikeRepository dislikeRepository,
            BookmarkRepository bookmarkRepository,
            ModelMapper modelMapper
    ) {
        this.websiteRepository = websiteRepository;
        this.memberRepository = memberRepository;
        this.likeRepository = likeRepository;
        this.dislikeRepository = dislikeRepository;
        this.bookmarkRepository = bookmarkRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public void setStatusAction(ActionDto actionDto) {
        String action = actionDto.getAction();
        boolean isAdded = actionDto.getIsAdded();

        // getReferenceById: 해당 Entity의 속성(필드)가 사용도기 전까지 실제 쿼리가 실행되지 않음(프록시)
        Member member = memberRepository.findById(actionDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID: " + actionDto.getMemberId()));
        Website website = websiteRepository.findById(actionDto.getWebsiteId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid website ID: " + actionDto.getWebsiteId()));

        // 클라이언트에서 보내는 값과 일치하는 것을 찾기
        switch (action.toLowerCase()) {
            case "like":
                if(isAdded){
                    if(!likeRepository.existsByWebsiteIdAndMemberId(member.getId(), website.getId())) {
                        Likes likes = new Likes();
                        likes.setMember(member);
                        likes.setWebsite(website);
                        likeRepository.save(likes);
                    }

                } else {
                    Likes existingLike = likeRepository
                            .findByWebsiteAndMember(website, member)
                            .orElseThrow(() -> new IllegalArgumentException("좋아요를 눌러주세요."));
                    likeRepository.delete(existingLike);
                }
                break;
            case "dislike":
                if (isAdded) {
                    // 싫어요 추가
                    if (!dislikeRepository.existsByWebsiteIdAndMemberId(member.getId(), website.getId())) {
                        Dislike dislikes = new Dislike();
                        dislikes.setMember(member);
                        dislikes.setWebsite(website);
                        dislikeRepository.save(dislikes);
                    }
                } else {
                    // 싫어요 취소
                    Dislike existingDislike = dislikeRepository.findByWebsiteAndMember(website, member)
                            .orElseThrow(() -> new IllegalArgumentException("의견이 없습니다."));
                    dislikeRepository.delete(existingDislike);
                }
                break;
            case "bookmark":
                if (isAdded) {
                    // 북마크 추가
                    if (!bookmarkRepository.existsByWebsiteIdAndMemberId(member.getId(), website.getId())) {
                        Bookmark bookmark = new Bookmark();
                        bookmark.setMember(member);
                        bookmark.setWebsite(website);
                        bookmarkRepository.save(bookmark);
                    }
                } else {
                    // 북마크 취소
                    Bookmark existingBookmark = bookmarkRepository.findByWebsiteAndMember(website, member)
                            .orElseThrow(() -> new IllegalArgumentException("북마크가 없습니다."));
                    bookmarkRepository.delete(existingBookmark);
                }
                break;

            default:
                throw new IllegalArgumentException("잘못된 요청입니다.");
        }
    }

    @Override
    public ActionResponseDto getStatusAction(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID: " + memberId));
//        Website website = websiteRepository.findById(websiteId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid website ID: " + websiteId));

        // 북마크된 웹사이트 ID 목록 가져오기
        List<ActionDto> bookmarkedActions = bookmarkRepository.findAllByMemberId(memberId).stream()
                .map(bookmark -> ActionDto.builder()
                        .memberId(memberId)
                        .websiteId(bookmark.getWebsite().getId())
                        .action("bookmark")
                        .isAdded(true)
                        .build())
                .toList();

        // 좋아요된 웹사이트 ID 목록 가져오기
        List<ActionDto> likedActions = likeRepository.findAllByMemberId(memberId).stream()
                .map(like -> ActionDto.builder()
                        .memberId(memberId)
                        .websiteId(like.getWebsite().getId())
                        .action("like")
                        .isAdded(true)
                        .build())
                .toList();

        // 싫어요된 웹사이트 ID 목록 가져오기
        List<ActionDto> dislikedActions = dislikeRepository.findAllByMemberId(memberId).stream()
                .map(dislike -> ActionDto.builder()
                        .memberId(memberId)
                        .websiteId(dislike.getWebsite().getId())
                        .action("dislike")
                        .isAdded(true)
                        .build())
                .toList();

        List<ActionDto> actionDtos = new ArrayList<>();
        actionDtos.addAll(bookmarkedActions);
        actionDtos.addAll(likedActions);
        actionDtos.addAll(dislikedActions);

        return ActionResponseDto.builder()
                .actionDtos(actionDtos)
                .build();
    }
}
