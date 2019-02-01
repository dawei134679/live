package com.tinypig.newadmin.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tinypig.newadmin.web.entity.IosVersion;

public interface IosVersionDao {
    int deleteByPrimaryKey(Long id);

    int insert(IosVersion record);

    int insertSelective(IosVersion record);

    IosVersion selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(IosVersion record);

    int updateByPrimaryKey(IosVersion record);

	List<IosVersion> getIosVersionList(@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

	List<IosVersion> getAllIosVersion();
}