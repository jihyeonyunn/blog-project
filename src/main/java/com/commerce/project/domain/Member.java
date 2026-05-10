package com.commerce.project.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity  // DB가 "아, 이건 테이블로 만들어야지!"라고 인식하게 함
@Getter  // 코드를 안 짜도 필드값을 가져오는 함수를 자동 생성 (Lombok)
@NoArgsConstructor // 파라미터 없는 기본 생성자를 자동 생성
public class Member {

    @Id // 기본키(Primary Key) 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 1, 2, 3... 자동 증가
    private Long id;

    private String email;
    private String name;
    private String password;

    // 데이터를 넣을 때 쓸 생성자
    public Member(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public void encodePassword(BCryptPasswordEncoder encoder) {
        this.password = encoder.encode(this.password);
    }
}
