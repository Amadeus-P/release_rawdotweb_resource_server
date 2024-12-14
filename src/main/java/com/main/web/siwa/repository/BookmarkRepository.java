package com.main.web.siwa.repository;

import com.main.web.siwa.entity.Bookmark;
import com.main.web.siwa.entity.Member;
import com.main.web.siwa.entity.Website;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Optional<Bookmark> findByWebsiteAndMember(Website website, Member member);
    List<Bookmark> findAllByMemberId(Long memberId);
    Boolean existsByWebsiteIdAndMemberId(Long memberId, Long websiteId);
    Long countByWebsiteId(Long websiteId);
    @Query("SELECT w.id, COUNT(b)" +
            "FROM Bookmark b JOIN b.website w " +
            "WHERE w.id IN :websiteIds " +
            "GROUP BY w.id " +
            "ORDER BY w.id")
    List<Object[]> findBookmarkCountsByWebsiteIds(@Param("websiteIds") List<Long> websiteIds);
}
