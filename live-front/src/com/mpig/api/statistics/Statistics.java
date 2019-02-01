package com.mpig.api.statistics;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.mashape.unirest.http.Unirest;
import com.mpig.api.dictionary.lib.UrlConfigLib;

public class Statistics {

	private static final Logger logger = Logger.getLogger(Statistics.class);

	// 统计 /tokenlogin 和 /baseConf
	static public void SendPigAnalysis(int nUid, String strOs, HttpServletRequest req) {
		try {
			// 用户id
//			Long uid = new Long(nUid);
			Long uid = (long) nUid;
			// 平台
			Integer os = null;
			if (null != strOs) {
				os = Integer.valueOf(strOs);
			}
			if (null == os) {
				os = -1;
			}
			// 手机型号
			String mobilemodel = req.getParameter("mobileModel");
			if (null == mobilemodel) {
				mobilemodel = "null";
			}
			// 渠道
			String channel = req.getParameter("channel");
			if (null == channel) {
				channel = "null";
			}
			// 版本号
			String mobileversion = req.getParameter("mobileVersion");
			if (null == mobileversion) {
				mobileversion = "null";
			}
			// =1初次激活 =2非初次
			String strtype = req.getParameter("istype");
			Integer istype = null;
			if (null != strtype) {
				istype = Integer.valueOf(strtype);
			}
			if (null == istype) {
				istype = -1;
			}
			// 激活时间
			Integer activationtime = null;
			String strActivationtime = req.getParameter("activationtime");
			if (null != strActivationtime) {
				activationtime = Integer.valueOf(strActivationtime);
			}
			if (null == activationtime) {
				activationtime = -1;
			}
			// 错误状态 =0正常 =1出错
			Integer iserror = null;
			String strIserror = req.getParameter("iserror");
			if (null != strIserror) {
				iserror = Integer.valueOf(strIserror);
			}
			if (null == iserror) {
				iserror = -1;
			}
			// 错误详情
			String errorcode = req.getParameter("errorcode");
			if (null == errorcode) {
				errorcode = "null";
			}
			// 设备物理地址
			String mac = req.getParameter("mac");
			if (null == mac) {
				mac = "null";
			}
			// 分辨率
			String display = req.getParameter("display");
			if (null == display) {
				display = "null";
			}
			// 存储
			String storage = req.getParameter("storage");
			if (null == storage) {
				storage = "null";
			}
			// 内存
			String memory = req.getParameter("memory");
			if (null == memory) {
				memory = "null";
			}
			// CPU
			String cpu = req.getParameter("cpu");
			if (null == cpu) {
				cpu = "null";
			}
			// 语言
//			String language = req.getParameter("language");
			String language = getIpAddr(req);
			if (null == language) {
				language = "null";
			}
			// 位置
			String location = req.getParameter("location");
			if (null == location) {
				location = "null";
			}
			// imei
			String imei = req.getParameter("imei");
			if (null == imei) {
				imei = "null";
			}
			// imsi
			String imsi = req.getParameter("imsi");
			if (null == imsi) {
				imsi = "null";
			}
			// ios是否越狱,android是否root
			String isOfficial = req.getParameter("isOfficial");
			if (null == isOfficial) {
				isOfficial = "null";
			}
			// 省份
			String province = req.getParameter("province");
			if (null == province) {
				province = "null";
			}
			// 城市
			String city = req.getParameter("city");
			if (null == city) {
				city = "null";
			}

//			logger.info("DEBUG~~~~~~~~~~~~~~~~<info>getAdmin_piganalysis>" + "uid "+ uid+" os "+os + " mobilemodel "+mobilemodel
//					+" channel "+channel+" mobileversion "+mobileversion+" istype "+istype+" activationtime "+activationtime+" iserror "+iserror
//					+" errorcode "+errorcode+" mac "+mac+" display "+display+" storage "+storage+" memory "+memory+" cpu "+cpu+" language "+language+" location "+location
//					+" imei "+imei+" imsi "+imsi+" isOfficial "+isOfficial+" province "+province+" city "+city);
			
			Unirest.post(UrlConfigLib.getUrl("url").getAdmin_piganalysis()).field("uid", uid).field("os", os)
					.field("mobilemodel", mobilemodel).field("channel", channel).field("mobileversion", mobileversion)
					.field("istype", istype).field("activationtime", activationtime).field("iserror", iserror)
					.field("errorcode", errorcode).field("mac", mac).field("display", display).field("storage", storage)
					.field("memory", memory).field("cpu", cpu).field("language", language).field("location", location)
					.field("imei", imei).field("imsi", imsi).field("isOfficial", isOfficial).field("province", province)
					.field("city", city).asStringAsync();
		} catch (Exception e) {
			logger.error("<error>getAdmin_piganalysis>" , e);
		}
	}
	
	static private String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-real-ip");//先从nginx自定义配置获取
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("x-forwarded-for");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
}
