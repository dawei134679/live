package com.tinypig.newadmin.web.entity;

import java.io.Serializable;

/**
 *	家族助理dto
 */
public class SalesmanDto implements Serializable {
	private static final long serialVersionUID = -5194619688347867053L;

	private Long id;

	private String name;

	private String contacts;

	private String contactsPhone;

	private Long strategicPartner;

	private Long extensionCenter;

	private Long promoters;

	private Long agentUser;

	private Integer status;

	private String qrcode;

	private Long createUser;

	private Long createTime;

	private Long modifyTime;

	private Long modifyUser;

	private String agentUserName;
	private String agentUserContacts;
	private String agentUserContactsPhone;

	private String promotersName;
	private String promotersContacts;
	private String promotersContactsPhone;

	private String extensionCenterName;
	private String extensionCenterContacts;
	private String extensionCenterContactsPhone;

	private String strategicPartnerName;
	private String strategicPartnerContacts;
	private String strategicPartnerContactsPhone;

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

	public Long getPromoters() {
		return promoters;
	}

	public void setPromoters(Long promoters) {
		this.promoters = promoters;
	}

	public Long getAgentUser() {
		return agentUser;
	}

	public void setAgentUser(Long agentUser) {
		this.agentUser = agentUser;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getQrcode() {
		return qrcode;
	}

	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
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

	public String getAgentUserName() {
		return agentUserName;
	}

	public void setAgentUserName(String agentUserName) {
		this.agentUserName = agentUserName;
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

	public Integer getAnchorNum() {
		return anchorNum;
	}

	public void setAnchorNum(Integer anchorNum) {
		this.anchorNum = anchorNum;
	}

	public String getAgentUserContacts() {
		return agentUserContacts;
	}

	public void setAgentUserContacts(String agentUserContacts) {
		this.agentUserContacts = agentUserContacts;
	}

	public String getAgentUserContactsPhone() {
		return agentUserContactsPhone;
	}

	public void setAgentUserContactsPhone(String agentUserContactsPhone) {
		this.agentUserContactsPhone = agentUserContactsPhone;
	}

	public String getPromotersContacts() {
		return promotersContacts;
	}

	public void setPromotersContacts(String promotersContacts) {
		this.promotersContacts = promotersContacts;
	}

	public String getPromotersContactsPhone() {
		return promotersContactsPhone;
	}

	public void setPromotersContactsPhone(String promotersContactsPhone) {
		this.promotersContactsPhone = promotersContactsPhone;
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

	public Long getStrategicPartner() {
		return strategicPartner;
	}

	public void setStrategicPartner(Long strategicPartner) {
		this.strategicPartner = strategicPartner;
	}
	
	

}