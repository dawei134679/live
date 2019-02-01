package com.mpig.api.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mpig.api.model.RoomGameInfoModel;
import com.mpig.api.redis.service.OtherRedisService;

public class GameServerUtil {

	private static Logger log = Logger.getLogger(GameServerUtil.class);

	public static Map<String, String> getHeader(String token, String url) {
		try {
			Map<String, String> headers = new HashMap<String, String>();
			Date nowDate = new Date();
			headers.put("timestamp", String.valueOf(nowDate.getTime()));
			String aesToken = AESCipher.aesEncryptString(String.format("%s_%s", token, MD5Encrypt.encrypt(headers.get("timestamp"))));
			headers.put("token", aesToken);
			headers.put("sign", MD5Encrypt.encrypt(String.format("%s%s%s", token, url, headers.get("timestamp"))));
			return headers;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}

	public static RoomGameInfoModel getGameInfoById(Long gameId) {
		if(gameId==null||gameId==0) {
			return null;
		}
		String json = OtherRedisService.getInstance().hget(RedisContant.gameHashList, String.valueOf(gameId));
		if(StringUtils.isBlank(json)) {
			return null;
		}
		return JSONObject.parseObject(json, RoomGameInfoModel.class);
	}
   
	public static boolean initGame(String gameId,Integer uid,String roomId) {
		try {
			RoomGameInfoModel gameInfo = getGameInfoById(Long.valueOf(gameId));
			if(StringUtils.isBlank(gameInfo.getInitUrl())) {
				return true;
			}
			HttpResponse<JsonNode> result = Unirest
					.get(gameInfo.getServerUrl()+gameInfo.getInitUrl())
					.queryString("roomId", roomId)
					.queryString("anchorId",uid)
					.queryString("t",System.currentTimeMillis())
					.asJson();
			org.json.JSONObject json = result.getBody().getObject();
			log.info(String.format("%s%s", "初始化游戏接口返回：",json));
			if(json==null||json.length()==0) {
				return false;
			}
			if(!"200".equals(json.getString("code"))) {
				log.info("初始化游戏失败：" + json);
				return false;
			}
			 return true;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		}
	}

	public static boolean destoryGame(String gameId, Integer uid) {
		try {
			RoomGameInfoModel gameInfo = getGameInfoById(Long.valueOf(gameId));
			if(StringUtils.isBlank(gameInfo.getDestoryUrl())) {
				return true;
			}
			HttpResponse<JsonNode> result = Unirest
					.get(String.valueOf(gameInfo.getServerUrl()+gameInfo.getDestoryUrl()))
					.queryString("anchorId",uid).asJson();
			org.json.JSONObject json = result.getBody().getObject();
			log.info(String.format("%s%s", "销毁游戏接口返回：",json));
			if(json==null||json.length()==0) {
				return false;
			}
			if(!"200".equals(json.getString("code"))) {
				log.info("销毁游戏失败：" + json);
				return false;
			}
			 return true;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			 return false;
		}
	}
}