package com.mpig.api.model;

import java.sql.ResultSet;

/**
 * vip
 * @author zyl
 *
 */
public class UserVipInfoModel implements PopulateTemplate<UserVipInfoModel>{
	private Integer id;
	private Integer uid;
	private Integer gid;
	private Integer starttime;
	private Integer endtime;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Integer getGid() {
		return gid;
	}

	public void setGid(Integer gid) {
		this.gid = gid;
	}

	public Integer getStarttime() {
		return starttime;
	}

	public void setStarttime(Integer starttime) {
		this.starttime = starttime;
	}

	public Integer getEndtime() {
		return endtime;
	}

	public void setEndtime(Integer endtime) {
		this.endtime = endtime;
	}

	@Override
	public UserVipInfoModel populateFromResultSet(ResultSet rs) {
		try {
			this.id = rs.getInt("id");
			this.uid = rs.getInt("uid");
			this.gid = rs.getInt("gid");
			this.starttime = rs.getInt("starttime");
			this.endtime = rs.getInt("endtime");
		} catch (Exception e) {
			// TODO: handle exception
		}
		return this;
	}
	
}
