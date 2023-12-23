package com.hoxton.crawler.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@Slf4j
class DailyStockDataServiceTest {

    @InjectMocks
    DailyStockDataService dailyStockDataService;
    ArrayList<String> totalMonthList = new ArrayList<>(List.of("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"));

    @Test
    void getYearMonthData1() {
        Map<String, List<String>> yearMonthData = dailyStockDataService.getYearMonthData(112, 12, 1);
        assertThat(yearMonthData).hasSize(1);
        log.info("當年尾，時間區間為1時，長度為1");
    }
    @Test
    void getYearMonthData2() {
        Map<String, List<String>> yearMonthData = dailyStockDataService.getYearMonthData(112, 12, 3);
        assertThat(yearMonthData).hasSize(3);
        log.info("當年尾，時間區間為3時，長度為3");
    }


    @Test
    void getYearMonthData3() {
        Map<String, List<String>> yearMonthData = dailyStockDataService.getYearMonthData(112, 12, 10);
        assertThat(yearMonthData).hasSize(5);
        assertThat(yearMonthData.get(String.valueOf(112))).isEqualTo(totalMonthList);
        assertThat(yearMonthData.get(String.valueOf(111))).isEqualTo(totalMonthList);
        assertThat(yearMonthData.get(String.valueOf(110))).isEqualTo(totalMonthList);
        log.info("當年尾，時間區間大於5時，長度為5，只會回傳前五年的資料");
    }
    @Test
    void getYearMonthData4() {
        Map<String, List<String>> yearMonthData = dailyStockDataService.getYearMonthData(112, 5, 1);
        assertThat(yearMonthData).hasSize(2);
        assertThat(yearMonthData.get(String.valueOf(112))).isEqualTo(totalMonthList.subList(0,5));
        assertThat(yearMonthData.get(String.valueOf(111))).isEqualTo(totalMonthList.subList(5,totalMonthList.size()));
        log.info("112年:{}", yearMonthData.get(String.valueOf(112)));
        log.info("111年:{}", yearMonthData.get(String.valueOf(111)));
        log.info("開始時間為五月，時間區間為1時，長度為2，日期區間正確");
    }


    @Test
    void getYearMonthData5() {
        Map<String, List<String>> yearMonthData = dailyStockDataService.getYearMonthData(112, 5, 3);
        assertThat(yearMonthData).hasSize(4);
        assertThat(yearMonthData.get(String.valueOf(112))).isEqualTo(totalMonthList.subList(0,5));
        assertThat(yearMonthData.get(String.valueOf(111))).isEqualTo(totalMonthList);
        assertThat(yearMonthData.get(String.valueOf(110))).isEqualTo(totalMonthList);
        assertThat(yearMonthData.get(String.valueOf(109))).isEqualTo(totalMonthList.subList(5,totalMonthList.size()));
        log.info("112年:{}", yearMonthData.get(String.valueOf(112)));
        log.info("111年:{}", yearMonthData.get(String.valueOf(111)));
        log.info("開始時間為五月，時間區間為3時，長度為4，日期區間正確");
    }

    @Test
    void getYearMonthData6() {
        Map<String, List<String>> yearMonthData = dailyStockDataService.getYearMonthData(112, 5, 10);
        assertThat(yearMonthData).hasSize(6);
        assertThat(yearMonthData.get(String.valueOf(112))).isEqualTo(totalMonthList.subList(0,5));
        assertThat(yearMonthData.get(String.valueOf(111))).isEqualTo(totalMonthList);
        assertThat(yearMonthData.get(String.valueOf(110))).isEqualTo(totalMonthList);
        assertThat(yearMonthData.get(String.valueOf(109))).isEqualTo(totalMonthList);
        assertThat(yearMonthData.get(String.valueOf(108))).isEqualTo(totalMonthList);
        assertThat(yearMonthData.get(String.valueOf(107))).isEqualTo(totalMonthList.subList(5,totalMonthList.size()));
        log.info("112年:{}", yearMonthData.get(String.valueOf(112)));
        log.info("111年:{}", yearMonthData.get(String.valueOf(111)));
        log.info("開始時間為五月，時間區間為10時，長度為6，日期區間正確");
    }
}