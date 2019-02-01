package com.tinypig.newadmin.web.dao;

import java.util.List;

import com.tinypig.newadmin.web.entity.SystemNotice;

public interface SystemNoticeDao {
    int deleteByPrimaryKey(Byte id);

    int insert(SystemNotice record);

    int insertSelective(SystemNotice record);

    int updateByPrimaryKeySelective(SystemNotice record);

    int updateByPrimaryKey(SystemNotice record);
    
    List<SystemNotice> selectList();
    
    Integer selectCount();
    
    SystemNotice selectByPrimaryKey(Byte id);
}