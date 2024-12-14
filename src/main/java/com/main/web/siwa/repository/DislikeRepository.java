package com.main.web.siwa.repository;

import com.main.web.siwa.entity.Dislike;
import com.main.web.siwa.entity.Member;
import com.main.web.siwa.entity.Website;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DislikeRepository extends JpaRepository<Dislike, Long> {
    Optional<Dislike> findByWebsiteAndMember(Website website, Member member);
    boolean existsByWebsiteIdAndMemberId(Long memberId, Long websiteId);
    List<Dislike> findAllByMemberId(Long memberId);
    Long countByWebsiteId(Long dislikedWebsiteId);
    Long countByWebsiteIdAndAction(Long websiteId, String action);

    @Query("SELECT w.id, COUNT(l) " +
            "FROM Dislike l JOIN l.website w " +
            "WHERE w.id IN :websiteIds " +
            "GROUP BY w.id " +
            "ORDER BY w.id")
    List<Object[]> findDislikeCountsByWebsiteIds(@Param("websiteIds") List<Long> websiteIds);
}
