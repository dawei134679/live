package com.tinypig.newadmin.web.dao;

import java.util.List;

import com.tinypig.newadmin.web.entity.VerifyCode;
import com.tinypig.newadmin.web.entity.VerifyCodeParamDto;

public interface VerifyCodeDao {
	int deleteByPrimaryKey(Long id);

	int insert(VerifyCode record);

	int insertSelective(VerifyCode record);

	VerifyCode selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(VerifyCode record);

	int updateByPrimaryKey(VerifyCode record);

	List<VerifyCode> getVerifyCodeListPage(VerifyCodeParamDto params);

	Integer getVerifyCodeTotal(VerifyCodeParamDto params);

}