package com.tinypig.newadmin.web.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.tinypig.newadmin.web.entity.ActivityGift;
import com.tinypig.newadmin.web.vo.ActivityGiftVo;

public interface ActivityGiftService {

    List<ActivityGiftVo> selectList(ActivityGiftVo gift);
    
    Integer selectCount(ActivityGiftVo gift);
    
    ActivityGiftVo selectByPrimaryKey(Integer id);
    
    Boolean updateByObject(ActivityGift gift);
	
	Boolean insertByObject(ActivityGift gift);
	
	Boolean deleteByPrimaryKey(Integer id);

	ArrayList<HashMap<String, Object>> getGiftList();
	
	ArrayList<HashMap<String, Object>> getAllGiftList();

}
