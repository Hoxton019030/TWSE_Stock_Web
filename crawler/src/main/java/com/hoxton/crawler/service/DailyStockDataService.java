package com.hoxton.crawler.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DailyStockDataService {

    private final TestMapper testD;
    public void print(){
        System.out.println("Hello Fuck");
    }
}
