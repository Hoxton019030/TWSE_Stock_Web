package com.hoxton.crawler.listener;

import com.hoxton.crawler.service.DailyStockDataService;
import com.hoxton.crawler.service.InitializeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

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
    private final InitializeService initializeService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        String path ="crawler/src/main/resources/twse.html";
        Path paths = Paths.get(path);

        if(paths.toFile().isFile()){
            log.info("twse.html檔案存在");
        }
        if(!paths.toFile().isFile()){
            log.info("檔案不存在，準備初始化");
            initializeService.downloadTWSEHTMLToLocal(path);
        }

    }
}
