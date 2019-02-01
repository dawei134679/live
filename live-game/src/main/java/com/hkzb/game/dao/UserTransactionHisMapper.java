package com.hkzb.game.dao;

import com.hkzb.game.model.UserTransactionHis;

public interface UserTransactionHisMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserTransactionHis record);

    int insertSelective(UserTransactionHis record);

    UserTransactionHis selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserTransactionHis record);

    int updateByPrimaryKey(UserTransactionHis record);
}