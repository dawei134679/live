package com.tinypig.newadmin.web.dao;

import java.util.List;
import java.util.Map;

import com.tinypig.newadmin.web.entity.UserTransactionHisSta;
import com.tinypig.newadmin.web.entity.UserTransactionHisStaParam;

public interface UserTransactionHisStaMapper {

	public List<UserTransactionHisSta> getUserTransactionHisListPage(UserTransactionHisStaParam param);
	
	public int getserTransactionHisTotalCount(UserTransactionHisStaParam param);
	
	public List<UserTransactionHisSta> getUserTransactionHisList(UserTransactionHisStaParam param);

	public Map<String, Object> getUserTransactionHisTotal(UserTransactionHisStaParam param);
}
