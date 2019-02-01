package com.tinypig.newadmin.web.entity;

import java.io.Serializable;

public class PayOrder implements Serializable {
	
	private static final long serialVersionUID = -5202026570943574322L;

	private String orderId;

    private Integer srcuid;

    private Integer dstuid;

    private Double amount;

    private Integer zhutou;

    private Integer creatat;

    private Integer paytime;

    private Integer os;

    private String paytype;

    private Integer status;

    private String inpourNo;

    private String details;

    private String usersource;

    private String channel;

    private Integer registtime;

    private Integer dataType;

    private Integer flag;

    private Long createTime;

    private Integer createUserId;

    private Long updateTime;

    private Integer updateUserId;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Integer getSrcuid() {
		return srcuid;
	}

	public void setSrcuid(Integer srcuid) {
		this.srcuid = srcuid;
	}

	public Integer getDstuid() {
		return dstuid;
	}

	public void setDstuid(Integer dstuid) {
		this.dstuid = dstuid;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Integer getZhutou() {
		return zhutou;
	}

	public void setZhutou(Integer zhutou) {
		this.zhutou = zhutou;
	}

	public Integer getCreatat() {
		return creatat;
	}

	public void setCreatat(Integer creatat) {
		this.creatat = creatat;
	}

	public Integer getPaytime() {
		return paytime;
	}

	public void setPaytime(Integer paytime) {
		this.paytime = paytime;
	}

	public Integer getOs() {
		return os;
	}

	public void setOs(Integer os) {
		this.os = os;
	}

	public String getPaytype() {
		return paytype;
	}

	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getInpourNo() {
		return inpourNo;
	}

	public void setInpourNo(String inpourNo) {
		this.inpourNo = inpourNo;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getUsersource() {
		return usersource;
	}

	public void setUsersource(String usersource) {
		this.usersource = usersource;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public Integer getRegisttime() {
		return registtime;
	}

	public void setRegisttime(Integer registtime) {
		this.registtime = registtime;
	}

	public Integer getDataType() {
		return dataType;
	}

	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Integer getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(Integer updateUserId) {
		this.updateUserId = updateUserId;
	}
}