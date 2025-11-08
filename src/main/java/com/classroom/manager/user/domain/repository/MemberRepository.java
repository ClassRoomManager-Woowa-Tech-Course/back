package com.classroom.manager.user.domain.repository;

import com.classroom.manager.user.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {
}
