package com.mpig.api.model;

/**
 * 搜索返回的人
 * 
 * @author tosy
 *
 */
public class SearchModel {
	Integer uid;

	String nickname;
	String numb;

	String avatar;
	String slogan;

	Boolean sex;

	Boolean living;
	Boolean isAttention; // 是否已经关注
	Boolean verified; // false 未认证 true 已认证

	Integer userLevel;
	Integer anchorLevel;
	Long fansCount; //粉丝数
	Long opentime; //开播时间
	public Integer getAnchorLevel() {
		return anchorLevel;
	}

	public void setAnchorLevel(Integer anchorLevel) {
		this.anchorLevel = anchorLevel;
	}

	public Integer getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(Integer userLevel) {
		this.userLevel = userLevel;
	}
	
	public Boolean getVerified() {
		return verified;
	}

	public void setVerified(Boolean verified) {
		this.verified = verified;
	}

	public Boolean getLiving() {
		return living;
	}

	public void setLiving(Boolean living) {
		this.living = living;
	}

	public Boolean getIsAttention() {
		return isAttention;
	}

	public void setIsAttention(Boolean isAttention) {
		this.isAttention = isAttention;
	}

	public Boolean getSex() {
		return sex;
	}

	public void setSex(Boolean sex) {
		this.sex = sex;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getSlogan() {
		return slogan;
	}

	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getNumb() {
		return numb;
	}

	public void setNumb(String numb) {
		this.numb = numb;
	}

	public Long getFansCount() {
		return fansCount;
	}

	public void setFansCount(Long fansCount) {
		this.fansCount = fansCount;
	}

	public Long getOpentime() {
		return opentime;
	}

	public void setOpentime(Long opentime) {
		this.opentime = opentime;
	}
	
	
}
