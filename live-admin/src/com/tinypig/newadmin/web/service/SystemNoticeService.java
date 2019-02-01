package com.tinypig.newadmin.web.service;

import java.util.List;

import com.tinypig.newadmin.web.entity.SystemNotice;
import com.tinypig.newadmin.web.entity.WebBanner;
import com.tinypig.newadmin.web.vo.WebBannerVo;

public interface SystemNoticeService {

    List<SystemNotice> selectList();
    
    Integer selectCount();
    
    SystemNotice selectByPrimaryKey(Byte id);
    
    Boolean updateByObject(SystemNotice notice);
	
	Boolean insertByObject(SystemNotice notice);
	
	Boolean deleteByPrimaryKey(Byte id);

}
