package com.hoxton.crawler.service;

import com.hoxton.crawler.dao.DailyStockDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Year;
import java.util.*;

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
        List<String> dataBaseMonth = dailyStockDataDao.findMonthList(stockCode);
        HashMap<String, HashSet<String>> mapOfMonth = new HashMap<>();
        for (String date : dataBaseMonth) {
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



        mapOfMonth.forEach((s, strings) -> {
            log.info("Hoxton log測試s321:{}", s);
            log.info("Hoxton log測試strings321:{}", strings);
        });
        int startYear = Year.now().getValue()-BC_TO_ROC_DIFF;
        int monthValue = LocalDate.now().getMonthValue();
        Map<String, List<String>> stringListMap = getYearMonthData(startYear, monthValue, yearInterval);
        stringListMap.forEach(
                (s, strings) -> {
                    log.info("Hoxton log測試s123:{}", s);
                    log.info("Hoxton log測試strings123:{}", strings);
                }
        );
        return null;


    }

    /**
     * @param startYear 從民國哪一年開始
     * @param startMonth 從哪一個月開始
     * @param yearInterval 往前推幾年
     * @return
     */
    public Map<String, List<String>> getYearMonthData(int startYear, int startMonth, int yearInterval) {
        LinkedList<String> list = new LinkedList<>(List.of("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"));
        HashMap<String, List<String>> answer = new HashMap<>();
        if(startMonth==12){ //如果在12月，就回傳整年數據
            if(yearInterval>5) yearInterval=5; //時間區間如果超過五年，則鎖在五年內
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
        if(yearInterval==2){
            answer.put(String.valueOf(startYear),list.subList(0,startMonth));
            answer.put(String.valueOf(startYear-1),list.subList(0,list.size()));
            answer.put(String.valueOf(startYear-2),list.subList(startMonth,list.size()));
            return answer;
        }
        if(yearInterval==3){
            answer.put(String.valueOf(startYear),list.subList(0,startMonth));
            answer.put(String.valueOf(startYear-1),list.subList(0,list.size()));
            answer.put(String.valueOf(startYear-2),list.subList(0,list.size()));
            answer.put(String.valueOf(startYear-3),list.subList(startMonth,list.size()));
            return answer;
        }
        if(yearInterval==4){
            answer.put(String.valueOf(startYear),list.subList(0,startMonth));
            answer.put(String.valueOf(startYear-1),list.subList(0,list.size()));
            answer.put(String.valueOf(startYear-2),list.subList(0,list.size()));
            answer.put(String.valueOf(startYear-3),list.subList(0,list.size()));
            answer.put(String.valueOf(startYear-4),list.subList(startMonth,list.size()));
            return answer;
        }
        if(yearInterval==5 || yearInterval>5){
            answer.put(String.valueOf(startYear),list.subList(0,startMonth));
            answer.put(String.valueOf(startYear-1),list.subList(0,list.size()));
            answer.put(String.valueOf(startYear-2),list.subList(0,list.size()));
            answer.put(String.valueOf(startYear-3),list.subList(0,list.size()));
            answer.put(String.valueOf(startYear-4),list.subList(0,list.size()));
            answer.put(String.valueOf(startYear-5),list.subList(startMonth,list.size()));
            return answer;
        }
        return answer;

    }
}
