package com.tinypig.newadmin.web.dao;

import com.tinypig.newadmin.web.entity.WebVer;

public interface WebVerDao {
    int insert(WebVer record);

    int insertSelective(WebVer record);
}