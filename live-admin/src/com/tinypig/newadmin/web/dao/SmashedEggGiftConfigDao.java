package com.tinypig.newadmin.web.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.tinypig.newadmin.web.entity.SmashedEggGiftConfig;

public interface SmashedEggGiftConfigDao {

	List<SmashedEggGiftConfig> getGiftList(@Param("hammerType") Integer hammerType,
			@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

	int saveSmashedEggGiftConfig(SmashedEggGiftConfig smashedEggGiftConfig);

	int updateByPrimaryKeySelective(SmashedEggGiftConfig smashedEggGiftConfig);

	/**
	 * 根据锤子类型获取砸蛋游戏礼物配置信息
	 * @param hammerType 锤子类型
	 * @return
	 */
	List<SmashedEggGiftConfig> getSmashedEggGiftConfig(@Param("hammerType") Integer hammerType);

	/**
	 * 获取中奖概率总和
	 * @param hammerType 锤子类型
	 * @param gid		  要排除的礼物ID
	 * @return
	 */
	Map<String, Object> getSumProbability(@Param("hammerType") Integer hammerType, @Param("id") Long id);

	int updateNoFirstPrize(@Param("hammerType") Integer hammerType, @Param("id") Long id);
	
	int  deleteByPrimaryKey(Long id);
}
