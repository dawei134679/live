package com.tinypig.newadmin.web.dao;

import java.util.List;
import java.util.Map;

import com.tinypig.newadmin.web.entity.BillcvgInfo;
import com.tinypig.newadmin.web.vo.BillcvgParamVo;

public interface BillcvgInfoDao {
    int deleteByPrimaryKey(Integer id);

    int insert(BillcvgInfo record);

    int insertSelective(BillcvgInfo record);

    BillcvgInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BillcvgInfo record);

    int updateByPrimaryKey(BillcvgInfo record);
    
    List<BillcvgInfo> getcvgStaList(BillcvgParamVo param);
    
    Map<String, Object> getgetcvgStaCount(BillcvgParamVo param);

	List<Map<String, Object>> getAllCvgStaList(BillcvgParamVo param);
}