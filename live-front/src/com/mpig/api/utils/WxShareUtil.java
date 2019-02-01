package com.mpig.api.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;

import com.alibaba.fastjson.JSON;
import com.mpig.api.redis.service.OtherRedisService;

public class WxShareUtil {
	
	public static final String appid = "wx354d8b9047f3585f";
	public static final String secret = "96ad8198949b3ac2b6d289f083eb1893";
	
	public static Map<String, Object> getSignature(String shareurl) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		Map<String, Object> model = new HashMap<String, Object>();
		// 查询微信accesstoken的有效期
		String jsapiticket = OtherRedisService.getInstance().getJsapiTicket("jsapi_ticket");
		if(jsapiticket == null){
			String accesstoken = OtherRedisService.getInstance().getAccessToken("access_token");
			if( accesstoken == null){
				Map<String, Object> map = getAccessToken();
				accesstoken = map.get("access_token").toString();
				int expires_in = Integer.parseInt(map.get("expires_in").toString());
				OtherRedisService.getInstance().setAccessToken("access_token", accesstoken,expires_in);
				Map<String, Object> jsapiMap = getJspApiTicket(accesstoken);
				jsapiticket = jsapiMap.get("ticket").toString();
				int jsapi_expires_in = Integer.parseInt(jsapiMap.get("expires_in").toString()); //errmsg : ok ; errcode : 0
				OtherRedisService.getInstance().setJsapiTicket("jsapi_ticket",jsapiticket, jsapi_expires_in);
			
				model = getWxSign(jsapiticket, shareurl); //返回本结果给客户端
			}else{
				Map<String, Object> jsapiMap = getJspApiTicket(accesstoken);
				jsapiticket = jsapiMap.get("ticket").toString();
				int jsapi_expires_in = Integer.parseInt(jsapiMap.get("expires_in").toString()); //errmsg : ok ; errcode : 0
				OtherRedisService.getInstance().setJsapiTicket("jsapi_ticket",jsapiticket, jsapi_expires_in);
			
				model =getWxSign(jsapiticket, shareurl); //返回本结果给客户端
			}
		}else{
			model = getWxSign(jsapiticket, shareurl); //返回本结果给客户端
		}
		return model;
	}
	
	/**
	 * 获取accesstoken
	 * @return
	 */
	public static Map<String,Object> getAccessToken(){
		String url = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s", appid,secret);
		Map<String, Object> accesstokenmap = gethttps(url);
		return accesstokenmap; 
	}
	
	/**
	 * 获取jspApiticket
	 * @param accesstoken
	 * @return
	 */
	public static Map<String,Object> getJspApiTicket(String accesstoken){
		String url = String.format("https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi", accesstoken);
		Map<String, Object> jsapiticketmap = gethttps(url);
		return jsapiticketmap; 
	}
	
	/**
	 * 获取JSAPI调用签名
	 * @param jsapiticket js API 调用票据
	 * @param shareurl 需要分享的连接
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static Map<String, Object> getWxSign(String jsapiticket, String shareurl) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		Map<String, Object> model = new HashMap<String, Object>();
		String timestamp = Long.toString(System.currentTimeMillis() / 1000);
		String noncestr = RandomStringUtils.random(16, true, true);
		String string1 = "jsapi_ticket=" + jsapiticket + "&noncestr="+ noncestr + "&timestamp=" + timestamp + "&url=" + shareurl;
		System.out.println("wxShare signature  :  "+string1);
		MessageDigest crypt = MessageDigest.getInstance("SHA-1");
		crypt.reset();
		crypt.update(string1.getBytes("UTF-8"));
		String signature = byteToHex(crypt.digest());
		
//		List<String> jsApiList = jsApiList();
		System.out.println("========2========appid:"+appid+"===timestamp:"+timestamp+"==noncestr:"+noncestr+"=====signature:"+signature);
		model.put("appId", appid);
//		model.put("debug", false);
		model.put("timestamp", timestamp);
		model.put("nonceStr", noncestr);
		model.put("signature", signature);
//		model.put("jsApiList", jsApiList);
		return model;
	}

	 /**
	  * sha1加密后格式化
	  * @param hash
	  * @return
	  */
	public static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}
	
	/**
	 * 封装JSAPI列表
	 * @return List
	 */
	public static List<String> jsApiList(){
		List<String> jsApiList = new ArrayList<String>();
		jsApiList.add("onMenuShareTimeline");
		jsApiList.add("onMenuShareAppMessage");
		jsApiList.add("onMenuShareQQ");
		jsApiList.add("onMenuShareWeibo");
		jsApiList.add("startRecord");
		jsApiList.add("stopRecord");
		jsApiList.add("onVoiceRecordEnd");
		jsApiList.add("playVoice");
		jsApiList.add("pauseVoice");
		jsApiList.add("stopVoice");
		jsApiList.add("onVoicePlayEnd");
		jsApiList.add("uploadVoice");
		jsApiList.add("downloadVoice");
		jsApiList.add("chooseImage");
		jsApiList.add("previewImage");
		jsApiList.add("uploadImage");
		jsApiList.add("downloadImage");
		jsApiList.add("translateVoice");
		jsApiList.add("getNetworkType");
		jsApiList.add("openLocation");
		jsApiList.add("getLocation");
		jsApiList.add("hideOptionMenu");
		jsApiList.add("showOptionMenu");
		jsApiList.add("hideMenuItems");
		jsApiList.add("showMenuItems");
		jsApiList.add("hideAllNonBaseMenuItem");
		jsApiList.add("showAllNonBaseMenuItem");
		jsApiList.add("closeWindow");
		jsApiList.add("scanQRCode");
		jsApiList.add("chooseWXPay");
		jsApiList.add("openProductSpecificView");
		jsApiList.add("addCard");
		jsApiList.add("chooseCard");
		jsApiList.add("openCard");
		return jsApiList;
	}

	/**
	 *  模拟HTTPS请求
	 * @param url
	 * @return
	 */
	public static Map<String, Object> gethttps(String url) {
		Map<String, Object> model = new HashMap<String, Object>();
		try {
			DefaultHttpClient httpclient = new DefaultHttpClient();
			SSLContext ctx = SSLContext.getInstance("SSL");
			X509TrustManager tm = new X509TrustManager() {

				public void checkClientTrusted(X509Certificate[] xcs,
						String string) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] xcs,
						String string) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public boolean isClientTrusted(X509Certificate[] arg0) {
					return false;
				}

				public boolean isServerTrusted(X509Certificate[] arg0) {
					return false;
				}
			};
			ctx.init(null, new TrustManager[] { tm }, null);
			SSLSocketFactory ssf = new SSLSocketFactory(ctx);

			ClientConnectionManager ccm = httpclient.getConnectionManager();
			SchemeRegistry sr = ccm.getSchemeRegistry();
			sr.register(new Scheme("https", 443, ssf));

			HttpGet httpget = new HttpGet(url);
			HttpParams params = httpclient.getParams();
			httpget.setParams(params);
			ResponseHandler responseHandler = new BasicResponseHandler();
			String responseBody;
			responseBody = httpclient.execute(httpget, responseHandler)
					.toString();
			model = JSON.parseObject(responseBody);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();

		}
		return model;
	}
	
}
