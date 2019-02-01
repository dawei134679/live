package com.tinypig.newadmin.web.service;

import java.util.List;

import com.tinypig.newadmin.web.entity.GiftLuckyProbabilitys;
import com.tinypig.newadmin.web.entity.SystemNotice;

public interface LuckyGiftService {

    List<GiftLuckyProbabilitys> selectList();
    
    Integer selectCount();
    
    GiftLuckyProbabilitys selectByPrimaryKey(Integer id);
    
    Boolean updateByObject(GiftLuckyProbabilitys gift);
	
	Boolean insertByObject(GiftLuckyProbabilitys gift);
	
	Boolean deleteByPrimaryKey(Integer id);
	
	Boolean checkMultiples(Integer multiples);

}
