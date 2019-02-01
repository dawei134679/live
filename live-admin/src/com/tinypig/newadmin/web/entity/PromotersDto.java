package com.tinypig.newadmin.web.entity;

import java.io.Serializable;

/**
 * 铂金公会DTO
 */
public class PromotersDto implements Serializable {
	private static final long serialVersionUID = -2709279667487878699L;

	private Long id;

	private String name;

	private String contacts;

	private String contactsPhone;

	private Long strategicPartner;
	private Long extensionCenter;

	private String extensionCenterName;
	private String extensionCenterContacts;
	private String extensionCenterContactsPhone;
	
	private String strategicPartnerName;
	private String strategicPartnerContacts;
	private String strategicPartnerContactsPhone;


	private Integer status;

	private Long createUser;

	private Long createTime;

	private Long modifyTime;

	private Long modifyUser;

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

	public Long getExtensionCenter() {
		return extensionCenter;
	}

	public void setExtensionCenter(Long extensionCenter) {
		this.extensionCenter = extensionCenter;
	}

	public String getExtensionCenterName() {
		return extensionCenterName;
	}

	public void setExtensionCenterName(String extensionCenterName) {
		this.extensionCenterName = extensionCenterName;
	}

	public String getExtensionCenterContacts() {
		return extensionCenterContacts;
	}

	public void setExtensionCenterContacts(String extensionCenterContacts) {
		this.extensionCenterContacts = extensionCenterContacts;
	}

	public String getExtensionCenterContactsPhone() {
		return extensionCenterContactsPhone;
	}

	public void setExtensionCenterContactsPhone(String extensionCenterContactsPhone) {
		this.extensionCenterContactsPhone = extensionCenterContactsPhone;
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

	public Long getStrategicPartner() {
		return strategicPartner;
	}

	public void setStrategicPartner(Long strategicPartner) {
		this.strategicPartner = strategicPartner;
	}

	public String getStrategicPartnerName() {
		return strategicPartnerName;
	}

	public void setStrategicPartnerName(String strategicPartnerName) {
		this.strategicPartnerName = strategicPartnerName;
	}

	public String getStrategicPartnerContacts() {
		return strategicPartnerContacts;
	}

	public void setStrategicPartnerContacts(String strategicPartnerContacts) {
		this.strategicPartnerContacts = strategicPartnerContacts;
	}

	public String getStrategicPartnerContactsPhone() {
		return strategicPartnerContactsPhone;
	}

	public void setStrategicPartnerContactsPhone(String strategicPartnerContactsPhone) {
		this.strategicPartnerContactsPhone = strategicPartnerContactsPhone;
	}
	

}