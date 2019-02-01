package com.tinypig.newadmin.web.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tinypig.admin.redis.service.RedisOperat;
import com.tinypig.admin.util.RedisContant;
import com.tinypig.newadmin.web.dao.SmashedEggGiftConfigDao;
import com.tinypig.newadmin.web.entity.SmashedEggGiftConfig;
import com.tinypig.newadmin.web.service.ISmashedEggGiftConfigService;

@Service
public class SmashedEggGiftConfigServiceImpl implements ISmashedEggGiftConfigService {

	@Autowired
	private SmashedEggGiftConfigDao smashedEggGiftConfigDao;

	@Override
	public List<SmashedEggGiftConfig> getGiftList(Integer hammerType, Integer page, Integer rows) {
		Integer startIndex = (page - 1) * rows;
		return smashedEggGiftConfigDao.getGiftList(hammerType, startIndex, rows);
	}

	@Override
	public int saveSmashedEggGiftConfig(SmashedEggGiftConfig smashedEggGiftConfig) {
		return smashedEggGiftConfigDao.saveSmashedEggGiftConfig(smashedEggGiftConfig);
	}

	@Override
	public int updateByPrimaryKeySelective(SmashedEggGiftConfig smashedEggGiftConfig) {
		return smashedEggGiftConfigDao.updateByPrimaryKeySelective(smashedEggGiftConfig);
	}

	@Override
	public Map<String, Object> getSumProbability(Integer hammerType, Long id) {
		return smashedEggGiftConfigDao.getSumProbability(hammerType, id);
	}

	@Override
	public Map<String, Object> reSmashedEggGiftConfigRedis() {
		Map<String, Object> map = new HashMap<String, Object>();
		RedisOperat.getInstance().del(RedisContant.host, RedisContant.port6379, RedisContant.SmashedEggGiftCfgList);
		for(Integer i = 1;i<4;i++) {//共3个锤子
			List<SmashedEggGiftConfig> eggGiftConfigList = smashedEggGiftConfigDao.getSmashedEggGiftConfig(i);
			RedisOperat.getInstance().hset(RedisContant.host, RedisContant.port6379, RedisContant.SmashedEggGiftCfgList,""+i,JSONObject.toJSONString(eggGiftConfigList),0);
		}
		map.put("code", "200");
		return map;
	}

	@Override
	public int updateNoFirstPrize(Integer hammerType, Long id) {
		return smashedEggGiftConfigDao.updateNoFirstPrize(hammerType, id);
	}

	@Override
	public int delSmashedEggGiftConfig(Long id) {
		return smashedEggGiftConfigDao.deleteByPrimaryKey(id);
	}
}
