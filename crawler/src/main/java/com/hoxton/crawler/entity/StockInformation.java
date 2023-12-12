package com.hoxton.crawler.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Data
@ToString
@Component
public class StockInformation {

    /**
     * 股票類型(普通股、特別股、ETF)
     */
    private String stockCategory;
    /**
     * 股票編號
     */
    private String stockCode;
    /**
     * 股票名稱
     */
    private String stockName;

    /**
     * 上市日
     */
    private String stockDate;

    /**
     * 股票市場別（上市、上櫃）
     */
    private String stockMarket;

    /**
     * 公司行業別(水泥業、塑膠業等等...）
     */
    private String stockIndustry;


}
