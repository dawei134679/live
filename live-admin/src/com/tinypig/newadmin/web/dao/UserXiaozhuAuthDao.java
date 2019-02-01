package com.tinypig.newadmin.web.dao;

import java.util.List;
import java.util.Map;

import com.tinypig.newadmin.web.entity.UserXiaozhuAuth;
import com.tinypig.newadmin.web.entity.UserXiaozhuAuthWithBLOBs;

public interface UserXiaozhuAuthDao {
    int deleteByPrimaryKey(Integer id);

    int insert(UserXiaozhuAuthWithBLOBs record);

    int insertSelective(UserXiaozhuAuthWithBLOBs record);

    UserXiaozhuAuthWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserXiaozhuAuthWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(UserXiaozhuAuthWithBLOBs record);

    int updateByPrimaryKey(Map<String, Object> map);
    
    Integer selectCount(UserXiaozhuAuth auth);
	
	List<UserXiaozhuAuth> selectList(UserXiaozhuAuth auth);
	
	int rejectByPrimaryKey(Map<String, Object> map);
	
	UserXiaozhuAuth checkNickName(String nickname);
}