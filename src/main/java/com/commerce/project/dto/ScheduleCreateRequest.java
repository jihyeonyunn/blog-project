package com.commerce.project.dto;

import com.commerce.project.domain.Member;
import com.commerce.project.domain.Platform;
import com.commerce.project.domain.RewardType;
import com.commerce.project.domain.Schedule;
import com.commerce.project.domain.ScheduleStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ScheduleCreateRequest {

    @NotBlank(message = "캠페인명은 필수입니다.")
    private String campaignName;

    private String companyName;
    private String category;

    @NotNull(message = "방문일시는 필수입니다.")
    private LocalDateTime visitDate;

    private LocalDate manuscriptDeadline;

    @NotNull(message = "상태는 필수입니다.")
    private ScheduleStatus status;

    private String address;
    private String contactNumber;
    private Long cost;
    private String memo;
    private String applyUrl;
    private String postUrl;
    private Platform platform;
    private RewardType rewardType;

    public Schedule toEntity(Member owner) {
        return new Schedule(owner, campaignName, companyName, category,
                visitDate, manuscriptDeadline, status,
                address, contactNumber, cost, memo,
                applyUrl, postUrl, platform, rewardType);
    }
}
