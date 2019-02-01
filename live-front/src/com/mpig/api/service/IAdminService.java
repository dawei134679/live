package com.mpig.api.service;

import com.mpig.api.model.ReturnModel;

public interface IAdminService {

	/**
	 * 添加VIP
	 * @param dstuid
	 * @param gid
	 * @param count 单位/天
	 * @param returnModel
	 * @return
	 */
	ReturnModel addVip(Integer dstuid,Integer gid,Integer count, ReturnModel returnModel);
	
	/**
	 * 添加守护
	 * @param srcuid 用户id
	 * @param dstuid 房间id
	 * @param gid
	 * @param count 单位/天
	 * @param returnModel
	 * @return
	 */
	ReturnModel addGuard(Integer srcuid, Integer dstuid, Integer gid, Integer count, ReturnModel returnModel);
	
	/**
	 * 添加座驾
	 * @param dstuid
	 * @param gid
	 * @param count 单位/天
	 * @param returnModel
	 * @return
	 */
	ReturnModel addCar(Integer dstuid,Integer gid,Integer count, ReturnModel returnModel);
	
	/**
	 * 添加经验
	 * @param dstuid
	 * @param exp 任务经验值
	 * @param wealths 用户财富值
	 * @param returnModel
	 * @return
	 */
	ReturnModel addExp(Integer dstuid,Integer exp, ReturnModel returnModel);
	
	/**
	 * 添加背包礼物
	 * @param dstuid
	 * @param gid
	 * @param count
	 * @param returnModel
	 * @return
	 */
	ReturnModel addGift(Integer dstuid, Integer gid, Integer count, ReturnModel returnModel);
	
	/**
	 * 增加金币
	 * @param uid 		被操作的用户uid
	 * @param zhutou	要增加的猪头数量
	 * @param credit	要增加的声援值（type=0此参数无效）
	 * @param type		类型 1 添加 2 消费
	 * @param ReturnModel returnModel
	 * @return
	 */
	ReturnModel modifyMoney(int uid,int zhutou,int credit,int type,String desc, ReturnModel returnModel);
	
}
