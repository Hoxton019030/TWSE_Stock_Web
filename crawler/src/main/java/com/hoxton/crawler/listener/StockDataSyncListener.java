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
        String path ="crawler/src/main/resources";
        Path twseHtmlPath = Paths.get(path+"/twse.html");
        Path jsonDirectPath = Paths.get(path + "/json/");

        //先去確認是否有twse的資料
        if(twseHtmlPath.toFile().isFile()){
            log.info("twse.html檔案存在");
        }
        if(!twseHtmlPath.toFile().isFile()){
            log.info("檔案不存在，準備初始化");
            initializeService.downloadTWSEHTMLToLocal(path);
            log.info("下載全台股市代碼到專案中");
        }
        //將twse的資料拆解成json
        if(!jsonDirectPath.toFile().exists()){
            log.info("json path不存在，需要創建");
            jsonDirectPath.toFile().mkdir();
            log.info("json path創建完成");
        }
        if(jsonDirectPath.toFile().exists()){
            log.info("json path存在，進行資料分類");
            initializeService.parseTWSEHTML(twseHtmlPath.toString(),jsonDirectPath.toString());
            log.info("資料分類完成");
        }

    }
}
