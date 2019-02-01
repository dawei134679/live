package com.mpig.api.model;

/**
 * 直播间中的观众列表
 * @author fang
 *
 */
public class UserListOfLiveModel {

	private String headImage; 	// 头像
	private Integer uid;		// 用户UID
	private Integer level;		// 用户等级
	private String nickName;	// 昵称
	private Boolean sex; 		// 性别
	private String signature;	// 签名
	private String city;		// 城市
	private Integer follows;	// 关注数
	private Integer fans;		// 粉丝数
	private Integer scount;		// 送出数
	private Integer gcount;		// 收到数
	
	public String getHeadImage() {
		return headImage;
	}
	public void setHeadImage(String headImage) {
		this.headImage = headImage;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public Boolean getSex() {
		return sex;
	}
	public void setSex(Boolean sex) {
		this.sex = sex;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public Integer getFollows() {
		return follows;
	}
	public void setFollows(Integer follows) {
		this.follows = follows;
	}
	public Integer getFans() {
		return fans;
	}
	public void setFans(Integer fans) {
		this.fans = fans;
	}
	public Integer getScount() {
		return scount;
	}
	public void setScount(Integer scount) {
		this.scount = scount;
	}
	public Integer getGcount() {
		return gcount;
	}
	public void setGcount(Integer gcount) {
		this.gcount = gcount;
	}
}
