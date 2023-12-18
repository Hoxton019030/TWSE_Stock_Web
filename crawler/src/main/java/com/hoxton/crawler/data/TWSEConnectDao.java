package com.hoxton.crawler.data;



import com.hoxton.crawler.entity.DailyStockData;
import com.hoxton.crawler.utils.CrawlerUtils;

import java.util.List;

public class TWSEConnectDao {

    /**
     * @param stockNo example:2330
     * @param date format:yyyyMM
     * @return
     */
    public  List<DailyStockData> getStockData(String stockNo, String date){
        return CrawlerUtils.getResponseList("https://www.twse.com.tw/rwd/zh/afterTrading/STOCK_DAY?date="+date+"&stockNo="+stockNo);
    }

}
