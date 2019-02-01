package com.tinypig.newadmin.web.entity;

import java.io.Serializable;

/**
 * 钻石公会Dto
 */
public class StrategicPartnerDto implements Serializable {
	private static final long serialVersionUID = -2079302337277472026L;

	private Long id;

	private String name;

	private String contacts;

	private String contactsPhone;

	private Integer status;

	private Long createUser;

	private Long createTime;

	private Long modifyTime;

	private Long modifyUser;

	private Integer extensionCenterNum;
	private Integer promotersNum;
	private Integer agentUserNum;
	private Integer salesmanNum;
	private Integer anchorNum;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts == null ? null : contacts.trim();
	}

	public String getContactsPhone() {
		return contactsPhone;
	}

	public void setContactsPhone(String contactsPhone) {
		this.contactsPhone = contactsPhone == null ? null : contactsPhone.trim();
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getCreateUser() {
		return createUser;
	}

	public void setCreateUser(Long createUser) {
		this.createUser = createUser;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Long modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Long getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(Long modifyUser) {
		this.modifyUser = modifyUser;
	}

	public Integer getExtensionCenterNum() {
		return extensionCenterNum;
	}

	public void setExtensionCenterNum(Integer extensionCenterNum) {
		this.extensionCenterNum = extensionCenterNum;
	}

	public Integer getPromotersNum() {
		return promotersNum;
	}

	public void setPromotersNum(Integer promotersNum) {
		this.promotersNum = promotersNum;
	}

	public Integer getAgentUserNum() {
		return agentUserNum;
	}

	public void setAgentUserNum(Integer agentUserNum) {
		this.agentUserNum = agentUserNum;
	}

	public Integer getSalesmanNum() {
		return salesmanNum;
	}

	public void setSalesmanNum(Integer salesmanNum) {
		this.salesmanNum = salesmanNum;
	}

	public Integer getAnchorNum() {
		return anchorNum;
	}

	public void setAnchorNum(Integer anchorNum) {
		this.anchorNum = anchorNum;
	}

}