package com.mpig.api.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.mpig.api.model.ConfigGiftActivityModel;
import com.mpig.api.model.ConfigGiftModel;
import com.mpig.api.model.ReturnModel;

public interface IConfigService {

	/**
	 * 获取礼物信息
	 * @param gid 礼物gid
	 * @param bl =true读取数据库 =false读取缓存
	 * @return
	 */
	public ConfigGiftModel getGiftInfoByGidNew(Integer gid);
	
	/**
	 * 获取坐骑的列表
	 * @return
	 */
	public ConcurrentHashMap<Integer, ConfigGiftModel> getConfigCarModels();
	/**
	 * 获取徽章列表
	 * @param gid 礼物gid
	 * @param bl =true读取数据库 =false读取缓存
	 * @return
	 */
	public List<Map<String, Object>> getBadges();
	/**
	 * 获取礼物列表
	 * 
	 * @param ver
	 *            app端的礼物版本号
	 * @param returnModel
	 */
	public void getGiftList(int ver, ReturnModel returnModel);
	
	/**
	 * 获取第三方流信息
	 * @param uid
	 * @return	返回uid对应的留地址
	 */
	public String getThirdStream(Integer uid);
	
	/**
	 * 获取礼物列表
	 * @param ver  app端的礼物版本号
	 * @param returnModel
	 */
	public void getGiftListNew(int ver,ReturnModel returnModel);

	/**
	 * 获取礼物列表
	 * @param uid 当前用户
	 * @param returnModel
	 */
	public void getGiftListPC(Integer uid,ReturnModel returnModel);
	
	/**
	 * 获取背包里的礼物列表
	 * @param uid
	 * @return
	 */
	public List<Map<String, Object>> getBaglists(Integer uid);
	
	/**
	 * 获取礼物列表
	 * @param ver  app端的礼物版本号
	 * @param returnModel
	 */
	public void getGiftListH5(ReturnModel returnModel);
	
	/**
	 * 获取活动列表
	 * @param bl
	 * @return
	 */
	public Map<Integer, ConfigGiftActivityModel> getGiftAct(boolean bl);
	
}
