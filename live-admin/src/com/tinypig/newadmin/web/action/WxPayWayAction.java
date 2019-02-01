package com.tinypig.newadmin.web.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.tinypig.newadmin.web.service.WxPayWayService;

@Controller
@RequestMapping("/wxp")
public class WxPayWayAction {
	
	@Autowired
	private WxPayWayService wxPayWayService;
	
	@RequestMapping(value = "/reWxPayWayRedis")
	@ResponseBody
	public String reWxPayWayRedis(HttpServletRequest requset) {
		String payWay = requset.getParameter("payWay");
		Map<String, Object> resultMap = wxPayWayService.reWxPayWayRedis(payWay);
		return JSONObject.toJSONString(resultMap);
	}
	
	@RequestMapping(value = "/initWxPayWay")
	@ResponseBody
	public String initWxPayWay() {
		Map<String, Object> resultMap = wxPayWayService.initWxPayWay();
		return JSONObject.toJSONString(resultMap);
	}
}
