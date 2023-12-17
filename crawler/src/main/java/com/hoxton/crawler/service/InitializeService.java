package com.hoxton.crawler.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Slf4j
public class InitializeService {
    public void downloadTWSEHTMLToLocal(String path) {
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
            try (FileWriter fileWriter = new FileWriter(path)) {
                fileWriter.write(line);
                log.info("內容已經存儲至:{}",path);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
