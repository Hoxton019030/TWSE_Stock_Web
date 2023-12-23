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
        int startYear = Year.now().getValue() - BC_TO_ROC_DIFF;
        int monthValue = LocalDate.now().getMonthValue();
        List<String> dataBaseMonth = dailyStockDataDao.findMonthList(stockCode);
        //將資料庫撈出的日期整理成HashMap
        Map<String, List<String>> dateInDataBase = dateListToHashMap(dataBaseMonth);
        //取得這段期間應該有的股票日期
        Map<String, List<String>> dateThatShouldBe = getYearMonthData(startYear, monthValue, yearInterval);
        List<String> intersection = intersection(dateThatShouldBe, dateInDataBase);
        //缺少的部分
        log.info("Hoxton log測試intersection:{}", intersection);


        return null;


    }

    /**
     * 取出叉集
     * @param dateThatShouldBe
     * @param dateInDataBase
     * @return
     */
    private List<String> intersection(Map<String, List<String>> dateThatShouldBe, Map<String, List<String>> dateInDataBase) {
        List<String> major = new ArrayList<>();
        List<String> sub = new ArrayList<>();
        List<String> answer = new ArrayList<>();
        dateThatShouldBe.forEach((s, strings) ->
                strings.forEach(s1 -> major.add(s + "/" + s1))
        );
        dateInDataBase.forEach((s, strings) ->
                strings.forEach(s1 -> sub.add(s + "/" + s1))
        );

        for (String s : major) {
            if(!sub.contains(s)){
                answer.add(s);
            }
        }
        log.info("Hoxton log測試strings1:{}", major);
        log.info("Hoxton log測試strings2:{}", sub);
        log.info("Hoxton log測試answer:{}", answer);

        return answer;


    }

    /**
     * 傳進來的資料如下
     * 112/05/01, 112/06/02 , 113/07/12, 113/08/30
     * 將整理成Map
     * key為年份 value為月份
     * key :112 value: 05 06
     * key :113 value: 07 08
     *
     * @param dataBaseMonth
     * @return
     */
    private static Map<String, List<String>> dateListToHashMap(List<String> dataBaseMonth) {
        Map<String, List<String>> mapOfMonth = new HashMap<>();
        for (String date : dataBaseMonth) {
            String[] datem = date.split("/");
            String year = datem[0];
            String month = datem[1];
            if (!mapOfMonth.containsKey(year)) {
                mapOfMonth.put(year, new ArrayList<>());
            }
            if (mapOfMonth.containsKey(year)) {
                //如果月份不包含在list中就塞進去
                if (!mapOfMonth.get(year).contains(month)) {
                    List<String> strings = mapOfMonth.get(year);
                    strings.add(month);
                    mapOfMonth.put(year, strings);
                }
            }
        }
        return mapOfMonth;
    }

    /**
     * 會回傳
     * 112 : 01 02
     * 109 : 03 04 05 06 07 08 09 10 11 12
     * <p>
     * 這樣結構的資料，就是去統計說這個區間，到底有哪些月份，算出來。
     * 原本想說要優化的，但我覺得這東西可讀性高一點應該比較好== 如果要弄成迴圈處理會太複雜
     *
     * @param startYear    從民國哪一年開始
     * @param startMonth   從哪一個月開始
     * @param yearInterval 往前推幾年
     * @return
     */
    public Map<String, List<String>> getYearMonthData(int startYear, int startMonth, int yearInterval) {
        //時間區間如果超過五年，則鎖在五年內
        if (yearInterval > 5) yearInterval = 5;
        LinkedList<String> list = new LinkedList<>(List.of("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"));
        HashMap<String, List<String>> answer = new HashMap<>();
        if (startMonth == 12) { //如果在12月，就回傳整年數據
            for (int i = 0; yearInterval > 0; i++) {
                int year = startYear - i;
                answer.put(String.valueOf(year), list.subList(0, list.size()));
                yearInterval--;
            }
            return answer;
        }
        if (yearInterval == 1) {
            answer.put(String.valueOf(startYear), list.subList(0, startMonth));
            answer.put(String.valueOf(startYear - 1), list.subList(startMonth, list.size()));
            return answer;
        }
        if (yearInterval == 2) {
            answer.put(String.valueOf(startYear), list.subList(0, startMonth));
            answer.put(String.valueOf(startYear - 1), list.subList(0, list.size()));
            answer.put(String.valueOf(startYear - 2), list.subList(startMonth, list.size()));
            return answer;
        }
        if (yearInterval == 3) {
            answer.put(String.valueOf(startYear), list.subList(0, startMonth));
            answer.put(String.valueOf(startYear - 1), list.subList(0, list.size()));
            answer.put(String.valueOf(startYear - 2), list.subList(0, list.size()));
            answer.put(String.valueOf(startYear - 3), list.subList(startMonth, list.size()));
            return answer;
        }
        if (yearInterval == 4) {
            answer.put(String.valueOf(startYear), list.subList(0, startMonth));
            answer.put(String.valueOf(startYear - 1), list.subList(0, list.size()));
            answer.put(String.valueOf(startYear - 2), list.subList(0, list.size()));
            answer.put(String.valueOf(startYear - 3), list.subList(0, list.size()));
            answer.put(String.valueOf(startYear - 4), list.subList(startMonth, list.size()));
            return answer;
        }
        if (yearInterval == 5) {
            answer.put(String.valueOf(startYear), list.subList(0, startMonth));
            answer.put(String.valueOf(startYear - 1), list.subList(0, list.size()));
            answer.put(String.valueOf(startYear - 2), list.subList(0, list.size()));
            answer.put(String.valueOf(startYear - 3), list.subList(0, list.size()));
            answer.put(String.valueOf(startYear - 4), list.subList(0, list.size()));
            answer.put(String.valueOf(startYear - 5), list.subList(startMonth, list.size()));
            return answer;
        }
        return answer;

    }
}
