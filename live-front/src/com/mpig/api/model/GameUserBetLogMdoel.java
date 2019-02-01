package com.mpig.api.model;

import java.sql.ResultSet;

public class GameUserBetLogMdoel implements PopulateTemplate<GameUserBetLogMdoel> {

	private int id;
	private String order_id;
	private int uid;
	private int rid;
	private int nid;
	private int type;
	private int deduct_money;
	private int add_price;
	private int add_time;
	private int update_time;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public int getRid() {
		return rid;
	}
	public void setRid(int rid) {
		this.rid = rid;
	}
	public int getNid() {
		return nid;
	}
	public void setNid(int nid) {
		this.nid = nid;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getDeduct_money() {
		return deduct_money;
	}
	public void setDeduct_money(int deduct_money) {
		this.deduct_money = deduct_money;
	}
	public int getAdd_price() {
		return add_price;
	}
	public void setAdd_price(int add_price) {
		this.add_price = add_price;
	}
	public int getAdd_time() {
		return add_time;
	}
	public void setAdd_time(int add_time) {
		this.add_time = add_time;
	}
	public int getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(int update_time) {
		this.update_time = update_time;
	}
	@Override
	public GameUserBetLogMdoel populateFromResultSet(ResultSet rs) {
		
		try {
			this.id = rs.getInt("id");
			this.order_id = rs.getString("order_id");
			this.uid = rs.getInt("uid");
			this.rid = rs.getInt("rid");
			this.nid = rs.getInt("nid");
			this.type = rs.getInt("type");
			this.deduct_money = rs.getInt("deduct_money");
			this.add_price = rs.getInt("add_price");
			this.add_time = rs.getInt("add_time");
			this.update_time = rs.getInt("update_time");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
}
