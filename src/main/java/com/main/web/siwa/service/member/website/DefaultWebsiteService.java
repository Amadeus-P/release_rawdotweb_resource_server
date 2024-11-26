package com.main.web.siwa.service.member.website;

import com.main.web.siwa.dto.member.website.*;
import com.main.web.siwa.entity.*;
import com.main.web.siwa.repository.*;
import com.main.web.siwa.usecase.ghost.websiteImage.dto.WebsiteImageListDto;
import com.main.web.siwa.utility.FileUpload;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service("memberWebsiteService")
public class DefaultWebsiteService implements WebsiteService {

    private final WebsiteRepository websiteRepository;
    private final MemberRepository memberRepository;
    private final WebsiteImageRepository websiteImageRepository;
    private final LikeRepository likeRepository;
    private final DislikeRepository dislikeRepository;
    private final BookmarkRepository bookmarkRepository;
    private final ModelMapper modelMapper;

    public DefaultWebsiteService(
            WebsiteRepository websiteRepository,
            MemberRepository memberRepository,
            WebsiteImageRepository websiteImageRepository,
            LikeRepository likeRepository,
            DislikeRepository dislikeRepository,
            BookmarkRepository bookmarkRepository,
            ModelMapper modelMapper){
        this.websiteRepository = websiteRepository;
        this.memberRepository = memberRepository;
        this.websiteImageRepository = websiteImageRepository;
        this.likeRepository = likeRepository;
        this.dislikeRepository = dislikeRepository;
        this.bookmarkRepository = bookmarkRepository;
        this.modelMapper = modelMapper;
    }

    // 전달 받은 데이터를
    @Override
    public WebsiteResponseDto getList(WebsiteSearchDto websiteSearchDto) {
        return getList(websiteSearchDto.getPage(), websiteSearchDto.getKeyWord(), websiteSearchDto.getCategoryId());
    }
    
    // 여기서 꺼내 쓰기
    @Override
    public WebsiteResponseDto getList(Integer page, String keyWord, Long categoryId) {
        System.out.println("categoryId" + categoryId);
        System.out.println("keyWord" + keyWord);
        System.out.println("page" + page);

        int size = 30; // 페이지당 보여줄 웹사이트 개수
        if (page == null || page < 1) {
            page = 1;
        }
        Sort sort = Sort.by("regDate").descending();
        Pageable pageable = PageRequest.of(page-1, size, sort); // 0
        Page<Website> websitePage = websiteRepository.findAll(keyWord, categoryId, page, size);

        // Entity를 DTO로 바꾸기 > Stream
        List<WebsiteListDto> websiteListDtos = websitePage
                .getContent()
                .stream()
                .map((Website website) -> {
                    WebsiteListDto websiteListDto = modelMapper.map(website, WebsiteListDto.class);

                    // 이미지
                    // 이미지, null이 아닐 경우에만 매핑
                    List<WebsiteImageListDto> imageListDtos =
                            website.getImages() != null ?
                                    website.getImages()
                                            .stream()
                                            .map((WebsiteImage image) -> modelMapper.map(image, WebsiteImageListDto.class))
                                            .toList() : new ArrayList<>();
                    websiteListDto.setImages(imageListDtos);

                    return websiteListDto;
                })
                .toList();

        // 조회된 모든 웹사이트 갯수
        long totalCount = websitePage.getTotalElements();
        long totalPages = websitePage.getTotalPages();
        boolean nextPage = websitePage.hasNext();
        boolean previousPage = websitePage.hasPrevious();

        // 현재 페이지
        page = (page == null) ? 1 : page;

        // 페이지 구간
        Integer offset = (page -1) % 5;

        // 시작번호 구하기
        Integer startNum = page - offset;

        // 현재 페이지에서 다른 페이지 구하기
        List<Integer> pages;
        if (totalPages <= 5) {
            // 총 페이지가 5개 이하인 경우, 전체 페이지 번호 반환
            pages = IntStream.rangeClosed(1, (int) totalPages).boxed().toList();
        } else {
            // 총 페이지가 5개 이상인 경우, 현재 페이지를 기준으로 5개만 반환
            pages = IntStream.range(startNum, Math.min(startNum + 5, (int) totalPages + 1)).boxed().toList();
        }

        return WebsiteResponseDto
                .builder()
                .websiteListDtos(websiteListDtos)
                .totalCount(totalCount)
                .totalPages(totalPages)
                .nextPage(nextPage)
                .prevPage(previousPage)
                .pages(pages)
                .build();
    }

