package com.tinypig.newadmin.web.entity;

import java.io.Serializable;

/**
 *	动态举报
 */
public class ReportFeed implements Serializable {
	private static final long serialVersionUID = -5452762559461037125L;

	private Integer id;

	private Integer reportUid;

	private Integer reportFid;

	private Integer reportNum;

	private Integer createAt;

	private Integer updateAt;

	private Integer status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getReportUid() {
		return reportUid;
	}

	public void setReportUid(Integer reportUid) {
		this.reportUid = reportUid;
	}

	public Integer getReportFid() {
		return reportFid;
	}

	public void setReportFid(Integer reportFid) {
		this.reportFid = reportFid;
	}

	public Integer getReportNum() {
		return reportNum;
	}

	public void setReportNum(Integer reportNum) {
		this.reportNum = reportNum;
	}

	public Integer getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Integer createAt) {
		this.createAt = createAt;
	}

	public Integer getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Integer updateAt) {
		this.updateAt = updateAt;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}