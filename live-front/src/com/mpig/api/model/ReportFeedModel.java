package com.mpig.api.model;

public class ReportFeedModel {
	private Integer id;
	private Integer reportUid; //被举报的用户
	private Integer reportFid; //被举报的动态id
	private Integer reportNum; //被举报的次数
	private int status; //状态 默认为0 未处理 1忽略 2删除 3.。。。
	
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
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	
}
