package com.commerce.project.dto;

import com.commerce.project.domain.ScheduleStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ScheduleStatusUpdateRequest {

    @NotNull(message = "상태는 필수입니다.")
    private ScheduleStatus status;
}
