package com.tinypig.newadmin.web.dao;

import com.tinypig.newadmin.web.entity.AdminRole;

public interface AdminRoleDao {
    int deleteByPrimaryKey(Byte roleId);

    int insert(AdminRole record);

    int insertSelective(AdminRole record);

    AdminRole selectByPrimaryKey(Byte roleId);

    int updateByPrimaryKeySelective(AdminRole record);

    int updateByPrimaryKey(AdminRole record);
}