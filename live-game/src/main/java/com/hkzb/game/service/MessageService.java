package com.hkzb.game.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hkzb.game.common.utils.EncryptUtils;
import com.hkzb.game.dto.BaseCMod;
import com.hkzb.game.dto.RoomNoticeTxtCmod;
import com.hkzb.game.dto.RunWayCMod;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

@Service
public class MessageService implements IMessageService {

	@Value("${im.serviceKey}")
	private String serviceKey;

	@Value("${im.serviceSecret}")
	private String serviceSecret;

	@Value("${adminrpc.publish.room}")
	private String adminrpcPublishRoom;

	@Value("${adminrpc.publish.room.all}")
	private String adminrpcPublishRoomAll;

	private Logger log = Logger.getLogger(MessageService.class);

	public void pushRoomTotalStakeMsg(String content, Long uid) {
		try {
			String signParams = EncryptUtils.signParams(serviceSecret, "appKey=" + serviceKey, "msgBody=" + content,
					"roomOwner=" + uid);

			Future<HttpResponse<JsonNode>> asJsonAsync = Unirest.post(adminrpcPublishRoom).field("appKey", serviceKey)
					.field("msgBody", content).field("roomOwner", String.valueOf(uid)).field("sign", signParams)
					.asJsonAsync();

			if (asJsonAsync != null) {
				if (asJsonAsync.get().getStatus() == 200) {
//					log.info(String.format("%s%s", "消息推送成功,sign:", signParams));
				} else {
					log.info(String.format("%s%s%s%s", "消息推送失败,sign:", signParams, ",status:",
							asJsonAsync.get().getStatus()));
				}
			} else {
				log.info("IM服务器返回null,signParams:" + signParams);
			}
		} catch (InterruptedException e) {
			log.error(e.getMessage(), e);
		} catch (ExecutionException e) {
			log.error(e.getMessage(), e);
		}
	}

	public void roomAllNotice(String uname, Long money) {
		try {
			// 增加跑道
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			HashMap<String, Object> map = new HashMap<String, Object>();
			List<Map<String, Object>> toUserList = new ArrayList<Map<String, Object>>();

			map.put("name", "恭喜：");
			map.put("color", "#ff2d55");
			list.add(map);
			toUserList.add(map);

			map = new HashMap<String, Object>();
			map.put("name", uname);
			map.put("color", "#fff08c");
			list.add(map);

			map = new HashMap<String, Object>();
			map.put("name", "，在欢乐六选三中赢得");
			map.put("color", "");
			list.add(map);
			toUserList.add(map);

			map = new HashMap<String, Object>();
			map.put("name", money);
			map.put("color", "#fff08c");
			list.add(map);
			toUserList.add(map);

			map = new HashMap<String, Object>();
			map.put("name", "金币");
			map.put("color", "");
			list.add(map);
			toUserList.add(map);

			RunWayCMod msgBody = new RunWayCMod();
			map = new HashMap<String, Object>();
			map.put("list", list);
			msgBody.setData(map);
			// 下面两行代码 不知道干嘛 应该没用 注释不影响功能
			// msgBody.setAnchorUid(anchorUid);
			// msgBody.setAnchorName(anchorName);

			String signParams = EncryptUtils.signParams(serviceSecret, "appKey=" + serviceKey,
					"msgBody=" + JSONObject.toJSONString(msgBody));
			Future<HttpResponse<JsonNode>> asJsonAsync = Unirest.post(adminrpcPublishRoomAll)
					.field("appKey", serviceKey).field("msgBody", JSONObject.toJSONString(msgBody))
					.field("sign", signParams).asJsonAsync();

			if (asJsonAsync != null) {
				if (asJsonAsync.get().getStatus() == 200) {
//					log.info(String.format("%s%s", "消息推送成功,sign:", signParams));
				} else {
					log.info(String.format("%s%s%s%s", "消息推送失败,sign:", signParams, ",status:",
							asJsonAsync.get().getStatus()));
				}
			} else {
				log.info("IM服务器返回null,signParams:" + signParams);
			}
		} catch (InterruptedException e) {
			log.error(e.getMessage(), e);
		} catch (ExecutionException e) {
			log.error(e.getMessage(), e);
		}
	}

