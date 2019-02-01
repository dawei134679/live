package com.mpig.api.service;

import com.mpig.api.model.UserAccountModel;
import com.mpig.api.model.UserAssetModel;
import com.mpig.api.model.UserBaseInfoModel;

public interface IUserInfoService {
	
	/**
	 * 根据UID将用户信息同步到userInfo表中
	 * @param uid
	 * @return
	 */
	public int saveUserInfoByUid(int uid);
	
	/**
	 * 更新userInfo表中的用户基本信息
	 * @param userBaseInfoModel
	 * @return
	 */
	public int updateUserInfoBase(UserBaseInfoModel userBaseInfoModel);
	
	/**
	 * 更新userInfo表中的用户账户信息
	 * @param userAccountModel
	 * @return
	 */
	public int updateUserInfoAccount(UserAccountModel userAccountModel);
	
	/**
	 * 更新userInfo表中的用户资产信息
	 * @param userAssetModel
	 * @return
	 */
	public int updateUserInfoBase(UserAssetModel userAssetModel);

	/**
	 * 更新userInfo表中的用户丛植总金额moneyRMB
	 * @param uid
	 * @param moneyRMB
	 * @return
	 */
	int updateUserInfoMoneyRMB(Integer uid, Double moneyRMB);

}
