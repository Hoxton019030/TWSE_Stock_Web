package com.hoxton.crawler.data;



import com.hoxton.crawler.entity.DailyStockData;
import com.hoxton.crawler.utils.CrawlerUtils;

import java.util.List;

public class TWSEConnectDao {

    public  List<DailyStockData> getStockData(String stockNo, String date){

        return CrawlerUtils.getResponseList("https://www.twse.com.tw/rwd/zh/afterTrading/STOCK_DAY?date="+date+"&stockNo="+stockNo);
    }

}
