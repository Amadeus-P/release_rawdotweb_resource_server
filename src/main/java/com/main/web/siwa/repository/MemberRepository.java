package com.main.web.siwa.repository;

import com.main.web.siwa.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByUsername(String username);

    boolean existsByUsername(String username);
}
