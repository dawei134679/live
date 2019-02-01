package com.tinypig.newadmin.web.entity;

import java.io.Serializable;

/**
 * 铂金公会
 */
public class Promoters implements Serializable {
	private static final long serialVersionUID = -2088793129460352056L;

	private Long id;

	private String name;

	private String contacts;

	private String contactsPhone;

	private Long strategicPartner;
	
	private Long extensionCenter;
	
	private Integer status;

	private Long createUser;

	private Long createTime;

	private Long modifyTime;

	private Long modifyUser;

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

	public Long getStrategicPartner() {
		return strategicPartner;
	}

	public void setStrategicPartner(Long strategicPartner) {
		this.strategicPartner = strategicPartner;
	}

	public Long getExtensionCenter() {
		return extensionCenter;
	}

	public void setExtensionCenter(Long extensionCenter) {
		this.extensionCenter = extensionCenter;
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
}