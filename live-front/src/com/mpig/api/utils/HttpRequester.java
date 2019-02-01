package com.mpig.api.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.log4j.Logger;

public class HttpRequester {
	
	private static Logger logger = Logger.getLogger(HttpRequester.class);
	private String defaultContentEncoding;  
	   
    public HttpRequester() {  
        this.defaultContentEncoding = Charset.defaultCharset().name();  
    }  
    
    /** 
     * 发送GET请求 
     *  
     * @param urlString 
     *            URL地址 
     * @return 响应对象 
     * @throws IOException 
     */  
    public String sendGet(String urlString) throws IOException {  
        return send(urlString, "GET", null, null);  
    }      
    
    /** 
     * 发送POST请求 
     *  
     * @param urlString 
     *            URL地址 
     * @return 响应对象 
     * @throws IOException 
     */  
    public String sendPost(String urlString) throws IOException {  
        return this.send(urlString, "POST", null, null);  
    }  
    /** 
     * 发送HTTP请求 
     *  
     * @param urlString 
     * @return 响映对象 
     * @throws IOException 
     */  
    public String send(String urlString, String method,  
            Map<String, String> parameters, Map<String, String> propertys)  
            throws IOException {  
        HttpURLConnection urlConnection = null;  
   
        if (method.equalsIgnoreCase("GET") && parameters != null) {  
            StringBuffer param = new StringBuffer();  
            int i = 0;  
            for (String key : parameters.keySet()) {  
                if (i == 0)  
                    param.append("?");  
                else  
                    param.append("&");  
                param.append(key).append("=").append(parameters.get(key));  
                i++;  
            }  
            urlString += param;  
        }  
        URL url = new URL(urlString);  
        urlConnection = (HttpURLConnection) url.openConnection();  
   
        urlConnection.setRequestMethod(method);  
        urlConnection.setDoOutput(true);  
        urlConnection.setDoInput(true);  
        urlConnection.setUseCaches(false);  
   
        if (propertys != null)  
            for (String key : propertys.keySet()) {  
                urlConnection.addRequestProperty(key, propertys.get(key));  
            }  
   
        if (method.equalsIgnoreCase("POST") && parameters != null) {  
            StringBuffer param = new StringBuffer();  
            for (String key : parameters.keySet()) {  
                param.append("&");  
                param.append(key).append("=").append(parameters.get(key));  
            }  
            urlConnection.getOutputStream().write(param.toString().getBytes());  
            urlConnection.getOutputStream().flush();  
            urlConnection.getOutputStream().close();  
        }  
   
        return this.makeContent(urlString, urlConnection);  
    }  
   
    /** 
     * 得到响应对象 
     *  
     * @param urlConnection 
     * @return 响应对象 
     * @throws IOException 
     */  
    private String makeContent(String urlString,  
            HttpURLConnection urlConnection) throws IOException {
        BufferedInputStream bis = null;
        try {
            InputStream in = urlConnection.getInputStream();  
             bis = new BufferedInputStream(in);
            StringBuffer temp = new StringBuffer();  
            byte[] buffer = new byte[8192*2];
            int byteCount = 0;
            while((byteCount = bis.read(buffer)) != -1){
            	temp.append(new String(buffer, 0, byteCount, "utf-8"));
            } 
            return temp.toString();
        } catch (Exception e) {
        	logger.error("<makeContent->IOException>"+e.getMessage());
        } finally {  
            if (urlConnection != null)  
                urlConnection.disconnect(); 
            if(bis != null){
            	bis.close();
            }
        }
        return "";
    }  
   
    /** 
     * 默认的响应字符集 
     */  
    public String getDefaultContentEncoding() {  
        return this.defaultContentEncoding;  
    }  
   
    /** 
     * 设置默认的响应字符集 
     */  
    public void setDefaultContentEncoding(String defaultContentEncoding) {  
        this.defaultContentEncoding = defaultContentEncoding;  
    }  
}  
