package com.hoxton.crawler.service;

import com.alibaba.fastjson.JSON;
import com.hoxton.crawler.entity.StockInformation;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class InitializeService {
    /**
     * 取得全台灣所有股票代碼
     * @param twsePath
     */
    public void downloadTWSEHTMLToLocal(String twsePath) {
        URL url;
        String line = "";
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

            // Save the content to a file (e.g., data.json) in the project directory
            try (FileWriter fileWriter = new FileWriter(twsePath)) {
                fileWriter.write(line);
                log.info("內容已經存儲至:{}",twsePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parseTWSEHTML(String twsePath,String jsonPath) {
        String line = "";
        try {
            // 读取文件内容为字节数组
            byte[] fileBytes = Files.readAllBytes(Paths.get(twsePath));
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
                categoryMap.get(category).add(stockInformation);
            }
        }

        categoryMap.forEach((s, stockInformation) -> {
            String fileName = s + ".json";
            File file = new File( jsonPath+File.separator+fileName);

            try {
                // 檢查檔案是否存在，如果不存在則創建新檔案
                if (!file.exists()) {
                    file.createNewFile();
                }

                try (FileWriter fileWriter = new FileWriter(file)) {
                    String jsonString = JSON.toJSONString(stockInformation);
                    fileWriter.write(jsonString);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });;
    }
}
