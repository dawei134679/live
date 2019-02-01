package com.tinypig.newadmin.web.entity;

import java.io.Serializable;

public class AnchorMicTimeParamDto implements Serializable{

	private static final long serialVersionUID = 670460739581025804L;
	
	private Integer startIndex;

	private Integer pageSize;

	private Integer anchorId;
	
	private String startTime;
	
	private String endTime;
	
	private Long salesmanId;
	
	private Long agentUserId;
	
	private Long promotersId;
	
	private Long extensionCenterId;

	private Long strategicPartnerId;
	
	private String addTime;

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

	public Integer getAnchorId() {
		return anchorId;
	}

	public void setAnchorId(Integer anchorId) {
		this.anchorId = anchorId;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Long getSalesmanId() {
		return salesmanId;
	}

	public void setSalesmanId(Long salesmanId) {
		this.salesmanId = salesmanId;
	}

	public Long getAgentUserId() {
		return agentUserId;
	}

	public void setAgentUserId(Long agentUserId) {
		this.agentUserId = agentUserId;
	}

	public Long getPromotersId() {
		return promotersId;
	}

	public void setPromotersId(Long promotersId) {
		this.promotersId = promotersId;
	}

	public Long getExtensionCenterId() {
		return extensionCenterId;
	}

	public void setExtensionCenterId(Long extensionCenterId) {
		this.extensionCenterId = extensionCenterId;
	}

	public Long getStrategicPartnerId() {
		return strategicPartnerId;
	}

	public void setStrategicPartnerId(Long strategicPartnerId) {
		this.strategicPartnerId = strategicPartnerId;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
}
