package com.mpig.api.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 *	互亿无线短信接口
 */
public class SmsIhwxUtil {
	private static String Url = "http://106.ihuyi.cn/webservice/sms.php?method=Submit";
	private static final Logger logger = Logger.getLogger(SmsIhwxUtil.class);

	public static Map<String, String> sendSms(String mobile, String mobilecode) {
		Map<String, String> result = new HashMap<>();
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(Url);

		client.getParams().setContentCharset("GBK");
		method.setRequestHeader("ContentType", "application/x-www-form-urlencoded;charset=GBK");

		String content = "您的验证码【" + mobilecode + "】，该验证码5分钟内有效，请勿泄漏于他人！";
		NameValuePair[] data = { // 提交短信
				new NameValuePair("account", "C78530243"), // 查看用户名是登录用户中心->验证码短信->产品总览->APIID
				new NameValuePair("password", "19c026d1654a4a36d5e6d6ea86ac723c"), // 查看密码请登录用户中心->验证码短信->产品总览->APIKEY
				// new NameValuePair("password", util.StringUtil.MD5Encode("密码")),
				new NameValuePair("mobile", mobile), //
				new NameValuePair("content", content)//
		};
		method.setRequestBody(data);

		try {
			client.executeMethod(method);

			String SubmitResult = method.getResponseBodyAsString();

			logger.debug(SubmitResult);

			Document doc = DocumentHelper.parseText(SubmitResult);
			Element root = doc.getRootElement();

			String code = root.elementText("code");// 2成功
			String msg = root.elementText("msg");// 结果描述
			String smsid = root.elementText("smsid");// 为流水号

			result.put("code", code);
			result.put("msg", msg);
			result.put("smsid", smsid);
		} catch (Exception e) {
			logger.error("发送短信发生异常：", e);
			result.put("code", "500");
			result.put("msg", "发送短信发生异常（本地异常）");
		}
		return result;
	}

	public static void main(String[] args) {
		System.out.println(sendSms("13691556402", "1234"));
	}
}
