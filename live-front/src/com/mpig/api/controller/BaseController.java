package com.mpig.api.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.mpig.api.model.ReturnModel;
import com.mpig.api.service.IAuthService;
import com.mpig.api.utils.CodeContant;
import com.mpig.api.utils.ParamHandleUtils;

public class BaseController {
	
	@Autowired
	protected IAuthService authService; 
	protected ReturnModel returnModel = new ReturnModel();
	
	/**
	 * json输出
	 * @param resp
	 * @param data
	 */
	protected void writeJson(HttpServletResponse resp,String data) {
		try {
			resp.setHeader("Content-type", "text/plain;charset=UTF-8");
			resp.getOutputStream().write(data.getBytes("UTF-8"));
			return;
		} catch (Exception e) {			
			// TODO: handle exception
		}
	}
	
	/**
	 * json输出
	 * @param resp
	 * @param data
	 */
	protected void writeJson(HttpServletResponse resp,Object data) {
		try {
			resp.setHeader("Content-type", "text/plain;charset=UTF-8");
			resp.getOutputStream().write(JSONObject.toJSONString(data).getBytes("UTF-8"));
			return;
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	/**
	 * 验证auth
	 * @param req
	 * @param bl
	 */
	protected void authToken(HttpServletRequest req,Boolean bl) {	
		//获取参数值
		String ncode = req.getParameter("ncode");
		String osString = req.getParameter("os");
		String imei = req.getParameter("imei");
		String reqString = req.getParameter("reqtime");		
		String token = req.getParameter("token");
		
		Byte os;
		Long reqtime;

		os = Byte.valueOf(osString);
		reqtime = Long.valueOf(reqString);
		if (token == null) {
			token = "";
		}

		//校验token状态值
		authService.authToken(ncode, os, imei, reqtime, token, bl,returnModel);
	}
	
	/**
	 * 基本参数校验，通用函数
	 * @param req
	 * @return
	 */
	protected boolean checkDefaultArgs(HttpServletRequest req ){
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return false;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return false;
		}

		// 验证auth及token
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			return false;
		}
		
		return true;
	}
}
