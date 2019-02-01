package com.tinypig.newadmin.web.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tinypig.newadmin.web.dao.VerifyCodeDao;
import com.tinypig.newadmin.web.entity.VerifyCodeParamDto;
import com.tinypig.newadmin.web.service.IVerifyCodeService;

@Service
@Transactional
public class VerifyCodeService implements IVerifyCodeService {

	@Autowired
	private VerifyCodeDao verifyCodeDao;

	@Override
	public Map<String, Object> getVerifyCodeList(VerifyCodeParamDto params) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("rows", verifyCodeDao.getVerifyCodeListPage(params));
		result.put("total", verifyCodeDao.getVerifyCodeTotal(params));
		return result;
	}

}
