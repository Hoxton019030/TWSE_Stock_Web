package com.hoxton.crawler.service;

import com.hoxton.crawler.dao.DailyStockDataDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyStockDataService {
    private  final DailyStockDataDao dailyStockDataDao;

    private final TestMapper testD;
    public void print(){
        System.out.println("Hello Fuck");
    }

    public List<String> findMissingMonth(String stockCode){
        List<String> missingMonth = dailyStockDataDao.findMissingMonth(stockCode);

        List<String> result = missingMonth;
        return result;
    }
}
