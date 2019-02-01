package com.tinypig.newadmin.web.entity;

import java.io.Serializable;

public class AnchorInfoParamDto implements Serializable {

	private static final long serialVersionUID = 8930997648934529373L;
	

	private Integer startIndex;

	private Integer pageSize;

	private Integer uid;
	
	private Integer spid;
	
	private Integer ecid;
	
	private Integer pid;
	
	private Integer auid;
	
	private Integer sid;

	private Integer liveStatus;

	public Integer getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Integer getSpid() {
		return spid;
	}

	public void setSpid(Integer spid) {
		this.spid = spid;
	}

	public Integer getEcid() {
		return ecid;
	}

	public void setEcid(Integer ecid) {
		this.ecid = ecid;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public Integer getAuid() {
		return auid;
	}

	public void setAuid(Integer auid) {
		this.auid = auid;
	}

	public Integer getSid() {
		return sid;
	}

	public void setSid(Integer sid) {
		this.sid = sid;
	}

	public Integer getLiveStatus() {
		return liveStatus;
	}

	public void setLiveStatus(Integer liveStatus) {
		this.liveStatus = liveStatus;
	}
}
