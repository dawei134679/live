package com.tinypig.newadmin.web.entity;

/**
 * 违规查询-主播信息
 * @author fangwuqing
 *
 */
public class PunishAnchor {

	private int anchoruid; // 主播uid
	private String nickname; // 主播昵称
	private String unionname; // 工会名
	private String adminname; // 归属人名
	private int anchorLevel; // 主播等级
	private int recommend; // 房间等级
	private int status;	// 主播状态
	private int warns;	// 警告次数
	private int violations; // 违规次数
	private boolean ids; // 身份认证
	
	public int getAnchoruid() {
		return anchoruid;
	}
	public String getNickname() {
		return nickname;
	}
	public String getUnionname() {
		return unionname;
	}
	public String getAdminname() {
		return adminname;
	}
	public int getAnchorLevel() {
		return anchorLevel;
	}
	public int getRecommend() {
		return recommend;
	}
	public int getStatus() {
		return status;
	}
	public int getWarns() {
		return warns;
	}
	public int getViolations() {
		return violations;
	}
	public boolean isIds() {
		return ids;
	}
	public void setAnchoruid(int anchoruid) {
		this.anchoruid = anchoruid;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public void setUnionname(String unionname) {
		this.unionname = unionname;
	}
	public void setAdminname(String adminname) {
		this.adminname = adminname;
	}
	public void setAnchorLevel(int anchorLevel) {
		this.anchorLevel = anchorLevel;
	}
	public void setRecommend(int recommend) {
		this.recommend = recommend;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public void setWarns(int warns) {
		this.warns = warns;
	}
	public void setViolations(int violations) {
		this.violations = violations;
	}
	public void setIds(boolean ids) {
		this.ids = ids;
	}
	
}
