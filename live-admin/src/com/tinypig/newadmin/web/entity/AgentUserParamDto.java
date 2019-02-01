package com.tinypig.newadmin.web.entity;

import java.io.Serializable;

public class AgentUserParamDto implements Serializable {
	private static final long serialVersionUID = 2032295861840705368L;

	private String name;
	private String contactsPhone;
	private Long strategicPartnerId;
	private Long extensionCenterId;
	private Long promotersId;
	private Long agentUserId;
	private String strategicPartnerName;
	private String extensionCenterName;
	private String promotersName;
	private Long stime;
	private Long etime;
	private Integer startIndex;
	private Integer pageSize;
	private String orderBy;
	private String orderSort;

	public Long getStrategicPartnerId() {
		return strategicPartnerId;
	}

	public void setStrategicPartnerId(Long strategicPartnerId) {
		this.strategicPartnerId = strategicPartnerId;
	}

	public String getStrategicPartnerName() {
		return strategicPartnerName;
	}

	public void setStrategicPartnerName(String strategicPartnerName) {
		this.strategicPartnerName = strategicPartnerName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContactsPhone() {
		return contactsPhone;
	}

	public void setContactsPhone(String contactsPhone) {
		this.contactsPhone = contactsPhone;
	}

	public Long getExtensionCenterId() {
		return extensionCenterId;
	}

	public void setExtensionCenterId(Long extensionCenterId) {
		this.extensionCenterId = extensionCenterId;
	}

	public Long getPromotersId() {
		return promotersId;
	}

	public void setPromotersId(Long promotersId) {
		this.promotersId = promotersId;
	}

	public Long getAgentUserId() {
		return agentUserId;
	}

	public void setAgentUserId(Long agentUserId) {
		this.agentUserId = agentUserId;
	}

	public String getExtensionCenterName() {
		return extensionCenterName;
	}

	public void setExtensionCenterName(String extensionCenterName) {
		this.extensionCenterName = extensionCenterName;
	}

	public String getPromotersName() {
		return promotersName;
	}

	public void setPromotersName(String promotersName) {
		this.promotersName = promotersName;
	}

	public Long getStime() {
		return stime;
	}

	public void setStime(Long stime) {
		this.stime = stime;
	}

	public Long getEtime() {
		return etime;
	}

	public void setEtime(Long etime) {
		this.etime = etime;
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

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getOrderSort() {
		return orderSort;
	}

	public void setOrderSort(String orderSort) {
		this.orderSort = orderSort;
	}

}