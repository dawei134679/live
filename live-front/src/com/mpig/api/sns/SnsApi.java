package com.mpig.api.sns;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

/**
 * 第三方接入
 * @author jackzhang
 */
public final class SnsApi {
	private static final Logger logger = Logger.getLogger(SnsApi.class);
	/**
	 * 微信开平接入方式web端
	 */
	public static String TencentWeixinAppIdForOpen = "wx0cb89cea01d4be87";
	public static String TencentWeixinAppSecretForOpen = "c914fb82a14a276cfcf39451459a02de";
	
	/**
	 * 公众号接入方式 h5
	 */
	public static String TencentWeixinAppIdForH5 = "wx354d8b9047f3585f";
	public static String TencentWeixinAppSecretForH5 = "96ad8198949b3ac2b6d289f083eb1893";
	
	//公众号
	public static String TencentWeixinAuthApiBaseMobile = "https://open.weixin.qq.com/connect/oauth2/";
	//网站
	public static String TencentWeixinAuthApiBaseWeb = "https://open.weixin.qq.com/connect/qrconnect/";
	//当前接入方式
	public static String TencentWeixinAuthApiBase = TencentWeixinAuthApiBaseWeb;
	
	/**
	 * 微信静默授权方式
	 */
	public static String TencentWeixinAuthorizeCodeSilentApi = TencentWeixinAuthApiBase+"authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_base&state=tinypig_alive#wechat_redirect";
	public static String TencentWeixinAuthorizeCodeApi = TencentWeixinAuthApiBase+"authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=tinypig_alive#wechat_redirect";
	
	public static String TencentWeixinAcessTokenApi = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
	public static String TencentWeixinSnsInfoApi = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";
	/**
	 * 获取微信公众号token
	 * @param code
	 * @return
	 */
	public static JSONObject getUserWXTokenForH5(String code) {
		String url = String.format(TencentWeixinAcessTokenApi,
				TencentWeixinAppIdForH5, TencentWeixinAppSecretForH5, code);
		logger.info("<getUserWXTokenForH5>--url" + url);
		HttpResponse<JsonNode> response = null;
		try {
			response = Unirest.get(url).asJson();
		} catch (Exception e) {
			logger.error("Error getUserWXTokenForH5-exception:"+e.toString());
		}
		if (response == null || response.getStatus() != 200) {
			return null;
		}

		JSONObject jsonObject = JSON.parseObject(response.getBody().toString());
		return jsonObject;
	}
	

	public static JSONObject getUserWXTokenForOpen(String code) {
		String url = String.format(TencentWeixinAcessTokenApi,
				TencentWeixinAppIdForOpen, TencentWeixinAppSecretForOpen, code);
		logger.info("<getUserWXTokenForOpen>--url" + url);
		HttpResponse<JsonNode> response = null;
		try {
			response = Unirest.get(url).asJson();
		} catch (Exception e) {
			logger.error("Error getUserWXTokenForOpen-exception:"+e.toString());
		}
		if (response == null || response.getStatus() != 200) {
			return null;
		}

		JSONObject jsonObject = JSON.parseObject(response.getBody().toString());
		return jsonObject;
	}
	
	public static JSONObject getUserInfoByToken(String token, String openid) {
		String url = String.format(TencentWeixinSnsInfoApi, token, openid);

		logger.info("<getUserInfoByToken>--url" + url);

		HttpResponse<String> response = null;
		try {
			response = Unirest.get(url).asString();
		} catch (Exception e) {
			logger.error("Error getUserInfoByToken-exception:"+e.toString());
		}
		if (response == null || response.getStatus() != 200) {
			return null;
		}

		JSONObject object = JSON.parseObject(response.getBody());
		logger.info("<getUserInfoByToken>--" + object.toString());
		if (object.get("errcode") != null) {
			return null;
		}
		return object;
	}
	
	
	
	/**
	 * QQ 采用网站应用 oauth信息
	 */
	public static String TencentQQAppId = "101333546";
	public static String TencentQQAppSecret = "781238e4b44354b5474489b6c263683a";
	
	/**
	 * sina微博接口
	 */
	public static String SinaWeiboAppId = "3753832253";
	public static String SinaWeiboAppSecret = "27ae9c0841c1f684479f0b554fe3bdbb";
	
