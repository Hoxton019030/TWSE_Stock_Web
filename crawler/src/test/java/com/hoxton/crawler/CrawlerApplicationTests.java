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
    void parseTWSEHTML() {
        String path = "src/main/resources/twse.html";
        String line = "";
        try {
            // 读取文件内容为字节数组
            byte[] fileBytes = Files.readAllBytes(Paths.get(path));
            // 将字节数组转换为字符串
            line = new String(fileBytes);

            // 打印文件内容
        } catch (IOException e) {
            e.printStackTrace();
        }



        Document document = Jsoup.parse(line);
        Element body = document.body();

        // 找到所有的tr元素
        Elements trElements = body.select("tr");

        HashMap<String, List<StockInformation>> categoryMap = new HashMap<>();
        ArrayList<String> objects = new ArrayList<>();
        String category = "";
        for (int i = 1; i < trElements.size(); i++) {
            Element element = trElements.get(i);
            Element bElement = element.selectFirst("B");
            if (bElement != null) {
                category = bElement.text();
                categoryMap.put(category, new ArrayList<>());
//                System.out.println(bElement.text());
                categoryMap.put(bElement.text(), new ArrayList<>());
            } else {
                StockInformation stockInformation = new StockInformation();
                String stockCodeAndCompanyName = element.selectFirst("td").text();
                String[] stockCodeAndCompanyNameArray = stockCodeAndCompanyName.split("　");
//                System.out.println(stockCodeAndCompanyName);
                String marketDate = element.select("td:nth-child(3)").text();
                String market = element.select("td:nth-child(4)").text();
                String industry = element.select("td:nth-child(5)").text();
                stockInformation.setStockCategory(category);
                stockInformation.setStockCode(stockCodeAndCompanyNameArray[0]);
                stockInformation.setStockName(stockCodeAndCompanyNameArray[1]);
                stockInformation.setStockDate(marketDate);
                stockInformation.setStockMarket(market);
                stockInformation.setStockIndustry(industry);
                log.info("Hoxton log測試stockInformation:{}", stockInformation.getStockName());
//                System.out.println(stockInformation);

//                System.out.println(industry.first().text());
                categoryMap.get(category).add(stockInformation);
            }
        }

        categoryMap.forEach((s, stockInformation) -> {
            String filePath = "src/main/resources/json/";
            String fileName = s + ".json";
            File file = new File(filePath + fileName);

            try {
                // 檢查檔案是否存在，如果不存在則創建新檔案
                if (!file.exists()) {
                    file.createNewFile();
                }

                try (FileWriter fileWriter = new FileWriter(file)) {
                    String jsonString = JSON.toJSONString(stockInformation);
                    fileWriter.write(jsonString);
                    // System.out.println("Content has been saved to " + fileName);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });;
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
