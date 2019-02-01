package com.mpig.api.modelcomet;

/**
 *同意PK消息
 */
public class PKStartCMod extends BaseCMod {

	public PKStartCMod(){
		this.setCometProtocol(CModProtocol.pk_start);
	}
	//对方主播ID
	private Integer anchorId;
	//对方主播头像
	private String headimage;
	//PK时间
	private Integer pkTime;
	//PK开始时间
	private Long startTime;
	
	public Integer getAnchorId() {
		return anchorId;
	}
	public void setAnchorId(Integer anchorId) {
		this.anchorId = anchorId;
	}
	public String getHeadimage() {
		return headimage;
	}
	public void setHeadimage(String headimage) {
		this.headimage = headimage;
	}
	public Integer getPkTime() {
		return pkTime;
	}
	public void setPkTime(Integer pkTime) {
		this.pkTime = pkTime;
	}
	public Long getStartTime() {
		return startTime;
	}
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
}
