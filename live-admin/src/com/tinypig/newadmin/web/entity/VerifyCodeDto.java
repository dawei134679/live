package com.tinypig.newadmin.web.entity;

import java.io.Serializable;

/**
 * 铂金公会DTO
 */
public class VerifyCodeDto implements Serializable {
	private static final long serialVersionUID = -2709279667487878699L;

	private Long id;

	private String mobile;

	private Integer type;

	private String verifycode;

	private Long takeeffecttime;

	private Long expirytime;

	private Long createtime;

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

	public String getVerifycode() {
		return verifycode;
	}

	public void setVerifycode(String verifycode) {
		this.verifycode = verifycode;
	}

	public Long getTakeeffecttime() {
		return takeeffecttime;
	}

	public void setTakeeffecttime(Long takeeffecttime) {
		this.takeeffecttime = takeeffecttime;
	}

	public Long getExpirytime() {
		return expirytime;
	}

	public void setExpirytime(Long expirytime) {
		this.expirytime = expirytime;
	}

	public Long getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Long createtime) {
		this.createtime = createtime;
	}

}