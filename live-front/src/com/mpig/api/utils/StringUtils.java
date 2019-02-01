package com.mpig.api.utils;

import java.util.Random;
import org.apache.commons.lang.StringEscapeUtils;

public class StringUtils {

	/**
	 * 获取随机数
	 * 
	 * @param min
	 *            开始数据
	 * @param max
	 *            结束数据
	 * @return
	 */
	public static Integer getRandom(Integer min, Integer max) {
		Random _random = new Random();
		Integer _integer = 0;
		if (min >= max) {
			// 参数规则有误
		} else {
			if (min == 0) {
				_integer = _random.nextInt(max);
			} else {
				_integer = _random.nextInt(max) % (max - min + 1) + min;
			}
		}
		return _integer;
	}

	/**
	 * IP String转Long
	 * 
	 * @param strIp
	 *            58.50.24.78
	 * @return
	 */
	public static Long ipToLong(String strIp) {
		long[] ip = new long[4];
		// 先找到IP地址字符串中.的位置
		int position1 = strIp.indexOf(".");
		int position2 = strIp.indexOf(".", position1 + 1);
		int position3 = strIp.indexOf(".", position2 + 1);
		// 将每个.之间的字符串转换成整型
		ip[0] = Long.parseLong(strIp.substring(0, position1));
		ip[1] = Long.parseLong(strIp.substring(position1 + 1, position2));
		ip[2] = Long.parseLong(strIp.substring(position2 + 1, position3));
		ip[3] = Long.parseLong(strIp.substring(position3 + 1));
		return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
	}

	/**
	 * IP Long转String
	 * 
	 * @param longIp
	 * @return
	 */
	public static String longToIP(Long longIp) {
		StringBuffer sb = new StringBuffer("");
		// 直接右移24位
		sb.append(String.valueOf((longIp >>> 24)));
		sb.append(".");
		// 将高8位置0，然后右移16位
		sb.append(String.valueOf((longIp & 0x00FFFFFF) >>> 16));
		sb.append(".");
		// 将高16位置0，然后右移8位
		sb.append(String.valueOf((longIp & 0x0000FFFF) >>> 8));
		sb.append(".");
		// 将高24位置0
		sb.append(String.valueOf((longIp & 0x000000FF)));
		return sb.toString();
	}

	/**
	 * xss过滤
	 * 
	 * @param str
	 * @return
	 */
	public static String xssClean(String str) {
		String string = StringEscapeUtils.escapeHtml(str);
		string = StringEscapeUtils.escapeJavaScript(string);
		string = StringEscapeUtils.escapeSql(string);
		return string;
	}

	public static String getSqlString(String sqlString, String dbName, Integer uid) {
		return String.format(sqlString, getSqlString(dbName,uid));
	}

	public static String getSqlString(String dbName, Integer uid) {
		int sufix = uid % 100;
		return sufix < 10 ? dbName + "0" + sufix : dbName + sufix;
	}

	public static String getPay(String paytype) {
		if ("alipay".equalsIgnoreCase(paytype) || "iappay_alipay".equalsIgnoreCase(paytype) || "iappayAlipay".equalsIgnoreCase(paytype)) {
			return "支付宝";
		} else if ("weixin".equalsIgnoreCase(paytype) || "iappay_weixin".equalsIgnoreCase(paytype) || "iappayWeixin".equalsIgnoreCase(paytype)|| "reapal_weixin".equalsIgnoreCase(paytype)) {
			return "微信充值";
		} else if ("apple".equalsIgnoreCase(paytype)) {
			return "苹果";
		} else if ("yingyongbao".equalsIgnoreCase(paytype)) {
			return "米大师";
		} else {
			return "未知";
		}
	}

	/**
	 * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为2,英文字符长度为1
	 * 
	 * @param String
	 *            s 需要得到长度的字符串
	 * @return int 得到的字符串长度
	 */
	public static int length(String s) {
		if (s == null)
			return 0;
		char[] c = s.toCharArray();
		int len = 0;
		for (int i = 0; i < c.length; i++) {
			len++;
			if (!isLetter(c[i])) {
				len++;
			}
		}
		return len;
	}

	public static boolean isLetter(char c) {
		int k = 0x80;
		return c / k == 0 ? true : false;
	}

	public static String cutLength(String value, int num) {
		StringBuffer sb = new StringBuffer();
		int charlen = 0;
		int strLen = value.length();

		try {
			for (int i = 0; i < strLen; i++) {
				String temp = value.substring(i, i + 1);
				charlen += temp.getBytes("GBK").length;
				if (charlen <= num) {
					sb.append(temp);
				} else {
					break;
				}

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return sb.toString().length() < strLen ? sb.toString() + "..." : sb.toString();
	}
}
