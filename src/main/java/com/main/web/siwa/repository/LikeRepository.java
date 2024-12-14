package com.main.web.siwa.repository;

import com.main.web.siwa.entity.Likes;
import com.main.web.siwa.entity.Member;
import com.main.web.siwa.entity.Website;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByWebsiteAndMember(Website website, Member member);
    boolean existsByWebsiteIdAndMemberId(Long memberId, Long websiteId);
    List<Likes> findAllByMemberId(Long memberId);
    Long countByWebsiteId(Long likedWebsiteId);

    @Query("SELECT w.id, COUNT(l) " +
            "FROM Likes l JOIN l.website w " +
            "WHERE w.id IN :websiteIds " +
            "GROUP BY w.id " +
            "ORDER BY w.id")
    List<Object[]> findLikeCountsByWebsiteIds(@Param("websiteIds") List<Long> websiteIds);
}
