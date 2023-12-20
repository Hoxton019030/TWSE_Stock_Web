package com.hoxton.crawler.service;

import com.hoxton.crawler.dao.DailyStockDataMapper;
import com.hoxton.crawler.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DailyStockDataService {
    private  final DailyStockDataMapper dailyStockDataDao;
    /**
     * 西元與民國之間的落差年
     */
    private static final  Integer BC_TO_ROC_DIFF = 1911;

    private final TestMapper testD;
    public void print(){
        System.out.println("Hello Fuck");
    }

    public List<String> findMissingMonth(String stockCode,Integer yearInterval){
        List<String> missingMonth = dailyStockDataDao.findMonthList(stockCode);
        List<LocalDate> localDates = new ArrayList<>();
        for (String date : missingMonth) {
            LocalDate localDate = DateUtils.BCConverter(date);
            localDates.add(localDate);
        }
        LocalDate currentDate = LocalDate.now();

        // 计算一年前的日期
        LocalDate oneYearAgo = currentDate.minusYears(yearInterval);

        // 過濾出一年内的日期
        List<LocalDate> datesWithinOneYear = localDates.stream()
                .filter(date -> date.isAfter(oneYearAgo) || date.isEqual(oneYearAgo))
                .collect(Collectors.toList());

        // 找出一年內有的日期
        Set<Integer> existingMonths = datesWithinOneYear.stream().map(LocalDate::getMonthValue).collect(Collectors.toSet());

        Set<Integer> allMonths = new HashSet<>();
        for (int i = 1; i <= 12; i++) {
            allMonths.add(i);
        }

        allMonths.removeAll(existingMonths);

        List<String> result = allMonths.stream()
                .map(missingMonthValue ->
                        String.format("%02d/%02d", oneYearAgo.getYear() - BC_TO_ROC_DIFF, missingMonthValue))
                .collect(Collectors.toList());

        log.info("Hoxton log測試result123123:{}", result);
        return result;

    }
}
