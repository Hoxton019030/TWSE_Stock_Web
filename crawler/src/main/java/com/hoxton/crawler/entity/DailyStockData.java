package com.hoxton.crawler.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

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
    @Column(name="date")
    private String date;
    @Column(name = "stock_Code")
    private String stockCode;

    /**
     * 成交股數。
     */
    @Column(name="volume")
    private Integer volume;

    /**
     * 成交金額。
     */
    @Column(name="amount")
    private Double amount;

    /**
     * 開盤價。
     */
    @Column(name="opening_price")
    private Double openingPrice;

    /**
     * 最高價。
     */
    @Column(name ="highest_price")
    private Double highestPrice;

    /**
     * 最低價。
     */
    @Column(name = "lowest_price")
    private Double lowestPrice;

    /**
     * 收盤價。
     */
    @Column(name = "closing_price")
    private Double closingPrice;

    /**
     * 漲跌價差。
     */
    @Column(name = "price_difference")
    private Double priceDifference;

    /**
     * 成交筆數。
     */
    @Column(name ="number_of_transactions")
    private Integer numberOfTransactions;
}
