package com.tinypig.admin.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMannerModel implements PopulateTemplate<UserMannerModel>{
	
	private Integer uid;
    private String nickname;
    private Integer anchorLevel;
    private Integer userLevel;
    private int handle;
	private String cause;
	private int adminid;
	private int lasttime;
	private String ph;
	private int num;
	public int getLasttime() {
		return lasttime;
	}
	public void setLasttime(int lasttime) {
		this.lasttime = lasttime;
	}
	public String getPh() {
		return ph;
	}
	public void setPh(String ph) {
		this.ph = ph;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public Integer getAnchorLevel() {
		return anchorLevel;
	}
	public void setAnchorLevel(Integer anchorLevel) {
		this.anchorLevel = anchorLevel;
	}
	public Integer getUserLevel() {
		return userLevel;
	}
	public void setUserLevel(Integer userLevel) {
		this.userLevel = userLevel;
	}
	public int getHandle() {
		return handle;
	}
	public void setHandle(int handle) {
		this.handle = handle;
	}
	public String getCause() {
		return cause;
	}
	public void setCause(String cause) {
		this.cause = cause;
	}
	public int getAdminid() {
		return adminid;
	}
	public void setAdminid(int adminid) {
		this.adminid = adminid;
	}
	@Override
	public UserMannerModel populateFromResultSet(ResultSet rs) {
		try {
			uid=rs.getInt("uid");
			nickname=rs.getString("nickname");
			anchorLevel=rs.getInt("anchorLevel");
			userLevel=rs.getInt("userLevel");
			handle=rs.getInt("handle");
			cause=rs.getString("cause");
			adminid=rs.getInt("adminid");
//			lasttime=rs.getInt("lasttime");
//			ph=rs.getString("ph");
			try {
				num=rs.getInt("num");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				num = 0;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}

}
