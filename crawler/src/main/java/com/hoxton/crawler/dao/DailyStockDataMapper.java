package com.hoxton.crawler.dao;

import com.hoxton.crawler.entity.DailyStockData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;

@Repository
@Mapper
public interface DailyStockDataMapper
        extends BaseMapper<DailyStockData> {
//@Select(value = "select date from daily_stock_data")
    List<String> findMissingMonth(@Param("stockCode") String stockCode);

    List<DailyStockData> all();


}
