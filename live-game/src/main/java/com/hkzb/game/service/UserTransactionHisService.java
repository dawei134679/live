package com.hkzb.game.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hkzb.game.dao.UserTransactionHisMapper;
import com.hkzb.game.model.UserTransactionHis;

@Service
@Transactional
public class UserTransactionHisService implements IUserTransactionHisService {

	@Autowired
	private UserTransactionHisMapper userTransactionHisMapper;
	
	@Override
	public int saveUserTransactionHis(UserTransactionHis userTransactionHis) {
		return userTransactionHisMapper.insertSelective(userTransactionHis);
	}
}
