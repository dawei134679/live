package com.tinypig.newadmin.web.dao;

import com.tinypig.newadmin.web.entity.SysCopyright;

public interface SysCopyrightDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SysCopyright record);

    int insertSelective(SysCopyright record);

    SysCopyright selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysCopyright record);

    int updateByPrimaryKey(SysCopyright record);

	SysCopyright getSysCopyright();
}