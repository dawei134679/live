package com.tinypig.newadmin.web.entity;

import java.io.Serializable;

public class MemberParamDto implements Serializable {

	private static final long serialVersionUID = -5822391089262914607L;

	private Integer startIndex;

	private Integer pageSize;

	private String order;

	private String sort;

	private Integer uid;

	private String phone;

	private String startRegisterTime;

	private String endRegisterTime;

	private Long agentUserId;
	
	private String agentUserName;

	private Long promotersId;
	
	private String promotersName;
	
	private Long salesmanId;
	
	private String salesmanName;

	private Long extensionCenterId;

	private String extensionCenterContactsName;

	private Long strategicPartnerId;

	private String strategicPartnerContactsName;

	private Integer supportUserFlag;

	public Long getStrategicPartnerId() {
		return strategicPartnerId;
	}

	public void setStrategicPartnerId(Long strategicPartnerId) {
		this.strategicPartnerId = strategicPartnerId;
	}

	public String getStrategicPartnerContactsName() {
		return strategicPartnerContactsName;
	}

	public void setStrategicPartnerContactsName(String strategicPartnerContactsName) {
		this.strategicPartnerContactsName = strategicPartnerContactsName;
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

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getStartRegisterTime() {
		return startRegisterTime;
	}

	public void setStartRegisterTime(String startRegisterTime) {
		this.startRegisterTime = startRegisterTime;
	}

	public String getEndRegisterTime() {
		return endRegisterTime;
	}

	public void setEndRegisterTime(String endRegisterTime) {
		this.endRegisterTime = endRegisterTime;
	}

	public Long getAgentUserId() {
		return agentUserId;
	}

	public void setAgentUserId(Long agentUserId) {
		this.agentUserId = agentUserId;
	}

	public String getAgentUserName() {
		return agentUserName;
	}

	public void setAgentUserName(String agentUserName) {
		this.agentUserName = agentUserName;
	}

	public Long getExtensionCenterId() {
		return extensionCenterId;
	}

	public void setExtensionCenterId(Long extensionCenterId) {
		this.extensionCenterId = extensionCenterId;
	}

	public String getExtensionCenterContactsName() {
		return extensionCenterContactsName;
	}

	public void setExtensionCenterContactsName(String extensionCenterContactsName) {
		this.extensionCenterContactsName = extensionCenterContactsName;
	}

	public Long getPromotersId() {
		return promotersId;
	}

	public void setPromotersId(Long promotersId) {
		this.promotersId = promotersId;
	}

	public String getPromotersName() {
		return promotersName;
	}

	public void setPromotersName(String promotersName) {
		this.promotersName = promotersName;
	}

	public Long getSalesmanId() {
		return salesmanId;
	}

	public void setSalesmanId(Long salesmanId) {
		this.salesmanId = salesmanId;
	}

	public String getSalesmanName() {
		return salesmanName;
	}

	public void setSalesmanName(String salesmanName) {
		this.salesmanName = salesmanName;
	}

	public Integer getSupportUserFlag() {
		return supportUserFlag;
	}

	public void setSupportUserFlag(Integer supportUserFlag) {
		this.supportUserFlag = supportUserFlag;
	}
}
