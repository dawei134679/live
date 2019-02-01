package com.tinypig.newadmin.web.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.tinypig.newadmin.web.entity.RobotBaseInfo;

public interface RobotBaseInfoDao {
	
    int deleteByPrimaryKey(Long uid);

    int insert(RobotBaseInfo record);

    int insertSelective(RobotBaseInfo record);

    RobotBaseInfo selectByPrimaryKey(Long uid);

    int updateByPrimaryKeySelective(RobotBaseInfo record);

    int updateByPrimaryKey(RobotBaseInfo record);
    
    List<RobotBaseInfo> getRobotBaseInfoRandom(Integer num);
    
    List<RobotBaseInfo> getRobotBaseInfoList(@Param("startIndex")Integer startIndex, @Param("rows")Integer rows);
    
    Map<String, Integer> getSumRobots();
    
    List<RobotBaseInfo> getAllRobotBaseInfoList();
}