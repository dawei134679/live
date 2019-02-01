package com.tinypig.admin.model;

import java.sql.ResultSet;

public class CashInfoModel implements PopulateTemplate<CashInfoModel>{
	private int time;
	private int uid;
	private String nickname;
	private int anchorLevel;
	private String unionname;
	private String type;
	private String billno;
	private double amount;
	private int createAt;
	private int isSecc;
	

	public String getUnionname() {
		return unionname;
	}


	public void setUnionname(String unionname) {
		this.unionname = unionname;
	}


	public int getTime() {
		return time;
	}


	public void setTime(int time) {
		this.time = time;
	}


	public int getUid() {
		return uid;
	}


	public void setUid(int uid) {
		this.uid = uid;
	}


	public String getNickname() {
		return nickname;
	}


	public void setNickname(String nickname) {
		this.nickname = nickname;
	}


	public int getAnchorLevel() {
		return anchorLevel;
	}


	public void setAnchorLevel(int anchorLevel) {
		this.anchorLevel = anchorLevel;
	}




	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getBillno() {
		return billno;
	}


	public void setBillno(String billno) {
		this.billno = billno;
	}


	public double getAmount() {
		return amount;
	}


	public void setAmount(double amount) {
		this.amount = amount;
	}


	public int getCreateAt() {
		return createAt;
	}


	public void setCreateAt(int createAt) {
		this.createAt = createAt;
	}


	public int getIsSecc() {
		return isSecc;
	}


	public void setIsSecc(int isSecc) {
		this.isSecc = isSecc;
	}


	@Override
	public CashInfoModel populateFromResultSet(ResultSet rs) {
		try {
			time=rs.getInt("time");
			uid=rs.getInt("uid");
			anchorLevel=rs.getInt("anchorLevel");
			unionname=rs.getString("unionname");
			createAt=rs.getInt("createAt");
			isSecc=rs.getInt("isSecc");
			amount=rs.getDouble("amount");
			nickname=rs.getString("nickname");
			type=rs.getString("type");
			billno=rs.getString("billno");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

}
