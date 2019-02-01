package com.mpig.api.modelcomet;

/**
 * 聊天广播MODEL基础
 * @author tosy
 *
 *	cometProtocol	消息类型
 *	
 *	uid				发送者的uid
 *	nickname		发送者的昵称
 *	sex				发送者性别
 *	level			发送者用户等级，非主播等级
 *	avatar			发送者头像	
 */
public class BaseCMod {
	
	private Integer cometProtocol;

	private Integer uid;
	private String 	nickname;
	private Boolean sex;
	private Integer level;
	private String 	avatar;
	
	
	public Integer getCometProtocol() {
		return cometProtocol;
	}
	public void setCometProtocol(Integer cometProtocol) {
		this.cometProtocol = cometProtocol;
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
	public Boolean getSex() {
		return sex;
	}
	public void setSex(Boolean sex) {
		this.sex = sex;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

}
