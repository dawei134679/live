package com.mpig.api.dictionary;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mpig.api.model.PopulateTemplate;
/**
 * 开屏VO
 * @author zyl
 * @time 2016-7-7
 */
public final class OpenScreenConfig implements PopulateTemplate<OpenScreenConfig>,Serializable{
	private String picUrl;
	private String jumpUrl;
	private int startShow;
	private int endShow;
	private int platform; //1 android, 2 ios, 3 all
	
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public String getJumpUrl() {
		return jumpUrl;
	}
	public void setJumpUrl(String jumpUrl) {
		this.jumpUrl = jumpUrl;
	}
	public int getStartShow() {
		return startShow;
	}
	public void setStartShow(int startShow) {
		this.startShow = startShow;
	}
	public int getEndShow() {
		return endShow;
	}
	public void setEndShow(int endShow) {
		this.endShow = endShow;
	}
	public int getPlatform() {
		return platform;      
	}
	public void setPlatform(int platform) {
		this.platform = platform;  
	}
	@Override
	public OpenScreenConfig populateFromResultSet(ResultSet rs) {
		try {
			this.picUrl = rs.getString("picUrl");
			this.jumpUrl = rs.getString("jumpUrl");
			this.startShow = rs.getInt("startShow");
			this.endShow = rs.getInt("endShow");
			this.platform = rs.getInt("platform");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this;
	}
	
	
}
