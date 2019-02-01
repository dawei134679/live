package com.mpig.api.dictionary;

import java.sql.ResultSet;

import com.mpig.api.model.PopulateTemplate;

public class GiftPromotionConfig implements PopulateTemplate<GiftPromotionConfig>{
	
	private Integer id;
	private Integer gid; //礼物gid
	private String promotionName; //促销活动名
	private Integer discount; //折扣[%]
	private Integer disPrice; //促销价
	private Integer isvalid; //=1有效 其他无效
	private Integer starttime; //促销开始时间
	private Integer endtime; //促销结束时间
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getGid() {
		return gid;
	}
	public void setGid(Integer gid) {
		this.gid = gid;
	}
	public String getPromotionName() {
		return promotionName;
	}
	public void setPromotionName(String promotionName) {
		this.promotionName = promotionName;
	}
	public Integer getDiscount() {
		return discount;
	}
	public void setDiscount(Integer discount) {
		this.discount = discount;
	}
	public Integer getDisPrice() {
		return disPrice;
	}
	public void setDisPrice(Integer disPrice) {
		this.disPrice = disPrice;
	}
	public Integer getIsvalid() {
		return isvalid;
	}
	public void setIsvalid(Integer isvalid) {
		this.isvalid = isvalid;
	}
	public Integer getStarttime() {
		return starttime;
	}
	public void setStarttime(Integer starttime) {
		this.starttime = starttime;
	}
	public Integer getEndtime() {
		return endtime;
	}
	public void setEndtime(Integer endtime) {
		this.endtime = endtime;
	}
	@Override
	public GiftPromotionConfig populateFromResultSet(ResultSet rs) {
		try {
			this.id = rs.getInt("id");
			this.gid = rs.getInt("gid");
			this.promotionName = rs.getString("promotionName");
			this.discount = rs.getInt("discount");
			this.disPrice = rs.getInt("disPrice");
			this.isvalid = rs.getInt("isvalid");
			this.starttime = rs.getInt("starttime");
			this.endtime = rs.getInt("endtime");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	
	
	
}
