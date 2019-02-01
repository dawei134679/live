package com.tinypig.newadmin.web.dao;

import com.tinypig.newadmin.web.entity.WebFeedback;

public interface WebFeedbackDao {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(WebFeedback record);

    WebFeedback selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WebFeedback record);
}