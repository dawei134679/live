package com.tinypig.newadmin.web.dao;

import java.util.List;

import com.tinypig.newadmin.web.entity.GiftLuckyProbabilitys;

public interface GiftLuckyProbabilitysDao {
    int deleteByPrimaryKey(Integer id);

    int insert(GiftLuckyProbabilitys record);

    int insertSelective(GiftLuckyProbabilitys record);

    GiftLuckyProbabilitys selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GiftLuckyProbabilitys record);

    int updateByPrimaryKey(GiftLuckyProbabilitys record);
    
    List<GiftLuckyProbabilitys> selectList();
    
    int selectCount();
    
    List<GiftLuckyProbabilitys> checkMultiples(Integer multiples);
}