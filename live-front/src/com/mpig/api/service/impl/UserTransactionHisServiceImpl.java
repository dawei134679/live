package com.mpig.api.service.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.mpig.api.SqlTemplete;
import com.mpig.api.db.DBHelper;
import com.mpig.api.service.IUserTransactionHisService;
import com.mpig.api.utils.VarConfigUtils;

@Service
public class UserTransactionHisServiceImpl implements IUserTransactionHisService {

	private static Logger logger = Logger.getLogger(UserTransactionHisServiceImpl.class);
	
	@Override
	public int saveUserTransactionHis(Integer transType, Integer uid, Double amount, Long money, Long createTime,String refId, Integer dataType) {
		try {
			int executeResult = DBHelper.execute(VarConfigUtils.dbZhuBill, 
					SqlTemplete.SQL_insUserTransactionHis,false,transType,uid,amount,money,createTime,refId,dataType);
			return executeResult;
		} catch (Exception e) {
			logger.error("保存用户交易失败："+e.getMessage(),e);
			return 0;
		}
	}
}
