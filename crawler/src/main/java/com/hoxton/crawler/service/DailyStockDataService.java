package com.hoxton.crawler.service;

import com.hoxton.crawler.dao.DailyStockDataMapper;
import com.hoxton.crawler.entity.DailyStockData;
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
        Map<Integer, List<String>> dateInDataBase = dateListToHashMap(dataBaseMonth);
        //取得這段期間應該有的股票日期
        Map<Integer, List<String>> dateThatShouldBe = getYearMonthData(startYear, monthValue, yearInterval);
        //取差集，就是需要補充的部分
        List<String> intersection = intersection(dateThatShouldBe, dateInDataBase);
        log.info("股票代碼:{},所缺少的月份為{}", stockCode, intersection);
        //缺少的部分
        return intersection;
    }

    /**
     * 取出叉集
     *
     * @param dateThatShouldBe
     * @param dateInDataBase
     * @return
     */
    private List<String> intersection(Map<Integer, List<String>> dateThatShouldBe, Map<Integer, List<String>> dateInDataBase) {
        List<String> major = new ArrayList<>();
        List<String> sub = new ArrayList<>();
        List<String> answer = new ArrayList<>();
        dateThatShouldBe.forEach((s, strings) ->
                strings.forEach(s1 -> major.add(s + BC_TO_ROC_DIFF + s1 + "01"))
        );
        dateInDataBase.forEach((s, strings) ->
                strings.forEach(s1 -> sub.add(s + BC_TO_ROC_DIFF + s1 + "01"))
        );

        for (String s : major) {
            if (!sub.contains(s)) {
                answer.add(s);
            }
        }
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
    private static Map<Integer, List<String>> dateListToHashMap(List<String> dataBaseMonth) {
        Map<Integer, List<String>> mapOfMonth = new HashMap<>();
        for (String date : dataBaseMonth) {
            String[] datem = date.split("/");
            Integer year = Integer.valueOf(datem[0]);
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
    public Map<Integer, List<String>> getYearMonthData(int startYear, int startMonth, int yearInterval) {
        //時間區間如果超過五年，則鎖在五年內
        if (yearInterval > 5) yearInterval = 5;
        LinkedList<String> list = new LinkedList<>(List.of("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"));
        HashMap<Integer, List<String>> answer = new HashMap<>();
        if (startMonth == 12) { //如果在12月，就回傳整年數據
            for (int i = 0; yearInterval > 0; i++) {
                int year = startYear - i;
                answer.put(year, list.subList(0, list.size()));
                yearInterval--;
            }
            return answer;
        }
        if (yearInterval == 1) {
            answer.put(startYear, list.subList(0, startMonth));
            answer.put(startYear - 1, list.subList(startMonth, list.size()));
            return answer;
        }
        if (yearInterval == 2) {
            answer.put(startYear, list.subList(0, startMonth));
            answer.put(startYear - 1, list.subList(0, list.size()));
            answer.put(startYear - 2, list.subList(startMonth, list.size()));
            return answer;
        }
        if (yearInterval == 3) {
            answer.put(startYear, list.subList(0, startMonth));
            answer.put(startYear - 1, list.subList(0, list.size()));
            answer.put(startYear - 2, list.subList(0, list.size()));
            answer.put(startYear - 3, list.subList(startMonth, list.size()));
            return answer;
        }
        if (yearInterval == 4) {
            answer.put(startYear, list.subList(0, startMonth));
            answer.put(startYear - 1, list.subList(0, list.size()));
            answer.put(startYear - 2, list.subList(0, list.size()));
            answer.put(startYear - 3, list.subList(0, list.size()));
            answer.put(startYear - 4, list.subList(startMonth, list.size()));
            return answer;
        }
        if (yearInterval == 5) {
            answer.put(startYear, list.subList(0, startMonth));
            answer.put(startYear - 1, list.subList(0, list.size()));
            answer.put(startYear - 2, list.subList(0, list.size()));
            answer.put(startYear - 3, list.subList(0, list.size()));
            answer.put(startYear - 4, list.subList(0, list.size()));
            answer.put(startYear - 5, list.subList(startMonth, list.size()));
            return answer;
        }
        return answer;
    }

    public void addAll(List<DailyStockData> listData) {
        for (DailyStockData listDatum : listData) {
            dailyStockDataDao.insert(listDatum);
        }
    }

    public List<String> getAllStockCode() {
        return null;
    }

    public List<String> getAllETFCode() {
        return null;
    }
}
