package com.commerce.project.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 일정의 소유자 (1인 사용자 모델)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String campaignName;     // 캠페인명

    private String companyName;      // 업체명

    private String category;         // 카테고리 (자유 텍스트: 맛집/뷰티 등)

    @Column(nullable = false)
    private LocalDateTime visitDate; // 방문일시 (월별 조회 기준, 시:분:초 포함)

    private LocalDate manuscriptDeadline;  // 원고마감일

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScheduleStatus status;   // 진행 상태

    private String address;          // 방문주소

    private String contactNumber;    // 연락처

    private Long cost;               // 비용 (원 단위)

    @Column(columnDefinition = "TEXT")
    private String memo;             // 메모 (긴 텍스트)

    private String applyUrl;         // 신청 링크

    private String postUrl;          // 포스팅 링크

    @Enumerated(EnumType.STRING)
    private Platform platform;       // 플랫폼

    @Enumerated(EnumType.STRING)
    private RewardType rewardType;   // 리워드 종류

    public Schedule(Member member, String campaignName, String companyName, String category,
                    LocalDateTime visitDate, LocalDate manuscriptDeadline, ScheduleStatus status,
                    String address, String contactNumber, Long cost, String memo,
                    String applyUrl, String postUrl, Platform platform, RewardType rewardType) {
        this.member = member;
        this.campaignName = campaignName;
        this.companyName = companyName;
        this.category = category;
        this.visitDate = visitDate;
        this.manuscriptDeadline = manuscriptDeadline;
        this.status = status;
        this.address = address;
        this.contactNumber = contactNumber;
        this.cost = cost;
        this.memo = memo;
        this.applyUrl = applyUrl;
        this.postUrl = postUrl;
        this.platform = platform;
        this.rewardType = rewardType;
    }

    // PUT - 전체 필드 갱신
    public void update(String campaignName, String companyName, String category,
                       LocalDateTime visitDate, LocalDate manuscriptDeadline, ScheduleStatus status,
                       String address, String contactNumber, Long cost, String memo,
                       String applyUrl, String postUrl, Platform platform, RewardType rewardType) {
        this.campaignName = campaignName;
        this.companyName = companyName;
        this.category = category;
        this.visitDate = visitDate;
        this.manuscriptDeadline = manuscriptDeadline;
        this.status = status;
        this.address = address;
        this.contactNumber = contactNumber;
        this.cost = cost;
        this.memo = memo;
        this.applyUrl = applyUrl;
        this.postUrl = postUrl;
        this.platform = platform;
        this.rewardType = rewardType;
    }

    // PATCH - 상태만 변경
    public void changeStatus(ScheduleStatus status) {
        this.status = status;
    }
}
