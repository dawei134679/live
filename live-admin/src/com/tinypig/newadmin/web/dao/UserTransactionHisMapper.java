package com.tinypig.newadmin.web.dao;

import com.tinypig.newadmin.web.entity.UserTransactionHis;

public interface UserTransactionHisMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserTransactionHis record);

    int insertSelective(UserTransactionHis record);

    UserTransactionHis selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserTransactionHis record);

    int updateByPrimaryKey(UserTransactionHis record);

	int delUserTransactionHisByRefId(String refId);
}