package com.hoxton.crawler.listener;

import com.hoxton.crawler.service.DailyStockDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 這個listener類用來同步股市資料，當SpringBoot啟動時會去Scan股市資料，同步到資料庫中
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StockDataSyncListener implements ApplicationListener<ApplicationReadyEvent> {

    //    private final Environment environment;
//    private final DailyStockDataDao dailyStockDataDao;
//    private final TestDao monthlyStockData;
    private final DailyStockDataService dailyStockDataService;
//    private final TestD testDao;
    private final ApplicationContext applicationContext;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println("執行");
//        DailyStockDataDao dailyStockDataDao = applicationContext.getBean("DailyStockDataDao", DailyStockDataDao.class);
        log.info("Hoxton log測試event:{}", event);
        log.info("Hoxton測試文字{}", "我進來啦");
        dailyStockDataService.print();
//        log.info("Hoxton log測試stockInformation.getStockName():{}", dailyStockDataService.print());
//        String property = environment.getProperty("spring.twse.year-interval");
//        System.out.println(property);
//        List<DailyStockData> monthlyStockData = dailyStockDataDao.getMonthlyStockData();
//        log.info("Hoxton log測試monthlyStockData:{}", monthlyStockData);

    }
}
