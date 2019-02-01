package com.mpig.api.utils;

import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.Unirest;
import com.mpig.api.dictionary.lib.UrlConfigLib;
import com.mpig.api.modelcomet.FeedNoticeCmod;
import com.mpig.api.modelcomet.OpenServiceNoticeCMod;
import com.mpig.api.modelcomet.GameWinNoticeCmod;
import com.mpig.api.modelcomet.RoomGiftTopCMod;
import com.mpig.api.modelcomet.RoomNoticeTxtCmod;
import com.mpig.api.modelcomet.ShareRoomCMod;
import com.mpig.api.modelcomet.SysMsgCMod;

public class ChatMessageUtil {
	
	
	/**
	 * 游戏中奖 在公聊区 信息通知
	 * @param anchoruid
	 * @param winNotice
	 */
	public static void gameWinNotice(int anchoruid,int uid,int money,Object winNotice){
		GameWinNoticeCmod msgBody = new GameWinNoticeCmod();
		msgBody.setAnchorUid(anchoruid);
		msgBody.setUid(uid);
		msgBody.setMoney(money);
		msgBody.setWinMsg(winNotice);

		String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret, "appKey=" + VarConfigUtils.ServiceKey,
				"msgBody=" + JSONObject.toJSONString(msgBody), "roomOwner=" + anchoruid);

		Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_room()).field("appKey", VarConfigUtils.ServiceKey)
				.field("msgBody", JSONObject.toJSONString(msgBody)).field("roomOwner", anchoruid)
				.field("sign", signParams).asJsonAsync();
	}
	 
	/**
	 * 房间幸运礼物中奖通知
	 * @param anchoruid
	 * @param message
	 * @param gid 中奖特效id
	 */
	public static void roomLuckyNotice(int anchoruid, Object message, Integer gid, Object luckyMsg, int uid, String nickname, Integer winnersMoney, Integer winnersMultiple, Integer winnersCount){
		RoomNoticeTxtCmod msgBody = new RoomNoticeTxtCmod();
		msgBody.setData(message);
		if(gid != null){
			msgBody.setGid(gid);	
		}
		msgBody.setUid(uid);
		msgBody.setNickname(nickname);
		if(winnersMoney != null){
			msgBody.setWinnersMoney(winnersMoney);
		}
		if(winnersMultiple != null){
			msgBody.setWinnersMultiple(winnersMultiple);
		}
		if(winnersCount != null){
			msgBody.setWinnersCount(winnersCount);
		}
		msgBody.setLuckyMsg(luckyMsg);
		String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret, "appKey=" + VarConfigUtils.ServiceKey,
				"msgBody=" + JSONObject.toJSONString(msgBody), "roomOwner=" + anchoruid);

		Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_room()).field("appKey", VarConfigUtils.ServiceKey)
				.field("msgBody", JSONObject.toJSONString(msgBody)).field("roomOwner", anchoruid)
				.field("sign", signParams).asJsonAsync();
	}
	
	/**
	 * 房间分享推送
	 * @param anchoruid
	 * @param message
	 */
	public static void shareRoomNotice(int anchoruid, Object message, int uid, String nickname, boolean sex, int level){
		ShareRoomCMod msgBody = new ShareRoomCMod();
		msgBody.setData(message);
		msgBody.setNickname(nickname);
		msgBody.setSex(sex);
		msgBody.setLevel(level);
		msgBody.setUid(uid);
		String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret, "appKey=" + VarConfigUtils.ServiceKey,
				"msgBody=" + JSONObject.toJSONString(msgBody), "roomOwner=" + anchoruid);

		Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_room()).field("appKey", VarConfigUtils.ServiceKey)
				.field("msgBody", JSONObject.toJSONString(msgBody)).field("roomOwner", anchoruid)
				.field("sign", signParams).asJsonAsync();
	}
	
	/**
	 * 服务开通推送
	 * @param anchoruid
	 * @param tid 服务开通的特效id
	 * @param message
	 */
	public static void openServiceNotice(Integer anchoruid,Integer tid, Integer uid, String nickname,Long creditTotal, Object message){
		OpenServiceNoticeCMod msgBody = new OpenServiceNoticeCMod();
		msgBody.setData(message);
		msgBody.setTid(tid);
		msgBody.setUid(uid);
		msgBody.setNickname(nickname);
		msgBody.setCreditTotal(creditTotal);
		String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret, "appKey=" + VarConfigUtils.ServiceKey,
				"msgBody=" + JSONObject.toJSONString(msgBody), "roomOwner=" + anchoruid);

		Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_room()).field("appKey", VarConfigUtils.ServiceKey)
				.field("msgBody", JSONObject.toJSONString(msgBody)).field("roomOwner", anchoruid)
				.field("sign", signParams).asJsonAsync();
	}
	
	/**
	 * 官方私信通知
	 */
	public static void sendSysMsg(){
		
		SysMsgCMod sysMsgCMod = new SysMsgCMod();
		
		String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
				"appKey=" + VarConfigUtils.ServiceKey, "msgBody=" + JSONObject.toJSONString(sysMsgCMod));

		Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_world()).field("appKey", VarConfigUtils.ServiceKey)
				.field("msgBody", JSONObject.toJSONString(sysMsgCMod)).field("sign", signParams).asJsonAsync();
	}
	
	/**
	 * 送礼更新资产和背包
	 * @param anchoruid
	 * @param winnersMoney 中奖金额 非必填
	 * @param winnersMultiple 中奖倍数 非必填
	 * @param giftList 更新背包礼物 非必填
	 * @param money 余额 非必填
	 * @param message
	 */
	public static void sendGiftUpdateAssetAndBag(int uid,String nickname, Integer money, Object message, Object giftList, Integer winnersMoney, Integer winnersMultiple, Integer winnersCount){
		RoomGiftTopCMod giftTopCMod = new RoomGiftTopCMod();
		giftTopCMod.setUid(uid);
		giftTopCMod.setNickname(nickname);
		if(money != null){
			giftTopCMod.setMoney(money.intValue());	
		}
		if(winnersMoney != null){
			giftTopCMod.setWinnersMoney(winnersMoney);
		}
		if(winnersMultiple != null){
			giftTopCMod.setWinnersMultiple(winnersMultiple);
		}
		if(winnersCount != null){
			giftTopCMod.setWinnersCount(winnersCount);
		}
		giftTopCMod.setData(message);
		giftTopCMod.setGiftList(giftList);
		String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
				"appKey=" + VarConfigUtils.ServiceKey, "msgBody=" + JSONObject.toJSONString(giftTopCMod),
				"users=" + uid);
		
		Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_live()).field("appKey", VarConfigUtils.ServiceKey).field("msgBody", JSONObject.toJSONString(giftTopCMod)).field("users", uid).field("sign", signParams).asJsonAsync();
	}
	
	/**
	 * 推送动态通知
	 * @param uid
	 * @param feedId
	 * @param message
	 */
	public static void sendFeedNotice(int uid,int feedId, Object message){
		FeedNoticeCmod noticeCmod = new FeedNoticeCmod();
		noticeCmod.setFeedId(feedId);
		if(message != null){
			noticeCmod.setData(message);	
		}
		
		String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
				"appKey=" + VarConfigUtils.ServiceKey, "msgBody=" + JSONObject.toJSONString(noticeCmod),
				"users=" + uid);
		
		Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_live()).field("appKey", VarConfigUtils.ServiceKey)
				.field("msgBody", JSONObject.toJSONString(noticeCmod)).field("users", uid).field("sign", signParams).asJsonAsync();
	}
}


