package com.mpig.api.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mpig.api.utils.CodeContant;
import com.mpig.api.utils.Constant;
import com.mpig.api.utils.ParamHandleUtils;
import com.qiniu.util.Auth;

@Controller
@Scope("prototype")
@RequestMapping("/token")
public class TokenController extends BaseController {
	private static final Logger logger = Logger.getLogger(TokenController.class);
	
	@RequestMapping(value = "/qiniu", method = RequestMethod.GET)
	public void getQiniuToken(HttpServletRequest req,HttpServletResponse resp){

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime","token")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os","reqtime")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}
		
		// 校验码验证及token
		authToken(req, false);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}
		String tokenStr = req.getParameter("token");// 充值操作对象
		int uid = authService.decryptToken(tokenStr, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}	
		Auth auth = Auth.create(Constant.qn_accessKey, Constant.qn_secretKey);		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("token", auth.uploadToken(Constant.qn_default_bucket,String.valueOf(uid)));
		map.put("key", uid);
		returnModel.setData(map);
		writeJson(resp, returnModel);
	}
	
	@RequestMapping(value = "/feed", method = RequestMethod.GET)
	public void getFeedQiniuToken(HttpServletRequest req,HttpServletResponse resp){

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime","token","feedId")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os","reqtime")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}
		
		// 校验码验证及token
		authToken(req, false);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}
		String tokenStr = req.getParameter("token");// 充值操作对象
		int uid = authService.decryptToken(tokenStr, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}	
		String fileName = RandomStringUtils.random(7, true, true) + System.currentTimeMillis();
		String feedId = req.getParameter("feedId");// 充值操作对象
		Auth auth = Auth.create(Constant.qn_accessKey, Constant.qn_secretKey);		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("token", auth.uploadToken(Constant.qn_feed_bucket,feedId+"/"+fileName));
		map.put("key", feedId+"/"+fileName);
		returnModel.setData(map);
		writeJson(resp, returnModel);
	}
	
	/**
	 * 获取主播封面的七牛token
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/coverqiniu", method = RequestMethod.GET)
	public void getCoverQiniuToken(HttpServletRequest req,HttpServletResponse resp){

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime","token")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os","reqtime")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}
		
		// 校验码验证及token
		authToken(req, false);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}
		String tokenStr = req.getParameter("token");
		int uid = authService.decryptToken(tokenStr, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}	
		Auth auth = Auth.create(Constant.qn_accessKey, Constant.qn_secretKey);		
		Map<String, Object> map = new HashMap<String, Object>();
		String fileName = RandomStringUtils.random(7, true, true) + System.currentTimeMillis();
		map.put("token", auth.uploadToken(Constant.qn_liveCover_bucket,String.valueOf(uid)+"/"+fileName));
		logger.error("debug " + Constant.qn_liveCover_bucket);
		map.put("key", uid+"/"+fileName);
		returnModel.setData(map);
		writeJson(resp, returnModel);
	}
}