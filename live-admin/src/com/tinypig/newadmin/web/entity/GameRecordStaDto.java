package com.tinypig.newadmin.web.entity;

import java.io.Serializable;

public class GameRecordStaDto implements Serializable {
	private static final long serialVersionUID = -5395442941553065331L;
	private Integer page;
	private Integer rows;
	private Integer startIndex;
	private Integer pageSize;
	private Long uid;
	private Long roomId;
	private Integer gtype;
	private Integer spid;
	private Integer ecid;
	private Integer pid;
	private Integer auid;
	private Integer sid;
	private String startDate;
	private String endDate;

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

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

	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public Integer getGtype() {
		return gtype;
	}

	public void setGtype(Integer gtype) {
		this.gtype = gtype;
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

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		return "GameRecordStaDto [page=" + page + ", rows=" + rows + ", startIndex=" + startIndex + ", pageSize="
				+ pageSize + ", uid=" + uid + ", roomId=" + roomId + ", gtype=" + gtype + ", spid=" + spid + ", ecid="
				+ ecid + ", pid=" + pid + ", auid=" + auid + ", sid=" + sid + ", startDate=" + startDate + ", endDate="
				+ endDate + "]";
	}
	
}
