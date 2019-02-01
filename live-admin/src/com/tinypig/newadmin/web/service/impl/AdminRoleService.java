package com.tinypig.newadmin.web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tinypig.newadmin.web.dao.AdminRoleDao;
import com.tinypig.newadmin.web.entity.AdminRole;
import com.tinypig.newadmin.web.service.IAdminRoleService;

@Service
@Transactional
public class AdminRoleService implements IAdminRoleService {

	@Autowired
	private AdminRoleDao adminRoleDao;

	@Override
	public AdminRole findById(Byte id) {
		return adminRoleDao.selectByPrimaryKey(id);
	}

}
