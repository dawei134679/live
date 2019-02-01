package com.tinypig.newadmin.web.service;

import java.util.List;
import java.util.Map;

import com.tinypig.newadmin.web.entity.SmashedEggGiftConfig;

public interface ISmashedEggGiftConfigService {

	List<SmashedEggGiftConfig> getGiftList(Integer hammerType, Integer page, Integer rows);

	int saveSmashedEggGiftConfig(SmashedEggGiftConfig smashedEggGiftConfig);

	int updateByPrimaryKeySelective(SmashedEggGiftConfig smashedEggGiftConfig);

	/**
	 * 获取中奖概率总和
	 * @param hammerType 锤子类型
	 * @param gid		  要排除的礼物ID
	 * @return
	 */
	Map<String, Object> getSumProbability(Integer hammerType, Long id);

	/**
	 * 刷新redis配置信息
	 * @return
	 */
	Map<String, Object> reSmashedEggGiftConfigRedis();

	int updateNoFirstPrize(Integer hammerType, Long id);

	int delSmashedEggGiftConfig(Long id);
}
