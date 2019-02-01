package com.tinypig.newadmin.web.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tinypig.admin.redis.service.RedisOperat;
import com.tinypig.admin.util.RedisContant;
import com.tinypig.newadmin.web.dao.SmashedEggConfigDao;
import com.tinypig.newadmin.web.service.ISmashedEggConfigService;

@Service
public class SmashedEggConfigServiceImpl implements ISmashedEggConfigService {
	@Autowired
	private SmashedEggConfigDao smashedEggConfigDao;

	@Override
	public Map<String, Object> reSmashedEggConfigRedis() {
		Map<String, Object> map = new HashMap<String, Object>();
		RedisOperat.getInstance().del(RedisContant.host, RedisContant.port6379, RedisContant.SmashedEggMoneyCfgList);
		List<Map<String, Object>> result = smashedEggConfigDao.getSmashedEggMoneyConfig();
		if(null != result && result.size() > 0) {
			String smashed1money = result.get(0).get("smashed1money") + "";
			String smashed2money = result.get(0).get("smashed2money") + "";
			String smashed3money = result.get(0).get("smashed3money") + "";
			RedisOperat.getInstance().hset(RedisContant.host, RedisContant.port6379, RedisContant.SmashedEggMoneyCfgList,"1", smashed1money, 0);
			RedisOperat.getInstance().hset(RedisContant.host, RedisContant.port6379, RedisContant.SmashedEggMoneyCfgList,"2", smashed2money, 0);
			RedisOperat.getInstance().hset(RedisContant.host, RedisContant.port6379, RedisContant.SmashedEggMoneyCfgList,"3", smashed3money, 0);
			map.put("code", "200");
			map.put("msg", "更新成功");
		}else {
			map.put("code", "500");
			map.put("msg", "更新失败");
		}
		return map;
	}

	@Override
	public List<Map<String, Object>> getSmashedEggMoneyConfig() {
		return smashedEggConfigDao.getSmashedEggMoneyConfig();
	}

	@Override
	public int saveSmashedEggConfig(Long id,Long smashed1money, Long smashed2money, Long smashed3money) {
		if(null != id) {
			return smashedEggConfigDao.updateSmashedEggConfig(id,smashed1money,smashed2money,smashed3money);
		}else {
			return smashedEggConfigDao.insertSmashedEggConfig(smashed1money,smashed2money,smashed3money);
		}
	}

}
