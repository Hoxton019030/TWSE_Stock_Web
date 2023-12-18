package com.hoxton.crawler.dao;

import com.hoxton.crawler.entity.DailyStockData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;

@Repository
@Mapper
public interface DailyStockDataDao
        extends BaseMapper<DailyStockData> {

    List<String> findMissingMonth(@Param("stockCode") String stockCode);

    List<DailyStockData> all();


}
