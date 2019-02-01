package com.tinypig.newadmin.web.service;

import java.util.List;
import java.util.Map;

import com.tinypig.newadmin.web.entity.AdminMenu;
import com.tinypig.newadmin.web.entity.AdminMenuDto;

public interface IAdminMenuService {

	AdminMenu findByUrl(String action);

	List<AdminMenuDto> getListById(Long mid);

	Map<String, Object> removeByMId(Integer id);

	Map<String, Object> saveMenu(AdminMenu adminMenu);

}
