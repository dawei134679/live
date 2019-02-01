package com.tinypig.newadmin.web.service;

import java.util.List;
import java.util.Map;

import com.tinypig.newadmin.web.entity.RobotBaseInfo;

public interface RobotBaseInfoService {

	List<RobotBaseInfo> getRobotBaseInfoRandom(Integer num);
	
	Map<String, Object> getRobotBaseInfoList(Integer page, Integer rows);
	
	public Map<String, Object> reAllRobotRedis();
	
	Map<String, Object> updateRobot(RobotBaseInfo robot);
	
	Map<String, Object> saveRobot(RobotBaseInfo robot);
}
