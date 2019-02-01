package com.tinypig.newadmin.web.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.tinypig.newadmin.web.entity.ActivityGift;
import com.tinypig.newadmin.web.vo.ActivityGiftVo;

public interface ActivityGiftDao {
    int deleteByPrimaryKey(Integer id);

    int insert(ActivityGift record);

    int insertSelective(ActivityGift record);

    ActivityGiftVo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ActivityGift record);

    int updateByPrimaryKey(ActivityGift record);
    
    List<ActivityGiftVo> selectList(ActivityGiftVo gift);
    
    int selectCount(ActivityGiftVo gift);
    
    ArrayList<HashMap<String, Object>> getGiftList();
    
    ArrayList<HashMap<String, Object>> getAllGiftList();
}