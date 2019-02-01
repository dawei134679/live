package com.mpig.api.model;

public class ReportAlbumModel {
	private Integer id;
	private Integer reportUid; //被举报的用户
	private Integer reportPid; //被举报的图片id
	private String copyFilename; //备份的文件名
	private String copyUrl; //备份的地址
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
	public Integer getReportPid() {
		return reportPid;
	}
	public void setReportPid(Integer reportPid) {
		this.reportPid = reportPid;
	}
	public String getCopyFilename() {
		return copyFilename;
	}
	public void setCopyFilename(String copyFilename) {
		this.copyFilename = copyFilename;
	}
	public String getCopyUrl() {
		return copyUrl;
	}
	public void setCopyUrl(String copyUrl) {
		this.copyUrl = copyUrl;
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
