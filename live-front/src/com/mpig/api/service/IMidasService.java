package com.mpig.api.service;

import com.mpig.api.model.ReturnModel;

public interface IMidasService {

	/**
	 * 米大师充值回调，查询余额并扣除，缓存至本地
	 * @param openid
	 * @param openkey
	 * @param pf
	 * @param pfkey
	 * @param type 1:qq 2:weixin
	 */
	public void pay(Integer uid, String openid, String openkey, String pf, String pfkey, String type, ReturnModel returnModel);
}
