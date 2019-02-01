package com.mpig.api.model;

import java.sql.ResultSet;

public class UserItemSpecialModel implements PopulateTemplate<UserItemSpecialModel> {
	private int id;
	private int uid;
	private int gid;
	private int type;
	private int subtype;
	private int num;
	private int starttime;
	private int endtime;
	
	public int getId() {
		return id;
	}
	public int getUid() {
		return uid;
	}
	public int getGid() {
		return gid;
	}
	public int getNum() {
		return num;
	}
	public int getStarttime() {
		return starttime;
	}
	public int getEndtime() {
		return endtime;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public void setGid(int gid) {
		this.gid = gid;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public void setStarttime(int starttime) {
		this.starttime = starttime;
	}
	public void setEndtime(int endtime) {
		this.endtime = endtime;
	}
	
	@Override
	public UserItemSpecialModel populateFromResultSet(ResultSet rs) {
		try {

			this.id = rs.getInt("id");
			this.uid = rs.getInt("uid");
			this.gid = rs.getInt("gid");
			this.num = rs.getInt("num");
			this.starttime = rs.getInt("starttime");
			this.endtime = rs.getInt("endtime");
			this.type = rs.getInt("type");
			this.subtype = rs.getInt("subtype");
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return this;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getSubtype() {
		return subtype;
	}
	public void setSubtype(int subtype) {
		this.subtype = subtype;
	}
	
}
