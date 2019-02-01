package com.mpig.api.controller;

import java.net.URL;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mpig.api.service.ILiveService;
import com.mpig.api.utils.Constant;
import com.qiniu.pili.utils.HMac;
import com.qiniu.pili.utils.UrlSafeBase64;

@Controller
@Scope("prototype")
@RequestMapping("/qiNiuNotice")
public class QiNiuNoticeController extends BaseController {

	private static Logger log = Logger.getLogger(QiNiuNoticeController.class);
	@Resource
	private ILiveService liveService;
	
	@RequestMapping(value = "/open")
	public void open(HttpServletRequest request) {
		try {
			String authorization = request.getHeader("authorization");
			String method = "POST";
			String contentType = request.getHeader("content-type");

			String remoteIp = request.getParameter("remoteIp");
			String status = request.getParameter("status");
			String timestamp = request.getParameter("timestamp");
			String title = request.getParameter("title");

			log.debug(String.format("七牛开播通知参数：remoteIp=%s,status=%s,timestamp=%s,title=%s,", remoteIp, status,timestamp, title));

			StringBuilder params = new StringBuilder();
			params.append("remoteIp=");
			params.append(remoteIp);
			params.append("&");
			params.append("status=open");
			params.append("&");
			params.append("timestamp=");
			params.append(timestamp);
			params.append("&");
			params.append("title=");
			params.append(title);
			URL url = new URL(request.getRequestURL().toString());
			String sign = signRequest(url, method, params.toString().getBytes("UTF-8"), contentType);
			if (!sign.equals(authorization)) {
				log.debug("验证签名失败");
				return;
			}
			
		} catch (Exception e) {
			log.error("处理七牛开播通知异常", e);
		}
	}

	@RequestMapping(value = "/close")
	public void close(HttpServletRequest request) {
		try {
			String authorization = request.getHeader("authorization");
			String method = "POST";
			String contentType = request.getHeader("content-type");

			String remoteIp = request.getParameter("remoteIp");
			String status = request.getParameter("status");
			String timestamp = request.getParameter("timestamp");
			String title = request.getParameter("title");

			log.debug(String.format("七牛断流通知参数：remoteIp=%s,status=%s,timestamp=%s,title=%s,", remoteIp, status, timestamp, title));

			StringBuilder params = new StringBuilder();
			params.append("remoteIp=");
			params.append(remoteIp);
			params.append("&");
			params.append("status=close");
			params.append("&");
			params.append("timestamp=");
			params.append(timestamp);
			params.append("&");
			params.append("title=");
			params.append(title);
			URL url = new URL(request.getRequestURL().toString());
			String sign = signRequest(url, method, params.toString().getBytes("UTF-8"), contentType);
			if (!sign.equals(authorization)) {
				log.debug("验证签名失败");
				return;
			}
			int uid = Integer.valueOf(title);
			liveService.closeLive(uid, returnModel);
		} catch (Exception e) {
			log.error("处理七牛断流通知异常", e);
		}
	}

	/**
	 * 七牛签名算法
	 * 
	 * @param url
	 * @param method
	 * @param body
	 * @param contentType
	 * @return
	 * @throws Exception
	 */
	private String signRequest(URL url, String method, byte[] body, String contentType) throws Exception {
		StringBuilder sb = new StringBuilder();

		sb.append(String.format("%s %s", method, url.getPath()));
		if (url.getQuery() != null) {
			sb.append(String.format("?%s", url.getQuery()));
		}

		sb.append(String.format("\nHost: %s", url.getHost()));
		if (url.getPort() > 0) {
			sb.append(String.format(":%d", url.getPort()));
		}

		if (contentType != null) {
			sb.append(String.format("\nContent-Type: %s", contentType));
		}
		sb.append("\n\n");
		if (incBody(body, contentType)) {
			sb.append(new String(body));
		}

		byte[] sum = HMac.HmacSHA1Encrypt(sb.toString(), Constant.qn_secretKey);
		String sign = UrlSafeBase64.encodeToString(sum);
		return "Qiniu " + Constant.qn_accessKey + ":" + sign;
	}

	private boolean incBody(byte[] body, String contentType) {
		int maxContentLength = 1024 * 1024;
		boolean typeOK = contentType != null && !contentType.equals("application/octet-stream");
		boolean lengthOK = body != null && body.length > 0 && body.length < maxContentLength;
		return typeOK && lengthOK;
	}
}