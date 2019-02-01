package com.mpig.api.service;

import java.util.List;
import java.util.Map;

public interface IPayOrderService {
	/**
	 * 根据申请账获取当前账期是否提交过
	 * @param uid
	 * @param ymtime
	 * @return
	 */
	int getAnchorSalaryCountForMonth(Integer uid, Integer ymtime);
	
	
	/**
	 * 根据用户查找兑换账单
	 * @param uid
	 * @return
	 */
	List<Object> getAnchorSalaryByUid(Integer uid);
	
	/**
	 * 根据用户id获取艺人的提成信息
	 * @param anchorId
	 * @return
	 */
	Map<String, Object> getUnionAnchorByAnchorid(Integer anchorId);
	
	/**
	 * 插入兑换申请记录
	 * @param ymtime
	 * @param uid
	 * @param unionid
	 * @param credits
	 * @param rate
	 * @param salary
	 * @param operatebak
	 * @return
	 */
	int insPayAnchorSalary(Integer ymtime, Integer uid, Integer unionid, Integer credits);
}
