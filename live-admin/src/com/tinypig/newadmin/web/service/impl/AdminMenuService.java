package com.tinypig.newadmin.web.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tinypig.newadmin.web.dao.AdminMenuDao;
import com.tinypig.newadmin.web.entity.AdminMenu;
import com.tinypig.newadmin.web.entity.AdminMenuDto;
import com.tinypig.newadmin.web.service.IAdminMenuService;

@Service
@Transactional
public class AdminMenuService implements IAdminMenuService {

	@Autowired
	private AdminMenuDao adminMenuDao;

	@Override
	public AdminMenu findByUrl(String url) {
		return adminMenuDao.selectByUrl(url);
	}

	@Override
	public List<AdminMenuDto> getListById(Long mid) {
		if (null == mid) {
			mid = 0L;
		}
		return adminMenuDao.getListById(mid);
	}

	@Override
	public Map<String, Object> removeByMId(Integer id) {
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			adminMenuDao.deleteByPrimaryKey(id);
			map.put("code", 200);
			map.put("msg", "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("code", 201);
			map.put("msg", "删除失败");
		}
		return map;
	}

	@Override
	public Map<String, Object> saveMenu(AdminMenu adminMenu) {
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			if(null != adminMenu.getMid() && adminMenu.getMid() > 0) {
				adminMenuDao.updateByPrimaryKeySelective(adminMenu);
			}else {
				adminMenuDao.insertSelective(adminMenu);
			}
			map.put("code", 200);
			map.put("msg", "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("code", 201);
			map.put("msg", "保存失败");
		}
		return map;
	}

}
