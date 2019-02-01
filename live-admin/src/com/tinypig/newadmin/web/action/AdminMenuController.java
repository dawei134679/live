package com.tinypig.newadmin.web.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tinypig.newadmin.web.entity.AdminMenu;
import com.tinypig.newadmin.web.entity.AdminMenuDto;
import com.tinypig.newadmin.web.service.IAdminMenuService;

@Controller
@RequestMapping("/system/adminMenu")
public class AdminMenuController {

	@Autowired
	private IAdminMenuService adminMenuService;

	@ResponseBody
	@RequestMapping(value = "/listByMid")
	public List<AdminMenuDto> listByMid(HttpServletRequest req, Long id) {
		List<AdminMenuDto> list = adminMenuService.getListById(id);
		return list;
	}

	@ResponseBody
	@RequestMapping(value = "/saveMenu")
	public Map<String, Object> updateById(HttpServletRequest req, AdminMenu adminMenu) {
		return adminMenuService.saveMenu(adminMenu);
	}
	
	@ResponseBody
	@RequestMapping(value = "/removeById")
	public Map<String, Object> removeById(HttpServletRequest req, Integer id) {
		return adminMenuService.removeByMId(id);
	}
}
