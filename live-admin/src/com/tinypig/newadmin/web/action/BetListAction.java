package com.tinypig.newadmin.web.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.tinypig.newadmin.web.service.BetListService;

@Controller
@RequestMapping("/bl")
public class BetListAction {
	
	@Autowired
	private BetListService betListService;
	
	@RequestMapping(value = "getBetList", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getBetList(HttpServletRequest req) {
		Integer page = Integer.valueOf(req.getParameter("page"));
		Integer rows = Integer.valueOf(req.getParameter("rows"));
		Long periods = null;
		Long anchorId = null;
		Long uid = null;
		if(StringUtils.isNotBlank(req.getParameter("periods"))) {
			periods = Long.valueOf(req.getParameter("periods"));
		}
		if(StringUtils.isNotBlank(req.getParameter("anchorId"))) {
			anchorId = Long.valueOf(req.getParameter("anchorId"));
		}
		if(StringUtils.isNotBlank(req.getParameter("uid"))) {
			uid = Long.valueOf(req.getParameter("uid"));
		}
		Map<String, Object> map = betListService.getBetList(periods, anchorId,uid, rows, page);
		return JSONObject.toJSONString(map);
	}
	
	@RequestMapping(value = "getBetTotal", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getBetTotal(HttpServletRequest req) {
		Long periods = null;
		Long anchorId = null;
		Long uid = null;
		if(StringUtils.isNotBlank(req.getParameter("periods"))) {
			periods = Long.valueOf(req.getParameter("periods"));
		}
		if(StringUtils.isNotBlank(req.getParameter("anchorId"))) {
			anchorId = Long.valueOf(req.getParameter("anchorId"));
		}
		if(StringUtils.isNotBlank(req.getParameter("uid"))) {
			uid = Long.valueOf(req.getParameter("uid"));
		}
		return JSONObject.toJSONString(betListService.getBetTotal(periods, anchorId,uid));
	}
}