	/**
	 * callback(
		{
		client_id: "101333546",
		openid: "035D77CE8244C1F64656AFE19816C890"
		}
		)
	 * 获取QQtoken
	 * @param code
	 * @return
	 */
	public static JSONObject getUserQQOpenId(String accessToken) {
		HttpResponse<String> response = null;
		try {
			response = Unirest.get("https://graph.qq.com/oauth2.0/me?access_token="+accessToken).asString();
			if(response != null){
				int start = response.getBody().indexOf("{");
				int end = response.getBody().lastIndexOf("}") + 1;
				String jsonString = response.getBody().substring(start, end);
				JSONObject jsonObject = JSON.parseObject(jsonString);
				logger.info("<getUserQQOpenId>--" + jsonObject.toString());
				if (jsonObject.get("errcode") != null) {
					return null;
				}
				return jsonObject;
			}
		} catch (Exception e) {
			logger.error("Error getUserQQOpenId-exception:"+e.toString());
		}
		return null;
	}

	/**
	 * https://graph.qq.com/user/get_user_info?access_token=%s&oauth_consumer_key=YOUR_APP_ID&openid=YOUR_OPENID
	 * 获取QQ用户基本信息
	 * @param token
	 * @param openid
	 * @return
	 */
	public static JSONObject getQQUserInfoByToken(String token, String openId) {
		String url = String.format("https://graph.qq.com/user/get_user_info?access_token=%s&oauth_consumer_key=%s&openid=%s",token,SnsApi.TencentQQAppId,openId);
		logger.info("<getQQUserInfoByToken>--url" + url);
		HttpResponse<JsonNode> response = null;
		try {
			response = Unirest.get(url).asJson();
		} catch (Exception e) {
			logger.error("Error getQQUserInfoByToken-exception:"+e.toString());
		}
		if (response == null || response.getStatus() != 200) {
			return null;
		}

		JSONObject jsonObject = JSON.parseObject(response.getBody().toString());
		logger.info("<getQQUserInfoByToken>--" + jsonObject.toString());
		if (jsonObject.get("errcode") != null) {
			return null;
		}
		return jsonObject;
	}
	
	
	/**
	 * ?client_id=xxx&client_secret=xxx&grant_type=authorization_code&
	 * {
       "access_token": "ACCESS_TOKEN",
       "expires_in": 1234,
       "remind_in":"798114",
       "uid":"12341234"
 	}
	回调地址：
	http://h5.xiaozhutv.com/app.html
	http://h5.xiaozhutv.com/index.html
	 */
	public static JSONObject getSinaWeiboId(String code,String redirectUrl) {
		HttpResponse<JsonNode> response = null;
		try {
			response = Unirest.post(String.format("https://api.weibo.com/oauth2/access_token?client_id=%s&client_secret=%s&grant_type=authorization_code&code=%s&redirect_uri=%s",
					SinaWeiboAppId,SinaWeiboAppSecret,code,redirectUrl)).asJson();
			if(response != null){
				JSONObject jsonObject = JSON.parseObject(response.getBody().toString());
				logger.info("<getSinaWeiboId>--" + jsonObject.toString());
				if(!jsonObject.containsKey("error_code")){
					return jsonObject;
				}
			}
		} catch (Exception e) {
			logger.error("Error getSinaWeiboId-exception:"+e.toString());
		}
		return null;
	}


	
	/**
	 * ?access_token=xxx
	 * {
    			"uid":"3456676543"
	   }
	 */
	public static String SinaWeiboSnsOpenIdApi = "https://api.weibo.com/2/account/get_uid.json";
	
	/**
	 * https://api.weibo.com/2/users/show.json
	 * 获取sina用户基本信息
	 * @param token
	 * @param openid
	 * @return
	 */
	public static JSONObject getSinaWeiboUserInfoByToken(String token, String uid) {
		if(token == null||uid == null){
			return null;
		}
		String url = String.format("https://api.weibo.com/2/users/show.json?access_token=%s&uid=%s",token,uid);
		logger.info("<getSinaWeiboUserInfoByToken>--url" + url);
		HttpResponse<JsonNode> response = null;
		try {
			response = Unirest.get(url).asJson();
		} catch (Exception e) {
			logger.error("Error getSinaWeiboUserInfoByToken-exception:"+e.toString());
		}
		if (response == null || response.getStatus() != 200) {
			return null;
		}

		JSONObject jsonObject = JSON.parseObject(response.getBody().toString());
		logger.info("<getSinaWeiboUserInfoByToken>--" + jsonObject.toString());
		if (jsonObject.get("errcode") != null) {
			return null;
		}
		return jsonObject;
	}
	
}
