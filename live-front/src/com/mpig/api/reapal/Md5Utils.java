package com.mpig.api.reapal;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Md5Utils {

	/**
	 * 把数组所有元素，按字母排序，然后按照“参数=参数值”的模式用“&”字符拼接成字符串
	 * 
	 * @param params
	 *            需要签名的参数
	 * @return 签名的字符串
	 */
	public static StringBuilder CreateLinkString(Map<String, String> params) {
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		StringBuilder prestr = new StringBuilder();
		String key = "";
		String value = "";
		for (int i = 0; i < keys.size(); i++) {
			key = (String) keys.get(i);
			value = (String) params.get(key);
			if ("".equals(value) || value == null || key.equalsIgnoreCase("sign")
					|| key.equalsIgnoreCase("sign_type")) {
				continue;
			}
			prestr.append(key).append("=").append(value).append("&");
		}
		return prestr.deleteCharAt(prestr.length() - 1);
	}

	/**
	 * 生成MD5签名
	 * 
	 * @param sArray
	 *            需要签名的参数
	 * @param key
	 *            密钥
	 * @return 签名结果
	 */
	public static String BuildMysign(Map<String, String> sArray, String key) {
		if (sArray != null && sArray.size() > 0) {
			StringBuilder prestr = CreateLinkString(sArray);
			//System.out.println("********************待签名字符串为：" + prestr.toString() + key);
			return md5(prestr.toString() + key, "UTF-8");
		}
		return null;
	}

	/**
	 * Used building output as Hex
	 */
	private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };

	public static String md5(String text, String charset) {
		MessageDigest msgDigest = null;

		try {
			msgDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("System doesn't support MD5 algorithm.");
		}

		try {
			msgDigest.update(text.getBytes(charset));

		} catch (UnsupportedEncodingException e) {

			throw new IllegalStateException("System doesn't support your  EncodingException.");

		}

		byte[] bytes = msgDigest.digest();

		String md5Str = new String(encodeHex(bytes));

		return md5Str;
	}

	public static char[] encodeHex(byte[] data) {

		int l = data.length;

		char[] out = new char[l << 1];

		// two characters form the hex value.
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
			out[j++] = DIGITS[0x0F & data[i]];
		}

		return out;
	}

	public static void main(String[] args) {

		String str = "_input_charset=GBK&batchBizid=100000000211414&batchCurrnum=1450838462443&batchDate=20151223&batchVersion=00d6ffe6173ca91dd312c2fabfe473dggcd53f5cd4eccaf33acgd6160a837240ae";

		String ss = Md5Utils.md5(str, "GBK");

		System.out.println(ss);
	}

}
