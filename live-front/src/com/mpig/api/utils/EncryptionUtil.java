package com.mpig.api.utils;

import java.security.MessageDigest;
import java.util.Date;
import java.util.HashMap;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Charsets;
import com.mpig.api.perf.vo.CostRequestLog;

public class EncryptionUtil {
	public static void main(String[] args) throws Exception{
		CostRequestLog createWith = CostRequestLog.createWith("user/index", 200, "ddddddd", new Date().toString());
		String string = JSON.toJSON(createWith).toString();
		System.err.println(string);
		System.err.println(new Boolean(true).toString());
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			Integer ddd = 0;
			for(int i = 0;i<10000;i++){
				map.put("opentime",ddd);
				System.err.println(map.get("opentime").getClass());
				map.put("opentime1", System.currentTimeMillis()/1000 - ((Integer)map.get("opentime")).longValue());
				System.err.println(map.get("opentime"));
				System.err.println(map.get("opentime1"));
			}
			
		} catch (Exception e) {
			System.err.println(e);
		}
		
		String responseWeixinPay = "<xml><return_code><![CDATA[%s]]></return_code><return_msg><![CDATA[%s]]></return_msg></xml>";
		String format = String.format(responseWeixinPay, "FAIL","DDDDDDD");
		System.err.println(format);
		String SecretKey = "vCwG1chKYFYjs0YS";
		String content = "1234567890";
		content = SecretKey+content;
		String md5 = EncryptUtils.md5ByCharset(content, Charsets.UTF_8);
		System.err.println(md5);
	}
	
	// 对字符串进行md5加密
	public static String md5(String str) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());

			byte[] b = md.digest();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < b.length; i++) {
				int v = (int) b[i];
				v = v < 0 ? 0x100 + v : v;
				String cc = Integer.toHexString(v);
				if (cc.length() == 1)
					sb.append('0');
				sb.append(cc);
			}

			return sb.toString();
		} catch (Exception e) {
		}
		return "";
	}
	
	// 对字符串进行sha256加密
	public static String sha256(String str) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(str.getBytes());

			byte[] b = md.digest();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < b.length; i++) {
				int v = (int) b[i];
				v = v < 0 ? 0x100 + v : v;
				String cc = Integer.toHexString(v);
				if (cc.length() == 1)
					sb.append('0');
				sb.append(cc);
			}

			return sb.toString();
		} catch (Exception e) {
		}
		return "";
	}
	// 对字符串进行sha1加密
	public static String sha1(String str) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(str.getBytes());

			byte[] b = md.digest();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < b.length; i++) {
				int v = (int) b[i];
				v = v < 0 ? 0x100 + v : v;
				String cc = Integer.toHexString(v);
				if (cc.length() == 1)
					sb.append('0');
				sb.append(cc);
			}

			return sb.toString();
		} catch (Exception e) {
		}
		return "";
	}
	
}
