package com.mpig.api.model;

import java.sql.ResultSet;

import org.elasticsearch.action.admin.indices.template.get.GetIndexTemplatesAction;

public class PayGetRedenvelopModel implements PopulateTemplate<PayGetRedenvelopModel> {

	private int id;
	private int envelopeid;
	private int srcUid;
	private int dstUid;
	private int roomId;
	private int money;
	private int getTime;
	public int getId() {
		return id;
	}
	public int getEnvelopeid() {
		return envelopeid;
	}
	public int getSrcUid() {
		return srcUid;
	}
	public int getDstUid() {
		return dstUid;
	}
	public int getRoomId() {
		return roomId;
	}
	public int getMoney() {
		return money;
	}
	public int getGetTime() {
		return getTime;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setEnvelopeid(int envelopeid) {
		this.envelopeid = envelopeid;
	}
	public void setSrcUid(int srcUid) {
		this.srcUid = srcUid;
	}
	public void setDstUid(int dstUid) {
		this.dstUid = dstUid;
	}
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public void setGetTime(int getTime) {
		this.getTime = getTime;
	}
	@Override
	public PayGetRedenvelopModel populateFromResultSet(ResultSet rs) {
		try{
			this.id = rs.getInt("id");
			this.envelopeid = rs.getInt("envelopeid");
			this.srcUid = rs.getInt("srcUid");
			this.dstUid = rs.getInt("dstUid");
			this.roomId = rs.getInt("roomId");
			this.money = rs.getInt("money");
			this.getTime = rs.getInt("getTime");
		}catch(Exception ex){
			ex.printStackTrace();
		}

		return this;
	}
}
