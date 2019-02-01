package com.tinypig.admin.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.tinypig.admin.util.VarConfigUtils;

public class CheckUserModel implements PopulateTemplate<CheckUserModel>{
	private int id;
	private int uid;
	private String realName;
	private String cardId;
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

	public int getAuditAt() {
		return auditAt;
	}
	public void setAuditAt(int auditAt) {
		this.auditAt = auditAt;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getBankAccount() {
		return bankAccount;
	}
	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}
	public String getProvinceOfBank() {
		return provinceOfBank;
	}
	public void setProvinceOfBank(String provinceOfBank) {
		this.provinceOfBank = provinceOfBank;
	}
	public String getCityOfBank() {
		return cityOfBank;
	}
	public void setCityOfBank(String cityOfBank) {
		this.cityOfBank = cityOfBank;
	}
	public String getBranchBank() {
		return branchBank;
	}
	public void setBranchBank(String branchBank) {
		this.branchBank = branchBank;
	}
	public String getPositiveImage() {
		return positiveImage;
	}
	public void setPositiveImage(String positiveImage) {
		this.positiveImage = positiveImage;
	}
	public String getNegativeImage() {
		return negativeImage;
	}
	public void setNegativeImage(String negativeImage) {
		this.negativeImage = negativeImage;
	}
	public String getHandImage() {
		return handImage;
	}
	public void setHandImage(String handImage) {
		this.handImage = handImage;
	}
	public int getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(int auditStatus) {
		this.auditStatus = auditStatus;
	}
	public String getCause() {
		return cause;
	}
	public void setCause(String cause) {
		this.cause = cause;
	}
	public int getCreateAt() {
		return createAt;
	}
	public void setCreateAt(int createAt) {
		this.createAt = createAt;
	}
	
	
	@Override
	public CheckUserModel populateFromResultSet(ResultSet rs) {
		try {
			this.id=rs.getInt("id");
			this.uid = rs.getInt("uid");
			this.bankAccount = rs.getString("bankAccount");
			this.branchBank = rs.getString("branchBank");
			this.cardNo = rs.getString("cardNo");
			this.realName = rs.getString("realName");
			this.cardId = rs.getString("cardId");
			this.cause = rs.getString("cause");
			this.cityOfBank = rs.getString("cityOfBank");
			this.handImage = VarConfigUtils.staticUrl+rs.getString("handImage");
			this.negativeImage = VarConfigUtils.staticUrl+rs.getString("negativeImage");
			this.provinceOfBank = rs.getString("provinceOfBank");
			this.positiveImage = VarConfigUtils.staticUrl+rs.getString("positiveImage");
			this.createAt = rs.getInt("createAt");
			this.auditAt = rs.getInt("auditAt");
			this.auditStatus=rs.getInt("auditStatus");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this;
	}
}
