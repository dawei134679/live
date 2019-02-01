package com.tinypig.admin.model;

import java.sql.ResultSet;

public class PayInfoModel implements PopulateTemplate<PayInfoModel>{
	
	private int srcuid;
	private String srcname;
	private int dstuid;
	private String dstname;
	
	private String registchannel;
	private String order_id;
	private String inpour_no;
	private double amount;
	private int os;
	private String paytype;
	private Long creatAt;
	private int status;
	private Long paytime;
	private int userLevel;
	private String details;
	private int registtime;

	public int getSrcuid() {
		return srcuid;
	}
	public void setSrcuid(int srcuid) {
		this.srcuid = srcuid;
	}
	
	public String getRegistchannel() {
		return registchannel;
	}
	public void setRegistchannel(String registchannel) {
		this.registchannel = registchannel;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public int getOs() {
		return os;
	}
	public void setOs(int os) {
		this.os = os;
	}
	public String getPaytype() {
		return paytype;
	}
	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}

	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	@Override
	public PayInfoModel populateFromResultSet(ResultSet rs) {
		try {
			
			srcuid = rs.getInt("srcuid");
			dstuid = rs.getInt("dstuid");
			registchannel = rs.getString("userSource");
			order_id = rs.getString("order_id");
			inpour_no = rs.getString("inpour_no");
			amount = rs.getDouble("amount");
			os = rs.getInt("os");
			paytype = rs.getString("paytype");
			creatAt = rs.getLong("creatAt");
			status = rs.getInt("status");
			paytime = rs.getLong("paytime");
			details = rs.getString("details");
			registtime = rs.getInt("registtime");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	public String getSrcname() {
		return srcname;
	}
	public void setSrcname(String srcname) {
		this.srcname = srcname;
	}
	public String getDstname() {
		return dstname;
	}
	public void setDstname(String dstname) {
		this.dstname = dstname;
	}
	public int getDstuid() {
		return dstuid;
	}
	public void setDstuid(int dstuid) {
		this.dstuid = dstuid;
	}
	public int getUserLevel() {
		return userLevel;
	}
	public void setUserLevel(int userLevel) {
		this.userLevel = userLevel;
	}
	public String getInpour_no() {
		return inpour_no;
	}
	public void setInpour_no(String inpour_no) {
		this.inpour_no = inpour_no;
	}
	public Long getCreatAt() {
		return creatAt;
	}
	public void setCreatAt(Long creatAt) {
		this.creatAt = creatAt;
	}
	public Long getPaytime() {
		return paytime;
	}
	public void setPaytime(Long paytime) {
		this.paytime = paytime;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public int getRegisttime() {
		return registtime;
	}
	public void setRegisttime(int registtime) {
		this.registtime = registtime;
	}
}
