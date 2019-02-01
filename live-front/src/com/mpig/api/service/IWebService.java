package com.mpig.api.service;

import java.util.List;
import java.util.Map;

import com.mpig.api.model.IosVersionModel;
import com.mpig.api.model.WebVerModel;

public interface IWebService {
	/**
	 * 添加反馈
	 * @param uid
	 * @param cls
	 * @param des
	 * @return
	 */
	public int addFeedback(int uid,int cls,String mobile,String des);
	
	/**
	 * 获取banner推荐数据
	 * @return
	 */
	public List<Map<String, Object>> getBanners(String os,String channel);
	
	/**
	 * 获取ios所有项是否显示
	 * @return
	 */
	public IosVersionModel getIosShow(String version);
	
	/**
	 * 获取app版本
	 * @return
	 */
	public WebVerModel getWebVer();
}
