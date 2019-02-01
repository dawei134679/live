package com.tinypig.admin.async;

import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.Unirest;
import com.tinypig.admin.model.RoomRobotSpeakCMod;
import com.tinypig.admin.util.Constant;
import com.tinypig.admin.util.EncryptUtils;
import com.tinypig.admin.util.SpringContextUtil;
import com.tinypig.admin.util.VarConfigUtils;
import com.tinypig.newadmin.web.entity.RobotBaseInfo;
import com.tinypig.newadmin.web.service.RobotBaseInfoService;

public class RobotSpeakTask implements IAsyncTask {

	private static Logger log = Logger.getLogger(RobotSpeakTask.class);
	
	private Integer uid;
	private Integer robotNum;
	
	private RobotBaseInfoService robotBaseInfoService;
	
	public RobotSpeakTask() {}
	
	public RobotSpeakTask(int uid ,int robotNum) {
		this.uid = uid;
		this.robotNum = robotNum;
	}
	
	@Override
	public void runAsync() {
		try {
			log.info(String.format("房间ID:%d,机器人说话-begin", uid));
			if(robotBaseInfoService==null) {
				robotBaseInfoService = SpringContextUtil.getBean("robotBaseInfoServiceImpl");
			}
			List<RobotBaseInfo> list = robotBaseInfoService.getRobotBaseInfoRandom(robotNum);
			int seelpTime = 3000;//50*1000/robotNum;
			for(RobotBaseInfo robotBaseInfo : list) {
				RoomRobotSpeakCMod msgBody = new RoomRobotSpeakCMod();
				msgBody.setUid(Integer.parseInt(robotBaseInfo.getUid().toString()));
				msgBody.setSex(robotBaseInfo.getSex()==0?true:false);
				msgBody.setNickname(robotBaseInfo.getNickname());
				msgBody.setAvatar(robotBaseInfo.getHeadimage());
				msgBody.setLevel(robotBaseInfo.getUserlevel());
				msgBody.setContent("来了");
				String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret, "appKey=" + VarConfigUtils.ServiceKey,
						"msgBody=" + JSONObject.toJSONString(msgBody), "roomOwner=" + String.valueOf(uid));
				Unirest.post(Constant.im_publish_room_url).field("appKey", VarConfigUtils.ServiceKey)
						.field("msgBody", JSONObject.toJSONString(msgBody))
						.field("roomOwner", String.valueOf(uid))
						.field("sign", signParams).asJsonAsync();
				Thread.sleep(seelpTime);
			}
			log.info(String.format("房间ID:%d,机器人说话-结束", uid));
		} catch (Exception e) {
			log.error("机器人说话异常",e);
		}
		
	}

	@Override
	public void afterOk() {
		
	}

	@Override
	public void afterError(Exception e) {
		
	}

	@Override
	public String getName() {
		return "RobotSpeakTask";
	}
}
