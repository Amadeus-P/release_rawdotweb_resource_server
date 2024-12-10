package com.main.web.siwa.repository;

import com.main.web.siwa.entity.Bookmark;
import com.main.web.siwa.entity.Member;
import com.main.web.siwa.entity.Website;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Optional<Bookmark> findByWebsiteAndMember(Website website, Member member);
    boolean existsByWebsiteIdAndMemberId(Long memberId, Long websiteId);
    List<Bookmark> findAllByMemberId(Long memberId);
    Long countByWebsiteId(Long websiteId);
    List<Long> countByWebsiteIdIn(List<Long> websiteIds);
}
