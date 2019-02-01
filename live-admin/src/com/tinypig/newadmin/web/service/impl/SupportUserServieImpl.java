package com.tinypig.newadmin.web.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.tinypig.admin.redis.service.RedisOperat;
import com.tinypig.admin.util.RedisContant;
import com.tinypig.newadmin.common.ConstantsAction;
import com.tinypig.newadmin.web.dao.SupportUserDao;
import com.tinypig.newadmin.web.entity.SupportUser;
import com.tinypig.newadmin.web.entity.SupportUserDto;
import com.tinypig.newadmin.web.entity.SupportUserParamDto;
import com.tinypig.newadmin.web.service.SupportUserServie;

@Service
public class SupportUserServieImpl implements SupportUserServie{
	@Autowired
	private SupportUserDao supportUserDao;

	@Override
	public Map<String, Object> getSupportUserList(SupportUserParamDto params) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<SupportUserDto> list = supportUserDao.getSupportUserList(params);
		resultMap.put("rows", list);
		resultMap.put("total",supportUserDao.getSupportUsersTotal(params));
		return resultMap;
	}

	@Override
	public Map<String, Object> saveSupportUser(SupportUser supportUser) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		SupportUser s = supportUserDao.findExistByUid(supportUser.getUid(),null);
		if(s != null) {
			resultMap.put(ConstantsAction.RESULT, false);
			resultMap.put(ConstantsAction.MSG, "扶持用户ID已存在");
			return resultMap;
		}
		try {
			int row = supportUserDao.insertSelective(supportUser);
			if(row != 1) {
				resultMap.put(ConstantsAction.RESULT, false);
				resultMap.put(ConstantsAction.MSG, "保存失败");
			}else {
				RedisOperat.getInstance().sadd(RedisContant.host, RedisContant.port6379, RedisContant.supportUsersID, supportUser.getUid().toString());
				resultMap.put(ConstantsAction.RESULT, true);
				resultMap.put(ConstantsAction.MSG, "保存成功");
			}
		} catch (DuplicateKeyException e) {
			e.printStackTrace();
			resultMap.put(ConstantsAction.RESULT, false);
			resultMap.put(ConstantsAction.MSG, "保存失败，扶持用户ID已存在");
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> updateSupportUser(SupportUser supportUser) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		SupportUser s = supportUserDao.findExistByUid(supportUser.getUid(),supportUser.getId());
		if(s != null) {
			resultMap.put(ConstantsAction.RESULT, false);
			resultMap.put(ConstantsAction.MSG, "扶持用户ID已存在");
			return resultMap;
		}
		int row = supportUserDao.updateByPrimaryKeySelective(supportUser);
		if(row != 1) {
			resultMap.put(ConstantsAction.RESULT, false);
			resultMap.put(ConstantsAction.MSG, "修改失败");
		}
		resultMap.put(ConstantsAction.RESULT, true);
		resultMap.put(ConstantsAction.MSG, "修改成功");
		return resultMap;
	}

	@Override
	public Map<String, Object> doValid(Long id, Integer uid, Integer status, Long updateUserId, Long updateTime) {
		int row = supportUserDao.doValid(id, status, updateUserId, updateTime);
		if(row ==1) {
			if(status == 1) {
				RedisOperat.getInstance().sadd(RedisContant.host, RedisContant.port6379, RedisContant.supportUsersID, uid.toString());
			}else {
				RedisOperat.getInstance().srem(RedisContant.host,  RedisContant.port6379, RedisContant.supportUsersID, uid.toString());
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("row", row);
		return map;
	}

	@Override
	public List<Map<String, Object>> getAllSupportUserList(SupportUserParamDto param) {
		return supportUserDao.getAllSupportUserList(param);
	}
	
	
}
