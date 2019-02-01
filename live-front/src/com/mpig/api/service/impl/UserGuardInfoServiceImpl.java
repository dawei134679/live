package com.mpig.api.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.mpig.api.dao.IUserGuardExpRecordDao;
import com.mpig.api.dao.IUserGuardInfoDao;
import com.mpig.api.dao.impl.UserGuardInfoDaoImpl;
import com.mpig.api.dictionary.lib.LevelConfigLib;
import com.mpig.api.model.UserGuardInfoModel;
import com.mpig.api.model.ValueaddPrivilegeModel;
import com.mpig.api.redis.service.RedisCommService;
import com.mpig.api.redis.service.UserRedisService;
import com.mpig.api.service.IUserGuardInfoService;
import com.mpig.api.utils.DateUtils;
import com.mpig.api.utils.RedisContant;
import com.mpig.api.utils.ValueaddServiceUtil;

@Service
public class UserGuardInfoServiceImpl implements IUserGuardInfoService{

	private static final Logger logger = Logger.getLogger(UserGuardInfoDaoImpl.class);
	@Autowired
	private IUserGuardInfoDao userGuardInfoDao;
	@Autowired
	private IUserGuardExpRecordDao userGuardExpRecordDao;
	@Override
	public int addGuardExp(int uid, int roomid, int exp, int gid) {
		if (exp <= 0) {
			return -1;
		}
		try {
			int executeResult = userGuardInfoDao.addExpByUid(uid, roomid, exp,gid);
			if (executeResult > 0) {
				// 更新缓存
				UserGuardInfoModel userGuardInfoModel = this.getUserGuardInfo(uid, roomid, true);
				if (userGuardInfoModel != null) {
					// 调用升级方法
					boolean updLevel = updUserGuardLevel(uid, roomid, userGuardInfoModel.getGid(), userGuardInfoModel.getLevel(), userGuardInfoModel.getExp());
					if (!updLevel) {
						logger.error(String.format("addGuardExp updUserGuardLevel filaed uid:%d,add exp:%d", uid, exp));
					}
					return userGuardInfoModel.getExp();
				}
			}

		} catch (Exception e) {
			logger.error("<addUserExpByTask->Exception>" + e.toString());
		}
		return -1;
	
	}
	
	@Override
	public UserGuardInfoModel getUserGuardInfo(int uid, int roomid,Boolean directReadMysql) {
		UserGuardInfoModel userGuardInfoModel = null;
		String userGuardKey = RedisContant.userGuard + uid + ":" + roomid;
		if (!directReadMysql) {
			// 读缓存
			String userGuardStr = UserRedisService.getInstance().get(userGuardKey);
			if (StringUtils.isNotEmpty(userGuardStr)) {
				userGuardInfoModel = (UserGuardInfoModel) JSONObject.parseObject(userGuardStr, UserGuardInfoModel.class);
			}
		}
		if (userGuardInfoModel == null) {
			//通过数据库查询获取守护信息
			userGuardInfoModel = userGuardInfoDao.getUserGuardInfo(uid, roomid);
			if(userGuardInfoModel!= null){
				UserRedisService.getInstance().set(userGuardKey, JSONObject.toJSONString(userGuardInfoModel));
				long cushiontime = userGuardInfoModel.getCushiontime();
				long nowtime = System.currentTimeMillis() / 1000;
				if (nowtime <= cushiontime) {
					long expiretime = cushiontime - nowtime;
					UserRedisService.getInstance().expire(userGuardKey, (int) expiretime);
				}
			}
		}
		return userGuardInfoModel;
	}
	
	@Override
	public UserGuardInfoModel getUserGuardInfoByUid(int uid, int roomid, int gid){
		return userGuardInfoDao.getUserGuardInfoByUid(uid, roomid, gid);
	}
	
