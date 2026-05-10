package com.commerce.project.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MonthlyStatResponse {

    private final int year;
    private final List<MonthlyEntry> months;  // 1~12월 12개 (데이터 없는 달은 0)

    private MonthlyStatResponse(int year, List<MonthlyEntry> months) {
        this.year = year;
        this.months = months;
    }

    public static MonthlyStatResponse from(int year, List<Object[]> rows) {
        long[] totals = new long[13];  // index 1~12 사용
        for (Object[] row : rows) {
            int month = ((Number) row[0]).intValue();
            long total = ((Number) row[1]).longValue();
            totals[month] = total;
        }

        List<MonthlyEntry> entries = new ArrayList<>(12);
        for (int m = 1; m <= 12; m++) {
            entries.add(new MonthlyEntry(m, totals[m]));
        }
        return new MonthlyStatResponse(year, entries);
    }

    @Getter
    public static class MonthlyEntry {
        private final int month;
        private final long totalCost;

        public MonthlyEntry(int month, long totalCost) {
            this.month = month;
            this.totalCost = totalCost;
        }
    }
}
