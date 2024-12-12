package com.main.web.siwa.action.service;

import com.main.web.siwa.action.dto.ActionDto;
import com.main.web.siwa.action.dto.ActionResponseDto;
import com.main.web.siwa.action.dto.WebsiteLikeRateDto;
import com.main.web.siwa.entity.*;
import com.main.web.siwa.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class DefaultActionService implements ActionService{

    private final WebsiteRepository websiteRepository;
    private final MemberRepository memberRepository;
    private final LikeRepository likeRepository;
    private final DislikeRepository dislikeRepository;
    private final BookmarkRepository bookmarkRepository;

    public DefaultActionService(
            WebsiteRepository websiteRepository,
            MemberRepository memberRepository,
            LikeRepository likeRepository,
            DislikeRepository dislikeRepository,
            BookmarkRepository bookmarkRepository
    ) {
        this.websiteRepository = websiteRepository;
        this.memberRepository = memberRepository;
        this.likeRepository = likeRepository;
        this.dislikeRepository = dislikeRepository;
        this.bookmarkRepository = bookmarkRepository;
    }

    @Override
    @Transactional
    public void setStatusAction(ActionDto actionDto) {
        String action = actionDto.getAction();
        boolean isAdded = actionDto.getIsAdded();

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
                        likes.setAction(actionDto.getAction());
                        likeRepository.save(likes);
                    }

                } else {
                    Likes existingLike = likeRepository
                            .findByWebsiteAndMember(website, member)
                            .orElseThrow(() -> new IllegalArgumentException("잘못된 좋아요 요청"));
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
                        dislikes.setAction(actionDto.getAction());
                        dislikeRepository.save(dislikes);
                    }
                } else {
                    // 싫어요 취소
                    Dislike existingDislike = dislikeRepository.findByWebsiteAndMember(website, member)
                            .orElseThrow(() -> new IllegalArgumentException("잘못된 싫어요 요청"));
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
                        bookmark.setAction(actionDto.getAction());
                        bookmarkRepository.save(bookmark);
                    }
                } else {
                    // 북마크 취소
                    Bookmark existingBookmark = bookmarkRepository.findByWebsiteAndMember(website, member)
                            .orElseThrow(() -> new IllegalArgumentException("잘못된 북마크 요청"));
                    bookmarkRepository.delete(existingBookmark);
                }
                break;

            default:
                throw new IllegalArgumentException("잘못된 요청입니다.");
        }
    }

    @Override
    public ActionResponseDto getMemberActionStatus(Long memberId) {

        // 북마크된 웹사이트 ID 목록 가져오기

        List<ActionDto> bookmarkedActions = bookmarkRepository.findAllByMemberId(memberId).stream()
                .map(bookmark -> ActionDto.builder()
                        .memberId(memberId)
                        .websiteId(bookmark.getWebsite().getId())
                        .action("bookmark")
                        .isAdded(bookmarkRepository.existsByWebsiteIdAndMemberId(memberId, bookmark.getWebsite().getId()))
                        .bookmarkCount(bookmarkRepository.countByWebsiteId(bookmark.getWebsite().getId()))
                        .build())
                .toList();

        // 좋아요된 웹사이트 ID 목록 가져오기
        List<ActionDto> likedActions = likeRepository.findAllByMemberId(memberId).stream()
                .map(like -> ActionDto.builder()
                        .memberId(memberId)
                        .websiteId(like.getWebsite().getId())
                        .action("like")
                        .isAdded(likeRepository.existsByWebsiteIdAndMemberId(memberId, like.getWebsite().getId()))
                        .likeCount(likeRepository.countByWebsiteId(like.getWebsite().getId()))
                        .build())
                .toList();

        // 싫어요된 웹사이트 ID 목록 가져오기
        List<ActionDto> dislikedActions = dislikeRepository.findAllByMemberId(memberId).stream()
                .map(dislike -> ActionDto.builder()
                        .memberId(memberId)
                        .websiteId(dislike.getWebsite().getId())
                        .action("dislike")
                        .isAdded(dislikeRepository.existsByWebsiteIdAndMemberId(memberId, dislike.getWebsite().getId()))
                        .dislikeCount(dislikeRepository.countByWebsiteId(dislike.getWebsite().getId()))
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

    @Override
    public ActionResponseDto getWebsiteActionStatus(Long websiteId) {
        // 웹사이트 확인
        Website website = websiteRepository.findById(websiteId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid website ID: " + websiteId));

        Long likeCount = likeRepository.countByWebsiteId(websiteId);
        Long dislikeCount = dislikeRepository.countByWebsiteId(websiteId);
        Long bookmarkCount = bookmarkRepository.countByWebsiteId(websiteId);

        List<ActionDto> actionDtos = new ArrayList<>();

        actionDtos.add(ActionDto.builder()
                .websiteId(websiteId)
                .action("like")
                .likeCount(likeCount)
                .build());
        actionDtos.add(ActionDto.builder()
                .websiteId(websiteId)
                .action("dislike")
                .dislikeCount(dislikeCount)
                .build());
        actionDtos.add(ActionDto.builder()
                .websiteId(websiteId)
                .action("bookmark")
                .bookmarkCount(bookmarkCount)
                .build());

        return ActionResponseDto
                .builder()
                .actionDtos(actionDtos)
                .build();
    }

    @Override
    public ActionResponseDto getAllActionStatus(List<Long> websiteIds) {
        List<Long> likeCounts = likeRepository.countByWebsiteIdIn(websiteIds);
        List<Long> dislikeCounts = dislikeRepository.countByWebsiteIdIn(websiteIds);
        List<Long> bookmarkCounts = bookmarkRepository.countByWebsiteIdIn(websiteIds);

        List<ActionDto> actionDtos = new ArrayList<>();
        for (int i = 0; i < websiteIds.size(); i++) {
            Long websiteId = websiteIds.get(i);

            actionDtos.add(
                    ActionDto.builder()
                    .websiteId(websiteId)
                    .action("like")
                    .likeCount(likeCounts.get(i))
                    .build());

            actionDtos.add(ActionDto.builder()
                    .websiteId(websiteId)
                    .action("dislike")
                    .dislikeCount(dislikeCounts.get(i))
                    .build());

            actionDtos.add(ActionDto.builder()
                    .websiteId(websiteId)
                    .action("bookmark")
                    .bookmarkCount(bookmarkCounts.get(i))
                    .build());
        }

        return ActionResponseDto.builder()
                .actionDtos(actionDtos)
                .build();
    }

    @Override
    public WebsiteLikeRateDto calculateWebsiteRatings(List<ActionDto> actionDtos) {

        BigDecimal zero = BigDecimal.ZERO;
        BigDecimal rate = zero;
        BigDecimal like = zero;
        BigDecimal dislike = zero;
        BigDecimal total = zero;

        for (ActionDto actionDto : actionDtos) {
            Long websiteId = actionDto.getWebsiteId();

            // 각 웹사이트의 좋아요 및 싫어요 개수 조회
            long likeCount = likeRepository.countByWebsiteId(websiteId);
            long dislikeCount = dislikeRepository.countByWebsiteId(websiteId);

            like = BigDecimal.valueOf(likeCount);
            dislike = BigDecimal.valueOf(dislikeCount);
            total = like.add(dislike);

            if (total.compareTo(BigDecimal.ZERO) > 0) {
                rate = like.divide(total, 2, RoundingMode.HALF_UP) // 비율 계산, 소수점 2자리까지
                        .multiply(BigDecimal.valueOf(100));// 퍼센트 계산
            }
        }
        // 계산한 비율을 정수형으로 변환 (반올림)
        int roundedRate = rate.setScale(0, RoundingMode.HALF_UP).intValue();

        return WebsiteLikeRateDto
                .builder()
                .likeRate(roundedRate)
                .build();
    }
}
