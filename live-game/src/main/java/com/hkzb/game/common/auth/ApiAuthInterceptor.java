package com.hkzb.game.common.auth;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.hkzb.game.common.utils.AESCipher;
import com.hkzb.game.common.utils.Constants;
import com.hkzb.game.common.utils.DateUtil;
import com.hkzb.game.common.utils.JsonUtil;
import com.hkzb.game.common.utils.LogUtils;
import com.hkzb.game.common.utils.MD5;
import com.hkzb.game.common.utils.RedisShardClient;
import com.hkzb.game.common.utils.ResponseUtils;
import com.hkzb.game.common.utils.ResultUtil;

public class ApiAuthInterceptor extends HandlerInterceptorAdapter {
	
	@Autowired
	private RedisShardClient redisShardClient;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		try {
			String contextPath = request.getContextPath();
			String reqUrl = request.getRequestURI();
			String baseUrl = StringUtils.removeStart(reqUrl, contextPath);

			if (needIgnore(baseUrl)) {
				return true;
			}

			if (!(handler instanceof HandlerMethod)) {
				return false;
			}
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			NoAuth interceptor = handlerMethod.getMethodAnnotation(NoAuth.class);
			if (interceptor != null) {
				return true;
			}
			SecurityCertificate securityCertificate = handlerMethod.getMethodAnnotation(SecurityCertificate.class);
			if (securityCertificate != null) {
				String ip = getRemoteHost(request);
				if(StringUtils.isBlank(ip)) {
					LogUtils.debug(getClass(), "权限验证失败:ip为空");
					String result = JsonUtil.toJson(ResultUtil.fail("权限验证失败"));
					ResponseUtils.renderHtml(response, result);
					return false;
				}
				List<String> list = redisShardClient.lrange(Constants.gameTrustedHost, 0, -1);
				if(list==null||list.size()==0) {
					LogUtils.debug(getClass(), "权限验证失败:可信IP列表为空");
					String result = JsonUtil.toJson(ResultUtil.fail("权限验证失败"));
					ResponseUtils.renderHtml(response, result);
					return false;
				}
				return list.contains(ip);
			}

			String timestamp = request.getHeader("timestamp");
			if(StringUtils.isBlank(timestamp)) {
				timestamp = request.getParameter("timestamp");
			}
			long now = DateUtil.nowTime();
			// 5分钟 * 1分钟 * 60秒 =300秒 = 300秒 = 300000毫秒
			if (StringUtils.isBlank(timestamp)||!NumberUtils.isNumber(timestamp) || (Math.abs(now - Long.parseLong(timestamp)) > 1000 * 60 * 5)) {
				LogUtils.debug(getClass(), "权限验证失败:时间戳不正确或超过5分钟或timestamp为空");
				String result = JsonUtil.toJson(ResultUtil.fail("权限验证失败"));
				ResponseUtils.renderHtml(response, result);
				return false;
			}
			String base64Token = request.getHeader("token");
			if(StringUtils.isBlank(base64Token)) {
				base64Token = java.net.URLDecoder.decode(request.getParameter("token"),"utf-8").replaceAll(" ", "+");
			}
			if(StringUtils.isBlank(base64Token)) {
				LogUtils.debug(getClass(), "权限验证失败:token为空");
				String result = JsonUtil.toJson(ResultUtil.fail("权限验证失败"));
				ResponseUtils.renderHtml(response, result);
				return false;
			}
			String str = AESCipher.aesDecryptString(base64Token);
			if(StringUtils.isBlank(str)) {
				LogUtils.debug(getClass(), "权限验证失败:解密token为空");
				String result = JsonUtil.toJson(ResultUtil.fail("权限验证失败"));
				ResponseUtils.renderHtml(response, result);
				return false;
			}
			if (str.split("_").length != 2) {
				LogUtils.debug(getClass(), "权限验证失败:解密token格式不正确");
				String result = JsonUtil.toJson(ResultUtil.fail("权限验证失败"));
				ResponseUtils.renderHtml(response, result);
				return false;
			}
			if (!MD5.MD(timestamp).equals(str.split("_")[1])) {
				LogUtils.debug(getClass(), "权限验证失败:时间戳校验失败");
				String result = JsonUtil.toJson(ResultUtil.fail("权限验证失败"));
				ResponseUtils.renderHtml(response, result);
				return false;
			}
			String sign = request.getHeader("sign");
			if(StringUtils.isBlank(sign)) {
				sign = request.getParameter("sign");
			}
			if (StringUtils.isBlank(sign)) {
				LogUtils.debug(getClass(), "权限验证失败:sign为空");
				String result = JsonUtil.toJson(ResultUtil.fail("sign为空"));
				ResponseUtils.renderHtml(response, result);
				return false;
			}
			String serverSign = MD5.MD(str.split("_")[0] + baseUrl + timestamp);
			if (!sign.equals(serverSign)) {
				LogUtils.debug(getClass(), "权限验证失败:sign校验失败");
				String result = JsonUtil.toJson(ResultUtil.fail("sign验证失败"));
				ResponseUtils.renderHtml(response, result);
				return false;
			}
			return true;
		} catch (Exception e) {
			LogUtils.error(getClass(), "系统异常：", e);
			String result = JsonUtil.toJson(ResultUtil.fail("权限验证异常"));
			ResponseUtils.renderHtml(response, result);
			return false;
		}
	}

	private boolean needIgnore(String baseUrl) {
		if (baseUrl.endsWith(".html") || baseUrl.endsWith(".htm") || baseUrl.endsWith(".js")|| baseUrl.endsWith(".json") || baseUrl.endsWith(".css")
				|| baseUrl.endsWith(".jpg") || baseUrl.endsWith(".jpeg") || baseUrl.endsWith(".gif")
				|| baseUrl.endsWith(".font")) {
			return true;
		}
		return false;
	}
	
	private String getRemoteHost(javax.servlet.http.HttpServletRequest request){
	    String ip = request.getHeader("x-forwarded-for");
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getHeader("Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getRemoteAddr();
	    }
	    return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
	}
}