package com.mpig.api.controller;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mpig.api.model.ReturnModel;
import com.mpig.api.redis.service.OtherRedisService;
import com.mpig.api.utils.CodeContant;

@Controller
@Scope("prototype")
@RequestMapping("/maiyaShare")
public class MaiYaShareController extends BaseController  {
	
	private static final Logger logger = Logger.getLogger(MaiYaShareController.class);

	private static String appid = "wxa5998293482fbe27";
	private static String secret ="a5d4c503e0c1ff8bda8e8f0dc57fdbd9";
	
	@RequestMapping(value="/getWebChatSign",method=RequestMethod.GET)
	@ResponseBody
	public ReturnModel getSign(HttpServletRequest request) {
		try {
			String url = request.getParameter("url");
			if(StringUtils.isEmpty(url)) {
				returnModel.setCode(CodeContant.CONAUTHEMPTY);
				returnModel.setMessage("URL不能为空");
				return returnModel;
			}
			String jsapiTicket = OtherRedisService.getInstance().getWebchatJsapiTicket();
			if(StringUtils.isEmpty(jsapiTicket)) {
				String accessToken = OtherRedisService.getInstance().getWebChatAccessToken();
				if(StringUtils.isBlank(accessToken)) {
					String accessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";
					HttpResponse<JsonNode> response = Unirest.get(accessTokenUrl).queryString("appid", appid).queryString("secret",secret).asJson();
					JSONObject json = response.getBody().getObject();
					
					logger.info("accessToken接口返回："+json.toString());
					
					if((json.has("errcode")&&json.getInt("errcode")!=0)||!json.has("access_token")) {
						returnModel.setCode(CodeContant.CONSYSTEMERR);
						returnModel.setMessage("系统异常");
						return returnModel;
					}
					accessToken = json.getString("access_token");
					if(StringUtils.isBlank(accessToken)) {
						returnModel.setCode(CodeContant.CONSYSTEMERR);
						returnModel.setMessage("系统异常");
						return returnModel;
					}
					OtherRedisService.getInstance().setWebChatAccessToken(accessToken, 7200-60);
				}
				String  jsapiTicketUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi";
				HttpResponse<JsonNode> response = Unirest.get(jsapiTicketUrl).queryString("access_token", accessToken).asJson();
				JSONObject json = response.getBody().getObject();
				
				logger.info("jsapiTicket接口返回："+json.toString());
				
				if(json.has("errcode")&&json.getInt("errcode")!=0) {
					returnModel.setCode(CodeContant.CONSYSTEMERR);
					returnModel.setMessage("系统异常");
					return returnModel;
				}
				jsapiTicket = json.getString("ticket");
				if(StringUtils.isBlank(jsapiTicket)) {
					returnModel.setCode(CodeContant.CONSYSTEMERR);
					returnModel.setMessage("系统异常");
					return returnModel;
				}
				OtherRedisService.getInstance().setWebchatJsapiTicket(jsapiTicket, 7200-60);
			}
			Map<String,String> map = sign(jsapiTicket, url);
			Map<String,String> dataMap = new HashMap<String,String>();
			dataMap.put("appid", appid);
			dataMap.put("nonceStr", map.get("nonceStr"));
			dataMap.put("timestamp", map.get("timestamp"));
			dataMap.put("signature", map.get("signature"));
			returnModel.setData(dataMap);
			return returnModel;
		}catch (Exception e) {
			logger.error("获取微信公众号签名异常",e);
			returnModel.setCode(CodeContant.CONSYSTEMERR);
			returnModel.setMessage("系统异常");
			return returnModel;
		}
	}
	
	/**
	 * 获取微信公众号签名信息
	 * @param jsapi_ticket 票据
	 * @param url 网页地址
	 * @return 签名后的map信息
	 */
    private static Map<String, String> sign(String jsapi_ticket, String url) {
        try{
           String nonce_str = create_nonce_str();
           String timestamp = create_timestamp();
        	//注意这里参数名必须全部小写，且必须有序
            StringBuilder sb = new StringBuilder();
            sb.append("jsapi_ticket=");
            sb.append(jsapi_ticket);
            sb.append("&noncestr=");
            sb.append(nonce_str);
            sb.append("&timestamp=");
            sb.append(timestamp);
            sb.append("&url=");
            sb.append(url);
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(sb.toString().getBytes("UTF-8"));
            
            Map<String, String> ret = new HashMap<String, String>();
            ret.put("url", url);
            ret.put("jsapi_ticket", jsapi_ticket);
            ret.put("nonceStr", nonce_str);
            ret.put("timestamp", timestamp);
            ret.put("signature", byteToHex(crypt.digest()));
            return ret;
        }
        catch (NoSuchAlgorithmException e){
        	logger.error(e.getMessage(),e);
            return null;
        }
        catch (UnsupportedEncodingException e)  {
        	logger.error(e.getMessage(),e);
        	logger.error(e.getMessage(),e);
            return null;
        }catch (Exception e) {
        	logger.error(e.getMessage(),e);
        	return null;
		}
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash){
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
    
    public static void main(String[] args) throws UnirestException, JSONException {
//    	String accessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";
//		HttpResponse<JsonNode> accessTokenUrlResponse = Unirest.get(accessTokenUrl).queryString("appid", appid).queryString("secret",secret).asJson();
//		JSONObject accessTokenJson = accessTokenUrlResponse.getBody().getObject();
//		System.out.println(accessTokenJson);
//    	String accessToken = "12_GW7Lf5CM7-9uszdqFGRe23zBR4XIwUGZzATApvwKOTmtHe0SjJBdsK2Lc414Hpr7KP5HmwGkAXuWg2x3f4KIxfzr_jDgDMbGZzrNOdxW21jiFCCG3c1nn4rtvKMANEjACAFSJ";
		
//		String  jsapiTicketUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi";
//		HttpResponse<JsonNode> jsapiTickeResponse = Unirest.get(jsapiTicketUrl).queryString("access_token", accessToken).asJson();
//		JSONObject jsapiTicketJson = jsapiTickeResponse.getBody().getObject();
//		System.out.println(jsapiTicketJson);

    	String jsapiTicket = "HoagFKDcsGMVCIY2vOjf9v4MfnRXDbdMXPyyhe5SAEjfL2JmPBOLQ-BdPpwuICEOQTUc55CsCAfa_rOXXWhQ5A";
    	Map<String,String> map = sign(jsapiTicket, "http://api.jieyingkj.com/test.html");
    	System.out.println(map);
    }
}
