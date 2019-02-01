package com.tinypig.newadmin.web.service.impl;

import java.util.Date;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.tinypig.admin.constant;
import com.tinypig.admin.model.BannerListModel;
import com.tinypig.admin.redis.service.OtherRedisService;
import com.tinypig.admin.util.DateUtil;
import com.tinypig.newadmin.common.HttpServerList;
import com.tinypig.newadmin.web.dao.WebBannerDao;
import com.tinypig.newadmin.web.entity.WebBanner;
import com.tinypig.newadmin.web.service.WebBannerService;
import com.tinypig.newadmin.web.vo.WebBannerVo;
@Service
public class WebBannerServiceImpl implements WebBannerService {
	
	@Autowired
	private WebBannerDao bannerDao;


	@Override
	public Integer selectCount(WebBanner banner) {
		return bannerDao.selectCount(banner);
	}

	@Override
	public List<WebBannerVo> selectList(WebBannerVo banner) {
		return bannerDao.selectList(banner);
	}

	@Override
	public Boolean deleteByPrimaryKey(Integer id) {
		int result = this.bannerDao.deleteByPrimaryKey(id);
		if(result == 1){
			delbannerredis(id);
			return true;
		}
		else
			return false;

	}

	@Override
	public WebBannerVo selectByPrimaryKey(Integer id) {
		return bannerDao.selectByPrimaryKey(id);
	}

	@Override
	public Boolean updateByObject(WebBanner banner) {
		//更新缓存
		int result = this.bannerDao.updateByPrimaryKeySelective(banner);
		if(result == 1){
			if(banner.getType() != 0){//轮播广告才更新缓存，开屏无须更新缓存
				if(banner.getSwi() == 1){//banner为显示状态
					delbannerredis(banner.getId());
					addbannerredis(banner.getId());
				}else{
					delbannerredis(banner.getId());
				}
				
			}
			
			return true;
		}else
			return false;
	}

	@Override
	public Boolean insertByObject(WebBanner banner) {
		// TODO 缓存
		if(banner!=null){
			banner.setId(null);
			banner.setCreateat(DateUtil.getTimeStamp(new Date()));
			Integer result = this.bannerDao.insertSelective(banner);
			if(result == 1){
				if(banner.getType() != 0 && banner.getSwi() == 1){//轮播广告才更新缓存，开屏无须更新缓存,并且banner需要为显示状态
					addbannerredis(banner.getId());
				}
				
				return true;
			}else{
				return false;
			}
				
		}
		return false;
	}

	public void addbannerredis(Integer id){
		WebBannerVo banner = bannerDao.selectByPrimaryKey(id);
		BannerListModel model = new BannerListModel();
		model.setId(id);
		model.setCreateAT(banner.getCreateat());
		if(banner.getEndshow()!=null)
		model.setEndShow(banner.getEndshow().intValue());
		model.setJumpUrl(banner.getJumpurl());
		model.setName(banner.getName());
		model.setPicUrl(banner.getPicurl());
		model.setSwi(banner.getSwi());
		model.setType(banner.getType());
		if(banner.getStartshow()!=null)
		model.setStartShow(banner.getStartshow().intValue());
		OtherRedisService.getInstance().setHomeBanner(id, JSONObject.toJSONString(model));
	}
	
	public void delbannerredis(Integer id){
		OtherRedisService.getInstance().delHomeBanner(id);
	}
	
	//开屏广告检查开始结束时间是否冲突
	@Override
	public Boolean checkSheduleTime(WebBanner banner){
		List<WebBannerVo> list = bannerDao.selectADVList(banner);
		if(list.size() > 0){//时间冲突
			return false;
		}else{//时间不冲突
			return true;
		}
	}
	
	
}
