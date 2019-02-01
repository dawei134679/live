package com.tinypig.newadmin.web.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.tinypig.admin.redis.service.RedisOperat;
import com.tinypig.admin.util.RedisContant;
import com.tinypig.newadmin.web.service.WxPayWayService;

@Service
public class WxPayWayServiceImpl implements WxPayWayService {

	@Override
	public Map<String, Object> reWxPayWayRedis(String payWay) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		RedisOperat.getInstance().del(RedisContant.host, RedisContant.port6379, RedisContant.wxPayWay);
		String result = RedisOperat.getInstance().set(RedisContant.host, RedisContant.port6379, RedisContant.wxPayWay,payWay);
		if (null == result) {
			resultMap.put("code", "201");
		}else {
			resultMap.put("code", "200");
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> initWxPayWay() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String result = RedisOperat.getInstance().get(RedisContant.host, RedisContant.port6379, RedisContant.wxPayWay);
		if (null == result) {
			resultMap.put("code", "201");
		}else {
			resultMap.put("code", "200");
			resultMap.put("payWay", result);
		}
		return resultMap;
	}

}
