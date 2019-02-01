package com.mpig.api.utils;

import java.security.MessageDigest;

public class MD5Encrypt {

	public static String byte2hex(byte[] b) {
		StringBuffer hs = new StringBuffer();
		String stmp = "";
		for (byte element : b) {
			stmp = java.lang.Integer.toHexString(element & 0xFF);
			if (stmp.length() == 1)
				hs.append("0").append(stmp);
			else hs.append(stmp);
		}
		return hs.toString();
	}

	/**
	 * �����ַ�
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String str) throws Exception {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			return byte2hex(md5.digest(str.getBytes()));
		}
		catch (Exception e) {
			throw e;
		}

	}

	public static void main(String[] args) {
		try {
			String str1 = encrypt("123456");
			System.out.println(str1);

			System.out.println();

			String str2 = encrypt("00-1B-38-A7-38-1F");
			System.out.println(str2);

			System.out.println(str1.equals(str2));
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}

}
