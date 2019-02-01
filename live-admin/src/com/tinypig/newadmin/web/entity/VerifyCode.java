package com.tinypig.newadmin.web.entity;

import java.io.Serializable;

public class VerifyCode implements Serializable {
	private static final long serialVersionUID = -2408088684095216180L;

	private Long id;

	private String mobile;

	private Integer type;

	private String verifyCode;

	private Long takeEffectTime;

	private Long expiryTime;

	private Long createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public Long getTakeEffectTime() {
		return takeEffectTime;
	}

	public void setTakeEffectTime(Long takeEffectTime) {
		this.takeEffectTime = takeEffectTime;
	}

	public Long getExpiryTime() {
		return expiryTime;
	}

	public void setExpiryTime(Long expiryTime) {
		this.expiryTime = expiryTime;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

}