package com.commerce.project.controller;

import com.commerce.project.domain.Member;
import com.commerce.project.dto.ApiResponse;
import com.commerce.project.dto.LoginRequest;
import com.commerce.project.dto.MemberJoinRequest;
import com.commerce.project.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/members/join")
    public ApiResponse<Void> join(@Valid @RequestBody MemberJoinRequest request) {
        Member member = new Member(request.getEmail(), request.getName(), request.getPassword());
        memberService.join(member);
        return ApiResponse.successMsg("회원가입이 완료되었습니다!");
    }

    @PostMapping("/members/login")
    public ApiResponse<String> login(@Valid @RequestBody LoginRequest request) {
        String token = memberService.login(request.getEmail(), request.getPassword());
        return ApiResponse.success(token);
    }

    @GetMapping("/members")
    public ApiResponse<List<Member>> getAllMembers() {
        return ApiResponse.success(memberService.findAllMembers());
    }
}