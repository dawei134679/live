package com.tinypig.newadmin.web.dao;

import com.tinypig.newadmin.web.entity.MobileGuideImg;

public interface MobileGuideImgDao {
    int deleteByPrimaryKey(Short id);

    int insertSelective(MobileGuideImg record);

    MobileGuideImg selectByPrimaryKey(Short id);

    int updateByPrimaryKeySelective(MobileGuideImg record);

}