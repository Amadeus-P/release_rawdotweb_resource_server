package com.main.web.siwa.action.service;

import com.main.web.siwa.action.dto.MemberActionDto;
import com.main.web.siwa.action.dto.MemberActionResponseDto;
import com.main.web.siwa.action.dto.WebsiteActionDto;
import com.main.web.siwa.action.dto.WebsiteActionResponseDto;
import com.main.web.siwa.entity.*;
import com.main.web.siwa.repository.*;
import com.main.web.siwa.utility.GetAuthMemberId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    public void setStatusAction(MemberActionDto memberActionDto) {
        String action = memberActionDto.getAction();
        boolean isAdded = memberActionDto.getIsAdded();

        Member member = memberRepository.findById(memberActionDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID: " + memberActionDto.getMemberId()));
        Website website = websiteRepository.findById(memberActionDto.getWebsiteId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid website ID: " + memberActionDto.getWebsiteId()));

        // 클라이언트에서 보내는 값과 일치하는 것을 찾기
        switch (action.toLowerCase()) {
            case "like":
                if(isAdded){
                    if(!likeRepository.existsByWebsiteIdAndMemberId(member.getId(), website.getId())) {
                        Likes likes = new Likes();
                        likes.setMember(member);
                        likes.setWebsite(website);
                        likes.setAction(memberActionDto.getAction());
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
                        dislikes.setAction(memberActionDto.getAction());
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
                        bookmark.setAction(memberActionDto.getAction());
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
    public MemberActionResponseDto getMemberActionStatus() {
        // 회원의 액션 상태

        GetAuthMemberId authenticatedId = new GetAuthMemberId();
        Long memberId = authenticatedId.getAuthMemberId();

        // 북마크된 웹사이트 ID 목록 가져오기
        List<MemberActionDto> bookmarkedActions = bookmarkRepository.findAllByMemberId(memberId).stream()
                .map(bookmark -> MemberActionDto.builder()
                        .memberId(memberId)
                        .websiteId(bookmark.getWebsite().getId())
                        .action("bookmark")
                        .isAdded(true) // 이미 등록된 상태만 가져오기 때문에 true
//                        .bookmarkCount(bookmarkRepository.countByWebsiteId(bookmark.getWebsite().getId()))
                        .build())
                .toList();

        // 좋아요된 웹사이트 ID 목록 가져오기
        List<MemberActionDto> likedActions = likeRepository.findAllByMemberId(memberId).stream()
                .map(like -> MemberActionDto.builder()
                        .memberId(memberId)
                        .websiteId(like.getWebsite().getId())
                        .action("like")
                        .isAdded(true)
//                        .likeCount(likeRepository.countByWebsiteId(like.getWebsite().getId()))
                        .build())
                .toList();

        // 싫어요한 웹사이트 ID 목록 가져오기
        List<MemberActionDto> dislikedActions = dislikeRepository.findAllByMemberId(memberId).stream()
                .map(dislike -> MemberActionDto.builder()
                        .memberId(memberId)
                        .websiteId(dislike.getWebsite().getId())
                        .action("dislike")
                        .isAdded(true)
//                        .dislikeCount(dislikeRepository.countByWebsiteId(dislike.getWebsite().getId()))
                        .build())
                .toList();

        List<MemberActionDto> memberActionDtos = new ArrayList<>();
        memberActionDtos.addAll(bookmarkedActions);
        memberActionDtos.addAll(likedActions);
        memberActionDtos.addAll(dislikedActions);

        System.out.println("============getMemberActionStatus=============");
        System.out.println("ActionResponseDto" + MemberActionResponseDto.builder()
                .memberActionDtos(memberActionDtos)
                .build());
        return MemberActionResponseDto.builder()
                .memberActionDtos(memberActionDtos)
                .build();
    }

    @Override
    public WebsiteActionDto getOneWebsiteActionStatus(Long websiteId) {
        // 상세페이지의 액션 상태
        Website website = websiteRepository.findById(websiteId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 웹사이트입니다."));
        Long memberId = website.getMember().getId();
        String profileName = website.getMember().getProfileName();

        Long likeCount = likeRepository.countByWebsiteId(websiteId);
        Long dislikeCount = dislikeRepository.countByWebsiteId(websiteId);
        Long bookmarkCount = bookmarkRepository.countByWebsiteId(websiteId);

        System.out.println("likeCount: " + likeCount);
        System.out.println("dislikeCount: " + dislikeCount);
        System.out.println("bookmarkCount: " + bookmarkCount);


        return WebsiteActionDto.builder()
                .memberId(memberId)
                .profileName(profileName)
                .websiteId(websiteId)
                .likeCount(likeCount)
                .dislikeCount(dislikeCount)
                .bookmarkCount(bookmarkCount)
                .build();
    }

    @Override
    public WebsiteActionResponseDto getWebsiteListActionStatus(List<Long> websiteIds) {
        // 목록 페이지의 액션 상태

        List<Long> sortedWebsiteIds = new ArrayList<>(websiteIds);
        Collections.sort(sortedWebsiteIds);

        Long memberId = null;
        try {// 회원
            GetAuthMemberId authenticatedId = new GetAuthMemberId();
            memberId = authenticatedId.getAuthMemberId();
        } catch (Exception e) { // 비회원
        }
//        Long likeCount = likeRepository.countByWebsiteId(websiteId);
//        Long dislikeCount = dislikeRepository.countByWebsiteId(websiteId);
//        Long bookmarkCount = bookmarkRepository.countByWebsiteId(websiteId);
//
//        System.out.println("================getOneWebsiteActionStatus===============");
//        System.out.println("likeCount: " + likeCount);
//        System.out.println("dislikeCount: " + dislikeCount);
//        System.out.println("bookmarkCount: " + bookmarkCount);

//        dislikeRepository.existsByWebsiteIdAndMemberId(memberId, dislike.getWebsite().getId())
        List<Object[]> AddedLikes = likeRepository.findLikeCountsByWebsiteIds(sortedWebsiteIds);
        List<Object[]> AddedDislikes = dislikeRepository.findDislikeCountsByWebsiteIds(sortedWebsiteIds);
        List<Object[]> AddedBookmarks = bookmarkRepository.findBookmarkCountsByWebsiteIds(sortedWebsiteIds);

        System.out.println("=========getAllWebsiteActionStatus=============");
//        System.out.println("AddedLikes: " + AddedLikes.toString());
//        System.out.println("AddedDislikes: " + AddedDislikes.toString());
//        System.out.println("AddedBookmarks: " + AddedBookmarks.toString());

        Map<Long, Long> likeMap = new HashMap<>();
        for (Object[] row : AddedLikes) {
            Long wId = (Long) row[0];
            Long count = (Long) row[1];
            likeMap.put(wId, count);
        }
        Map<Long, Long> dislikeMap = new HashMap<>();
        for (Object[] row : AddedDislikes) {
            Long wId = (Long) row[0];
            Long count = (Long) row[1];
            dislikeMap.put(wId, count);
        }
        Map<Long, Long> bookmarkMap = new HashMap<>();
        for (Object[] row : AddedBookmarks) {
            Long wId = (Long) row[0];
            Long count = (Long) row[1];
            bookmarkMap.put(wId, count);
        }
        List<WebsiteActionDto> websiteActionDtos = new ArrayList<>();
        for (Long websiteId : websiteIds) {
            // 웹사이트 추천, 비추천, 북마크 수
            Long likeCount = likeMap.getOrDefault(websiteId, 0L);
            Long dislikeCount = dislikeMap.getOrDefault(websiteId, 0L);
            Long bookmarkCount = bookmarkMap.getOrDefault(websiteId, 0L);

//            System.out.println("likeCount: " + likeCount);
//            System.out.println("dislikeCount: " + dislikeCount);
//            System.out.println("bookmarkCount: " + bookmarkCount);

            Boolean userLiked = likeRepository.existsByWebsiteIdAndMemberId(websiteId, memberId);
            Boolean userDisliked = dislikeRepository.existsByWebsiteIdAndMemberId(websiteId, memberId);
            Boolean userBookmarked = bookmarkRepository.existsByWebsiteIdAndMemberId(websiteId, memberId);

//            System.out.println("userLiked: " + userLiked);
//            System.out.println("userDisliked: " + userDisliked);
//            System.out.println("userBookmarked: " + userBookmarked);

            websiteActionDtos.add(WebsiteActionDto.builder()
                    .memberId(memberId)
                    .websiteId(websiteId)
                    .action("like")
                    .isAdded(userLiked)
                    .likeCount(likeCount)
                    .build());

            websiteActionDtos.add(WebsiteActionDto.builder()
                    .memberId(memberId)
                    .websiteId(websiteId)
                    .action("dislike")
                    .isAdded(userDisliked)
                    .dislikeCount(dislikeCount)
                    .build());

            websiteActionDtos.add(WebsiteActionDto.builder()
                    .memberId(memberId)
                    .websiteId(websiteId)
                    .action("bookmark")
                    .isAdded(userBookmarked)
                    .bookmarkCount(bookmarkCount)
                    .build());

        }

//        System.out.println("actionDtos: " + actionDtos);
        return WebsiteActionResponseDto.builder()
                .websiteActionDtos(websiteActionDtos)
                .build();
    }
}
