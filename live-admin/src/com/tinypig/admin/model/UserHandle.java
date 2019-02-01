package com.tinypig.admin.model;

import java.sql.ResultSet;

public class UserHandle  {
	private int uid;
	private int handle;
	private String cause;
	private int source;
	private int adminid;
	private int createAt;
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
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
	public int getSource() {
		return source;
	}
	public void setSource(int source) {
		this.source = source;
	}
	public int getAdminid() {
		return adminid;
	}
	public void setAdminid(int adminid) {
		this.adminid = adminid;
	}
	public int getCreateAt() {
		return createAt;
	}
	public void setCreateAt(int createAt) {
		this.createAt = createAt;
	}
	
	 public UserHandle populateFromResultSet(ResultSet rs) {
		 try {
			uid=rs.getInt("uid");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	 }
}
