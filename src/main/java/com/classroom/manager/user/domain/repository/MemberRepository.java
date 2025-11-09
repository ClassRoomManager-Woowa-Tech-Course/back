package com.classroom.manager.user.domain.repository;

import com.classroom.manager.user.domain.Member;
import com.classroom.manager.user.domain.exception.NotFoundMemberException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    default Member getByMemberId(String memberId) {
        return findById(memberId).orElseThrow(NotFoundMemberException::new);
    }
}
