package com.commerce.project.domain;

import lombok.Getter;

@Getter
public enum ScheduleStatus {
    APPLIED("신청완료"),
    SELECTED("선정"),
    IN_PROGRESS("진행중"),
    POSTED("포스팅완료"),
    CLOSED("종료");

    private final String label;

    ScheduleStatus(String label) {
        this.label = label;
    }
}
