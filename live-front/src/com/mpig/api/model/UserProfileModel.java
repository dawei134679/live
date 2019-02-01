package com.mpig.api.model;

/**
 * 用户简介
 * @author fang
 *
 */
public class UserProfileModel {
	private Integer uid;		//用户UID
	private String headimage;	//用户小图标
	private Integer levels;		//主播等级
	
	private String nickName;	//用户昵称
	private String signature;	//签名
	private String city;		//城市 
	private Integer follows;	//关注人数
	private Integer fans;		//粉丝人数
	private Integer sends;		//送出数
	private Integer gets;		//收到数
	
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public String getHeadimage() {
		return headimage;
	}
	public void setHeadimage(String headimage) {
		this.headimage = headimage;
	}
	public Integer getLevels() {
		return levels;
	}
	public void setLevels(Integer levels) {
		this.levels = levels;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
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
	public Integer getSends() {
		return sends;
	}
	public void setSends(Integer sends) {
		this.sends = sends;
	}
	public Integer getGets() {
		return gets;
	}
	public void setGets(Integer gets) {
		this.gets = gets;
	}
}
