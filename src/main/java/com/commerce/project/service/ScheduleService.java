package com.commerce.project.service;

import com.commerce.project.domain.Member;
import com.commerce.project.domain.Schedule;
import com.commerce.project.domain.ScheduleStatus;
import com.commerce.project.dto.MonthlyStatResponse;
import com.commerce.project.dto.ScheduleCreateRequest;
import com.commerce.project.dto.ScheduleListResponse;
import com.commerce.project.dto.ScheduleResponse;
import com.commerce.project.dto.ScheduleUpdateRequest;
import com.commerce.project.repository.MemberRepository;
import com.commerce.project.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final MemberRepository memberRepository;

    /**
     * 일정 생성
     */
    @Transactional
    public Long create(ScheduleCreateRequest req) {
        Schedule schedule = req.toEntity(currentMember());
        scheduleRepository.save(schedule);
        return schedule.getId();
    }

    /**
     * 일정 상세 조회
     */
    public ScheduleResponse findOne(Long id) {
        Schedule schedule = loadOwned(id);
        return ScheduleResponse.from(schedule);
    }

    /**
     * 일정 목록 조회 (year+month, status 필터 모두 optional)
     */
    public List<ScheduleListResponse> findList(Integer year, Integer month, ScheduleStatus status) {
        Member me = currentMember();
        List<Schedule> schedules;

        if (year != null && month != null) {
            LocalDate firstDay = LocalDate.of(year, month, 1);
            LocalDateTime from = firstDay.atStartOfDay();
            LocalDateTime to = firstDay.plusMonths(1).atStartOfDay().minusNanos(1);
            schedules = (status != null)
                    ? scheduleRepository.findByMemberAndVisitDateBetweenAndStatusOrderByVisitDateAsc(me, from, to, status)
                    : scheduleRepository.findByMemberAndVisitDateBetweenOrderByVisitDateAsc(me, from, to);
        } else if (status != null) {
            schedules = scheduleRepository.findByMemberAndStatusOrderByVisitDateDesc(me, status);
        } else {
            schedules = scheduleRepository.findByMemberOrderByVisitDateDesc(me);
        }

        return ScheduleListResponse.fromList(schedules);
    }

    /**
     * 일정 전체 수정 (PUT)
     */
    @Transactional
    public void update(Long id, ScheduleUpdateRequest req) {
        Schedule schedule = loadOwned(id);
        schedule.update(
                req.getCampaignName(), req.getCompanyName(), req.getCategory(),
                req.getVisitDate(), req.getManuscriptDeadline(), req.getStatus(),
                req.getAddress(), req.getContactNumber(), req.getCost(), req.getMemo(),
                req.getApplyUrl(), req.getPostUrl(), req.getPlatform(), req.getRewardType()
        );
    }

    /**
     * 상태만 변경 (PATCH)
     */
    @Transactional
    public void changeStatus(Long id, ScheduleStatus status) {
        Schedule schedule = loadOwned(id);
        schedule.changeStatus(status);
    }

    /**
     * 일정 삭제
     */
    @Transactional
    public void delete(Long id) {
        Schedule schedule = loadOwned(id);
        scheduleRepository.delete(schedule);
    }

    /**
     * 월별 비용 통계 (1~12월 zero-fill)
     */
    public MonthlyStatResponse monthlyStats(int year) {
        List<Object[]> rows = scheduleRepository.sumCostPerMonth(currentMember(), year);
        return MonthlyStatResponse.from(year, rows);
    }

    /**
     * 본인 소유의 일정만 로드 (없거나 남의 것이면 동일하게 예외)
     */
    private Schedule loadOwned(Long id) {
        return scheduleRepository.findByIdAndMember(id, currentMember())
                .orElseThrow(() -> new IllegalArgumentException("일정을 찾을 수 없습니다."));
    }

    /**
     * 현재 인증된 회원 조회 — JwtFilter가 principal에 email을 넣어줌
     */
    private Member currentMember() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("인증된 회원을 찾을 수 없습니다."));
    }
}
