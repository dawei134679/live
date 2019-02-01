package com.mpig.api.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mpig.api.dao.ISalesmanDao;
import com.mpig.api.model.SalesmanModel;
import com.mpig.api.service.ISalesmanService;

@Service
public class SalesmanServiceImpl implements ISalesmanService {
	@Resource
	private ISalesmanDao salesmanDao;

	@Override
	public SalesmanModel getSalesmanById(long id) {
		return salesmanDao.getSalesmanById(id);
	}

}
