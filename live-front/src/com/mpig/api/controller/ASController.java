package com.mpig.api.controller;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.mpig.api.acl.AclFilter;
import com.mpig.api.as.util.AsUtil;
import com.mpig.api.model.ReturnModel;
import com.mpig.api.redis.service.AdStatisticsRedisService;
import com.mpig.api.utils.CodeContant;
import com.mpig.api.utils.EncryptUtils;
import com.mpig.api.utils.ParamHandleUtils;
import com.mpig.api.utils.RedisContant;

@Controller
@Scope("prototype")
@RequestMapping("/as")
public class ASController extends BaseController{
	private static Logger logger = Logger.getLogger(ASController.class.getSimpleName());
	
	/**
	 * 
	 * @param muid 广点通设备ID
	 * @param click_time 点击时间 精确到秒
	 * @param click_id 广点通生成的用户唯一ID
	 * @param appid 应用ID
	 * @param app_type android/ios
	 * @param advertiser_id 
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping("/gdt/feedback")
	@ResponseBody
	public Map<String,Object> feedback(String muid, Long click_time, String appid, String click_id, String app_type,String advertiser_id, HttpServletRequest req, HttpServletResponse resp){
		Map<String,Object> model = new HashMap<String, Object>();
		if (ParamHandleUtils.isBlank(req, "muid", "click_time", "appid", "click_id", "app_type", "advertiser_id")) {
			model.put("ret", 1);
			return model;
		}
		if(!appid.equals(AsUtil.gdt_AppId)){
			model.put("ret", 1);
			return model;
		}
		try {
			if(app_type.equals("ios")){
				if(AdStatisticsRedisService.getInstance().hget(RedisContant.adTxIOSStatistics, muid)==null){ //激活列表没找到结果 插入广告点击结果
					Map<String,Object> data = new HashMap<String, Object>();
					data.put("muid", muid);
					data.put("click_time", click_time);
					data.put("appid", appid);
					data.put("click_id", click_id);
					data.put("app_type", app_type);
					data.put("advertiser_id", advertiser_id);
					AdStatisticsRedisService.getInstance().setex(RedisContant.adTxFeedBack+muid, 604800, JSONObject.toJSONString(data));
				}
			}
			model.put("ret", 0); //0为成功处理
		} catch (Exception e) {
			model.put("ret", 1);
			logger.error("广点通点击通知处理失败", e);
		}
		return model;
	}
	/**
	 *  爱思广告
	 * @param appid
	 * @param mac
	 * @param os
	 * @param idfa
	 * @param openudid
	 * @param callback
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping("/as/feedback")
	@ResponseBody
	public Map<String,Object> asFeedback(String appid, String mac, String os , String idfa, String openudid,String callback, HttpServletRequest req, HttpServletResponse resp){
		Map<String,Object> model = new HashMap<String, Object>();
		if (ParamHandleUtils.isBlank(req, "appid", "idfa", "callback")) {
			model.put("success", false); 
			model.put("message", "参数有误"); 
			return model;
		}
		if(!appid.equals("1091139786")){
			model.put("success", false); 
			model.put("message", "参数有误"); 
			return model;
		}
		try {
			if(AdStatisticsRedisService.getInstance().hget(RedisContant.adAsIOSStatistics, idfa)==null){ //激活列表没找到结果 插入广告点击结果
				Map<String,Object> data = new HashMap<String, Object>();
				data.put("appid", appid);
				data.put("mac", mac);
				data.put("os", os);
				data.put("idfa", idfa);
				data.put("openudid", openudid);
				data.put("callback", URLDecoder.decode(callback));
				AdStatisticsRedisService.getInstance().setex(RedisContant.adAsFeedBack+idfa, 604800, JSONObject.toJSONString(data));
			}
			model.put("success", true); 
			model.put("message", "成功"); 
		} catch (Exception e) {
			model.put("success", false); 
			model.put("message", "失败"); 
			logger.error("广点通点击通知处理失败", e);
		}
		return model;
	}
	
	/**
	 *  mob 广告
	 * @param uuid
	 * @param clickid
	 * @param subchannel
	 * @param ip
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping("/mob/feedback")
	@ResponseBody
	public void mobFeedback(String uuid, String clickid, String subchannel , String ip, HttpServletRequest req, HttpServletResponse resp){
		if (ParamHandleUtils.isBlank(req, "uuid", "clickid", "subchannel")) {
			return;
		}
		String remoteIP = AclFilter.getRemoteAddrIp(req);
		try {
			String muidStr = remoteIP;
			String muid = EncryptUtils.md5(muidStr.toUpperCase(), null);
			if(AdStatisticsRedisService.getInstance().hget(RedisContant.adMobIOSStatistics, muid)==null){ //激活列表没找到结果 插入广告点击结果
				Map<String,Object> data = new HashMap<String, Object>();
				data.put("uuid", uuid);
				data.put("clickid", clickid);
				data.put("subchannel", subchannel);
				data.put("ip", ip);
				AdStatisticsRedisService.getInstance().setex(RedisContant.adMobFeedBack+muid, 86400, JSONObject.toJSONString(data));
			}
			resp.sendRedirect("https://itunes.apple.com/cn/app/id1091139786");
		} catch (Exception e) {
			logger.error("mob点击通知处理失败", e);
		}
	}
	/**
	 * 激活接口
	 * @param muid 广点通ID
	 * @param ostype 0 android 1ios
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping("/activation")
	@ResponseBody
	public ReturnModel activation(String idfa, Long ostype, HttpServletRequest req, HttpServletResponse resp){
		if(StringUtils.isEmpty(idfa) || ostype==null){
			returnModel.setCode(CodeContant.CONAUTHEMPTY);
			returnModel.setMessage("参数不允许为空!");
			return returnModel;
		}
		try {
			String os = "";
			if(ostype==0){
				os="ANDROID";
			}else if(ostype==1){
				os="IOS";
			}else{
				returnModel.setCode(CodeContant.CONPARAMSEXCEPT);
				returnModel.setMessage("参数类型错误!");
				return returnModel;
			}
			AsUtil.adStatistics(idfa, os, req);
		} catch (Exception e) {
			returnModel.setCode(CodeContant.CONSYSTEMERR);
			returnModel.setMessage("系统错误!");
			logger.error("处理激活请求失败 : "+ e.getMessage());
		}
		return returnModel;
	}
	
	
}
