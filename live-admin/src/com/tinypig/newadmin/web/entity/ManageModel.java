package com.tinypig.newadmin.web.entity;

public class ManageModel {

	private int uid;
	private int adduid;
	private String addnick;
	private int addtime;
	
	public int getUid() {
		return uid;
	}
	public int getAdduid() {
		return adduid;
	}
	public String getAddnick() {
		return addnick;
	}
	public int getAddtime() {
		return addtime;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public void setAdduid(int adduid) {
		this.adduid = adduid;
	}
	public void setAddnick(String addnick) {
		this.addnick = addnick;
	}
	public void setAddtime(int addtime) {
		this.addtime = addtime;
	}
	
}
