package com.hkzb.game.dto;

import java.io.Serializable;

public class UserBaseDto implements Serializable {
	private static final long serialVersionUID = 3593538831137009873L;
	private Integer uid;
	private String nickname;
	private Integer familyId;
	private Boolean sex;
	private Integer anchorLevel;
	private Integer userLevel;
	private Integer identity;
	private String headimage;
	private String livimage;
	private String birthday;
	private String phone;
	private String province;
	private String city;
	private String signature;
	private Long registip;
	private Integer registtime;
	private String registchannel;
	private String subregistchannel;
	private Byte registos;
	private String registimei;
	private Boolean liveStatus;
	private Integer opentime;
	private Integer recommend;
	private Integer videoline;
	private boolean verified;
	private String verified_reason;
	private int contrRq;
	private Long exp = 0l;
	private String constellation = ""; // 星座
	private String hobby = ""; // 爱好
	private String pcimg1; // PC封面4:3
	private String pcimg2; // PC封面4:3
	private Integer grade = 0; // 主播评级
	private Long gameId; // 游戏ID
	private Integer gameStatus; // 游戏状态
	private String gameIconUrl; // 游戏图片地址
	private String gamePageUrl; // 游戏页面地址
	private String gameServerUrl; // 游戏页面地址

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

	public Integer getFamilyId() {
		return familyId;
	}

	public void setFamilyId(Integer familyId) {
		this.familyId = familyId;
	}

	public Boolean getSex() {
		return sex;
	}

	public void setSex(Boolean sex) {
		this.sex = sex;
	}

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

	public Integer getIdentity() {
		return identity;
	}

	public void setIdentity(Integer identity) {
		this.identity = identity;
	}

	public String getHeadimage() {
		return headimage;
	}

	public void setHeadimage(String headimage) {
		this.headimage = headimage;
	}

	public String getLivimage() {
		return livimage;
	}

	public void setLivimage(String livimage) {
		this.livimage = livimage;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public Long getRegistip() {
		return registip;
	}

	public void setRegistip(Long registip) {
		this.registip = registip;
	}

	public Integer getRegisttime() {
		return registtime;
	}

	public void setRegisttime(Integer registtime) {
		this.registtime = registtime;
	}

	public String getRegistchannel() {
		return registchannel;
	}

	public void setRegistchannel(String registchannel) {
		this.registchannel = registchannel;
	}

	public String getSubregistchannel() {
		return subregistchannel;
	}

	public void setSubregistchannel(String subregistchannel) {
		this.subregistchannel = subregistchannel;
	}

	public Byte getRegistos() {
		return registos;
	}

	public void setRegistos(Byte registos) {
		this.registos = registos;
	}

	public String getRegistimei() {
		return registimei;
	}

	public void setRegistimei(String registimei) {
		this.registimei = registimei;
	}

	public Boolean getLiveStatus() {
		return liveStatus;
	}

	public void setLiveStatus(Boolean liveStatus) {
		this.liveStatus = liveStatus;
	}

	public Integer getOpentime() {
		return opentime;
	}

	public void setOpentime(Integer opentime) {
		this.opentime = opentime;
	}

	public Integer getRecommend() {
		return recommend;
	}

	public void setRecommend(Integer recommend) {
		this.recommend = recommend;
	}

	public Integer getVideoline() {
		return videoline;
	}

	public void setVideoline(Integer videoline) {
		this.videoline = videoline;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public String getVerified_reason() {
		return verified_reason;
	}

	public void setVerified_reason(String verified_reason) {
		this.verified_reason = verified_reason;
	}

	public int getContrRq() {
		return contrRq;
	}

	public void setContrRq(int contrRq) {
		this.contrRq = contrRq;
	}

	public Long getExp() {
		return exp;
	}

	public void setExp(Long exp) {
		this.exp = exp;
	}

	public String getConstellation() {
		return constellation;
	}

	public void setConstellation(String constellation) {
		this.constellation = constellation;
	}

	public String getHobby() {
		return hobby;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

	public String getPcimg1() {
		return pcimg1;
	}

	public void setPcimg1(String pcimg1) {
		this.pcimg1 = pcimg1;
	}

	public String getPcimg2() {
		return pcimg2;
	}

	public void setPcimg2(String pcimg2) {
		this.pcimg2 = pcimg2;
	}

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}

	public Integer getGameStatus() {
		return gameStatus;
	}

	public void setGameStatus(Integer gameStatus) {
		this.gameStatus = gameStatus;
	}

	public String getGameIconUrl() {
		return gameIconUrl;
	}

	public void setGameIconUrl(String gameIconUrl) {
		this.gameIconUrl = gameIconUrl;
	}

	public String getGamePageUrl() {
		return gamePageUrl;
	}

	public void setGamePageUrl(String gamePageUrl) {
		this.gamePageUrl = gamePageUrl;
	}

	public String getGameServerUrl() {
		return gameServerUrl;
	}

	public void setGameServerUrl(String gameServerUrl) {
		this.gameServerUrl = gameServerUrl;
	}
}