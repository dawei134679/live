package com.hkzb.game.common.utils;

import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;

public class EncryptUtils {
	 
	private static Logger logger = Logger.getLogger(EncryptUtils.class.getSimpleName());
   
    /**
     * MD5 加密
     * @param data
     * @return
     */
	public static String md5Encrypt(String data) {		
		char hexDigits[] = {'0', '1', '2', '3', '4',
	                '5', '6', '7', '8', '9',
	                'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = data.getBytes();
            //获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            //使用指定的字节更新摘要
            mdInst.update(btInput);
            //获得密文
            byte[] md = mdInst.digest();
            //把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
        	logger.error("<md5Encrypt->Exception>"+e.getMessage());
            return null;
        }
	}
	
	/**
	 * 生成签名
	 * @param dataMap
	 * @param securityKey
	 * @param charset
	 * @return
	 */
	public static String getFormDataParamMD5(Map<String,String> dataMap,String securityKey,String charset){
        if(dataMap == null) return null;

        Set<String> keySet = dataMap.keySet();
        List<String> keyList = new ArrayList<String>(keySet);
        Collections.sort(keyList);

        StringBuilder toMD5StringBuilder = new StringBuilder();
        for(String key : keyList){
            String value = dataMap.get(key);
            if(value != null && value.length()>0){
                toMD5StringBuilder.append(key+"="+ value+"&");
            }
        }
        try{
            String securityKeyMD5 = md5(securityKey,charset);
            toMD5StringBuilder.append(securityKeyMD5);

            String toMD5String = toMD5StringBuilder.toString();

            String lastMD5Result = md5(toMD5String,charset);

            return lastMD5Result;
        }catch (Exception ex){
            //ignore
            return "";
        }
    }
	
	public static String md5(String text, String charset) throws Exception {
        if(charset == null || charset.length()==0)
            charset = "UTF-8";

		byte[] bytes = text.getBytes(charset);
		
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		messageDigest.update(bytes);
		bytes = messageDigest.digest();
		
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < bytes.length; i ++)
		{
			if((bytes[i] & 0xff) < 0x10)
			{
				sb.append("0");
			}

			sb.append(Long.toString(bytes[i] & 0xff, 16));
		}
		
		return sb.toString().toLowerCase();
	}
	
	public static String md5ByCharset(String text, Charset charset) throws Exception {
		byte[] bytes = text.getBytes(charset);
		
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		messageDigest.update(bytes);
		bytes = messageDigest.digest();
		
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < bytes.length; i ++)
		{
			if((bytes[i] & 0xff) < 0x10)
			{
				sb.append("0");
			}

			sb.append(Long.toString(bytes[i] & 0xff, 16));
		}
		
		return sb.toString().toLowerCase();
	}
	
	/**
	 * 验证签名是否正确
	 * @param dataMap
	 * @param securityKey
	 * @param currentSignature
	 * @return
	 */
	public static boolean validateFormDataParamMD5(Map<String,String> dataMap,String securityKey,String currentSignature){
        if(dataMap == null) return false;

        Set<String> keySet = dataMap.keySet();
        List<String> keyList = new ArrayList<String>(keySet);
        Collections.sort(keyList);

        StringBuilder toMD5StringBuilder = new StringBuilder();
        for(String key : keyList){
            String value = dataMap.get(key);

            if(value != null && value.length()>0){
                toMD5StringBuilder.append(key+"="+ value+"&");
            }
        }

        try{
            String securityKeyMD5 = md5(securityKey,"");
            toMD5StringBuilder.append(securityKeyMD5);

            String toMD5String = toMD5StringBuilder.toString();

            String actualMD5Value = md5(toMD5String,"");

            return actualMD5Value.equals(currentSignature);
        }catch (Exception ex){
            return false;
        }
    }
	
	/**
	 * url后面参数 转换成Map
	 * @param reportContent
	 * @param reportCharset
	 * @param targetCharset
	 * @return
	 */
	public static Map<String,String> parseFormDataPatternReportWithDecode(String reportContent,String reportCharset,String targetCharset) {
        if(reportContent == null || reportContent.length() ==0) return null;

        String[] domainArray = reportContent.split("&");

        Map<String,String> key_value_map = new HashMap<String, String>();
        for(String domain : domainArray){
            String[] kvArray = domain.split("=");

            if(kvArray.length == 2){
                try{
                    String decodeString = URLDecoder.decode(kvArray[1], reportCharset);
                    String lastInnerValue = new String(decodeString.getBytes(reportCharset), targetCharset);
                    key_value_map.put(kvArray[0], lastInnerValue);
                }catch (Exception ex){
                    // ignore
                }
            }
        }
        return key_value_map;
    }
	
	public final static Charset utf8 = Charset.forName("utf8");
	
	/**
	 * 信息签名
	 * @param salt
	 * @param params
	 * @return
	 */
	public static String signParams(String salt, String... params) {
		StringBuffer whole = new StringBuffer();
		for (int i = 0; i < params.length; i++) {
			whole.append(params[i]);
			if (i < params.length - 1) {
				whole.append('&');
			}
		}
		whole.append(salt);
		MessageDigest digest = LocalObject.md5.get();
		return Hex.encodeHexString((digest.digest(whole.toString().getBytes(utf8))));
	}

	static byte[] hextable = "0123456789abcdef".getBytes(utf8);
	/**
	 * 信息签名
	 * @param salt
	 * @param params
	 * @return
	 */
	public static String signParamsForBaofeng(String salt, String... params) {
		StringBuffer whole = new StringBuffer();
		for (int i = 0; i < params.length; i++) {
			whole.append(params[i]);
			whole.append('&');
		}
		whole.append(salt);
		MessageDigest digest = LocalObject.md5.get();

		byte[] wholeBytes = whole.toString().getBytes(utf8);
		byte[] digests = digest.digest(wholeBytes);
		System.out.println(digests);
		return bytesToHexString(digests);
//		byte[] dst = new byte[digests.length * 2];
//		for(int i = 0; i < digests.length; i++){
//			byte v = digests[i];
//			dst[i*2] = (byte)hextable[v>>4];
//			dst[i*2+1] = hextable[v&0x0f];
//		}
//		return new String(dst);
	}

	public static String bytesToHexString(byte[] src){
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
	 * 信息验签
	 * @param salt
	 * @param sign
	 * @param params
	 * @return
	 */
	public static boolean checkSign(String salt, String sign, String... params) {
		return sign.equals(signParams(salt, params));
	}


	public static void main(String... args){
		final String PaySecret = "secret-7jIhRZpz";
		String signParams = EncryptUtils.signParamsForBaofeng(
				PaySecret,
				"money=" + 10,
				"order_id=" + 12345678,
				"uuid=" + "17310288949"
		);
		System.out.println(signParams);
	}

	
}
