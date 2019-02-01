package com.tinypig.newadmin.web.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.tinypig.admin.redis.service.RedisOperat;
import com.tinypig.admin.util.RedisContant;
import com.tinypig.newadmin.web.service.IManageService;

public class ManageServiceImpl implements IManageService {

	
	@Override
	public List<JSONObject> getManagerList() {

		List<JSONObject> list = new ArrayList<JSONObject>();
		Map<String, String> managerlist = RedisOperat.getInstance().hgetAll(RedisContant.host, RedisContant.port6379,
				RedisContant.webManager);

		if (managerlist != null) {
			for (Map.Entry<String, String> entry : managerlist.entrySet()) {
				JSONObject jsonObject = JSONObject.parseObject(entry.getValue());
				list.add(jsonObject);
			}
		}
		return list;
	}

	@Override
	public void setManager(String uid) {
		
		
	}
}
