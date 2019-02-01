package com.midas.common;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.mpig.api.iapppay.Base64;

/**
 * User: rizenguo
 * Date: 2014/10/29
 * Time: 15:23
 */
public class Signature {

	/**
	 * 获取签名的方法
	 * @param url
	 * @param map
	 * @return
	 * @throws Exception
	 */
    public static String getSign(String url, String appKey, Map<String,String> map) throws Exception{
        String sortStr = sortStr(map);
        sortStr = sortStr.substring(0,sortStr.length()-1);
        URL formatUrl = new URL(url);
        String uri = "/v3/r"+formatUrl.getPath();
        System.out.println(sortStr);
        String urlEncodeResult = URLEncoder.encode(sortStr,"UTF-8");
        String result = "GET&"+URLEncoder.encode(uri,"UTF-8")+"&"+urlEncodeResult;
        System.out.println(result);
        byte[] sha1Str = HmacSHA1Encrypt(result, appKey+"&");
        System.out.println(Base64.encode(sha1Str));
        return Base64.encode(sha1Str);
    }
	
	/**
	 * 將map里的字符串降序排列
	 * @param map
	 * @return
	 */
	public static String sortStr(Map<String,String> map){
		 ArrayList<String> list = new ArrayList<String>();
	        for(Map.Entry<String,String> entry:map.entrySet()){
	            if(entry.getValue()!=""){
	                list.add(entry.getKey() + "=" + entry.getValue() + "&");
	            }
	        }
	        int size = list.size();
	        String [] arrayToSort = list.toArray(new String[size]);
	        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
	        StringBuilder sb = new StringBuilder();
	        for(int i = 0; i < size; i ++) {
	            sb.append(arrayToSort[i]);
	        }
	        return sb.toString();
	}
	
	/**
	 * 將map里的字符串降序排列
	 * @param map
	 * @return
	 */
	public static String cookieStr(Map<String,String> map){
		 ArrayList<String> list = new ArrayList<String>();
	        for(Map.Entry<String,String> entry:map.entrySet()){
	            if(entry.getValue()!=""){
	                list.add(entry.getKey() + "=" + entry.getValue() + ";");
	            }
	        }
	        int size = list.size();
	        String [] arrayToSort = list.toArray(new String[size]);
	        StringBuilder sb = new StringBuilder();
	        for(int i = 0; i < size; i ++) {
	            sb.append(arrayToSort[i]);
	        }
	        return sb.toString();
	}
	
	/**
	 * 组装cookie参数
	 * @param type 1：qq 2：weixin
	 * @param url 请求url
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws MalformedURLException
	 */
	public static String getCookieStr(String type, String url) throws UnsupportedEncodingException, MalformedURLException{
		Map<String, String> cookieMap = new HashMap<String, String>();
		if(type.equals("1")){
			cookieMap.put("session_id", URLEncoder.encode("openid","UTF-8"));
			cookieMap.put("session_type", URLEncoder.encode("kp_actoken","UTF-8"));
		}else{
			cookieMap.put("session_id", URLEncoder.encode("hy_gameid","UTF-8"));
			cookieMap.put("session_type", URLEncoder.encode("wc_actoken","UTF-8"));
		}
		URL formatUrl = new URL(url);
		cookieMap.put("org_loc", URLEncoder.encode(formatUrl.getPath(),"UTF-8"));
		String cookie = Signature.cookieStr(cookieMap);
		return cookie;
	}
	
	
	/**
	 * sha1加密算法
	 * @param encryptText
	 * @param encryptKey
	 * @return
	 * @throws Exception
	 */
	public static byte[] HmacSHA1Encrypt(String encryptText, String encryptKey) throws Exception{
		byte[] data=encryptKey.getBytes(Configure.ENCODING);  
	    //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称  
		SecretKey secretKey = new SecretKeySpec(data, Configure.MAC_NAME);   
	    //生成一个指定 Mac 算法 的 Mac 对象  
	    Mac mac = Mac.getInstance(Configure.MAC_NAME);   
	    //用给定密钥初始化 Mac 对象  
	    mac.init(secretKey);    
	    byte[] text = encryptText.getBytes(Configure.ENCODING);    
	    //完成 Mac 操作   
	    return mac.doFinal(text); 
	}    

}
