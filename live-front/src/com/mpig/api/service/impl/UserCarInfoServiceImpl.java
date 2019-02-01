package com.mpig.api.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.mpig.api.dao.IUserCarInfoDao;
import com.mpig.api.model.UserCarInfoModel;
import com.mpig.api.redis.service.UserRedisService;
import com.mpig.api.service.IUserCarInfoService;
import com.mpig.api.utils.RedisContant;

@Service
public class UserCarInfoServiceImpl implements IUserCarInfoService{

	@Autowired
	private IUserCarInfoDao userCarInfoDao;
	
	
	@Override
	public List<UserCarInfoModel> getUserValidCars(Integer uid) {
		return userCarInfoDao.getUserValidCars(uid);
	}

	@Override
	public UserCarInfoModel getUserCarInfo(Integer uid, Integer gid, Boolean directReadMysql) {
		UserCarInfoModel userCarInfoModel = null;
		String userCarInfoKey = RedisContant.userCar+uid;
		if(!directReadMysql){
			String userCarStr = UserRedisService.getInstance().get(userCarInfoKey);
			if (StringUtils.isNotEmpty(userCarStr)) {
				userCarInfoModel = (UserCarInfoModel) JSONObject.parseObject(userCarStr, UserCarInfoModel.class);
			}
		}else{
			userCarInfoModel = userCarInfoDao.getUserCarInfoByGid(uid, gid);
			setRedisForCar(uid, gid, userCarInfoModel);
		}
		return userCarInfoModel;
	}


	@Override
	public int addUserCarInfo(Integer uid, Integer gid, Integer starttime, Integer endtime, Integer status) {
		int rec = userCarInfoDao.addUserCarInfo(uid, gid, starttime, endtime, status);
		if(rec >0 && status ==1){
			UserCarInfoModel userCarInfoModel = userCarInfoDao.getUserCarInfoByGid(uid, gid);
			setRedisForCar(uid, gid, userCarInfoModel);
		}
		return rec;
	}


	@Override
	public int updUserCarInfo(Integer uid, Integer gid, Integer starttime, Integer endtime, Integer status) {
		int rec = userCarInfoDao.updUserCarInfo(uid, gid, starttime, endtime, status);
		if(rec >0 && status == 1){
			UserCarInfoModel userCarInfoModel = userCarInfoDao.getUserCarInfoByGid(uid, gid);
			setRedisForCar(uid, gid, userCarInfoModel);
		}
		return rec;
	}
	
	@Override
	public int updUserCarInfoUnStatus(Integer uid) {
		return userCarInfoDao.updUserCarInfoUnStatus(uid);
	}
	
	@Override
	public int updUserCarInfoStatus(Integer uid,Integer gid, Integer type) {
		int rsc = 0;
		if(type==1){
			updUserCarInfoUnStatus(uid);
			rsc = userCarInfoDao.updUserCarInfoStatus(uid, gid, 1);
			UserCarInfoModel userCarInfoModel = userCarInfoDao.getUserCarInfoByGid(uid, gid);
			setRedisForCar(uid, gid, userCarInfoModel);
		}else{
			rsc = userCarInfoDao.updUserCarInfoStatus(uid, gid, 0);
			delRedisForCar(uid);
		}
		return rsc;
	}

	public void delRedisForCar(Integer uid){
		String userCarInfoKey = RedisContant.userCar+uid;
		UserRedisService.getInstance().del(userCarInfoKey);
	}
	
	public void setRedisForCar(Integer uid, Integer gid,UserCarInfoModel userCarInfoModel){
		String userCarInfoKey = RedisContant.userCar+uid;
		if(userCarInfoModel!= null){
			UserRedisService.getInstance().set(userCarInfoKey, JSONObject.toJSONString(userCarInfoModel));
			long cushiontime = userCarInfoModel.getEndtime();
			long nowtime = System.currentTimeMillis() / 1000;
			if (nowtime <= cushiontime) {
				long expiretime = cushiontime - nowtime;
				UserRedisService.getInstance().expire(userCarInfoKey, (int) expiretime);
			}
		}
	}
}
