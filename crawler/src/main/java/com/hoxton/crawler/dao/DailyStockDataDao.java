package com.hoxton.crawler.dao;

import com.hoxton.crawler.entity.DailyStockData;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DailyStockDataDao{

     void insertData();

    List<DailyStockData> getMonthlyStockData();
}
