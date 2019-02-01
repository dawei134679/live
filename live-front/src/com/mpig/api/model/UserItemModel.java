package com.mpig.api.model;

import java.sql.ResultSet;

public class UserItemModel implements PopulateTemplate<UserItemModel> {

	private int id;
	private int uid;
	private int gid;
	private int type;
	private int subtype;
	private int num;

	//非数据库字段
	private int gprice;
	
	public int getId() {
		return id;
	}
	public int getUid() {
		return uid;
	}
	public int getGid() {
		return gid;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getNum() {
		return num;
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
	public int getSubtype() {
		return subtype;
	}
	public void setSubtype(int subtype) {
		this.subtype = subtype;
	}
	public int getGprice() {
		return gprice;
	}
	public void setGprice(int gprice) {
		this.gprice = gprice;
	}

	@Override
	public UserItemModel populateFromResultSet(ResultSet rs) {
		try {
			this.id = rs.getInt("id");
			this.uid = rs.getInt("uid");
			this.gid = rs.getInt("gid");
			this.num = rs.getInt("num");
			this.type = rs.getInt("type");
			this.subtype = rs.getInt("subtype");
			this.gprice = rs.getInt("gprice");
		} catch (Exception e) {

		}
		return this;
	}
		
}
