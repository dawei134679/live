package com.mpig.api.model;

import java.sql.ResultSet;

public class PayMallListModel implements PopulateTemplate<PayMallListModel>{

	private int id;
	private int gid;
	private String gname;
	private int srcuid;
	private String srcnickname;
	private int dstuid;
	private String dstnickname;
	private int count;
	private int price;
	private int realprice;
	private int pricetotal;
	private int realpricetotal;
	private int credit;
	private int createAt;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getGid() {
		return gid;
	}
	public void setGid(int gid) {
		this.gid = gid;
	}
	public String getGname() {
		return gname;
	}
	public void setGname(String gname) {
		this.gname = gname;
	}
	public int getSrcuid() {
		return srcuid;
	}
	public void setSrcuid(int srcuid) {
		this.srcuid = srcuid;
	}
	public String getSrcnickname() {
		return srcnickname;
	}
	public void setSrcnickname(String srcnickname) {
		this.srcnickname = srcnickname;
	}
	public int getDstuid() {
		return dstuid;
	}
	public void setDstuid(int dstuid) {
		this.dstuid = dstuid;
	}
	public String getDstnickname() {
		return dstnickname;
	}
	public void setDstnickname(String dstnickname) {
		this.dstnickname = dstnickname;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getRealprice() {
		return realprice;
	}
	public void setRealprice(int realprice) {
		this.realprice = realprice;
	}
	public int getPricetotal() {
		return pricetotal;
	}
	public void setPricetotal(int pricetotal) {
		this.pricetotal = pricetotal;
	}
	public int getRealpricetotal() {
		return realpricetotal;
	}
	public void setRealpricetotal(int realpricetotal) {
		this.realpricetotal = realpricetotal;
	}
	public int getCredit() {
		return credit;
	}
	public void setCredit(int credit) {
		this.credit = credit;
	}
	public int getCreateAt() {
		return createAt;
	}
	public void setCreateAt(int createAt) {
		this.createAt = createAt;
	}
	@Override
	public PayMallListModel populateFromResultSet(ResultSet rs) {
		try {
			this.id = rs.getInt("id");
			this.gid = rs.getInt("gid");
			this.gname = rs.getString("gname");
			this.srcuid = rs.getInt("srcuid");
			this.srcnickname = rs.getString("srcnickname");
			this.dstuid = rs.getInt("dstuid");
			this.dstnickname = rs.getString("dstnickname");
			this.count = rs.getInt("count");
			this.price = rs.getInt("price");
			this.realprice = rs.getInt("realprice");
			this.pricetotal = rs.getInt("pricetotal");
			this.realpricetotal = rs.getInt("realpricetotal");
			this.credit = rs.getInt("credit");
			this.createAt = rs.getInt("createAt");
		} catch (Exception e) {

		}
		return this;
	}
}
