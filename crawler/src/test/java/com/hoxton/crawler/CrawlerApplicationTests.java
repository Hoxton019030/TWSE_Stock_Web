package com.hoxton.crawler;

import com.alibaba.fastjson.JSON;
import com.hoxton.crawler.data.TWSEDataDao;
import com.hoxton.crawler.entity.DailyStockData;
import com.hoxton.crawler.entity.StockInformation;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootTest
@Slf4j
class CrawlerApplicationTests {

    @Test
    void downloadTWSEHTMLToLocal() {
        URL url;
        String line = "";
        String path = "src/main/resources/twse.html";
        try {
            url = new URL("https://isin.twse.com.tw/isin/C_public.jsp?strMode=2");
            InputStream in = url.openStream();

            // Specify the character set (UTF-8 in this case)
            InputStreamReader inr = new InputStreamReader(in, "BIG5");

            BufferedReader br = new BufferedReader(inr);
            StringBuilder sr = new StringBuilder();

            while ((line = br.readLine()) != null) {
                sr.append(line);
            }

            line = sr.toString();
            System.out.println("line.isEmpty() = " + line.isEmpty());

            // Save the content to a file (e.g., data.json) in the project directory
            try (FileWriter fileWriter = new FileWriter(path)) {
                fileWriter.write(line);
                System.out.println("Content has been saved to data.json");
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Test
    void requestTest() {
        String date = "2023101";
        String stockNo = "2330";
        String dateFormat = "yyyyMMdd";
        List<DailyStockData> dailyStockDatalist = new ArrayList<>();

// Create a SimpleDateFormat object for parsing and formatting dates
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        for (int i = 1; i <= 12; i++) {
            // Construct the date string for the current month and day
            String dayOfMonth = String.format("%02d", i);
            String dateString = date.substring(0, 4) + dayOfMonth + date.substring(6);

            try {
                // Parse the date string to a Date object
                Date currentDate = sdf.parse(dateString);

                // Create a Calendar object and set it to the parsed date
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(currentDate);

                // Get the formatted date string
                String dateFormatted = sdf.format(calendar.getTime());

                TWSEDataDao twseDataDao = new TWSEDataDao();
                List<DailyStockData> stockData = twseDataDao.getStockData(stockNo, dateFormatted);

                for (DailyStockData stockDatum : stockData) {
                    String date1 = stockDatum.getDate();
                    double closingPrice = stockDatum.getClosingPrice();
                    log.info("Hoxton log測試date:{},stockPrice:{}", date1,closingPrice);
                    dailyStockDatalist.add(stockDatum);
                }
            } catch (Exception e) {
                e.printStackTrace();
                // Handle parsing errors if necessary
            }
        }


        Optional<DailyStockData> max = dailyStockDatalist.stream().max(Comparator.comparingDouble(DailyStockData::getPriceDifference));
        DailyStockData dailyStockData = max.get();
        log.info("Hoxton log測試dailyStockData:{}", dailyStockData);

    }



}
