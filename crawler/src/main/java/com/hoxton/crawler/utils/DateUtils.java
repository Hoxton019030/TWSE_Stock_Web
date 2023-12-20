package com.hoxton.crawler.utils;

import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DateUtils {
    /**
     * 西元與民國之間的落差年
     */
    private static final  Integer BC_TO_ROC_DIFF = 1911;

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

    /**
     * 將民國日期轉成西元日期 112/01/01 -> 2023/01/01
     * @param taiwanDate
     * @return
     */
    public static LocalDate BCConverter(String taiwanDate){
        if (ObjectUtils.isEmpty(taiwanDate)) {
            throw new RuntimeException("日期為空，有錯誤");
        }
        String[] split = taiwanDate.split("/");
        int year = BC_TO_ROC_DIFF+Integer.parseInt(split[0]);
        int month = Integer.parseInt(split[1]);
        int day = Integer.parseInt(split[2]);
        return LocalDate.of(year,month,day);
    }
}
