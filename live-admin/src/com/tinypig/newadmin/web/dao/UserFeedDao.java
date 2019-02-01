package com.tinypig.newadmin.web.dao;

import com.tinypig.newadmin.web.entity.UserFeed;

public interface UserFeedDao {
    int deleteByPrimaryKey(Integer id);

    int insert(UserFeed record);

    int insertSelective(UserFeed record);

    UserFeed selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserFeed record);

    int updateByPrimaryKey(UserFeed record);
}