package com.tinypig.newadmin.web.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.tinypig.newadmin.web.entity.ReportFeed;
import com.tinypig.newadmin.web.entity.ReportFeedParamDto;
import com.tinypig.newadmin.web.service.ReportFeedService;
import com.tinypig.newadmin.web.service.UserFeedService;

@Controller
@RequestMapping("/reportFeed")
public class ReportFeedAction {

	@Autowired
	private ReportFeedService reportFeedService;

	@Autowired
	private UserFeedService userFeedService;

	@RequestMapping(value = "/pageList", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String pageList(HttpServletRequest res,ReportFeedParamDto reportFeedParamDto) {
		List<ReportFeed> list = reportFeedService.pageList(reportFeedParamDto);
		return JSONObject.toJSONString(list);
	}

	@RequestMapping(value = "/detail", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String detail(HttpServletRequest res,Integer id) {
		Map<String,Object> map = reportFeedService.details(id);
		return JSONObject.toJSONString(map);
	}

	//处理举报数据
	@RequestMapping(value = "/chuliFeed", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String chuliFeed(HttpServletRequest res,Integer id,Integer status,Integer userFeedId) {
		Map<String,Object> map = userFeedService.chuliFeed(id,status,userFeedId);
		return JSONObject.toJSONString(map);
	}

}
