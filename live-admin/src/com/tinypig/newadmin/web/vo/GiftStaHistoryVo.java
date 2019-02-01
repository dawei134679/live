package com.tinypig.newadmin.web.vo;

import java.io.Serializable;

public class GiftStaHistoryVo implements Serializable {
	private static final long serialVersionUID = -5826272562696635820L;
	private Long srcuid;
	private String srcnickname;
	private Long dstuid;
	private String dstnickname;
	private Long addtime;
	private Long gid;
	private String gname;
	private Integer price;
	private Integer count;
	private Integer gflag;
	private Integer supportUserFlag;

	public Long getSrcuid() {
		return srcuid;
	}

	public void setSrcuid(Long srcuid) {
		this.srcuid = srcuid;
	}

	public String getSrcnickname() {
		return srcnickname;
	}

	public void setSrcnickname(String srcnickname) {
		this.srcnickname = srcnickname;
	}

	public Long getDstuid() {
		return dstuid;
	}

	public void setDstuid(Long dstuid) {
		this.dstuid = dstuid;
	}

	public String getDstnickname() {
		return dstnickname;
	}

	public void setDstnickname(String dstnickname) {
		this.dstnickname = dstnickname;
	}

	public Long getAddtime() {
		return addtime;
	}

	public void setAddtime(Long addtime) {
		this.addtime = addtime;
	}

	public Long getGid() {
		return gid;
	}

	public void setGid(Long gid) {
		this.gid = gid;
	}

	public String getGname() {
		return gname;
	}

	public void setGname(String gname) {
		this.gname = gname;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getGflag() {
		return gflag;
	}

	public void setGflag(Integer gflag) {
		this.gflag = gflag;
	}

	public Integer getSupportUserFlag() {
		return supportUserFlag;
	}

	public void setSupportUserFlag(Integer supportUserFlag) {
		this.supportUserFlag = supportUserFlag;
	}

}
