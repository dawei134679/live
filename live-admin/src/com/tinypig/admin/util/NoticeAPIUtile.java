package com.tinypig.admin.util;

import java.util.List;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.tinypig.newadmin.common.HttpServerList;

public class NoticeAPIUtile {

	public static void noticeApi(String method,String paramFleid,String paramValue){

		try {
			// 测试上 *****************
//			HttpResponse<JsonNode> res = Unirest.get("http://api-test.xiaozhutv.com:10000/admin/" + method + "?" + paramFleid + "=" + paramValue).asJson();
//			org.json.JSONObject object = res.getBody().getObject();
//			if(Integer.parseInt(object.get("code").toString()) == 200){
//
//			}else{
//				System.out.println("noticeApi method=" + method + " paramFleid=" + paramFleid + " paramValue=" + paramValue);
//			}
			
			// 正式上 *****************
			List<String> list = HttpServerList.getInstance().getServerList();
			String url = "";
			for(int i=0;i<list.size();i++){
				url = list.get(i);
				HttpResponse<JsonNode> res = Unirest.get(url + "admin/" + method + "?" + paramFleid + "=" + paramValue).asJson();
				org.json.JSONObject object = res.getBody().getObject();
				if(Integer.parseInt(object.get("code").toString()) == 200){

				}else{
					System.out.println("noticeApi method=" + method + " paramFleid=" + paramFleid + " paramValue=" + paramValue);
				}
			}
		} catch (UnirestException e) {
			e.printStackTrace();
		}
	}
}
