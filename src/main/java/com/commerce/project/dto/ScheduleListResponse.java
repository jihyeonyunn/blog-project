package com.commerce.project.dto;

import com.commerce.project.domain.Platform;
import com.commerce.project.domain.Schedule;
import com.commerce.project.domain.ScheduleStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ScheduleListResponse {

    private final Long id;
    private final String campaignName;
    private final String companyName;
    private final LocalDateTime visitDate;
    private final ScheduleStatus status;
    private final String statusLabel;
    private final Long cost;
    private final Platform platform;

    private ScheduleListResponse(Schedule s) {
        this.id = s.getId();
        this.campaignName = s.getCampaignName();
        this.companyName = s.getCompanyName();
        this.visitDate = s.getVisitDate();
        this.status = s.getStatus();
        this.statusLabel = s.getStatus() != null ? s.getStatus().getLabel() : null;
        this.cost = s.getCost();
        this.platform = s.getPlatform();
    }

    public static ScheduleListResponse from(Schedule s) {
        return new ScheduleListResponse(s);
    }

    public static List<ScheduleListResponse> fromList(List<Schedule> schedules) {
        return schedules.stream().map(ScheduleListResponse::from).toList();
    }
}
