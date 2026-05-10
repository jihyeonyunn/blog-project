package com.commerce.project.repository;

import com.commerce.project.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    // 이메일로 회원을 찾는 메서드 (Spring Data JPA가 알아서 SQL 생성)
    Optional<Member> findByEmail(String email);
}