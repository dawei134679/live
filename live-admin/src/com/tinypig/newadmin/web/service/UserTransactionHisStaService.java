package com.tinypig.newadmin.web.service;

import java.util.List;
import java.util.Map;

import com.tinypig.newadmin.web.entity.UserTransactionHisSta;
import com.tinypig.newadmin.web.entity.UserTransactionHisStaParam;

public interface UserTransactionHisStaService {
	
	public Map<String, Object> getUserTransactionHisListPage(UserTransactionHisStaParam param);
	
	public List<UserTransactionHisSta> getUserTransactionHisList(UserTransactionHisStaParam param);
	
	public Map<String,Object> getUserTransactionHisTotal(UserTransactionHisStaParam param);
}
