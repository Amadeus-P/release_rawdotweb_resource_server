package com.main.web.siwa.repository;

import com.main.web.siwa.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c WHERE c.website.id = :websiteId ORDER BY c.regDate DESC")
    List<Comment> findAllByWebsiteId(Long websiteId);
}
