package com.tinypig.newadmin.web.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tinypig.admin.redis.service.RedisOperat;
import com.tinypig.admin.util.JsonUtil;
import com.tinypig.admin.util.RedisContant;
import com.tinypig.newadmin.web.dao.IosVersionDao;
import com.tinypig.newadmin.web.entity.IosVersion;
import com.tinypig.newadmin.web.service.IIosVersionService;

@Service
@Transactional
public class IosVersionService implements IIosVersionService {

	@Autowired
	private IosVersionDao iosVersionDao;

	@Override
	public List<IosVersion> getIosVersionList(Integer page, Integer rows) {
		Integer startIndex = (page - 1) * rows;
		return iosVersionDao.getIosVersionList(startIndex, rows);
	}

	@Override
	public List<IosVersion> getAllIosVersion() {
		return iosVersionDao.getAllIosVersion();
	}

	@Override
	public int saveIosVersion(IosVersion iosVersion) {
		return iosVersionDao.insertSelective(iosVersion);
	}

	@Override
	public int updateIosVersion(IosVersion iosVersion) {
		return iosVersionDao.updateByPrimaryKeySelective(iosVersion);
	}

	@Override
	public Map<String, Object> refreshRedisCache() {
		Map<String, Object> mapResult = new HashMap<String, Object>();
		mapResult.put("code", 200);
		try {
			List<IosVersion> list = iosVersionDao.getAllIosVersion();
			if (null != list && list.size() > 0) {
				for (IosVersion iosVersion : list) {
					RedisOperat.getInstance().set(RedisContant.host, RedisContant.port6379,RedisContant.verifyIos + ":" + iosVersion.getVersion(), JsonUtil.toJson(iosVersion));
				}
			}
			mapResult.put("msg", "刷新成功");
		} catch (Exception e) {
			mapResult.put("code", 201);
			mapResult.put("msg", "刷新redis异常");
			e.printStackTrace();
		}
		return mapResult;
	}

	@Override
	public int delIosVersion(Long id) {
		return iosVersionDao.deleteByPrimaryKey(id);
	}

}
