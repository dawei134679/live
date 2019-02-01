package com.tinypig.newadmin.web.dao;

import java.util.List;

import com.tinypig.newadmin.web.entity.WebBanner;
import com.tinypig.newadmin.web.vo.WebBannerVo;

public interface WebBannerDao {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(WebBanner record);

    WebBannerVo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WebBanner record);
    
    Integer selectCount(WebBanner banner);
	
	List<WebBannerVo> selectList(WebBannerVo banner);
	
	List<WebBannerVo> selectADVList(WebBanner banner);

}