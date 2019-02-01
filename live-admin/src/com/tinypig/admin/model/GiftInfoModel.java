package com.tinypig.admin.model;

import java.sql.ResultSet;

public class GiftInfoModel implements PopulateTemplate<GiftInfoModel>{
	
	private int srcuid;
	private String srcnickname;
	private int dstuid;
	private String dstnickname;
	private String giftname;
	private int count;
	private int zhutou;
	private int getmoney;
	private int addtime;
	
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
	
	public String getGiftname() {
		return giftname;
	}
	public void setGiftname(String giftname) {
		this.giftname = giftname;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getGetmoney() {
		return getmoney;
	}
	public void setGetmoney(int getmoney) {
		this.getmoney = getmoney;
	}
	public int getAddtime() {
		return addtime;
	}
	public void setAddtime(int addtime) {
		this.addtime = addtime;
	}
	@Override
	public GiftInfoModel populateFromResultSet(ResultSet rs) {
		try {
			srcuid=rs.getInt("srcuid");
			dstuid=rs.getInt("dstuid");
			dstnickname=rs.getString("dstnickname");
			giftname=rs.getString("giftname");
			count=rs.getInt("count");
			getmoney=rs.getInt("getmoney");
			addtime=rs.getInt("addtime");
			srcnickname=rs.getString("srcnickname");
			zhutou = rs.getInt("zhutou");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	public int getZhutou() {
		return zhutou;
	}
	public void setZhutou(int zhutou) {
		this.zhutou = zhutou;
	}

}
