package com.hkzb.game.dto;

import java.io.Serializable;

public class StakeRecordParamDto implements Serializable {

	private static final long serialVersionUID = -175821523103463912L;

	private Integer pageNo;

	private Integer pageSize;

	private Long uid;

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
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
}