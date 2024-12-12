package com.main.web.siwa.repository;

import com.main.web.siwa.entity.Dislike;
import com.main.web.siwa.entity.Member;
import com.main.web.siwa.entity.Website;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DislikeRepository extends JpaRepository<Dislike, Long> {
    Optional<Dislike> findByWebsiteAndMember(Website website, Member member);
    boolean existsByWebsiteIdAndMemberId(Long memberId, Long websiteId);
    List<Dislike> findAllByMemberId(Long memberId);
    Long countByWebsiteId(Long dislikedWebsiteId);
    Long countByWebsiteIdAndAction(Long websiteId, String action);
    List<Long> countByWebsiteIdIn(List<Long> websiteIds);
}
