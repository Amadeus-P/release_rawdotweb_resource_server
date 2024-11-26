package com.main.web.siwa.repository;

import com.main.web.siwa.entity.Member;
import com.main.web.siwa.entity.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRoleRepository extends JpaRepository<MemberRole, Long> {

    @Query("from MemberRole mr left join fetch mr.role where mr.member.username= :username ")
    List<MemberRole> findAllByMember_Username(@Param("username") String username);
}
