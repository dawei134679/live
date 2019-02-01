package com.tinypig.newadmin.web.dao;

import java.util.List;
import java.util.Map;

import com.tinypig.newadmin.web.entity.RealNameInfo;
import com.tinypig.newadmin.web.entity.RealNameParamDto;

public interface RealNameCheckDao {
    int deleteByPrimaryKey(Integer id);

    int insert(RealNameInfo record);

    int insertSelective(RealNameInfo record);

    RealNameInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RealNameInfo record);

    int updateByPrimaryKey(RealNameInfo record);
    
    List<Map<String, Object>> getRealNameList(RealNameParamDto param);
    
    Integer getRealNameTotal(RealNameParamDto param);
    
    List<Map<String, Object>> getAllRealNameList(RealNameParamDto param);
    
}