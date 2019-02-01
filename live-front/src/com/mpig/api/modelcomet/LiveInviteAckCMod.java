package com.mpig.api.modelcomet;

import java.util.Map;

public class LiveInviteAckCMod extends BaseCMod{
	public LiveInviteAckCMod(){
		this.setCometProtocol(CModProtocol.live_invite_ack);
	}

	
	private Integer isJoin;		//是否开启连麦
	
	private Integer uid2nd;
	private String 	nickname2nd;
	private Boolean sex2nd;
	private Integer level2nd;
	private String 	avatar2nd;
	
	private Map<String, Object>  videoStream;		//连麦流的DOMAIN	同enterRoom的domain

	public Integer getIsJoin() {
		return isJoin;
	}

	public void setIsJoin(Integer isJoin) {
		this.isJoin = isJoin;
	}

	public Map<String, Object> getVideoStream() {
		return videoStream;
	}

	public void setVideoStream(Map<String, Object> videoStream) {
		this.videoStream = videoStream;
	}

	public Integer getUid2nd() {
		return uid2nd;
	}

	public void setUid2nd(Integer uid2nd) {
		this.uid2nd = uid2nd;
	}

	public String getNickname2nd() {
		return nickname2nd;
	}

	public void setNickname2nd(String nickname2nd) {
		this.nickname2nd = nickname2nd;
	}

	public Boolean getSex2nd() {
		return sex2nd;
	}

	public void setSex2nd(Boolean sex2nd) {
		this.sex2nd = sex2nd;
	}

	public Integer getLevel2nd() {
		return level2nd;
	}

	public void setLevel2nd(Integer level2nd) {
		this.level2nd = level2nd;
	}

	public String getAvatar2nd() {
		return avatar2nd;
	}

	public void setAvatar2nd(String avatar2nd) {
		this.avatar2nd = avatar2nd;
	}

}
