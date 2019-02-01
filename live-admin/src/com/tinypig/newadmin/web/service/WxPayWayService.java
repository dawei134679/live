package com.tinypig.newadmin.web.service;

import java.util.Map;

public interface WxPayWayService {
	
	Map<String, Object> reWxPayWayRedis(String payWay);
	
	Map<String, Object> initWxPayWay();
}
