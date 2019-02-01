package com.mpig.api.model;

import java.io.Serializable;
import java.sql.ResultSet;

public class AuthenticationModel implements PopulateTemplate<AuthenticationModel>,Serializable {
	private static final long serialVersionUID = -266481439811339198L;
	private int id;
	private int uid;
	private String realName;
	private String cardID;
	private String cardNo;
	private String bankAccount;
	private String provinceOfBank;
	private String cityOfBank;
	private String branchBank;
	private String positiveImage;
	private String negativeImage;
	private String handImage;
	private int auditStatus;
	private String cause;
	private int createAt;
	private int auditAt;
	
	public int getId() {
		return id;
	}
	public int getUid() {
		return uid;
	}
	public String getRealName() {
		return realName;
	}
	public String getCardID() {
		return cardID;
	}
	public String getCardNo() {
		return cardNo;
	}
	public String getBankAccount() {
		return bankAccount;
	}
	public String getProvinceOfBank() {
		return provinceOfBank;
	}
	public String getCityOfBank() {
		return cityOfBank;
	}
	public String getBranchBank() {
		return branchBank;
	}
	public String getPositiveImage() {
		return positiveImage;
	}
	public String getNegativeImage() {
		return negativeImage;
	}
	public String getHandImage() {
		return handImage;
	}
	public int getAuditStatus() {
		return auditStatus;
	}
	public String getCause() {
		return cause;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public void setCardID(String cardID) {
		this.cardID = cardID;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}
	public void setProvinceOfBank(String provinceOfBank) {
		this.provinceOfBank = provinceOfBank;
	}
	public void setCityOfBank(String cityOfBank) {
		this.cityOfBank = cityOfBank;
	}
	public void setBranchBank(String branchBank) {
		this.branchBank = branchBank;
	}
	public void setPositiveImage(String positiveImage) {
		this.positiveImage = positiveImage;
	}
	public void setNegativeImage(String negativeImage) {
		this.negativeImage = negativeImage;
	}
	public void setHandImage(String handImage) {
		this.handImage = handImage;
	}
	public void setAuditStatus(int auditStatus) {
		this.auditStatus = auditStatus;
	}
	public void setCause(String cause) {
		this.cause = cause;
	}
	
	public int getCreateAt() {
		return createAt;
	}
	public int getAuditAt() {
		return auditAt;
	}
	public void setCreateAt(int createAt) {
		this.createAt = createAt;
	}
	public void setAuditAt(int auditAt) {
		this.auditAt = auditAt;
	}
	@Override
	public AuthenticationModel populateFromResultSet(ResultSet rs) {
		try {
			this.id = rs.getInt("id");
			this.realName = rs.getString("realName");
			this.cardID = rs.getString("cardID");
			this.cardNo = rs.getString("cardNo");
			this.bankAccount = rs.getString("bankAccount");
			this.provinceOfBank = rs.getString("provinceOfBank");
			this.cityOfBank = rs.getString("cityOfBank");
			this.branchBank = rs.getString("branchBank");
			this.positiveImage = rs.getString("positiveImage");
			this.negativeImage = rs.getString("negativeImage");
			this.handImage = rs.getString("handImage");
			this.auditStatus = rs.getInt("auditStatus");
			this.cause = rs.getString("cause");
			this.createAt = rs.getInt("createAt");
			this.auditAt = rs.getInt("auditAt");
			this.uid = rs.getInt("uid");
			
		} catch (Exception e) {
			System.out.println("AuthenticationModel="+e.toString());
		}
		return this;
	}
}
