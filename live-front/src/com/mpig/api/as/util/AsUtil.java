package com.mpig.api.as.util;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.mpig.api.acl.AclFilter;
import com.mpig.api.redis.service.AdStatisticsRedisService;
import com.mpig.api.utils.Base64Util;
import com.mpig.api.utils.EncryptUtils;
import com.mpig.api.utils.HttpUtil;
import com.mpig.api.utils.RedisContant;

public class AsUtil {
	private static Logger logger = Logger.getLogger(AsUtil.class.getSimpleName());
	
	public static String gdt_AppId = "1091139786";
	public static String gdt_ADId = "2043940";
	public static String gdt_encrypt_key = "BAAAAAAAAAAAHzAk";
	public static String gdt_sign_key = "4d5ba1a66b0d306f";
	public static String gdt_uploadApi_url = "http://t.gdt.qq.com/conv/app/%s/conv?"; //广点通激活上报接口
	public static String mob_uploadApi_url = "http://stat.mobvista.com/install?"; //mob点应激活上报接口
	public static String as_uploadApi_url = "http://ios.api.i4.cn/appactivatecb.xhtml?";//爱思激活上报接口
	
	
	/**
	 * 激活资格统计
	 * @param muid
	 * @param http://t.gdt.qq.com/conv/app/{appid}/conv?v={data}&conv_type={conv_type}&app_type={ app_type}&advertiser_id={uid}
	 */
	public static void adStatistics(String idfa, String ostype, HttpServletRequest req){
		try {
			String remoteIP = AclFilter.getRemoteAddrIp(req);
			String muidStr = remoteIP;
			String mobid = EncryptUtils.md5(muidStr.toUpperCase(), null);
			
			String muid = EncryptUtils.md5(idfa.toUpperCase(), null);
			//查找是否在广点通广告点击结果里
			if(AdStatisticsRedisService.getInstance().exists(RedisContant.adTxFeedBack+muid)){
				//获取点击结果 插入到激活列表 并删除点击key
				String feedstr = AdStatisticsRedisService.getInstance().get(RedisContant.adTxFeedBack+muid);
				logger.info("<gdt getfeedbackStr : " + feedstr);
				AdStatisticsRedisService.getInstance().hset(RedisContant.adTxIOSStatistics, muid, feedstr);
				AdStatisticsRedisService.getInstance().del(RedisContant.adTxFeedBack+muid);
				noticeGdt(muid, ostype, feedstr);
			}
			//查找是否在mob广告点击结果里
			if(AdStatisticsRedisService.getInstance().exists(RedisContant.adMobFeedBack+mobid)){
				//获取点击结果 插入到激活列表 并删除点击key
				String feedstr = AdStatisticsRedisService.getInstance().get(RedisContant.adMobFeedBack+mobid);
				logger.info("<mob getfeedbackStr : " + feedstr);
				AdStatisticsRedisService.getInstance().hset(RedisContant.adMobIOSStatistics, mobid, feedstr);
				AdStatisticsRedisService.getInstance().del(RedisContant.adMobFeedBack+mobid);
				noticeMob(mobid, ostype, feedstr,idfa);
			}
			//查找是否在as广告点击结果里
			if(AdStatisticsRedisService.getInstance().exists(RedisContant.adAsFeedBack+idfa)){
				//获取点击结果 插入到激活列表 并删除点击key
				String feedstr = AdStatisticsRedisService.getInstance().get(RedisContant.adAsFeedBack+idfa);
				logger.info("<as getfeedbackStr : " + feedstr);
				AdStatisticsRedisService.getInstance().hset(RedisContant.adAsIOSStatistics, idfa, feedstr);
				AdStatisticsRedisService.getInstance().del(RedisContant.adAsFeedBack+idfa);
				noticeAs(feedstr,idfa);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 爱思激活上报接口
	 * @param feedstr
	 * @param idfa
	 * @return
	 */
	public static String noticeAs(String feedstr, String idfa){
		JSONObject jsonObject = JSONObject.parseObject(feedstr);
		
		//发送上报请求
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String callback = jsonObject.get("callback").toString();
		String result = HttpUtil.doGet(callback, paramMap);
		logger.info("< do get as : " + paramMap.toString());
		return result;
	}
	
	/**
	 *  mob激活上报接口
	 * @param muid
	 * @param ostype
	 * @param feedstr
	 * @param idfa
	 * @return
	 */
	public static String noticeMob(String muid, String ostype, String feedstr, String idfa){
		JSONObject jsonObject = JSONObject.parseObject(feedstr);
		
		//发送上报请求
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("mobvista_pl", ostype);
		paramMap.put("mobvista_campuuid", jsonObject.get("uuid").toString());
		paramMap.put("mobvista_ip", jsonObject.get("ip").toString());
		paramMap.put("mobvista_clickid", jsonObject.get("clickid").toString());
		paramMap.put("mobvista_gaid", idfa);
		paramMap.put("mobvista_devid", idfa);
		logger.info("< do get mob : " + paramMap.toString());
		String result = HttpUtil.doGet(mob_uploadApi_url, paramMap);
		return result;
	}
	/**
	 * 广点通激活上报
	 * @param muid
	 * @param ostype
	 * @param feedstr
	 * @return
	 * @throws Exception
	 */
	public static String noticeGdt(String muid, String ostype, String feedstr) throws Exception{
		String url = String.format(gdt_uploadApi_url, gdt_AppId);
		//通知广点通
		JSONObject jsonObject = JSONObject.parseObject(feedstr);
		String click_id = jsonObject.get("click_id").toString();
	    Long conv_time = System.currentTimeMillis()/1000;
	    //组装组合参数
	    StringBuffer vbuffer = new StringBuffer();
	    vbuffer.append("muid=").append(muid);
	    vbuffer.append("&click_id=").append(click_id);
	    vbuffer.append("&conv_time=").append(conv_time);
	    String fullUrl = url+vbuffer.toString();
	    String encodePage = URLEncoder.encode(fullUrl);
	    logger.info("< encoder url : " + encodePage);
	    String signatureStr = gdt_sign_key+"&GET&"+encodePage;
	    logger.info("< signatureStr : " + signatureStr);
	    //获得组合参数签名
	    String sign = EncryptUtils.md5(signatureStr, null);
	    logger.info("< sign : " + sign);
	    //组装加密参数 v
	    vbuffer.append("&sign").append(sign);
	    logger.info("< vbuffer : " + vbuffer.toString());
		String v = Base64Util.encode(vbuffer.toString(), gdt_encrypt_key);
		logger.info("< param v : " + v);
		
		//发送上报请求
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("v", v);
		paramMap.put("conv_type", "MOBILEAPP_ACTIVITE");
		paramMap.put("app_type", ostype);
		paramMap.put("advertiser_id", gdt_ADId);
		logger.info("< do get gdt : " + paramMap.toString());
		String result = HttpUtil.doGet(url, paramMap);
		logger.info("< do get gdt result : " + result);
		return result;
	}
	/**
	 * 查找注册来源 (供注册使用)
	 * @param muid
	 * @return
	 */
	public static String registerSource(String idfa, HttpServletRequest req){
		String ostype = "";
		try {
			String muid = EncryptUtils.md5(idfa.toUpperCase(), null);
			System.out.println(AdStatisticsRedisService.getInstance().hget(RedisContant.adTxIOSStatistics, muid));
			if(AdStatisticsRedisService.getInstance().hget(RedisContant.adTxIOSStatistics, muid)!=null){
				ostype = "ios_gdt";
			}
			String remoteIP = AclFilter.getRemoteAddrIp(req);
			String muidStr = remoteIP;
			String mobid = EncryptUtils.md5(muidStr.toUpperCase(), null);
			if(AdStatisticsRedisService.getInstance().hget(RedisContant.adMobIOSStatistics, mobid)!=null){
				ostype = "ios_mob";
			}
			if(AdStatisticsRedisService.getInstance().hget(RedisContant.adAsIOSStatistics, idfa)!=null){
				ostype = "ios_as";
			}
		} catch (Exception e) {
			logger.info(" 查找注册来源错误 : " + e.getMessage());
		}
		
		return ostype;
	}
}
