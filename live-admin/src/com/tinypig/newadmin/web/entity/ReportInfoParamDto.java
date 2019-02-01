package com.tinypig.newadmin.web.entity;

import java.io.Serializable;

public class ReportInfoParamDto implements Serializable {
	private static final long serialVersionUID = 4282333325646630384L;

	private Long uid;// 被举报人

	private Long rid;// 举报人

	private Long handle_uid;// 处理人Id

	private Integer status;

	private Long screatetime;
	private Long ecreatetime;

	private Long shandletime;
	private Long ehandletime;

	private Integer startIndex;
	private Integer pageSize;

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Long getRid() {
		return rid;
	}

	public void setRid(Long rid) {
		this.rid = rid;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getScreatetime() {
		return screatetime;
	}

	public void setScreatetime(Long screatetime) {
		this.screatetime = screatetime;
	}

	public Long getEcreatetime() {
		return ecreatetime;
	}

	public void setEcreatetime(Long ecreatetime) {
		this.ecreatetime = ecreatetime;
	}

	public Long getShandletime() {
		return shandletime;
	}

	public void setShandletime(Long shandletime) {
		this.shandletime = shandletime;
	}

	public Long getEhandletime() {
		return ehandletime;
	}

	public void setEhandletime(Long ehandletime) {
		this.ehandletime = ehandletime;
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

	public Long getHandle_uid() {
		return handle_uid;
	}

	public void setHandle_uid(Long handle_uid) {
		this.handle_uid = handle_uid;
	}

}
