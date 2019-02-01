package com.tinypig.admin.TimerTask;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Future;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.tinypig.admin.async.AsyncManager;
import com.tinypig.admin.async.RobotEnterRoomTask;
import com.tinypig.admin.async.RobotSpeakTask;
import com.tinypig.admin.model.SetRoomShowUserNumCMod;
import com.tinypig.admin.model.UserBaseInfoModel;
import com.tinypig.admin.redis.service.OtherRedisService;
import com.tinypig.admin.redis.service.RedisOperat;
import com.tinypig.admin.util.Constant;
import com.tinypig.admin.util.EncryptUtils;
import com.tinypig.admin.util.RedisContant;
import com.tinypig.admin.util.VarConfigUtils;

public class RoomSortService {

	private static Logger log = Logger.getLogger(RoomSortService.class);
	
	/**
	 * 定时设置房间的人数
	 */
	public void setRoomSort(){
		
		log.info("根据机器人倍数设置房间显示人数-开始");
		
		Set<String> liveList = new HashSet<String>();
		Set<String> _set = null;
		_set = OtherRedisService.getInstance().getRecommendRoom(0);
		if(_set!=null) {
			for (String string : _set) {
				liveList.add(string);
			}	
		}
		_set = OtherRedisService.getInstance().getHotRoom(0);
		if(_set!=null) {
			for (String string : _set) {
				liveList.add(string);
			}
		}
		_set = OtherRedisService.getInstance().getBaseRoom(0);
		if(_set!=null) {
			for (String string : _set) {
				liveList.add(string);
			}
		}
		_set = OtherRedisService.getInstance().getHeadRoom(0);
		if(_set!=null) {
			for (String string : _set) {
				liveList.add(string);
			}
		}
		
		log.info(String.format("共有%d个房间", liveList.size()));
		
		for(String uid : liveList){
			UserBaseInfoModel userBaseInfoModel = null;
			String userbaseinfo = RedisOperat.getInstance().hget(RedisContant.host, RedisContant.port6379, "usr:base", uid);
			if (StringUtils.isNotEmpty(userbaseinfo)) {
				 userBaseInfoModel = (UserBaseInfoModel) JSONObject.parseObject(userbaseinfo, UserBaseInfoModel.class);
			}
			if(userBaseInfoModel!=null && userBaseInfoModel.getLiveStatus()){
				//设置机器人倍数
				int contrRq = userBaseInfoModel.getContrRq(); //后台添加的人数
				
				//房间真实人数
				Integer realUserNum = RedisOperat.getInstance().hlen(RedisContant.host, RedisContant.port6379, RedisContant.roomUsers+uid);
				if(realUserNum==null) {
					realUserNum  = 0;
				}
				//房间机器人数
				Integer roomRobotsNum = RedisOperat.getInstance().hlen(RedisContant.host, RedisContant.port6379, RedisContant.roomRobots+uid);
				if(roomRobotsNum==null) {
					roomRobotsNum = 0 ;
				}
				
				//房间内人数 包含机器人
				int thisRoomUserNum = realUserNum+roomRobotsNum;
				
				//显示人数
				int showUserNum = 0;
				if(realUserNum>0){
					showUserNum = thisRoomUserNum + (realUserNum * contrRq);
				}
				
				log.info(String.format("%s房间真实人数:%d,显示人数:%d",uid,realUserNum,showUserNum));
				
				if(realUserNum>0 && contrRq>0 && (realUserNum*contrRq) > thisRoomUserNum && thisRoomUserNum <= 20) {
					int addRobotNum = 0;
					if(showUserNum >= 20) {
						addRobotNum = 20 - (realUserNum+roomRobotsNum);
					}else {
						addRobotNum = showUserNum - (realUserNum+roomRobotsNum);
					}
					if(addRobotNum>0) {
						AsyncManager.getInstance().execute(new RobotEnterRoomTask(Integer.parseInt(uid),addRobotNum));
					}
				}
				if(contrRq>0&&(realUserNum+roomRobotsNum)>=20) {
					AsyncManager.getInstance().execute(new RobotSpeakTask(Integer.parseInt(uid),18));
				}
				
				if(userBaseInfoModel.getRecommend() == 2 || userBaseInfoModel.getRecommend() == 3){
					RedisOperat.getInstance().zadd(RedisContant.host, RedisContant.port6381, "rm:recomm", showUserNum, uid);
				}
				//记录当前房间的显示人数
				RedisOperat.getInstance().zadd(RedisContant.host, RedisContant.port6381, "rm:show:uc:", showUserNum, uid);
				if(contrRq>0) {
					SetRoomShowUserNumCMod msgBody = new SetRoomShowUserNumCMod();
					msgBody.setShowUserNum(showUserNum);
					pushRoomMsg(msgBody,uid);
				}
			}
		}
		log.info("根据机器人倍数设置房间显示人数-结束");
	}
	
	private void pushRoomMsg(SetRoomShowUserNumCMod msgBody,String roomOwnerUid) {
		try {
			String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret, "appKey=" + VarConfigUtils.ServiceKey,
					"msgBody=" + JSONObject.toJSONString(msgBody), "roomOwner=" + String.valueOf(roomOwnerUid));
			Future<HttpResponse<JsonNode>> asJsonAsync = Unirest.post(Constant.im_publish_room_url).field("appKey", VarConfigUtils.ServiceKey)
					.field("msgBody", JSONObject.toJSONString(msgBody))
					.field("roomOwner", String.valueOf(roomOwnerUid))
					.field("sign", signParams).asJsonAsync();
			if (asJsonAsync != null) {
				if (asJsonAsync.get().getStatus() == 200) {
					log.info(String.format("%s%s", "消息推送成功,sign:", signParams));
				} else {
					log.info(String.format("%s%s%s%s", "消息推送失败,sign:", signParams, ",status:",asJsonAsync.get().getStatus()));
				}
			} else {
				log.error("IM服务器返回null,signParams:" + signParams);
			}
		}catch (Exception e) {
			log.error("推送消息异常",e);
		}
	}
}
