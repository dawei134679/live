package com.mpig.api.model;

public class TokenModel {

	private Integer uid;
	private String pword;
	private Byte os;
	private String imei;
	private Long timestamp;
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public String getPword() {
		return pword;
	}
	public void setPword(String pword) {
		this.pword = pword;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	public Byte getOs() {
		return os;
	}
	public void setOs(Byte os) {
		this.os = os;
	}
}
