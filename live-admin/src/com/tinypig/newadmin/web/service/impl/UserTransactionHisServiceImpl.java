package com.tinypig.newadmin.web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tinypig.newadmin.web.dao.UserTransactionHisMapper;
import com.tinypig.newadmin.web.entity.UserTransactionHis;
import com.tinypig.newadmin.web.service.UserTransactionHisService;

@Service
@Transactional
public class UserTransactionHisServiceImpl implements UserTransactionHisService {

	@Autowired
	private UserTransactionHisMapper userTransactionHisMapper;
	
	@Override
	public int saveUserTransactionHis(UserTransactionHis userTransactionHis) {
		return userTransactionHisMapper.insertSelective(userTransactionHis);
	}

	@Override
	public int delUserTransactionHisByRefId(String refId) {
		return userTransactionHisMapper.delUserTransactionHisByRefId(refId);
	}

}
