package com.tinypig.newadmin.web.entity;

import java.io.Serializable;

public class VerifyCodeParamDto implements Serializable {
	private static final long serialVersionUID = -3220777535675560432L;

	private String mobile;
	private Integer startIndex;
	private Integer pageSize;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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

}