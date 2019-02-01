package com.tinypig.newadmin.web.entity;

import java.io.Serializable;

public class UserTransactionHisSta implements Serializable {

	private static final long serialVersionUID = 5433313527004297520L;

    private Integer transType;
    
    private String transTypeStr;

    private Integer uid;
    
    private String nickname;

    private Double amount;

    private Long money;

    private Long createTime;
    
    private String createTimeStr;

    private String refId;

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
	public Integer getTransType() {
		return transType;
	}
	public void setTransType(Integer transType) {
		this.transType = transType;
	}
	public String getTransTypeStr() {
		return transTypeStr;
	}
	public void setTransTypeStr(String transTypeStr) {
		this.transTypeStr = transTypeStr;
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
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Long getMoney() {
		return money;
	}
	public void setMoney(Long money) {
		this.money = money;
	}
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	public String getCreateTimeStr() {
		return createTimeStr;
	}
	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}
	public String getRefId() {
		return refId;
	}
	public void setRefId(String refId) {
		this.refId = refId;
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