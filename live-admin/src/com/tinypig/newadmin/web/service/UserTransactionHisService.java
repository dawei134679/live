package com.tinypig.newadmin.web.service;

import com.tinypig.newadmin.web.entity.UserTransactionHis;

public interface UserTransactionHisService {

	public int saveUserTransactionHis(UserTransactionHis userTransactionHis);
	
	public int delUserTransactionHisByRefId(String refId);
}
