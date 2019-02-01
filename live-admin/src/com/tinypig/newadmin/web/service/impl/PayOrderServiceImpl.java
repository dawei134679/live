package com.tinypig.newadmin.web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.tinypig.newadmin.web.dao.PayOrderMapper;
import com.tinypig.newadmin.web.entity.PayOrder;
import com.tinypig.newadmin.web.entity.UserTransactionHis;
import com.tinypig.newadmin.web.service.PayOrderService;
import com.tinypig.newadmin.web.service.UserTransactionHisService;

@Service
@Transactional
public class PayOrderServiceImpl implements PayOrderService {

	@Autowired
	private PayOrderMapper payOrderMapper;
	
	@Autowired
	private UserTransactionHisService userTransactionHisService;
	
	@Override
	public int savePayOrder(PayOrder payOrder) {
		int i = payOrderMapper.insertSelective(payOrder);
		if(i==1) {
			UserTransactionHis userTransactionHis = new UserTransactionHis();
			userTransactionHis.setUid(payOrder.getSrcuid());
			userTransactionHis.setTransType(12);
			userTransactionHis.setDataType(2);
			userTransactionHis.setAmount(payOrder.getAmount());
			userTransactionHis.setRefId(payOrder.getOrderId());
			System.out.println(payOrder.getPaytime()+","+payOrder.getPaytime()*1000);
			Long time = Long.valueOf(payOrder.getPaytime())*1000;
			userTransactionHis.setCreateTime(time);
			int j = userTransactionHisService.saveUserTransactionHis(userTransactionHis);
			if(j==0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
				return 0;
			}
		}
		return 1;
	}

	@Override
	public int delPayOrder(PayOrder payOrder) {
		int i = payOrderMapper.delPayOrder(payOrder);
		if(i==1) {
			int j = userTransactionHisService.delUserTransactionHisByRefId(payOrder.getOrderId());
			if(j==0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动开启事务回滚
				return 0;
			}
		}
		return 1;
	}
}
