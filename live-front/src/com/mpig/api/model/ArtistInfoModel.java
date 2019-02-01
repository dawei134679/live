package com.mpig.api.model;

/**
 * 用户进入房间的主播信息
 * @author fang
 *
 */
public class ArtistInfoModel {
	private Integer uid;		//主播uid
	private String nickname;	//主播昵称
	private String headimage;	//主播头像
	private String slogan;		//主播slogan
	private Byte isplay;		//主播状态
	private Long gets;			//主播总获取
	
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
	public String getHeadimage() {
		return headimage;
	}
	public void setHeadimage(String headimage) {
		this.headimage = headimage;
	}
	public String getSlogan() {
		return slogan;
	}
	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}
	public Byte getIsplay() {
		return isplay;
	}
	public void setIsplay(Byte isplay) {
		this.isplay = isplay;
	}
	public Long getGets() {
		return gets;
	}
	public void setGets(Long gets) {
		this.gets = gets;
	}
	
}
