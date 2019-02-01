package com.mpig.api.service;

import com.mpig.api.model.PrizeModel;
import com.mpig.api.model.ReturnModel;

public interface IActivitiesModeService {
	/*
	 * 领取对应奖励
	 */
	public PrizeModel acceptPrize(int uid , String prize);

	/*
	 * 获取活动按照天快照
	 */
	public void amtopsnap(int srcuid,  long startsec,long endsec, String amname,ReturnModel rt);
}
