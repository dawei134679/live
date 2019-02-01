package com.tinypig.newadmin.web.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tinypig.admin.dao.UserDao;
import com.tinypig.admin.redis.service.OtherRedisService;
import com.tinypig.admin.redis.service.UserRedisService;
import com.tinypig.admin.util.Constant;
import com.tinypig.admin.util.HttpUtil;
import com.tinypig.newadmin.web.dao.UserXiaozhuAuthDao;
import com.tinypig.newadmin.web.entity.UserXiaozhuAuth;
import com.tinypig.newadmin.web.entity.UserXiaozhuAuthWithBLOBs;
import com.tinypig.newadmin.web.service.XiaozhuAuthService;
@Service
public class XiaozhuAuthServiceImpl implements XiaozhuAuthService {
	
	@Autowired
	private UserXiaozhuAuthDao authDao;


	@Override
	public Integer selectCount(UserXiaozhuAuth auth) {
		return authDao.selectCount(auth);
	}

	@Override
	public List<UserXiaozhuAuth> selectList(UserXiaozhuAuth auth) {
		return authDao.selectList(auth);
	}

	@Override
	public UserXiaozhuAuthWithBLOBs selectByPrimaryKey(Integer id) {
		return authDao.selectByPrimaryKey(id);
	}

	@Override
	public Boolean updateByPrimaryKey(Map<String, Object> map) {
		int result = authDao.updateByPrimaryKey(map);
		if(result == 1){
			int id = Integer.parseInt(map.get("id").toString());
			UserXiaozhuAuthWithBLOBs xiaozhuAuth =  authDao.selectByPrimaryKey(id);
			//修改小猪认证缓存,修改为通过3
			updateXiaozhuAuth(xiaozhuAuth.getUid(), 3);
			//修改用户基础信息数据库
			new UserDao().updateUserBaseInfo(xiaozhuAuth.getUid(), xiaozhuAuth.getNickname(), xiaozhuAuth.getAuthcontent());
			//修改用户基础信息缓存
			updateUserAuth(xiaozhuAuth.getUid(), xiaozhuAuth.getAuthcontent(), xiaozhuAuth.getNickname());
			
			try {
				updateEs(xiaozhuAuth.getUid(), xiaozhuAuth.getNickname());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}
		else
			return false;
	}

	@Override
	public Boolean rejectByPrimaryKey(Map<String, Object> map) {
		int result = authDao.rejectByPrimaryKey(map);
		if(result == 1){
			int id = Integer.parseInt(map.get("id").toString());
			UserXiaozhuAuthWithBLOBs xiaozhuAuth =  authDao.selectByPrimaryKey(id);
			//修改小猪认证缓存,修改为驳回2
			updateXiaozhuAuth(xiaozhuAuth.getUid(), 2);
			return true;
		}
		else
			return false;
	}
	
	//修改小猪认证缓存
	private void updateXiaozhuAuth(int uid, int status){
		OtherRedisService.getInstance().updateXiaozhuAuth(uid, status);
	}
	
	//修改用户基础信息认证部分缓存
	private void updateUserAuth(int uid, String authContent, String nickname){
		String userAuth = UserRedisService.getInstance().getUserBaseInfo(uid);
		JSONObject jsonObject = JSONObject.parseObject(userAuth);
		jsonObject.put("verified", true);
		jsonObject.put("verified_reason", authContent);
		jsonObject.put("nickname", nickname);
		UserRedisService.getInstance().setUserBaseInfo(uid, jsonObject.toJSONString());
	}

	@Override
	public UserXiaozhuAuth checkNickName(String nickname) {
		return authDao.checkNickName(nickname);
	}
	
	public static void updateEs(Integer filedName,String filedValue) throws Exception {
		String url = "http://"+Constant.es_ip+":"+Constant.es_port+"/all/user/"+filedName+"/_update";
		JSONObject json = new JSONObject();
		json.put("nickname", filedValue);
		JSONObject jsonc = new JSONObject();
		jsonc.put("doc", json);
		HttpUtil.postBody(url, jsonc.toString());
	}
	
}