	public void roomNotice(Long anchorid, Integer uid, String uname, Long money) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> toUserList = new ArrayList<Map<String, Object>>();
		try {
			map.put("name", "手气爆棚！");
			map.put("color", "#ff2d55");
			list.add(map);
			toUserList.add(map);

			map = new HashMap<String, Object>();
			map.put("name", "恭喜 ");
			map.put("color", "");
			list.add(map);
			toUserList.add(map);

			map = new HashMap<String, Object>();
			map.put("name", uname);
			map.put("color", "#fff08c");
			list.add(map);

			map = new HashMap<String, Object>();
			map.put("name", "，游戏赢得");
			map.put("color", "");
			list.add(map);
			toUserList.add(map);

			map = new HashMap<String, Object>();
			map.put("name", money);
			map.put("color", "#fff08c");
			list.add(map);
			toUserList.add(map);

			map = new HashMap<String, Object>();
			map.put("name", "金币");
			map.put("color", "");
			list.add(map);
			toUserList.add(map);

			RoomNoticeTxtCmod msgBody = new RoomNoticeTxtCmod();
			msgBody.setData(list);
			// 下面七行代码 不知道干嘛 应该没用 注释不影响功能
			msgBody.setGid(0);
			msgBody.setUid(uid);
			msgBody.setNickname(uname);
			msgBody.setWinnersMoney(null);
			msgBody.setWinnersMultiple(null);
			msgBody.setWinnersCount(null);
			msgBody.setLuckyMsg("");
			String signParams = EncryptUtils.signParams(serviceSecret, "appKey=" + serviceKey,
					"msgBody=" + JSONObject.toJSONString(msgBody), "roomOwner=" + anchorid);

			Future<HttpResponse<JsonNode>> asJsonAsync = Unirest.post(adminrpcPublishRoom).field("appKey", serviceKey)
					.field("msgBody", JSONObject.toJSONString(msgBody)).field("roomOwner", anchorid)
					.field("sign", signParams).asJsonAsync();

			if (asJsonAsync != null) {
				if (asJsonAsync.get().getStatus() == 200) {
//					log.info(String.format("%s%s", "消息推送成功,sign:", signParams));
				} else {
					log.info(String.format("%s%s%s%s", "消息推送失败,sign:", signParams, ",status:",
							asJsonAsync.get().getStatus()));
				}
			} else {
				log.info("IM服务器返回null,signParams:" + signParams);
			}
		} catch (InterruptedException e) {
			log.error(e.getMessage(), e);
		} catch (ExecutionException e) {
			log.error(e.getMessage(), e);
		}
	}

	@Override
	public void roomChatNotice(Long anchorid, BaseCMod msgBody) {
		String signParams = EncryptUtils.signParams(serviceSecret, "appKey=" + serviceKey,
				"msgBody=" + JSONObject.toJSONString(msgBody), "roomOwner=" + anchorid);

		Future<HttpResponse<JsonNode>> asJsonAsync = Unirest.post(adminrpcPublishRoom).field("appKey", serviceKey)
				.field("msgBody", JSONObject.toJSONString(msgBody)).field("roomOwner", anchorid)
				.field("sign", signParams).asJsonAsync();
		try {
			if (asJsonAsync != null) {
				if (asJsonAsync.get().getStatus() == 200) {
//					log.info(String.format("%s%s", "消息推送成功,sign:", signParams));
				} else {
					log.info(String.format("%s%s%s%s", "消息推送失败,sign:", signParams, ",status:",
							asJsonAsync.get().getStatus()));
				}
			} else {
				log.info("IM服务器返回null,signParams:" + signParams);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void roomWorldNotice(Long anchoruid, BaseCMod msgBody) {
		// 下面两行代码 不知道干嘛 应该没用 注释不影响功能
		// msgBody.setAnchorUid(anchorUid);
		// msgBody.setAnchorName(anchorName);

		String signParams = EncryptUtils.signParams(serviceSecret, "appKey=" + serviceKey,
				"msgBody=" + JSONObject.toJSONString(msgBody));
		Future<HttpResponse<JsonNode>> asJsonAsync = Unirest.post(adminrpcPublishRoomAll)
				.field("appKey", serviceKey).field("msgBody", JSONObject.toJSONString(msgBody))
				.field("sign", signParams).asJsonAsync();

		try {
			if (asJsonAsync != null) {
				if (asJsonAsync.get().getStatus() == 200) {
//					log.info(String.format("%s%s", "消息推送成功,sign:", signParams));
				} else {
					log.info(String.format("%s%s%s%s", "消息推送失败,sign:", signParams, ",status:",
							asJsonAsync.get().getStatus()));
				}
			} else {
				log.info("IM服务器返回null,signParams:" + signParams);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}