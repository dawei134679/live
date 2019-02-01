package com.tinypig.newadmin.web.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tinypig.newadmin.web.dao.SysVersionDao;
import com.tinypig.newadmin.web.entity.SysVersion;
import com.tinypig.newadmin.web.service.SysVersionService;
@Service
public class SysVersionServiceImpl implements SysVersionService {
	@Autowired
	private SysVersionDao sysVersionDao;

	@Override
	public int insertSelective(SysVersion record) {
		return sysVersionDao.insertSelective(record);
	}

	@Override
	public Map<String, Object> getVersionList() {
		Map<String, Object> map = new HashMap<String, Object>();
		List<SysVersion> list = sysVersionDao.getVersionList();
		map.put("rows", list);
		map.put("total", list.size());
		return map;
	}
	
	
	
}
