package com.main.web.siwa.repository;

import com.main.web.siwa.entity.Website;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebsiteRepository extends JpaRepository<Website, Long>,WebsiteCustomRepository {

//    오버로딩이 아니라 @Query 한 번에 조건 처리 해야할 때
//    @Query("from Website where (:name is null or name like concat('%', :website_name, '%')) and (:category_ids is null or categories in (:category_ids)) ")
//    Page<Website> findAll(@Param("website_name") String query, @Param("category_ids") List<Long> cid, Pageable pageable);
//    @Query("from Website where (:name is null or name like concat('%', :website_name, '%'))")
//    Page<Website> findAll(@Param("website_name") String query, Pageable pageable);

//    웹사이트 이름 검색
//    select * from website where name regexp '[ㄱ-힇]';
//    @Query("from Website w where w.title like concat('%', :name, '%')")
//    Page<Website> findAll(@Param("name") String query, Pageable pageable);



}
