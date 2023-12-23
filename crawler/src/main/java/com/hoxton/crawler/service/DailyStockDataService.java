package com.hoxton.crawler.service;

import com.hoxton.crawler.dao.DailyStockDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DailyStockDataService {
    private final DailyStockDataMapper dailyStockDataDao;
    /**
     * 西元與民國之間的落差年
     */
    private static final Integer BC_TO_ROC_DIFF = 1911;

//    private static final String[] monthArray = {"","01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};

    private final TestMapper testD;

    public void print() {
        System.out.println("Hello Fuck");
    }

    public List<String> findMissingMonth(String stockCode, Integer yearInterval) {
        List<String> missingMonth = dailyStockDataDao.findMonthList(stockCode);
        HashMap<String, HashSet<String>> mapOfMonth = new HashMap<>();
        for (String date : missingMonth) {
            String[] datem = date.split("/");
            String year = datem[0];
            String month = datem[1];
            if (!mapOfMonth.containsKey(year)) {
                mapOfMonth.put(year, new HashSet<>());
            }
            if (mapOfMonth.containsKey(year)) {
                HashSet<String> strings = mapOfMonth.get(year);
                strings.add(month);
                mapOfMonth.put(year, strings);
            }
        }
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int monthInt = calendar.get(Calendar.MONTH) + 1;
        Year now = Year.now();
        Map<String,List<String>> monthOfYear = test3(year,monthInt,yearInterval);
        String month = null;
        if (monthInt < 10) {
            month = "0" + monthInt;
        } else {
            month = String.valueOf(monthInt);
        }

        int initMonth = 0;
        for (int i = 0; i < monthArray.length; i++) {
            if (month.equals(monthArray[i])) {
                initMonth = i;
                break;
            }
        }
        initMonth =3;
        log.info("Hoxton log測試initMonth:{}", initMonth);

        Integer startYear = now.getValue() - BC_TO_ROC_DIFF;
        HashMap<String, HashSet<String>> shouldOfMonth = new HashMap<>();
        Integer yearInterval2 = yearInterval;
        for (int i = startYear; yearInterval != 0; i--) {
            int monthCount =yearInterval * 12;
            int forMonth = 0;
            if (yearInterval.equals(yearInterval2)) {
                forMonth = initMonth;
            }
            if (!yearInterval.equals(yearInterval2)) {
                forMonth = 12;
            }

            shouldOfMonth.put(String.valueOf(i), new HashSet<>());
            for (int j = forMonth; j != 0; j--) {
                log.info("Hoxton log測試j的值:{}", j);
                log.info("Hoxton log測試initMonth的值:{}", initMonth);
                if(initMonth==-1) break;;

                HashSet<String> strings = shouldOfMonth.get(String.valueOf(i));
                strings.add(monthArray[initMonth]);
                initMonth--;
                monthCount--;
            }
            initMonth = 12;
            yearInterval--;
        }

        shouldOfMonth.forEach((s, strings) -> {
            log.info("Hoxton log測試s:{}", s);
            log.info("Hoxton log測試strings:{}", strings);
        });
        return null;


    }

    public Map<String, List<String>> test3(int startYear, int startMonth, int yearInterval) {
        LinkedList<String> list = new LinkedList<>(List.of("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"));
        HashMap<String, List<String>> answer = new HashMap<>();
        if(startMonth==12){ //如果在12月，就回傳整年數據
            for(int i =0 ; yearInterval>0;i++){
                int year = startYear- i;
                answer.put(String.valueOf(year), list.subList(0,list.size()));
                yearInterval--;
            }
            return answer;
        }
        if(yearInterval==1){
            answer.put(String.valueOf(startYear),list.subList(0,startMonth));
            answer.put(String.valueOf(startYear-1),list.subList(startMonth,list.size()));
            return answer;
        }
        int t=yearInterval+1;
        return null;

    }
}
