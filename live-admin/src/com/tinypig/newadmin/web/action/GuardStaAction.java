package com.tinypig.newadmin.web.action;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/guardSta")
public class GuardStaAction {
	
	@RequestMapping(value = "/getGameList", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getGuardSta(HttpServletRequest request) {
		Integer page = Integer.valueOf(request.getParameter("page"));
		Integer rows = Integer.valueOf(request.getParameter("rows"));
		return JSONObject.toJSONString(null);
	}
}
