package com.mpig.api.service;

public interface IUserTransactionHisService {

	public int saveUserTransactionHis(Integer transType,Integer uid,Double amount,Long money,Long createTime,String refId,Integer dataType);
}
