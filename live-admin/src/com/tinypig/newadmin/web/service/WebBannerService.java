package com.tinypig.newadmin.web.service;

import java.util.List;

import com.tinypig.newadmin.web.entity.WebBanner;
import com.tinypig.newadmin.web.vo.WebBannerVo;

public interface WebBannerService {

	Integer selectCount(WebBanner banner);
	
	List<WebBannerVo> selectList(WebBannerVo banner) ;
	
	Boolean deleteByPrimaryKey(Integer id);
	
	WebBannerVo selectByPrimaryKey(Integer id);
	
	Boolean updateByObject(WebBanner banner);
	
	Boolean insertByObject(WebBanner banner);
	
	Boolean checkSheduleTime(WebBanner banner);
}
