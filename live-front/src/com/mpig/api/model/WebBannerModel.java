package com.mpig.api.model;

import java.sql.ResultSet;

public class WebBannerModel implements PopulateTemplate<WebBannerModel> {

	private int id;
	private String picUrl;
	private String jumpUrl;
	private int sort;
	private int startShow;
	private int endShow;
	private int switchOn;
	private int createAT;
	private String name;
	private int type;
	private int os; // 1 android 2 ios 3 all
	private int roomId;
	private int roomType;
	
	public int getRoomId() {
		return roomId;
	}
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}
	public int getRoomType() {
		return roomType;
	}
	public void setRoomType(int roomType) {
		this.roomType = roomType;
	}
	public int getId() {
		return id;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public String getJumpUrl() {
		return jumpUrl;
	}
	public int getSort() {
		return sort;
	}
	public int getStartShow() {
		return startShow;
	}
	public int getEndShow() {
		return endShow;
	}
	public int getSwitchOn() {
		return switchOn;
	}
	public int getCreateAT() {
		return createAT;
	}
	public String getName() {
		return name;
	}
	public int getType() {
		return type;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public void setJumpUrl(String jumpUrl) {
		this.jumpUrl = jumpUrl;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public void setStartShow(int startShow) {
		this.startShow = startShow;
	}
	public void setEndShow(int endShow) {
		this.endShow = endShow;
	}
	public void setSwitchOn(int switchOn) {
		this.switchOn = switchOn;
	}
	public void setCreateAT(int createAT) {
		this.createAT = createAT;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	@Override
	public WebBannerModel populateFromResultSet(ResultSet rs) {
		try {
			this.id=rs.getInt("id");
			this.picUrl = rs.getString("picUrl");
			this.jumpUrl = rs.getString("jumpUrl");
			this.sort = rs.getInt("sort");
			this.startShow = rs.getInt("startShow");
			this.endShow = rs.getInt("endShow");
			this.switchOn = rs.getInt("switch");
			this.createAT = rs.getInt("createAT");
			this.name = rs.getString("name");
			this.type = rs.getInt("type");
			this.os = rs.getInt("platform");
			this.roomId = rs.getInt("roomId");
			this.roomType = rs.getInt("roomType");
					
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
}
