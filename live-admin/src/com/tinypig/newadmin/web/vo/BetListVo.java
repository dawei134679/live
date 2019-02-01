package com.tinypig.newadmin.web.vo;

public class BetListVo {
	
	private Integer page;
	
	private Integer rows;
	
	private Long anchorId;
	
	private Long periods;

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

	public Long getAnchorId() {
		return anchorId;
	}

	public void setAnchorId(Long anchorId) {
		this.anchorId = anchorId;
	}

	public Long getPeriods() {
		return periods;
	}

	public void setPeriods(Long periods) {
		this.periods = periods;
	}

}
