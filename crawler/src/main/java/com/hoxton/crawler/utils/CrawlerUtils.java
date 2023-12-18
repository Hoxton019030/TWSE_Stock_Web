package com.hoxton.crawler.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hoxton.crawler.entity.DailyStockData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * 參考網址
 * https://blog.csdn.net/weixin_35688430/article/details/119750922
 */
@Slf4j
public class CrawlerUtils {

    /**
     * 股票代碼在陣列中的位置
     */
    private static final int CODE_POSITION = 1;
    static WebClient webClient = WebClient.create();

    public static<T> T getResponse(String url,Class<T> T){
        Mono<T> stringMono = webClient.get().uri(url).retrieve().bodyToMono(T);
        return stringMono.block();
    }

    public  static  List<DailyStockData> getResponseList(String url){
//        log.info("Hoxton log測試url:{}", url);
        Mono<String> stringMono = webClient.get().uri(url).retrieve().bodyToMono(String.class);
        String block = stringMono.block();
        JSONObject jsonObject = JSONObject.parseObject(block);
        return parseToDailyStockData(jsonObject);

    }

    private  static List<DailyStockData> parseToDailyStockData(JSONObject jsonObject) {
        String titleDescript = jsonObject.getString("title");
        log.info("Hoxton log測試titleDescript:{}", titleDescript);
        String stockCode = titleDescript.split(" ")[CODE_POSITION];
        log.info("Hoxton log測試stockCode:{}", stockCode);

        JSONArray data = jsonObject.getJSONArray("data");
        List<DailyStockData> dailyStockDatalist = new ArrayList<>();
        for (Object datum : data) {
            DailyStockData dailyStockData = new DailyStockData();
            JSONArray dataArray = JSONArray.parseArray(datum.toString());
            String date = dataArray.getString(0).trim();
            int volume = formatInt(dataArray.getString(1));
            //幫我完成剩下的部分
            dailyStockData.setStockCode(stockCode);
            dailyStockData.setDate(date);
            dailyStockData.setVolume(volume);
            dailyStockData.setAmount(formatDouble(dataArray.getString(2)));
            dailyStockData.setOpeningPrice(formatDouble(dataArray.getString(3)));
            dailyStockData.setHighestPrice(formatDouble(dataArray.getString(4)));
            dailyStockData.setLowestPrice(formatDouble(dataArray.getString(5)));
            dailyStockData.setClosingPrice(formatDouble(dataArray.getString(6)));
            dailyStockData.setPriceDifference(formatDouble(dataArray.getString(7)));
            dailyStockData.setNumberOfTransactions(formatInt(dataArray.getString(8)));
            dailyStockDatalist.add(dailyStockData);
        }
        return dailyStockDatalist;
    }

    private static int formatInt(String string) {
        return Integer.parseInt(string.replaceAll(",", ""));
    }

    private static double formatDouble(String string) {
        return Double.parseDouble(string.replaceAll(",", "").replaceAll("X",""));
    }

}
