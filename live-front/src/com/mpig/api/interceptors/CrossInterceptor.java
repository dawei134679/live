package com.mpig.api.interceptors;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSONObject;
import com.mpig.api.utils.CodeContant;
import com.mpig.api.utils.ParamHandleUtils;
import com.mpig.api.utils.SignUtil;

public class CrossInterceptor extends HandlerInterceptorAdapter{
	
	//访问路径列表
	public final static Map<String,String> uriMap = new HashMap<String, String>();
	static{
		uriMap.put("/peanut/entertainment/exchange", "/peanut/entertainment/exchange");
	}
	
	//访问的渠道名称列表
	public final static Map<String,String> channelMap = new HashMap<String,String>();
	static{
		channelMap.put("HSYL", "HSYL");
	}
	//渠道KEY列表
	public final static Map<String,String> channelKeyMap = new HashMap<String,String>();
	static{
		channelKeyMap.put("HSYL", "1eTRoTx3huj9797ILF");
	}
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		response.setHeader("Access-Control-Allow-Origin", "*");
		super.preHandle(request, response, handler);
		
		String requestURI = request.getRequestURI();
		if(uriMap.containsKey(requestURI)){
			String channel = request.getParameter("channel");
			String sign = request.getParameter("sign");
			if(channelMap.containsKey(channel)){
				Map<String, Object> paramMap = new HashMap<String, Object>();
			    Map<String, String[]> parameterMap = request.getParameterMap();
				for (String key : parameterMap.keySet()) {
					paramMap.put(key, ParamHandleUtils.getParamValue(parameterMap.get(key)));
				}
				try {
					String serverSign = SignUtil.getSign(paramMap,channelKeyMap.get(channel),true);
					if(StringUtils.isBlank(serverSign) || !serverSign.equals(sign)){
						returnMap.put("code",CodeContant.signError);
						returnMap.put("message", "签名错误！");
						writeJson(response, returnMap);
						return false;
					}else{
						return true;
					}
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}else{
				returnMap.put("code", CodeContant.channelError);
				returnMap.put("message", "渠道错误");
				writeJson(response, returnMap);
				return false;
			}
		}else{
			return true;
		}
	}
	
	protected void writeJson(HttpServletResponse resp,Object data) {
		try {
			resp.setHeader("Content-type", "text/plain;charset=UTF-8");
			resp.getOutputStream().write(JSONObject.toJSONString(data).getBytes("UTF-8"));
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
