package com.tinypig.newadmin.web.vo;

import java.io.Serializable;

public class BillcvgParamVo implements Serializable {
	private static final long serialVersionUID = -8673160082080051323L;

	private Integer startIndex;

	private Integer pageSize;

	private Long uid;

	private Long anchorid;

	private String gname;

	private Integer type;

	private String startTime;

	private String endTime;

	private Integer gstatus;

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

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Long getAnchorid() {
		return anchorid;
	}

	public void setAnchorid(Long anchorid) {
		this.anchorid = anchorid;
	}

	public String getGname() {
		return gname;
	}

	public void setGname(String gname) {
		this.gname = gname;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Integer getGstatus() {
		return gstatus;
	}

	public void setGstatus(Integer gstatus) {
		this.gstatus = gstatus;
	}

	@Override
	public String toString() {
		return "BillcvgParamVo [startIndex=" + startIndex + ", pageSize=" + pageSize + ", uid=" + uid + ", anchorid="
				+ anchorid + ", gname=" + gname + ", type=" + type + ", startTime=" + startTime + ", endTime=" + endTime
				+ ", gstatus=" + gstatus + "]";
	}
}
