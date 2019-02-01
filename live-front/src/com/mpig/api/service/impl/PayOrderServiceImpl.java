package com.mpig.api.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mpig.api.dao.IPayOrderDao;
import com.mpig.api.service.IPayOrderService;

@Service
public class PayOrderServiceImpl implements IPayOrderService{

	@Autowired
	private IPayOrderDao payOrderDao;
	@Override
	public int getAnchorSalaryCountForMonth(Integer uid, Integer ymtime) {
		return payOrderDao.getAnchorSalaryCountForMonth(uid, ymtime);
	}

	@Override
	public List<Object> getAnchorSalaryByUid(Integer uid) {
		return payOrderDao.getAnchorSalaryByUid(uid);
	}

	@Override
	public Map<String, Object> getUnionAnchorByAnchorid(Integer anchorId) {
		return payOrderDao.getUnionAnchorByAnchorid(anchorId);
	}

	@Override
	public int insPayAnchorSalary(Integer ymtime, Integer uid, Integer unionid,
			Integer credits) {
		return payOrderDao.insPayAnchorSalary(ymtime, uid, unionid, credits);
	}

	
}
