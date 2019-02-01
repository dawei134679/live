package com.tinypig.admin.service;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.tinypig.admin.config.GlobalConfig;
import com.tinypig.admin.protocol.Protocol;

public class WorldNoticeService {
	private static final Logger logger = LoggerFactory.getLogger(WorldNoticeService.class);
	private static WorldNoticeService instance = null;
	private static Object lock = new Object();
	private WorldNoticeService(){
		
	}
	
	public static WorldNoticeService getInstance(){
		synchronized (lock) {
			if(instance == null){
				instance = new WorldNoticeService();
			}
		}
		return instance;
	}
	
	
	/**
	 * 通知客户端重新拉取客户端列表
	 */
	public void pullAnchors(){
		JSONObject command = new JSONObject();
		command.put("appKey", GlobalConfig.getInstance().getAppKey());
		JSONObject msgBody = new JSONObject();
		msgBody.put("cometProtocol", Protocol.Notice_PullAnchors);
		command.put("msgBody", msgBody);
		String url = GlobalConfig.getInstance().getPublishWordUrl();
		HttpResponse<JsonNode> response;
		try {
			response = Unirest.get(url).asJson();
			 if (response.getStatus() != 200) {
		            logger.error("<pullReCommends>--" + response.getBody().toString());
		        }
		} catch (UnirestException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 通知客户端重新拉取客户端列表
	 */
	public void pullRemoveSomeAbchor(String... uids){
		JSONObject command = new JSONObject();
		command.put("appKey", GlobalConfig.getInstance().getAppKey());
		JSONObject msgBody = new JSONObject();
		msgBody.put("cometProtocol", Protocol.Notice_RemoveSomeAnchor);
		msgBody.put("uids", uids);
		command.put("msgBody", msgBody);
		System.err.println(command.toString());
		String url = GlobalConfig.getInstance().getPublishWordUrl();
		HttpResponse<JsonNode> response;
		try {
			response = Unirest.post(url).asJson();
			 if (response.getStatus() != 200) {
		            logger.error("<pullReCommends>--" + response.getBody().toString());
		        }
		} catch (UnirestException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		WorldNoticeService.getInstance().pullRemoveSomeAbchor("jack","xujin");
	}
	
}
