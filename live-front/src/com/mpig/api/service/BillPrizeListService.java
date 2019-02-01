package com.mpig.api.service;

import java.util.List;

import com.mpig.api.model.BillPrizeListModel;

public interface BillPrizeListService {
	
	/**
	 * 根据用户获取中奖详情
	 * @param uid
	 * @return
	 */
	public List<BillPrizeListModel> getListByUser(int uid);
}
