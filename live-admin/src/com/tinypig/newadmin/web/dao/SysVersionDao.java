package com.tinypig.newadmin.web.dao;

import java.util.List;

import com.tinypig.newadmin.web.entity.SysVersion;

public interface SysVersionDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SysVersion record);

    int insertSelective(SysVersion record);

    SysVersion selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysVersion record);

    int updateByPrimaryKey(SysVersion record);
    
    List<SysVersion> getVersionList();
}