    // GET + id
    @Override
    public WebsiteListDto getById(Long id) {
       Website website =  websiteRepository
               .findById(id)
               .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 웹사이트입니다. id = " + id));
        return modelMapper.map(website, WebsiteListDto.class);
    }
    // POST
    @Override
    @Transactional
    public WebsiteCreateDto create(WebsiteCreateDto websiteCreateDto, MultipartFile image) {

        Website createdWebsite = websiteRepository.save(modelMapper.map(websiteCreateDto, Website.class));

        // 이미지 파일 경로에 저장
        if (image != null && !image.isEmpty()) {
            FileUpload fileUpload = new FileUpload();
            String savedImagePath = fileUpload.saveImage(image, "rawdotweb/images/website");
            System.out.println("savedImagePath: " + savedImagePath);
            
            // 경로 제외 파일명만
//            String fileName = savedImagePath.substring(savedImagePath.lastIndexOf(File.separator) + 1); // 파일명 추출
            String fileName = Paths.get(savedImagePath).getFileName().toString();
            System.out.println("Extracted File Name: " + fileName);
            // 3. WebsiteImage 엔티티 생성 및 저장

            WebsiteImage websiteImage = WebsiteImage.builder()
                    .src(fileName)
                    .isDefault(true) // 첫 번째 이미지는 기본 이미지로 설정
                    .website(createdWebsite) // 연관 관계 설정
                    .build();

            websiteImageRepository.save(websiteImage); // 저장
        }

        return modelMapper.map(createdWebsite, WebsiteCreateDto.class);
    }
    // PUT
    @Override
    public WebsiteListDto update(WebsiteListDto websiteListDto, MultipartFile newImage) {
        // 1. JPA SQL Method
        // 업데이트는 기존에 존재하는 테이블에만 데이터를 저장할 수 있게
        // 테이블 id가 있는지 확인하는 과정을 넣는다.
        Website website = websiteRepository
                .findById(websiteListDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 웹사이트입니다. id = " + websiteListDto.getId()));

        // 일괄 업데이트가 아닌 부분 업데이트 코드
        if(websiteListDto.getTitle() != null)
            website.setTitle(websiteListDto.getTitle());

        if(websiteListDto.getUrl() != null)
            website.setUrl(websiteListDto.getUrl());

        if(websiteListDto.getCategory() != null)
            website.setCategory(modelMapper.map(websiteListDto.getCategory(), Category.class));
        
        // 이미지 업데이트
        if (newImage != null && !newImage.isEmpty()) {
            FileUpload fileUpload = new FileUpload();
            String savedImagePath = fileUpload.saveImage(newImage, "rawdotweb/images");

            // WebsiteImage 엔티티 생성 및 저장
            WebsiteImage newWebsiteImage = WebsiteImage.builder()
                    .src(savedImagePath)
                    .isDefault(true) // 새 이미지는 기본 이미지가 아닐 수 있음
                    .website(website) // 연관 관계 설정
                    .build();

            websiteImageRepository.save(newWebsiteImage);
        }
        // 저장
        Website updatedWebsite = websiteRepository.save(website);
        // 업데이트 된 웹사이트 가져오기
//        Website updateWebsite = websiteRepository
//                .findById(websiteListDto.getId())
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 웹사이트입니다. id = " + websiteListDto.getId()));
        return modelMapper.map(updatedWebsite, WebsiteListDto.class);

    }
    // DELETE
    @Override
    public void delete(Long id) {
        websiteRepository.deleteById(id);
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
