package com.tinypig.newadmin.web.dao;

import java.util.List;

import com.tinypig.newadmin.web.entity.AdminMenu;
import com.tinypig.newadmin.web.entity.AdminMenuDto;

public interface AdminMenuDao {
	int deleteByPrimaryKey(Integer mid);

	int insert(AdminMenu record);

	int insertSelective(AdminMenu record);

	AdminMenu selectByPrimaryKey(Integer mid);

	int updateByPrimaryKeySelective(AdminMenu record);

	int updateByPrimaryKey(AdminMenu record);

	AdminMenu selectByUrl(String url);

	List<AdminMenuDto> getListById(Long mid);
}