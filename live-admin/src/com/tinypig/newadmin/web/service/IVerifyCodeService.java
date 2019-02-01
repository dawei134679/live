package com.tinypig.newadmin.web.service;

import java.util.Map;

import com.tinypig.newadmin.web.entity.VerifyCodeParamDto;

public interface IVerifyCodeService {

	Map<String,Object> getVerifyCodeList(VerifyCodeParamDto params);

}
