package com.commerce.project.repository;

import com.commerce.project.domain.Member;
import com.commerce.project.domain.Schedule;
import com.commerce.project.domain.ScheduleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    // 단건 조회 - owner 검증을 같이 수행 (없거나 남의 것이면 empty)
    Optional<Schedule> findByIdAndMember(Long id, Member member);

    // 전체 목록 (필터 없을 때)
    List<Schedule> findByMemberOrderByVisitDateDesc(Member member);

    // 월 범위 필터
    List<Schedule> findByMemberAndVisitDateBetweenOrderByVisitDateAsc(
            Member member, LocalDateTime from, LocalDateTime to);

    // 월 범위 + 상태 필터
    List<Schedule> findByMemberAndVisitDateBetweenAndStatusOrderByVisitDateAsc(
            Member member, LocalDateTime from, LocalDateTime to, ScheduleStatus status);

    // 상태 단독 필터
    List<Schedule> findByMemberAndStatusOrderByVisitDateDesc(
            Member member, ScheduleStatus status);

    // 월별 비용 합계 (year 기준) — [month, totalCost] 행들 반환, 데이터 없는 달은 누락
    @Query("""
            select function('MONTH', s.visitDate), coalesce(sum(s.cost), 0)
            from Schedule s
            where s.member = :member and function('YEAR', s.visitDate) = :year
            group by function('MONTH', s.visitDate)
            """)
    List<Object[]> sumCostPerMonth(@Param("member") Member member, @Param("year") int year);
}
