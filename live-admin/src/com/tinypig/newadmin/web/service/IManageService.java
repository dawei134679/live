package com.tinypig.newadmin.web.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public interface IManageService {

	/**
	 * 获取运营管理人员列表
	 * @return
	 */
	List<JSONObject> getManagerList();
	
	/**
	 * 设置管理人员
	 */
	void setManager(String uid);
}
