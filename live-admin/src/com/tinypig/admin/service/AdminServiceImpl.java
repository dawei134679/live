package com.tinypig.admin.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.tinypig.admin.dao.AdminUserDao;

@Service
public class AdminServiceImpl {

	/**
	 * 获取管理员操作日志
	 * @param acton
	 * @param uid
	 * @param stime
	 * @param etime
	 * @return
	 */
	public Map<String, Object> getOperatLogs(int action,int uid,Long stime,Long etime,int page,int size){
		
		return AdminUserDao.getInstance().getOperatLogs(action, uid, stime, etime, page, size);
	}

	/**
	 * 获取管理员填充下拉框
	 * @param isvalid =9全部
	 * @return
	 */
	public List<Map<String, Object>> getAdminForSelect (int isvalid){
		return AdminUserDao.getInstance().getAdminForSelect(isvalid);
	}
}
