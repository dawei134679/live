package com.mpig.api.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.mpig.api.dao.IUserVipInfoDao;
import com.mpig.api.model.UserVipInfoModel;
import com.mpig.api.redis.service.UserRedisService;
import com.mpig.api.service.IUserVipInfoService;
import com.mpig.api.utils.RedisContant;

@Service
public class UserVipInfoServiceImpl implements IUserVipInfoService{

	@Autowired
	private IUserVipInfoDao userVipInfoDao;
	
	@Override
	public UserVipInfoModel getUserVipInfo(int uid, Boolean directReadMysql) {
		UserVipInfoModel userVipInfoModel = null;
		String userVipKey = RedisContant.userVip+uid;
		if (!directReadMysql) {
			String userVipStr = UserRedisService.getInstance().get(userVipKey);
			if(StringUtils.isNotEmpty(userVipStr)){
				userVipInfoModel = (UserVipInfoModel) JSONObject.parseObject(userVipStr, UserVipInfoModel.class);
			}
		}
		if(userVipInfoModel == null){
			userVipInfoModel = userVipInfoDao.getUserVipInfo(uid);
			if(userVipInfoModel!=null){
				UserRedisService.getInstance().set(userVipKey, JSONObject.toJSONString(userVipInfoModel));
				long nowtime = System.currentTimeMillis() / 1000;
				int endtime = userVipInfoModel.getEndtime();
				if (nowtime <= endtime) {
					long expiretime = endtime - nowtime;
					UserRedisService.getInstance().expire(userVipKey, (int) expiretime);
				}
			}
		}
		return userVipInfoModel;
	}

	@Override
	public UserVipInfoModel getUserVipInfoByUid(int uid, int gid) {
		return userVipInfoDao.getUserVipInfoByUid(uid, gid);
	}

	@Override
	public int addUserVipInfo(int uid, int gid, int starttime, int endtime,
			int isdel) {
		return userVipInfoDao.addUserVipInfo(uid, gid, starttime, endtime, isdel);
	}

	@Override
	public int updUserVipInfo(int uid, int starttime, int endtime, int isdel, int gid) {
		return userVipInfoDao.updUserVipInfo(uid, starttime, endtime, isdel, gid);
	}
	
}
