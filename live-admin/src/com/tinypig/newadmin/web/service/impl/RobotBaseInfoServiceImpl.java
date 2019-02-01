package com.tinypig.newadmin.web.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.tinypig.admin.redis.service.RedisOperat;
import com.tinypig.admin.util.JsonUtil;
import com.tinypig.admin.util.RedisContant;
import com.tinypig.newadmin.common.ConstantsAction;
import com.tinypig.newadmin.web.dao.RobotBaseInfoDao;
import com.tinypig.newadmin.web.entity.RobotBaseInfo;
import com.tinypig.newadmin.web.service.RobotBaseInfoService;

@Service
@Transactional
public class RobotBaseInfoServiceImpl  implements RobotBaseInfoService{

	@Autowired
	private RobotBaseInfoDao robotBaseInfoDao;
	
	@Override
	public List<RobotBaseInfo> getRobotBaseInfoRandom(Integer num) {
		if(num==null) {
			return null;
		}
		List<String> list = RedisOperat.getInstance().srandmember(RedisContant.host, RedisContant.port6379, RedisContant.keyRobotUidSet, num);
		if(list!=null) {
			List<String> robotBaseInfoStrList  = RedisOperat.getInstance().hmget(RedisContant.host, RedisContant.port6379, RedisContant.keyRobotBaseInfoList,list.toArray(new String[list.size()]));
			if(robotBaseInfoStrList!=null) {
				List<RobotBaseInfo> resList  = new ArrayList<RobotBaseInfo>();
				for (String json : robotBaseInfoStrList) {
					resList.add(JsonUtil.toBean(json, RobotBaseInfo.class));
				}
				return resList;
			}
		}
		//redis获取失败 从数据中说
		return robotBaseInfoDao.getRobotBaseInfoRandom(num);
	}

	@Override
	public Map<String, Object> getRobotBaseInfoList(Integer page, Integer rows) {
		Integer startIndex = (page-1)*rows;
		List<RobotBaseInfo> list = robotBaseInfoDao.getRobotBaseInfoList(startIndex, rows);
		Map<String, Integer> map = robotBaseInfoDao.getSumRobots();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("rows", list);
		resultMap.putAll(map);
		return resultMap;
	}

	@Override
	public Map<String, Object> reAllRobotRedis() {
		Map<String,Object> resMap = new HashMap<String,Object>();
		List<RobotBaseInfo> list = robotBaseInfoDao.getAllRobotBaseInfoList();
		RedisOperat.getInstance().del(RedisContant.host, RedisContant.port6379, RedisContant.keyRobotBaseInfoList);
		RedisOperat.getInstance().del(RedisContant.host, RedisContant.port6379, RedisContant.keyRobotUidSet);
		for(RobotBaseInfo bean : list) {
			RedisOperat.getInstance().hset(RedisContant.host, RedisContant.port6379, RedisContant.keyRobotBaseInfoList, String.valueOf(bean.getUid()), JSONObject.toJSONString(bean), 0);
			//用于redis随机获取机器人
			RedisOperat.getInstance().sadd(RedisContant.host, RedisContant.port6379, RedisContant.keyRobotUidSet, String.valueOf(bean.getUid()));
		}
		resMap.put("code", "200");
		resMap.put("msg", "更新缓存成功");
		return resMap;
	}

	@Override
	public Map<String, Object> updateRobot(RobotBaseInfo robot) {
		robotBaseInfoDao.updateByPrimaryKeySelective(robot);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(ConstantsAction.RESULT, true);
		map.put(ConstantsAction.MSG, "修改成功");
		return map;
	}

	@Override
	public Map<String, Object> saveRobot(RobotBaseInfo robot) {
		robotBaseInfoDao.insertSelective(robot);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(ConstantsAction.RESULT, true);
		map.put(ConstantsAction.MSG, "保存成功");
		return map;
	}
}
