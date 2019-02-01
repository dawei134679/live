package com.tinypig.admin.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BannerListModel implements PopulateTemplate<BannerListModel>{
	private int id;
	private int sort;
	private int startShow;
	private int endShow;
	private int swi;
	private int createAT;
	private int type;
	private String picUrl;
	private String jumpUrl;
	private String name;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
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
	public int getSwi() {
		return swi;
	}
	public void setSwi(int swi) {
		this.swi = swi;
	}
	public int getCreateAT() {
		return createAT;
	}
	public void setCreateAT(int createAT) {
		this.createAT = createAT;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public BannerListModel populateFromResultSet(ResultSet rs) {
		try {
			this.id=rs.getInt("id");
			this.createAT= rs.getInt("createAT");
			this.endShow = rs.getInt("endShow");
			this.sort = rs.getInt("sort");
			this.startShow = rs.getInt("startShow");
			this.swi= rs.getInt("switch");
			this.type = rs.getInt("type");
			this.jumpUrl = rs.getString("jumpUrl");
			this.name = rs.getString("name");
			this.picUrl = rs.getString("picUrl");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this;
	}

}
