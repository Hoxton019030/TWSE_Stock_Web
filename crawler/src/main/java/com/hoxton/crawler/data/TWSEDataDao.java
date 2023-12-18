package com.hoxton.crawler.data;

import com.hoxton.crawler.entity.DailyStockData;
import com.hoxton.crawler.utils.DateUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
@Slf4j
public class TWSEDataDao extends TWSEConnectDao {

    public List<DailyStockData> getMonthlyStockData(String stockNo, String date){
        return getStockData(stockNo, date);
    }
    public List<DailyStockData> getIntervalStockData(String stockNo, String startDate,String endDate){
        List<String> monthsInRange = DateUtils.getMonthsInRange(startDate, endDate);
        List<DailyStockData> dailyStockData = new ArrayList<>();
        for (String month : monthsInRange) {
            List<DailyStockData> stockData = getStockData(stockNo, month);
            log.info("獲取{}股票代號{}之資料",month,stockNo);
            dailyStockData.addAll(stockData);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return dailyStockData;
    }
}
