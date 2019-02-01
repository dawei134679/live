package com.hkzb.game.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	
	/**
	 * 标准  MD5加密
	 * @param text
	 * @param len
	 * @return
	 */
	private static String MD(String text,int len) {
		StringBuffer buf = new StringBuffer("");
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(text.getBytes());
			byte b[] = md.digest();
			int i;
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return len==32?buf.toString():buf.toString().substring(8,len);
	}
	
	/**
	 * 32位加密
	 * @param text
	 * @return
	 */
	public static String MD(String text){
		return MD(text,32);
	}
	
	/**
	 * 16位加密
	 * @param text
	 * @return
	 */
	public static String MD16(String text){
		return MD(text,16);
	}

	public static void main(String[] args) {
		System.out.println(MD("Malianwa2016"));
	}

}
