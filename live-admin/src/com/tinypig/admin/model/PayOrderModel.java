package com.tinypig.admin.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PayOrderModel implements PopulateTemplate<PayOrderModel> {
	
	private String order_id;
	private int srcUid;
	private int dstUid;
	private double amount;
	private int creatAt;
	private int paytime;
	private int os;
	private String paytype;
	private int status;
	private String inpour_no;
	private String details;
	private String channel;
	
	

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public int getSrcUid() {
		return srcUid;
	}

	public void setSrcUid(int srcUid) {
		this.srcUid = srcUid;
	}

	public int getDstUid() {
		return dstUid;
	}

	public void setDstUid(int dstUid) {
		this.dstUid = dstUid;
	}

	public int getCreatAt() {
		return creatAt;
	}

	public void setCreatAt(int creatAt) {
		this.creatAt = creatAt;
	}

	public int getPaytime() {
		return paytime;
	}

	public void setPaytime(int paytime) {
		this.paytime = paytime;
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

	public String getInpour_no() {
		return inpour_no;
	}

	public void setInpour_no(String inpour_no) {
		this.inpour_no = inpour_no;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	@Override
	public PayOrderModel populateFromResultSet(ResultSet rs) {
		try {
			this.order_id = rs.getString("order_id");
			this.srcUid = rs.getInt("srcUid");
			this.dstUid = rs.getInt("dstUid");
			this.amount = rs.getDouble("amount");
			this.creatAt = rs.getInt("creatAt");
			this.paytime = rs.getInt("paytime");
			this.os = rs.getInt("os");
			this.paytype = rs.getString("paytype");
			this.status = rs.getInt("status");
			this.inpour_no = rs.getString("inpour_no");
			this.details = rs.getString("details");
			this.channel = rs.getString("channel");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
}
