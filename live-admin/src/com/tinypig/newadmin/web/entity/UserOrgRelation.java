package com.tinypig.newadmin.web.entity;

import java.io.Serializable;

public class UserOrgRelation implements Serializable {
	private static final long serialVersionUID = 8544475062680741721L;

	private Long id;

    private Integer uid;

    private String phone;

    private Long registtime;

    private Long strategicPartnerId;

    private Long extensionCenterId;

    private Long promotersId;

    private Long agentUserId;

    private Long salesmanId;

    private Long createTime;

    private Long createUser;

    private Long updateTime;

    private Long updateUser;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public Long getRegisttime() {
        return registtime;
    }

    public void setRegisttime(Long registtime) {
        this.registtime = registtime;
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

    public Long getAgentUserId() {
        return agentUserId;
    }

    public void setAgentUserId(Long agentUserId) {
        this.agentUserId = agentUserId;
    }

    public Long getSalesmanId() {
        return salesmanId;
    }

    public void setSalesmanId(Long salesmanId) {
        this.salesmanId = salesmanId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }
}