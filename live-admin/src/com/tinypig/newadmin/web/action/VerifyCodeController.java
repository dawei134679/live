package com.tinypig.newadmin.web.action;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.tinypig.newadmin.web.entity.VerifyCodeParamDto;
import com.tinypig.newadmin.web.service.IVerifyCodeService;

/**
 * 钻石公会
 */
@Controller
@RequestMapping("/verifyCode")
public class VerifyCodeController {

	@Resource
	private IVerifyCodeService verifyCodeServie;

	@RequestMapping(value = "/getVerifyCodeList", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getVerifyCodeList(HttpServletRequest request, VerifyCodeParamDto params) {
		Integer page = Integer.valueOf(request.getParameter("page"));
		Integer rows = Integer.valueOf(request.getParameter("rows"));
		Integer startIndex = (page - 1) * rows;
		params.setStartIndex(startIndex);
		params.setPageSize(rows);
		Map<String, Object> result = verifyCodeServie.getVerifyCodeList(params);
		return JSONObject.toJSONString(result);
	}
}
