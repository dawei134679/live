package com.mpig.api.model;

import java.sql.ResultSet;

public class ConfigGiftActivityModel implements PopulateTemplate<ConfigGiftActivityModel> {

	private int id;
	private int gid;
	private int atype;
	private int stime;
	private int etime;
	private boolean isvalid;
	public int getId() {
		return id;
	}
	public int getGid() {
		return gid;
	}
	public int getAtype() {
		return atype;
	}
	public int getStime() {
		return stime;
	}
	public int getEtime() {
		return etime;
	}
	public boolean isIsvalid() {
		return isvalid;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setGid(int gid) {
		this.gid = gid;
	}
	public void setAtype(int atype) {
		this.atype = atype;
	}
	public void setStime(int stime) {
		this.stime = stime;
	}
	public void setEtime(int etime) {
		this.etime = etime;
	}
	public void setIsvalid(boolean isvalid) {
		this.isvalid = isvalid;
	}
	@Override
	public ConfigGiftActivityModel populateFromResultSet(ResultSet rs) {

		try {
			this.id = rs.getInt("id");
			this.gid = rs.getInt("gid");
			this.atype = rs.getInt("atype");
			this.stime = rs.getInt("stime");
			this.etime = rs.getInt("etime");
			this.isvalid = rs.getBoolean("isvalid");
		} catch (Exception e) {
			// TODO: handle exception
		}
		return this;
	}
}
