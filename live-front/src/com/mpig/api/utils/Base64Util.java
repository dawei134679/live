package com.mpig.api.utils;

import org.apache.commons.codec.binary.Base64;

public class Base64Util {

	/** 
	 * base64编码 
	 *  
	 * @param s 
	 * @param key 
	 * @return 
	 */  
	public static String encode(String s, String key) {  
	    return base64Encode(xorWithKey(s.getBytes(), key.getBytes()));  
	}
	
	private static String base64Encode(byte[] bytes) {  
	    byte[] encodeBase64 = Base64.encodeBase64(bytes);  
	    return new String(encodeBase64);  
	}  
	
	public static byte[] xorWithKey(byte[] a, byte[] key) {  
	    byte[] out = new byte[a.length];  
	    for (int i = 0; i < a.length; i++) {  
	        out[i] = (byte) (a[i] ^ key[i % key.length]);  
	    }  
	    return out;  
	}  
}
