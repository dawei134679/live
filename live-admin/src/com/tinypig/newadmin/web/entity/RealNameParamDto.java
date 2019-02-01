package com.tinypig.newadmin.web.entity;

import java.io.Serializable;

public class RealNameParamDto implements Serializable {

	private static final long serialVersionUID = -5384248154353263328L;

	private Integer startIndex;

	private Integer pageSize;

	private Integer uid;
	
	private String realName;
	
	private String cardID;
	
	private Integer spid;
	
	private Integer ecid;
	
	private Integer pid;
	
	private Integer auid;
	
	private Integer sid;

	private Integer auditStatus;

	private Long cStartTime;
	
	private Long cEndTime;
	
	private Long pStartTime;
	
	private Long pEndTime;

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

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}
	
	
	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getCardID() {
		return cardID;
	}

	public void setCardID(String cardID) {
		this.cardID = cardID;
	}

	public Integer getSpid() {
		return spid;
	}

	public void setSpid(Integer spid) {
		this.spid = spid;
	}

	public Integer getEcid() {
		return ecid;
	}

	public void setEcid(Integer ecid) {
		this.ecid = ecid;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public Integer getAuid() {
		return auid;
	}

	public void setAuid(Integer auid) {
		this.auid = auid;
	}

	public Integer getSid() {
		return sid;
	}

	public void setSid(Integer sid) {
		this.sid = sid;
	}

	public Integer getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}

	public Long getcStartTime() {
		return cStartTime;
	}

	public void setcStartTime(Long cStartTime) {
		this.cStartTime = cStartTime;
	}

	public Long getcEndTime() {
		return cEndTime;
	}

	public void setcEndTime(Long cEndTime) {
		this.cEndTime = cEndTime;
	}

	public Long getpStartTime() {
		return pStartTime;
	}

	public void setpStartTime(Long pStartTime) {
		this.pStartTime = pStartTime;
	}

	public Long getpEndTime() {
		return pEndTime;
	}

	public void setpEndTime(Long pEndTime) {
		this.pEndTime = pEndTime;
	}

	@Override
	public String toString() {
		return "RealNameParamDto [startIndex=" + startIndex + ", pageSize=" + pageSize + ", uid=" + uid + ", realName="
				+ realName + ", cardID=" + cardID + ", spid=" + spid + ", ecid=" + ecid + ", pid=" + pid + ", auid="
				+ auid + ", sid=" + sid + ", auditStatus=" + auditStatus + ", cStartTime=" + cStartTime + ", cEndTime="
				+ cEndTime + ", pStartTime=" + pStartTime + ", pEndTime=" + pEndTime + "]";
	}

}