	@Override
	public boolean updUserGuardLevel(int uid, int roomid, int gid, int userLevel, int exp) {
		int ires = 0;

		int curLevel = LevelConfigLib.getValueaddSuitableLevel(gid, exp);

		System.err.println("updUserGuardLevel    userLevel=" + userLevel + " curLevel" + curLevel + " exp=" + exp);

		if (userLevel < curLevel) {
			// 升级
			ires = userGuardInfoDao.updUserGuardLevel(uid, roomid, curLevel, gid);
			return ires == 1 ? true : false;
		} else {
			// 未升级
			return false;
		}
	}

	@Override
	public int addUserGuardInfo(int roomid, int uid, int gid, int level,
			int exp, int starttime, int endtime, int cushiontime, int isdel) {
		return userGuardInfoDao.addUserGuardInfo(roomid, uid, gid, level, exp, starttime, endtime, cushiontime, isdel);
	}

	@Override
	public int updUserGuardInfo(int exp, int starttime, int endtime, int cushiontime,int isdel,int uid,int roomid, int gid) {
		return userGuardInfoDao.updUserGuardInfo(exp, starttime, endtime, cushiontime, isdel, uid, roomid, gid);
	}
	
	@Override
	public int firstLoginUpdExp(int uid) {
		String firstLogin = RedisCommService.getInstance().get(RedisContant.RedisNameOther, RedisContant.guardFirstLoginOfDay + uid);
		int mengzhuCount = 0;
		if (firstLogin == null) {
			List<UserGuardInfoModel> guardInfoModels = userGuardInfoDao.selUserAllGardInfo(uid);
			for (UserGuardInfoModel guardInfoModel : guardInfoModels) {
				RedisCommService.getInstance().incrBy(RedisContant.RedisNameOther, RedisContant.guardFirstLoginOfDay + uid, 1, DateUtils.getSecondeToNextDay());
				// 首次登陆增加守护经验值
				ValueaddPrivilegeModel privilegeModel = ValueaddServiceUtil.getPrivilege(guardInfoModel.getGid(), guardInfoModel.getLevel());
				addGuardExp(uid, guardInfoModel.getRoomid(), privilegeModel.getFirstLoginExp(),guardInfoModel.getGid());
				//插入成长值记录
				userGuardExpRecordDao.insExpRecord(uid, guardInfoModel.getRoomid(), guardInfoModel.getGid(), privilegeModel.getFirstSpendExp(), 1);
				mengzhuCount = mengzhuCount+privilegeModel.getRqCount();
			}
			
		}
		return mengzhuCount;
	}

	@Override
	public void firstSpendUpdExp(int uid, int roomid) {
		UserGuardInfoModel guardInfoModel = ValueaddServiceUtil.getGuardInfo(uid, roomid);
		if(guardInfoModel != null){
			String firstSpend = RedisCommService.getInstance().get(RedisContant.RedisNameOther,RedisContant.guardFirstSpendOfDay+uid);
			if(firstSpend == null){
				RedisCommService.getInstance().incrBy(RedisContant.RedisNameOther,RedisContant.guardFirstSpendOfDay+uid, 1, DateUtils.getSecondeToNextDay());
				//首次送礼增加守护经验值
				ValueaddPrivilegeModel privilegeModel = ValueaddServiceUtil.getPrivilege(guardInfoModel.getGid(), guardInfoModel.getLevel());
				addGuardExp(guardInfoModel.getUid(), guardInfoModel.getRoomid(), privilegeModel.getFirstSpendExp(), guardInfoModel.getGid());
				//插入成长值记录
				userGuardExpRecordDao.insExpRecord(uid,guardInfoModel.getRoomid(),guardInfoModel.getGid(),  privilegeModel.getFirstSpendExp(), 2);
			}
			
		}
	}

	@Override
	public List<UserGuardInfoModel> selUserAllGardInfoByRoomId(int roomid) {
		return userGuardInfoDao.selUserAllGardInfoByRoomId(roomid);
	}
	
}
