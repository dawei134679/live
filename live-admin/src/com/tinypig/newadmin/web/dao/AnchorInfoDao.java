package com.tinypig.newadmin.web.dao;

import java.util.List;
import java.util.Map;

import com.tinypig.newadmin.web.entity.AnchorInfoParamDto;

public interface AnchorInfoDao {
    List<Map<String, Object>> getAnchorList(AnchorInfoParamDto param);
    
    Integer getAnchorTotal(AnchorInfoParamDto param);
    
    List<Map<String,Object>> getAnchorAllList(AnchorInfoParamDto param);
}