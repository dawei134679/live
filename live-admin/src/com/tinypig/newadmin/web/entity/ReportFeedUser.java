package com.tinypig.newadmin.web.entity;

public class ReportFeedUser {
	private Integer id;

	private Integer rid;

	private Integer fid;

	private String reportReason;

	private Integer dstuid;

	private Integer dstAt;

	private Integer status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRid() {
		return rid;
	}

	public void setRid(Integer rid) {
		this.rid = rid;
	}

	public Integer getFid() {
		return fid;
	}

	public void setFid(Integer fid) {
		this.fid = fid;
	}

	public String getReportReason() {
		return reportReason;
	}

	public void setReportReason(String reportReason) {
		this.reportReason = reportReason == null ? null : reportReason.trim();
	}

	public Integer getDstuid() {
		return dstuid;
	}

	public void setDstuid(Integer dstuid) {
		this.dstuid = dstuid;
	}

	public Integer getDstAt() {
		return dstAt;
	}

	public void setDstAt(Integer dstAt) {
		this.dstAt = dstAt;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}