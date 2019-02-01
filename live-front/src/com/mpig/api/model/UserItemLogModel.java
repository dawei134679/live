package com.mpig.api.model;

import java.sql.ResultSet;

public class UserItemLogModel implements PopulateTemplate<UserItemLogModel>{
	
	private int id;
	private int uid;
	private String nickname;
	private int gid;
	private String gname;
	private int type;
	private int subtype;
	private int num;
	private int sourc;
	private int addtime;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public int getGid() {
		return gid;
	}
	public void setGid(int gid) {
		this.gid = gid;
	}
	public String getGname() {
		return gname;
	}
	public void setGname(String gname) {
		this.gname = gname;
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
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public int getSourc() {
		return sourc;
	}
	public void setSourc(int sourc) {
		this.sourc = sourc;
	}
	
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public int getAddtime() {
		return addtime;
	}
	public void setAddtime(int addtime) {
		this.addtime = addtime;
	}
	@Override
	public UserItemLogModel populateFromResultSet(ResultSet rs) {
		try {
			this.uid = rs.getInt("uid");
			this.gid = rs.getInt("gid");
			this.num = rs.getInt("num");
			this.subtype = rs.getInt("subtype");
			this.addtime = rs.getInt("addtime");
		} catch (Exception e) {
		}
		return this;
	}
	
	
}
