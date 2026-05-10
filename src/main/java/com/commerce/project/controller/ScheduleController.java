package com.commerce.project.controller;

import com.commerce.project.domain.ScheduleStatus;
import com.commerce.project.dto.ApiResponse;
import com.commerce.project.dto.MonthlyStatResponse;
import com.commerce.project.dto.ScheduleCreateRequest;
import com.commerce.project.dto.ScheduleListResponse;
import com.commerce.project.dto.ScheduleResponse;
import com.commerce.project.dto.ScheduleStatusUpdateRequest;
import com.commerce.project.dto.ScheduleUpdateRequest;
import com.commerce.project.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ApiResponse<Long> create(@Valid @RequestBody ScheduleCreateRequest request) {
        Long id = scheduleService.create(request);
        return ApiResponse.success(id);
    }

    @GetMapping
    public ApiResponse<List<ScheduleListResponse>> list(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) ScheduleStatus status) {
        return ApiResponse.success(scheduleService.findList(year, month, status));
    }

    @GetMapping("/{id}")
    public ApiResponse<ScheduleResponse> detail(@PathVariable Long id) {
        return ApiResponse.success(scheduleService.findOne(id));
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable Long id,
                                    @Valid @RequestBody ScheduleUpdateRequest request) {
        scheduleService.update(id, request);
        return ApiResponse.successMsg("일정이 수정되었습니다.");
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<Void> changeStatus(@PathVariable Long id,
                                          @Valid @RequestBody ScheduleStatusUpdateRequest request) {
        scheduleService.changeStatus(id, request.getStatus());
        return ApiResponse.successMsg("상태가 변경되었습니다.");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        scheduleService.delete(id);
        return ApiResponse.successMsg("일정이 삭제되었습니다.");
    }

    @GetMapping("/stats/monthly")
    public ApiResponse<MonthlyStatResponse> monthlyStats(@RequestParam int year) {
        return ApiResponse.success(scheduleService.monthlyStats(year));
    }
}
