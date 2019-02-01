package com.mpig.api.model;

import java.sql.ResultSet;

public class WebVerModel implements PopulateTemplate<WebVerModel> {

	private String iosVer;
	private int iosAt;
	private String androidVer;
	private int androidAt;
	public String getIosVer() {
		return iosVer;
	}
	public int getIosAt() {
		return iosAt;
	}
	public String getAndroidVer() {
		return androidVer;
	}
	public int getAndroidAt() {
		return androidAt;
	}
	public void setIosVer(String iosVer) {
		this.iosVer = iosVer;
	}
	public void setIosAt(int iosAt) {
		this.iosAt = iosAt;
	}
	public void setAndroidVer(String androidVer) {
		this.androidVer = androidVer;
	}
	public void setAndroidAt(int androidAt) {
		this.androidAt = androidAt;
	}
	@Override
	public WebVerModel populateFromResultSet(ResultSet rs) {
		try {
			this.iosVer = rs.getString("iosVer");
			this.iosAt = rs.getInt("iosAt");
			this.androidVer = rs.getString("androidVer");
			this.androidAt = rs.getInt("androidAt");
		} catch (Exception e) {
		}
		return this;
	}
}
