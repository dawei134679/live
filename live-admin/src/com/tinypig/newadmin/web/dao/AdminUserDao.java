package com.tinypig.newadmin.web.dao;

import com.tinypig.admin.model.AdminUser;

public interface AdminUserDao {
	int insertSelective(AdminUser adminUser);
}