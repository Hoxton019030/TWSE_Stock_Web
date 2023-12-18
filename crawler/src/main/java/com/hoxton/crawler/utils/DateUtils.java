package com.hoxton.crawler.utils;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DateUtils {
    /**
     * @param startDateStr yyyyMM
     * @param endDateStr yyyyMM
     * @return
     */
    public static List<String> getMonthsInRange(String startDateStr, String endDateStr) {
        List<String> monthsInRange = new ArrayList<>();

        LocalDate startDate = parseDateString(startDateStr);
        LocalDate endDate = parseDateString(endDateStr);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate currentMonth = startDate;

        while (!currentMonth.isAfter(endDate)) {
            monthsInRange.add(currentMonth.format(formatter));
            currentMonth = currentMonth.plusMonths(1);
        }

        return monthsInRange;
    }
    private static LocalDate parseDateString(String dateString) {
        return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
}
