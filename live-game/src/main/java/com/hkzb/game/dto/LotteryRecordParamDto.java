package com.hkzb.game.dto;

import java.io.Serializable;

public class LotteryRecordParamDto implements Serializable {

	private static final long serialVersionUID = 3359249290989018692L;

	private Integer pageNo;

	private Integer pageSize;

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

}
