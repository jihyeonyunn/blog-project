package com.commerce.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class CommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommerceApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


	/*
	테스트 주석처리
	@Bean
	public CommandLineRunner initData(MemberService memberService) {
		return args -> {
			// 회원가입
			memberService.join(new Member("jihyeon@test.com", "윤지현"));
			System.out.println("✅ Service를 통해 지현님의 데이터가 안전하게 저장되었습니다!");
		};
	}*/
}