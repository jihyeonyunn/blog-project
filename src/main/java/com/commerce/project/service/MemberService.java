package com.commerce.project.service;

import com.commerce.project.domain.Member;
import com.commerce.project.jwt.JwtUtil;
import com.commerce.project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // 기본적으로 읽기 전용 (성능 최적화)
@RequiredArgsConstructor // 레포지토리를 자동으로 연결해줌
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * 회원 가입
     */
    @Transactional // 쓰기가 일어나는 곳에만 따로 붙여줌
    public Long join(Member member) {
        // 1. 중복 회원 검증 (가장 먼저 해야 할 일!)
        validateDuplicateMember(member);

        member.encodePassword(passwordEncoder);
        memberRepository.save(member);
        return member.getId();
    }

    /**
     * 중복 회원 검증
     */
    private void validateDuplicateMember(Member member) {
        memberRepository.findByEmail(member.getEmail())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    /**
     * 로그인
     */
    public String login(String email, String password) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        return jwtUtil.generateToken(email);
    }

    /**
     * 전체 회원 조회
     */
    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }
}