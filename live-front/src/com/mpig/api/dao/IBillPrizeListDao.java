package com.mpig.api.dao;

import java.util.List;

import com.mpig.api.model.BillPrizeListModel;

public interface IBillPrizeListDao {
	/**
	 * 获取当前用户的所有中奖订单信息
	 * @param uid 用户id
	 * @return
	 */
	List<BillPrizeListModel> getListByUser(int uid);
}
