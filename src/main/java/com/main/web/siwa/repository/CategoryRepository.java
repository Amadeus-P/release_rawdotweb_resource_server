package com.main.web.siwa.repository;

import com.main.web.siwa.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    // 대분류에서 소분류를 찾아가기. 대분류는 parentId가 없기에
    List<Category> findAllByParentIdIsNull();
    List<Category> findAllByParentId(Long parentId);
    boolean existsByName(String name);
}
