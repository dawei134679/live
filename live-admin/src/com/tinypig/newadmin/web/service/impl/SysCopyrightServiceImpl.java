package com.tinypig.newadmin.web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tinypig.newadmin.web.dao.SysCopyrightDao;
import com.tinypig.newadmin.web.entity.SysCopyright;
import com.tinypig.newadmin.web.service.ISysCopyrightService;

@Service
@Transactional
public class SysCopyrightServiceImpl implements ISysCopyrightService {

	@Autowired
	private SysCopyrightDao sysCopyrightDao;
	
	@Override
	public int saveSysCopyright(SysCopyright sysCopyright) {
		return sysCopyrightDao.insertSelective(sysCopyright);
	}

	@Override
	public int updateSysCopyright(SysCopyright sysCopyright) {
		return sysCopyrightDao.updateByPrimaryKeySelective(sysCopyright);
	}

	@Override
	public SysCopyright getSysCopyright() {
		return sysCopyrightDao.getSysCopyright();
	}
}
