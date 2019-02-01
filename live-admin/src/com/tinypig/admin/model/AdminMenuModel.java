package com.tinypig.admin.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminMenuModel implements PopulateTemplate<AdminMenuModel> {

	private int mid;
	private String menuname;
	private int pid;
	private String url;
	private int show;
	
	public int getMid() {
		return mid;
	}
	public void setMid(int mid) {
		this.mid = mid;
	}
	public String getMenuname() {
		return menuname;
	}
	public void setMenuname(String menuname) {
		this.menuname = menuname;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getShow() {
		return show;
	}
	public void setShow(int show) {
		this.show = show;
	}
	
	@Override
	public AdminMenuModel populateFromResultSet(ResultSet rs) {
		try {
			this.mid = rs.getInt("mid");
			this.menuname = rs.getString("menuname");
			this.pid = rs.getInt("pid");
			this.url = rs.getString("url");
			this.show = rs.getInt("show");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this;
	}
	
}
