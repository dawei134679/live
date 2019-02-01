package com.mpig.api.model;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SalesmanModel implements PopulateTemplate<SalesmanModel>, Serializable {
	private static final long serialVersionUID = -4007167638308824870L;

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
		this.name = name;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getContactsPhone() {
		return contactsPhone;
	}

	public void setContactsPhone(String contactsPhone) {
		this.contactsPhone = contactsPhone;
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

	@Override
	public SalesmanModel populateFromResultSet(ResultSet rs) {
		try {
			this.id = rs.getLong("id");
			this.name = rs.getString("name");
			this.contacts = rs.getString("contacts");
			this.contactsPhone = rs.getString("contacts_phone");
			this.strategicPartner = rs.getLong("strategic_partner");
			this.extensionCenter = rs.getLong("extension_center");
			this.promoters = rs.getLong("promoters");
			this.agentUser = rs.getLong("agent_user");
			this.status = rs.getInt("status");
			this.qrcode = rs.getString("qrcode");
			this.createUser = rs.getLong("create_user");
			this.createTime = rs.getLong("create_time");
			this.modifyTime = rs.getLong("modify_time");
			this.modifyUser = rs.getLong("modify_user");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this;
	}

}
