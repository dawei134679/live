package com.tinypig.newadmin.web.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class MemberInfo implements Serializable {
	private static final long serialVersionUID = 6633730718904094493L;

	private Integer uid;

	private String nickname;

	private Integer familyid;

	private Integer anchorlevel;

	private Integer userlevel;

	private Integer sex;

	private Integer identity;

	private String headimage;

	private String livimage;

	private String pcimg1;

	private String pcimg2;

	private String birthday;

	private Long exp;

	private String phone;

	private String province;

	private String city;

	private String signature;

	private Long registip;

	private Integer registtime;

	private String registchannel;

	private String subregistchannel;

	private Integer registos;

	private String registimei;

	private Integer livestatus;

	private Integer opentime;

	private Integer recommend;

	private Integer videoline;

	private Integer verified;

	private String verifiedReason;

	private Integer contrrq;

	private String constellation;

	private String hobby;

	private Integer grade;

	private Integer gamestatus;

	private Long gameid;

	private Integer accountid;

	private String accountname;

	private String password;

	private String authkey;

	private String unionid;

	private Integer status;

	private Integer money;

	private Long wealth;

	private Integer credit;

	private Integer credittotal;

	private Integer frozencredit;

	private Long salesmanId;

	private String salesmanName;

	private String salesmanContactsName;

	private String salesmanContactsPhone;

	private Long agentUserId;

	private String agentUserName;

	private String agentUserContactsName;

	private String agentUserContactsPhone;

	private Long promotersId;

	private String promotersName;

	private String promotersContactsName;

	private String promotersContactsPhone;

	private Long extensionCenterId;

	private String extensionCenterName;

	private String extensionCenterContactsName;

	private String extensionCenterContactsPhone;

	private Long strategicPartnerId;

	private String strategicPartnerName;

	private String strategicPartnerContactsName;

	private String strategicPartnerContactsPhone;

	private BigDecimal moneyRmb;
	
	private String statusName;
	
	private String registtimeStr;
	
	private String supportUserFlag;

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

	public Integer getFamilyid() {
		return familyid;
	}

	public void setFamilyid(Integer familyid) {
		this.familyid = familyid;
	}

	public Integer getAnchorlevel() {
		return anchorlevel;
	}

	public void setAnchorlevel(Integer anchorlevel) {
		this.anchorlevel = anchorlevel;
	}

	public Integer getUserlevel() {
		return userlevel;
	}

	public void setUserlevel(Integer userlevel) {
		this.userlevel = userlevel;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
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

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public Long getExp() {
		return exp;
	}

	public void setExp(Long exp) {
		this.exp = exp;
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

	public Integer getRegistos() {
		return registos;
	}

	public void setRegistos(Integer registos) {
		this.registos = registos;
	}

	public String getRegistimei() {
		return registimei;
	}

	public void setRegistimei(String registimei) {
		this.registimei = registimei;
	}

	public Integer getLivestatus() {
		return livestatus;
	}

	public void setLivestatus(Integer livestatus) {
		this.livestatus = livestatus;
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

	public Integer getVerified() {
		return verified;
	}

	public void setVerified(Integer verified) {
		this.verified = verified;
	}

	public String getVerifiedReason() {
		return verifiedReason;
	}

	public void setVerifiedReason(String verifiedReason) {
		this.verifiedReason = verifiedReason;
	}

	public Integer getContrrq() {
		return contrrq;
	}

	public void setContrrq(Integer contrrq) {
		this.contrrq = contrrq;
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

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	public Integer getGamestatus() {
		return gamestatus;
	}

	public void setGamestatus(Integer gamestatus) {
		this.gamestatus = gamestatus;
	}

	public Long getGameid() {
		return gameid;
	}

	public void setGameid(Long gameid) {
		this.gameid = gameid;
	}

	public Integer getAccountid() {
		return accountid;
	}

	public void setAccountid(Integer accountid) {
		this.accountid = accountid;
	}

	public String getAccountname() {
		return accountname;
	}

	public void setAccountname(String accountname) {
		this.accountname = accountname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAuthkey() {
		return authkey;
	}

	public void setAuthkey(String authkey) {
		this.authkey = authkey;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getMoney() {
		return money;
	}

	public void setMoney(Integer money) {
		this.money = money;
	}

	public Long getWealth() {
		return wealth;
	}

	public void setWealth(Long wealth) {
		this.wealth = wealth;
	}

	public Integer getCredit() {
		return credit;
	}

	public void setCredit(Integer credit) {
		this.credit = credit;
	}

	public Integer getCredittotal() {
		return credittotal;
	}

	public void setCredittotal(Integer credittotal) {
		this.credittotal = credittotal;
	}

	public Integer getFrozencredit() {
		return frozencredit;
	}

	public void setFrozencredit(Integer frozencredit) {
		this.frozencredit = frozencredit;
	}

	public Long getSalesmanId() {
		return salesmanId;
	}

	public void setSalesmanId(Long salesmanId) {
		this.salesmanId = salesmanId;
	}

	public String getSalesmanName() {
		return salesmanName;
	}

	public void setSalesmanName(String salesmanName) {
		this.salesmanName = salesmanName;
	}

	public String getSalesmanContactsName() {
		return salesmanContactsName;
	}

	public void setSalesmanContactsName(String salesmanContactsName) {
		this.salesmanContactsName = salesmanContactsName;
	}

	public String getSalesmanContactsPhone() {
		return salesmanContactsPhone;
	}

	public void setSalesmanContactsPhone(String salesmanContactsPhone) {
		this.salesmanContactsPhone = salesmanContactsPhone;
	}

	public Long getAgentUserId() {
		return agentUserId;
	}

	public void setAgentUserId(Long agentUserId) {
		this.agentUserId = agentUserId;
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

	public Long getPromotersId() {
		return promotersId;
	}

	public void setPromotersId(Long promotersId) {
		this.promotersId = promotersId;
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

	public Long getExtensionCenterId() {
		return extensionCenterId;
	}

	public void setExtensionCenterId(Long extensionCenterId) {
		this.extensionCenterId = extensionCenterId;
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

	public Long getStrategicPartnerId() {
		return strategicPartnerId;
	}

	public void setStrategicPartnerId(Long strategicPartnerId) {
		this.strategicPartnerId = strategicPartnerId;
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

	public BigDecimal getMoneyRmb() {
		return moneyRmb;
	}

	public void setMoneyRmb(BigDecimal moneyRmb) {
		this.moneyRmb = moneyRmb;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getRegisttimeStr() {
		return registtimeStr;
	}

	public void setRegisttimeStr(String registtimeStr) {
		this.registtimeStr = registtimeStr;
	}

	public String getSupportUserFlag() {
		return supportUserFlag;
	}

	public void setSupportUserFlag(String supportUserFlag) {
		this.supportUserFlag = supportUserFlag;
	}
}