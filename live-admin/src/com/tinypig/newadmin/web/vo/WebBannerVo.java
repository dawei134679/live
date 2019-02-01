package com.tinypig.newadmin.web.vo;

import com.tinypig.newadmin.web.entity.WebBanner;

public class WebBannerVo extends WebBanner{

	private String startDate;
	
	private String endDate;

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
	
	
}
