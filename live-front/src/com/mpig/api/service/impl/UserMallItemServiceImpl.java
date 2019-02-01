package com.mpig.api.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.mpig.api.dao.IUserMallItemDao;
import com.mpig.api.model.UserMallItemModel;
import com.mpig.api.redis.service.UserRedisService;
import com.mpig.api.service.IUserMallItemService;
import com.mpig.api.utils.RedisContant;

@Service
public class UserMallItemServiceImpl implements IUserMallItemService{

	@Autowired
	private IUserMallItemDao userMallItemDao;
	@Override
	public int addUserMallItem(int uid, int gid, int type, int subtype,
			int num, int starttime, int endtime) {
		return userMallItemDao.addUserMallItem(uid, gid, type, subtype, num, starttime, endtime);
	}

	@Override
	public int updUserMallItem(int uid, int subtype, int starttime, int endtime) {
		return userMallItemDao.updUserMallItem(uid, subtype, starttime, endtime);
	}

	@Override
	public UserMallItemModel getItem(Integer uid, Integer subtype,Boolean directReadMysql) {
		UserMallItemModel userMallItemModel = null;
		String itemKey = "";
		if(subtype==1){ //萌猪
			itemKey = RedisContant.mengzhucard + uid;
		}else{
			return userMallItemModel;
		}
		if (!directReadMysql) {
			// 读缓存
			String userMallItemStr = UserRedisService.getInstance().get(itemKey);
			if (StringUtils.isNotEmpty(userMallItemStr)) {
				userMallItemModel = (UserMallItemModel) JSONObject.parseObject(userMallItemStr, UserMallItemModel.class);
			}
		}
		if (userMallItemModel == null) {
			//通过数据库查询获取守护信息
			userMallItemModel = userMallItemDao.getItemBySubtype(uid, subtype);
			if(userMallItemModel!= null){
				UserRedisService.getInstance().set(itemKey, JSONObject.toJSONString(userMallItemModel));
				long endtime = userMallItemModel.getEndtime();
				long nowtime = System.currentTimeMillis() / 1000;
				if (nowtime <= endtime) {
					long expiretime = endtime - nowtime;
					UserRedisService.getInstance().expire(itemKey, (int) expiretime);
				}
			}
		}
		return userMallItemModel;
	}

	@Override
	public UserMallItemModel getUserAllMallItemBySubtype(Integer uid, Integer subtype) {
		return userMallItemDao.getAllItemBySubtype(uid, subtype);
	}

	
	
	
}
