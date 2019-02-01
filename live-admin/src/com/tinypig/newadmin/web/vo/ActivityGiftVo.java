package com.tinypig.newadmin.web.vo;

import com.tinypig.newadmin.web.entity.ActivityGift;

public class ActivityGiftVo extends ActivityGift{

	private String gname;
	private String starttime;
	private String endtime;

	public String getGname() {
		return gname;
	}

	public void setGname(String gname) {
		this.gname = gname;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	
	
}
