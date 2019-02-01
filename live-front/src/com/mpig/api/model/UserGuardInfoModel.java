package com.mpig.api.model;

import java.sql.ResultSet;

/**
 * 守护
 * @author zyl
 *
 */
public class UserGuardInfoModel implements PopulateTemplate<UserGuardInfoModel>{

	private Integer id;
	private Integer roomid;
	private Integer uid;
	private Integer gid;
	private Integer level;
	private Integer exp;
	private Integer starttime;
	private Integer endtime;
	private Integer cushiontime;
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRoomid() {
		return roomid;
	}

	public void setRoomid(Integer roomid) {
		this.roomid = roomid;
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

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getExp() {
		return exp;
	}

	public void setExp(Integer exp) {
		this.exp = exp;
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

	public Integer getCushiontime() {
		return cushiontime;
	}

	public void setCushiontime(Integer cushiontime) {
		this.cushiontime = cushiontime;
	}

	@Override
	public UserGuardInfoModel populateFromResultSet(ResultSet rs) {
		try {
			this.id = rs.getInt("id");
			this.uid = rs.getInt("uid");
			this.gid = rs.getInt("gid");
			this.roomid = rs.getInt("roomid");
			this.starttime = rs.getInt("starttime");
			this.endtime = rs.getInt("endtime");
			this.level = rs.getInt("level");
			this.exp = rs.getInt("exp");
			this.cushiontime = rs.getInt("cushiontime");
			
		} catch (Exception e) {
			
		}
		return this;
	}
	
	
}
