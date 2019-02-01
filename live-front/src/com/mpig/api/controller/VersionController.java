package com.mpig.api.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.mpig.api.dictionary.VersionConfig;
import com.mpig.api.dictionary.lib.VersionConfigLib;
import com.mpig.api.model.IosVersionModel;
import com.mpig.api.model.ReturnModel;
import com.mpig.api.redis.service.RedisCommService;
import com.mpig.api.utils.CodeContant;
import com.mpig.api.utils.EncryptUtils;
import com.mpig.api.utils.JsonUtil;
import com.mpig.api.utils.ParamHandleUtils;
import com.mpig.api.utils.RedisContant;

@RestController
@Scope("prototype")
@RequestMapping("/version")
public class VersionController extends BaseController {
	private static final Logger logger = Logger.getLogger(VersionController.class);
	
	@RequestMapping(value="/notshow")
	@ResponseBody
	public ReturnModel isNotShow(HttpServletRequest req,HttpServletResponse resp){
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime","appid")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		// 验证auth及token
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			return returnModel;
		}
		String appid = req.getParameter("appid");
		String iosVersionJson = RedisCommService.getInstance().get(RedisContant.RedisNameUser, RedisContant.verifyIos+":"+appid);
		IosVersionModel iosVersionBean = JsonUtil.toBean(iosVersionJson, IosVersionModel.class);
		
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("version", iosVersionBean.getVersion());
		returnModel.setData(map);
		
		return returnModel;
	}
	
	
	@RequestMapping(value = "ios", method = RequestMethod.GET)
	public ReturnModel getIosVersion(HttpServletRequest req, HttpServletResponse resp) {
		if("2".equals(req.getParameter("os"))&&StringUtils.isBlank(req.getParameter("appVersion"))) {
			returnModel.setCode(CodeContant.VersionNoExist);
			returnModel.setMessage("版本没有配置");
			return returnModel;
		}
		VersionConfig config = VersionConfigLib.getConfig("ios");
		if (config == null) {
			returnModel.setCode(CodeContant.VersionNoExist);
			returnModel.setMessage("版本没有配置");
			return returnModel;
		}
		returnModel.setData(config.toHashMap());
		return returnModel;
	}

	@RequestMapping(value = "android", method = RequestMethod.GET)
	public ReturnModel getAndroidVersion(HttpServletRequest req, HttpServletResponse resp) {
		String versioncode = req.getParameter("versioncode");
		System.out.println("============ versioncode" + versioncode);
		VersionConfig config = VersionConfigLib.getConfig("android");
		if (config == null) {
			returnModel.setCode(CodeContant.VersionNoExist);
			returnModel.setMessage("版本没有配置");
		} else {
			if (versioncode == null) {
				returnModel.setData(config.toHashMap());
			} else {
				int sysVersion = Integer.parseInt(config.getLastVersion().replace(".", "0"));
				int appversion = Integer.parseInt(versioncode);
				if (sysVersion > appversion) {
					returnModel.setData(config.toHashMap());
				} else {
					returnModel.setCode(CodeContant.VersionNoExist);
					returnModel.setMessage("版本没有配置");
				}
			}
		}
		return returnModel;
	}
	
	/**
	 * 接口密钥
	 */
	private static final String SecretKey = "GgkAKLr9WQ2b33MV";
	private static final int DnsApiTimeOut = 20 * 1000;
	
	/**
	 * dns解析服务接口
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping("/dns")
	@ResponseBody
	public Object dnsApi(HttpServletRequest req, HttpServletResponse resp){
		HashMap<String,String> data = Maps.newHashMap();
		if (ParamHandleUtils.isBlank(req,"timestamp","host","sig")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			data.put("api", "");
		}
		int timestamp = Integer.parseInt(req.getParameter("timestamp"));
		String sig = req.getParameter("sig");
		String host = req.getParameter("host");
		String content = SecretKey+timestamp;
		try {
			String md5 = EncryptUtils.md5ByCharset(content, Charsets.UTF_8);
			if(!md5.equals(sig)){
				returnModel.setCode(CodeContant.SignatureFail);
				returnModel.setMessage("请求签名异常");
				data.put("api", "");
			}else{
				if(System.currentTimeMillis()  - timestamp > DnsApiTimeOut){
					returnModel.setCode(CodeContant.SignatureTimeOut);
					returnModel.setMessage("请求签名超时");
					data.put("api", "");
				}else{
					data.put("api", "http://203.107.1.1/125754/d?host="+host);
				}
			}
		} catch (Exception e) {
			logger.error("dnsApi>>md5 failed",e);
			returnModel.setCode(CodeContant.SignatureFail);
			returnModel.setMessage("请求签名加密异常");
			data.put("api", "");
		}
		returnModel.setData(data);
		return returnModel;
	}
}
