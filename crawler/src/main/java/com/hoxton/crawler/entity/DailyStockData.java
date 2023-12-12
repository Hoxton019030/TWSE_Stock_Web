package com.hoxton.crawler.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Entity
public class DailyStockData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    /**
     * 交易日期。
     */
    private String date;

    /**
     * 成交股數。
     */
    private int volume;

    /**
     * 成交金額。
     */
    private double amount;

    /**
     * 開盤價。
     */
    private double openingPrice;

    /**
     * 最高價。
     */
    private double highestPrice;

    /**
     * 最低價。
     */
    private double lowestPrice;

    /**
     * 收盤價。
     */
    private double closingPrice;

    /**
     * 漲跌價差。
     */
    private double priceDifference;

    /**
     * 成交筆數。
     */
    private int numberOfTransactions;
}
