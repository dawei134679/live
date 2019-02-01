package com.mpig.api.service.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.mpig.api.SqlTemplete;
import com.mpig.api.db.DBHelper;
import com.mpig.api.service.IVerifyCodeService;
import com.mpig.api.utils.VarConfigUtils;

@Service
public class VerifyCodeServiceImpl implements IVerifyCodeService, SqlTemplete {
	private static final Logger logger = Logger.getLogger(VerifyCodeServiceImpl.class);

	private final static VerifyCodeServiceImpl instance = new VerifyCodeServiceImpl();

	public static VerifyCodeServiceImpl getInstance() {
		return instance;
	}

	@Override
	public int insertVerifyCode(String mobile, String strType, int code, long takeEffectTime, long expiryTime) {
		long nowTime = System.currentTimeMillis()/1000;
		try {
			String sql = SQL_InsertVerifyCode;
			int type = 1;//默认1 注册
			if("forget".equals(strType)) {//重置密码
				type = 2;
			}
			return DBHelper.execute(VarConfigUtils.dbZhuAdmin, sql, false, mobile,type,code,takeEffectTime,expiryTime,nowTime);
		} catch (Exception e) {
			logger.error("<saveVerifyCode->Exception>" + e.toString());
			return 0;
		}
	}
}
