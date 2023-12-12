package com.hoxton.crawler.data;

import com.hoxton.crawler.entity.DailyStockData;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class TWSEDataDao extends TWSEConnectDao {

    public List<DailyStockData> getMonthlyStockData(String stockNo, String date){
        List<DailyStockData> stockData = getStockData(stockNo, date);
        return null;
    }
}
