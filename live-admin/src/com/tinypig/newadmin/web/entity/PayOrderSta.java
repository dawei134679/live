package com.tinypig.newadmin.web.entity;

import java.io.Serializable;

public class PayOrderSta implements Serializable {

	private static final long serialVersionUID = 6443558210946216179L;

	private String orderId;
	
	private Integer srcuid;
	
	private String srcnickname;
	
	private Double amount;
	
	private Integer zhutou;
	
	private Integer creatAt;
	
	private String creatAtStr;
	
	private Integer paytime;
	
	private String paytimeStr;
	
	private String paytype;
	
	private Integer status;
	
	private String statusStr;
	
	private String inpourNo;
	
	private Integer dataType;
	
	private String dataTypeStr;
	
	private Integer registtime;
	
	private String registtimeStr;
	
	private String salesmanName;

	private String salesmanContactsPhone;

	private String promotersName;

	private String promotersContactsName;

	private String promotersContactsPhone;

	private String extensionCenterName;

	private String extensionCenterContactsName;

	private String extensionCenterContactsPhone;

	private String strategicPartnerName;

	private String strategicPartnerContactsName;

	private String strategicPartnerContactsPhone;

	private String agentUserName;

	private String agentUserContactsName;

	private String agentUserContactsPhone;

	private Long agentUserId;
	private Long strategicPartnerId;
	private Long extensionCenterId;
	private Long promotersId;
	private Long salesmanId;
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
	public String getSrcnickname() {
		return srcnickname;
	}
	public void setSrcnickname(String srcnickname) {
		this.srcnickname = srcnickname;
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
	public Integer getCreatAt() {
		return creatAt;
	}
	public void setCreatAt(Integer creatAt) {
		this.creatAt = creatAt;
	}
	public String getCreatAtStr() {
		return creatAtStr;
	}
	public void setCreatAtStr(String creatAtStr) {
		this.creatAtStr = creatAtStr;
	}
	public Integer getPaytime() {
		return paytime;
	}
	public void setPaytime(Integer paytime) {
		this.paytime = paytime;
	}
	public String getPaytimeStr() {
		return paytimeStr;
	}
	public void setPaytimeStr(String paytimeStr) {
		this.paytimeStr = paytimeStr;
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
	public String getStatusStr() {
		return statusStr;
	}
	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}
	public String getInpourNo() {
		return inpourNo;
	}
	public void setInpourNo(String inpourNo) {
		this.inpourNo = inpourNo;
	}
	
	public Integer getDataType() {
		return dataType;
	}
	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}
	public String getDataTypeStr() {
		return dataTypeStr;
	}
	public void setDataTypeStr(String dataTypeStr) {
		this.dataTypeStr = dataTypeStr;
	}
	public Integer getRegisttime() {
		return registtime;
	}
	public void setRegisttime(Integer registtime) {
		this.registtime = registtime;
	}
	public String getRegisttimeStr() {
		return registtimeStr;
	}
	public void setRegisttimeStr(String registtimeStr) {
		this.registtimeStr = registtimeStr;
	}
	public String getSalesmanName() {
		return salesmanName;
	}
	public void setSalesmanName(String salesmanName) {
		this.salesmanName = salesmanName;
	}
	public String getSalesmanContactsPhone() {
		return salesmanContactsPhone;
	}
	public void setSalesmanContactsPhone(String salesmanContactsPhone) {
		this.salesmanContactsPhone = salesmanContactsPhone;
	}
	public String getPromotersName() {
		return promotersName;
	}
	public void setPromotersName(String promotersName) {
		this.promotersName = promotersName;
	}
	public String getPromotersContactsName() {
		return promotersContactsName;
	}
	public void setPromotersContactsName(String promotersContactsName) {
		this.promotersContactsName = promotersContactsName;
	}
	public String getPromotersContactsPhone() {
		return promotersContactsPhone;
	}
	public void setPromotersContactsPhone(String promotersContactsPhone) {
		this.promotersContactsPhone = promotersContactsPhone;
	}
	public String getExtensionCenterName() {
		return extensionCenterName;
	}
	public void setExtensionCenterName(String extensionCenterName) {
		this.extensionCenterName = extensionCenterName;
	}
	public String getExtensionCenterContactsName() {
		return extensionCenterContactsName;
	}
	public void setExtensionCenterContactsName(String extensionCenterContactsName) {
		this.extensionCenterContactsName = extensionCenterContactsName;
	}
	public String getExtensionCenterContactsPhone() {
		return extensionCenterContactsPhone;
	}
	public void setExtensionCenterContactsPhone(String extensionCenterContactsPhone) {
		this.extensionCenterContactsPhone = extensionCenterContactsPhone;
	}
	public String getStrategicPartnerName() {
		return strategicPartnerName;
	}
	public void setStrategicPartnerName(String strategicPartnerName) {
		this.strategicPartnerName = strategicPartnerName;
	}
	public String getStrategicPartnerContactsName() {
		return strategicPartnerContactsName;
	}
	public void setStrategicPartnerContactsName(String strategicPartnerContactsName) {
		this.strategicPartnerContactsName = strategicPartnerContactsName;
	}
	public String getStrategicPartnerContactsPhone() {
		return strategicPartnerContactsPhone;
	}
	public void setStrategicPartnerContactsPhone(String strategicPartnerContactsPhone) {
		this.strategicPartnerContactsPhone = strategicPartnerContactsPhone;
	}
	public String getAgentUserName() {
		return agentUserName;
	}
	public void setAgentUserName(String agentUserName) {
		this.agentUserName = agentUserName;
	}
	public String getAgentUserContactsName() {
		return agentUserContactsName;
	}
	public void setAgentUserContactsName(String agentUserContactsName) {
		this.agentUserContactsName = agentUserContactsName;
	}
	public String getAgentUserContactsPhone() {
		return agentUserContactsPhone;
	}
	public void setAgentUserContactsPhone(String agentUserContactsPhone) {
		this.agentUserContactsPhone = agentUserContactsPhone;
	}
	public Long getAgentUserId() {
		return agentUserId;
	}
	public void setAgentUserId(Long agentUserId) {
		this.agentUserId = agentUserId;
	}
	public Long getStrategicPartnerId() {
		return strategicPartnerId;
	}
	public void setStrategicPartnerId(Long strategicPartnerId) {
		this.strategicPartnerId = strategicPartnerId;
	}
	public Long getExtensionCenterId() {
		return extensionCenterId;
	}
	public void setExtensionCenterId(Long extensionCenterId) {
		this.extensionCenterId = extensionCenterId;
	}
	public Long getPromotersId() {
		return promotersId;
	}
	public void setPromotersId(Long promotersId) {
		this.promotersId = promotersId;
	}
	public Long getSalesmanId() {
		return salesmanId;
	}
	public void setSalesmanId(Long salesmanId) {
		this.salesmanId = salesmanId;
	}
}
