package com.mpig.api.model;

import java.sql.ResultSet;

public class BillRedenvelopeModel implements PopulateTemplate<BillRedenvelopeModel> {

	private int id;
	private int roomId;
	private String reddesc;
	private int isfinish;
	private int srcUid;
	private int dstUid;
	private int sendmoney;
	private int sendcnts;
	private int getmoney;
	private int getcnts;
	private int sendtime;
	private int gettime;
	public int getId() {
		return id;
	}
	public int getRoomId() {
		return roomId;
	}
	public String getReddesc() {
		return reddesc;
	}
	public int getIsfinish() {
		return isfinish;
	}
	public int getSrcUid() {
		return srcUid;
	}
	public int getDstUid() {
		return dstUid;
	}
	public int getSendmoney() {
		return sendmoney;
	}
	public int getSendcnts() {
		return sendcnts;
	}
	public int getGetmoney() {
		return getmoney;
	}
	public int getGetcnts() {
		return getcnts;
	}
	public int getSendtime() {
		return sendtime;
	}
	public int getGettime() {
		return gettime;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}
	public void setReddesc(String reddesc) {
		this.reddesc = reddesc;
	}
	public void setIsfinish(int isfinish) {
		this.isfinish = isfinish;
	}
	public void setSrcUid(int srcUid) {
		this.srcUid = srcUid;
	}
	public void setDstUid(int dstUid) {
		this.dstUid = dstUid;
	}
	public void setSendmoney(int sendmoney) {
		this.sendmoney = sendmoney;
	}
	public void setSendcnts(int sendcnts) {
		this.sendcnts = sendcnts;
	}
	public void setGetmoney(int getmoney) {
		this.getmoney = getmoney;
	}
	public void setGetcnts(int getcnts) {
		this.getcnts = getcnts;
	}
	public void setSendtime(int sendtime) {
		this.sendtime = sendtime;
	}
	public void setGettime(int gettime) {
		this.gettime = gettime;
	}
	@Override
	public BillRedenvelopeModel populateFromResultSet(ResultSet rs) {
		try{
			this.id = rs.getInt("id");
			this.roomId = rs.getInt("roomId");
			this.reddesc = rs.getString("reddesc");
			this.isfinish = rs.getInt("isfinish");
			this.srcUid = rs.getInt("srcUid");
			this.dstUid = rs.getInt("dstUid");
			this.sendmoney = rs.getInt("sendmoney");
			this.sendcnts = rs.getInt("sendcnts");
			this.sendtime = rs.getInt("sendtime");
			this.getmoney = rs.getInt("getmoney");
			this.getcnts = rs.getInt("getcnts");
			this.gettime = rs.getInt("gettime");
		}catch(Exception ex){
			
		}
		return this;
	}
}
