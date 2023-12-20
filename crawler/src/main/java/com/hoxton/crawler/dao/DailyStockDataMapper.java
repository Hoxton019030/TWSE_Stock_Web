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
    /**
     * 取得這個股票有哪些日期的資料
     * 回傳 112/11/02,112/11/03之類的資料
     * @param stockCode
     * @return
     */
    List<String> findMonthList(@Param("stockCode") String stockCode);

    List<DailyStockData> all();


}
