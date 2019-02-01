package com.tinypig.newadmin.web.entity;

import java.io.Serializable;

public class GuardStaDto implements Serializable {

	private static final long serialVersionUID = 4462659901004912213L;

	private Integer uid;
	
	private String nickName;
	
	private Integer anchorId;
	
	private String anchorName;
	
	private Integer gid;
	
	private String gname;
	
	private Integer gprice;
	
	private Long startTime;
	
	private Long endTime;

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Integer getAnchorId() {
		return anchorId;
	}

	public void setAnchorId(Integer anchorId) {
		this.anchorId = anchorId;
	}

	public String getAnchorName() {
		return anchorName;
	}

	public void setAnchorName(String anchorName) {
		this.anchorName = anchorName;
	}

	public Integer getGid() {
		return gid;
	}

	public void setGid(Integer gid) {
		this.gid = gid;
	}

	public String getGname() {
		return gname;
	}

	public void setGname(String gname) {
		this.gname = gname;
	}

	public Integer getGprice() {
		return gprice;
	}

	public void setGprice(Integer gprice) {
		this.gprice = gprice;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
}
