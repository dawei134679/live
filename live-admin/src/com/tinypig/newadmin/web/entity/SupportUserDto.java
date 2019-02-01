package com.tinypig.newadmin.web.entity;

import java.io.Serializable;

public class SupportUserDto implements Serializable {
	private static final long serialVersionUID = -2184182929186234851L;

	private Long id;

	private Integer uid;

	private String notes;

	private Integer status;

	private Long createTime;

	private Long createUserId;

	private Long updateTime;

	private Long updateUserId;

	private String salesmanName;

	private String salesmanContactsPhone;

	private String promotersName;

	private String promotersContactsName;

	private String promotersContactsPhone;

	private String extensionCenterName;

	private String extensionCenterContactsName;

	private String extensionCenterContactsPhone;

	private String strategicPartnerName;

	private String strategicPartnerContactsName;

	private String strategicPartnerContactsPhone;

	private String agentUserName;

	private String agentUserContactsName;

	private String agentUserContactsPhone;

	private Long agentUserId;
	private Long strategicPartnerId;
	private Long extensionCenterId;
	private Long promotersId;
	private Long salesmanId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes == null ? null : notes.trim();
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public Long getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(Long updateUserId) {
		this.updateUserId = updateUserId;
	}

	@Override
	public String toString() {
		return "SupportUser [id=" + id + ", uid=" + uid + ", notes=" + notes + ", status=" + status + ", createTime="
				+ createTime + ", createUserId=" + createUserId + ", updateTime=" + updateTime + ", updateUserId="
				+ updateUserId + "]";
	}

	public String getSalesmanName() {
		return salesmanName;
	}

	public void setSalesmanName(String salesmanName) {
		this.salesmanName = salesmanName;
	}

	public String getSalesmanContactsPhone() {
		return salesmanContactsPhone;
	}

	public void setSalesmanContactsPhone(String salesmanContactsPhone) {
		this.salesmanContactsPhone = salesmanContactsPhone;
	}

	public String getPromotersName() {
		return promotersName;
	}

	public void setPromotersName(String promotersName) {
		this.promotersName = promotersName;
	}

	public String getPromotersContactsName() {
		return promotersContactsName;
	}

	public void setPromotersContactsName(String promotersContactsName) {
		this.promotersContactsName = promotersContactsName;
	}

	public String getPromotersContactsPhone() {
		return promotersContactsPhone;
	}

	public void setPromotersContactsPhone(String promotersContactsPhone) {
		this.promotersContactsPhone = promotersContactsPhone;
	}

	public String getExtensionCenterName() {
		return extensionCenterName;
	}

	public void setExtensionCenterName(String extensionCenterName) {
		this.extensionCenterName = extensionCenterName;
	}

	public String getExtensionCenterContactsName() {
		return extensionCenterContactsName;
	}

	public void setExtensionCenterContactsName(String extensionCenterContactsName) {
		this.extensionCenterContactsName = extensionCenterContactsName;
	}

	public String getExtensionCenterContactsPhone() {
		return extensionCenterContactsPhone;
	}

	public void setExtensionCenterContactsPhone(String extensionCenterContactsPhone) {
		this.extensionCenterContactsPhone = extensionCenterContactsPhone;
	}

	public String getStrategicPartnerName() {
		return strategicPartnerName;
	}

	public void setStrategicPartnerName(String strategicPartnerName) {
		this.strategicPartnerName = strategicPartnerName;
	}

	public String getStrategicPartnerContactsName() {
		return strategicPartnerContactsName;
	}

	public void setStrategicPartnerContactsName(String strategicPartnerContactsName) {
		this.strategicPartnerContactsName = strategicPartnerContactsName;
	}

	public String getStrategicPartnerContactsPhone() {
		return strategicPartnerContactsPhone;
	}

	public void setStrategicPartnerContactsPhone(String strategicPartnerContactsPhone) {
		this.strategicPartnerContactsPhone = strategicPartnerContactsPhone;
	}

	public String getAgentUserName() {
		return agentUserName;
	}

	public void setAgentUserName(String agentUserName) {
		this.agentUserName = agentUserName;
	}

	public String getAgentUserContactsName() {
		return agentUserContactsName;
	}

	public void setAgentUserContactsName(String agentUserContactsName) {
		this.agentUserContactsName = agentUserContactsName;
	}

	public String getAgentUserContactsPhone() {
		return agentUserContactsPhone;
	}

	public void setAgentUserContactsPhone(String agentUserContactsPhone) {
		this.agentUserContactsPhone = agentUserContactsPhone;
	}

	public Long getAgentUserId() {
		return agentUserId;
	}

	public void setAgentUserId(Long agentUserId) {
		this.agentUserId = agentUserId;
	}

	public Long getStrategicPartnerId() {
		return strategicPartnerId;
	}

	public void setStrategicPartnerId(Long strategicPartnerId) {
		this.strategicPartnerId = strategicPartnerId;
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

	public Long getSalesmanId() {
		return salesmanId;
	}

	public void setSalesmanId(Long salesmanId) {
		this.salesmanId = salesmanId;
	}

}