package com.mpig.api.service;

import java.util.List;
import java.util.Map;

import com.mpig.api.model.UserItemLogModel;
import com.mpig.api.model.UserItemModel;
import com.mpig.api.model.UserItemSpecialModel;

public interface IUserItemService {

	/**
	 * 获取用户背包的礼物 供app使用
	 * 
	 * @param uid
	 * @return list map<gid,num>
	 */
	List<Map<String, Object>> getBagByUid(int uid);

	Map<Integer,UserItemSpecialModel> getItemSpecialByUid(int uid, boolean bl);

	Map<Integer,UserItemModel> getItemListByUid(int uid, boolean bl);

	/**
	 * 用户送礼变更信息
	 *
	 * @param uid
	 * @param money
	 * @return
	 */
	int updUserItemBySendUid(int uid, int gid,int count);
	
	
	public enum ItemSource{
		Other,
		Task,
		Activity,
		Shop,
		rechargeActivity,
		smashedEgg,
		inviteReward,
		systemAdd,
		consumeLottery,
		newYearConsumeLottery
	}
	
	
	/**
	 * 添加背包
	 * @param uid
	 * @param gid
	 * @param num
	 * @param source =1首充 =2签到 =3任务
	 */
	void insertUserItem(final int uid, int gid, int num, ItemSource source);
	
	/**
	 * 获取用户徽章列表
	 * @param uid
	 * @return
	 */
	List<Integer> getBadgeListByUid(int uid,boolean bl);
	
	/**
	 * 查询某个用户背包里指定商品的数量
	 * @param uid
	 * @param gid
	 * @return
	 */
	int getItemCountByGid(Integer uid, Integer gid);
	
	/**
	 * 删除用户背包里指定的商品并更新缓存
	 * @param uid
	 * @param gid
	 * @return
	 */
	int delItemByGid(Integer uid, Integer gid);
	
	/**
	 * 获取当前用户某个类型的背包礼物记录信息
	 * @param subtype
	 * @param source
	 * @param uid
	 * @param start
	 * @param rows
	 * @return
	 */
	public List<Map<String,Object>> selItemLogBySubType(ItemSource source, int start, int rows);
	
	/**
	 * 插入背包数据 不写log等相关操作
	 * @param uid
	 * @param gid
	 * @param num
	 * @param source
	 */
	public void insertUserItemNew(int uid, int gid, int num);
	
	/**
	 * 写入背包log信息
	 * @param uid
	 * @param gid
	 * @param num
	 * @param source
	 * @return
	 */
	public int insertUserItemLog(int uid, int gid, int num, ItemSource source);
}
