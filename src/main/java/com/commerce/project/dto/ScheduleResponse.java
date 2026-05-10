package com.commerce.project.dto;

import com.commerce.project.domain.Platform;
import com.commerce.project.domain.RewardType;
import com.commerce.project.domain.Schedule;
import com.commerce.project.domain.ScheduleStatus;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class ScheduleResponse {

    private final Long id;
    private final String campaignName;
    private final String companyName;
    private final String category;
    private final LocalDateTime visitDate;
    private final LocalDate manuscriptDeadline;
    private final ScheduleStatus status;
    private final String statusLabel;        // 한글 표시용
    private final String address;
    private final String contactNumber;
    private final Long cost;
    private final String memo;
    private final String applyUrl;
    private final String postUrl;
    private final Platform platform;
    private final RewardType rewardType;
    private final String rewardTypeLabel;    // 한글 표시용

    private ScheduleResponse(Schedule s) {
        this.id = s.getId();
        this.campaignName = s.getCampaignName();
        this.companyName = s.getCompanyName();
        this.category = s.getCategory();
        this.visitDate = s.getVisitDate();
        this.manuscriptDeadline = s.getManuscriptDeadline();
        this.status = s.getStatus();
        this.statusLabel = s.getStatus() != null ? s.getStatus().getLabel() : null;
        this.address = s.getAddress();
        this.contactNumber = s.getContactNumber();
        this.cost = s.getCost();
        this.memo = s.getMemo();
        this.applyUrl = s.getApplyUrl();
        this.postUrl = s.getPostUrl();
        this.platform = s.getPlatform();
        this.rewardType = s.getRewardType();
        this.rewardTypeLabel = s.getRewardType() != null ? s.getRewardType().getLabel() : null;
    }

    public static ScheduleResponse from(Schedule s) {
        return new ScheduleResponse(s);
    }
}
