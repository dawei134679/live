package com.tinypig.admin.async;

import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.mashape.unirest.http.Unirest;
import com.tinypig.admin.redis.service.RedisOperat;
import com.tinypig.admin.util.Constant;
import com.tinypig.admin.util.RedisContant;
import com.tinypig.admin.util.SpringContextUtil;
import com.tinypig.newadmin.web.entity.RobotBaseInfo;
import com.tinypig.newadmin.web.service.RobotBaseInfoService;

public class RobotEnterRoomTask implements IAsyncTask {

	private static Logger log = Logger.getLogger(RobotEnterRoomTask.class);
	
	private Integer uid;
	private Integer addRoboEnterNum;
	
	private RobotBaseInfoService robotBaseInfoService;
	
	public RobotEnterRoomTask() {}
	
	public RobotEnterRoomTask(int uid ,int addRoboEnterNum) {
		this.uid = uid;
		this.addRoboEnterNum = addRoboEnterNum;
	}
	
	@Override
	public void runAsync() {
		try {
			log.info(String.format("房间ID:%d,增加机器人-begin", uid));
			if(robotBaseInfoService==null) {
				robotBaseInfoService = SpringContextUtil.getBean("robotBaseInfoServiceImpl");
			}
			List<RobotBaseInfo> list = robotBaseInfoService.getRobotBaseInfoRandom(addRoboEnterNum);
			//预留20S
			int seelpTime = 3000;//40*1000/addRoboEnterNum;
			log.info(String.format("房间ID:%d,增加带头像机器人:%d,每%d秒进一个,预计%d秒内完成", uid,addRoboEnterNum,seelpTime/1000,seelpTime/1000*addRoboEnterNum));
			for(RobotBaseInfo robotBaseInfo : list) {
				RedisOperat.getInstance().hset(RedisContant.host, RedisContant.port6379, RedisContant.roomRobots+uid,
						String.valueOf(robotBaseInfo.getUid()),JSON.toJSONString(robotBaseInfo),0);
				 Unirest.get(Constant.business_server_url+"/admin/enter_room")
						.queryString("dstUid", uid)
						.queryString("srcUid", robotBaseInfo.getUid())
						.queryString("level",robotBaseInfo.getUserlevel())
						.queryString("nick",robotBaseInfo.getNickname())
						.queryString("avatar",robotBaseInfo.getHeadimage())
						.queryString("sex",robotBaseInfo.getSex())
						.asJsonAsync();
				 Thread.sleep(seelpTime);
			}
			log.info(String.format("房间ID:%d,增加机器人-结束", uid));
		} catch (Exception e) {
			log.error("异步添加机器人失败",e);
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
		return "RobotEnterRoomTask";
	}
}
