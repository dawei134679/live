package com.mpig.api.service;

public interface IVerifyCodeService {

	int insertVerifyCode(String mobile, String strType, int code, long takeEffectTime, long expiryTime);

}
