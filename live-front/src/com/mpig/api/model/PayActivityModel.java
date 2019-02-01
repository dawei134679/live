package com.mpig.api.model;

import java.sql.ResultSet;

public class PayActivityModel implements PopulateTemplate<PayActivityModel> {

	private int id;
	private int uid;
	private int source;
	private int act_id;
	private String act_name;
	private int zhutou;
	private int createAt;
	
	public int getId() {
		return id;
	}
	public int getUid() {
		return uid;
	}
	public int getSource() {
		return source;
	}
	public void setSource(int source) {
		this.source = source;
	}
	public int getAct_id() {
		return act_id;
	}
	public String getAct_name() {
		return act_name;
	}
	public int getZhutou() {
		return zhutou;
	}
	public int getCreateAt() {
		return createAt;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public void setAct_id(int act_id) {
		this.act_id = act_id;
	}
	public void setAct_name(String act_name) {
		this.act_name = act_name;
	}
	public void setZhutou(int zhutou) {
		this.zhutou = zhutou;
	}
	public void setCreateAt(int createAt) {
		this.createAt = createAt;
	}
	@Override
	public PayActivityModel populateFromResultSet(ResultSet rs) {
		try{
			this.id = rs.getInt("id");
			this.uid = rs.getInt("uid");
			this.act_id = rs.getInt("act_id");
			this.act_name = rs.getString("act_name");
			this.zhutou = rs.getInt("zhutou");
			this.createAt = rs.getInt("createAt");
			this.source = rs.getInt("source");
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return this;
	}
}
