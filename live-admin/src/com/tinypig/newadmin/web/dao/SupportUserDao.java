package com.tinypig.newadmin.web.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.tinypig.newadmin.web.entity.SupportUser;
import com.tinypig.newadmin.web.entity.SupportUserDto;
import com.tinypig.newadmin.web.entity.SupportUserParamDto;

public interface SupportUserDao {
	int deleteByPrimaryKey(Long id);

	int insert(SupportUser record);

	int insertSelective(SupportUser record);

	SupportUser selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(SupportUser record);

	int updateByPrimaryKey(SupportUser record);

	List<SupportUserDto> getSupportUserList(SupportUserParamDto params);

	Integer getSupportUsersTotal(SupportUserParamDto params);

	int doValid(@Param("id") Long id, @Param("status") Integer status, @Param("updateUserId") Long updateUserId,
			@Param("updateTime") Long updateTime);

	List<Map<String, Object>> getAllSupportUserList(SupportUserParamDto param);

	SupportUser findExistByUid(@Param("uid")Integer uid, @Param("excludeId")Long excludeId);
}