package com.tinypig.newadmin.web.entity;

import java.io.Serializable;

public class RobotBaseInfo implements Serializable {

	private static final long serialVersionUID = 2999847634395122211L;

	private Long uid;

    private String nickname;

    private Integer familyid;

    private Integer anchorlevel;

    private Integer userlevel;

    private Integer sex;

    private Integer identity;

    private String headimage;

    private String livimage;

    private String birthday;

    private String avatarcolor;

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

    private Boolean livestatus;

    private Integer opentime;

    private Integer recommend;

    private Integer videoline;

    private Boolean verified;

    private String verifiedReason;

    private Integer contrrq;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
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
        this.headimage = headimage == null ? null : headimage.trim();
    }

    public String getLivimage() {
        return livimage;
    }

    public void setLivimage(String livimage) {
        this.livimage = livimage == null ? null : livimage.trim();
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday == null ? null : birthday.trim();
    }

    public String getAvatarcolor() {
        return avatarcolor;
    }

    public void setAvatarcolor(String avatarcolor) {
        this.avatarcolor = avatarcolor == null ? null : avatarcolor.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature == null ? null : signature.trim();
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
        this.registchannel = registchannel == null ? null : registchannel.trim();
    }

    public String getSubregistchannel() {
        return subregistchannel;
    }

    public void setSubregistchannel(String subregistchannel) {
        this.subregistchannel = subregistchannel == null ? null : subregistchannel.trim();
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
        this.registimei = registimei == null ? null : registimei.trim();
    }

    public Boolean getLivestatus() {
        return livestatus;
    }

    public void setLivestatus(Boolean livestatus) {
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

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public String getVerifiedReason() {
        return verifiedReason;
    }

    public void setVerifiedReason(String verifiedReason) {
        this.verifiedReason = verifiedReason == null ? null : verifiedReason.trim();
    }

    public Integer getContrrq() {
        return contrrq;
    }

    public void setContrrq(Integer contrrq) {
        this.contrrq = contrrq;
    }

	@Override
	public String toString() {
		return "RobotBaseInfo [uid=" + uid + ", nickname=" + nickname + ", familyid=" + familyid + ", anchorlevel="
				+ anchorlevel + ", userlevel=" + userlevel + ", sex=" + sex + ", identity=" + identity + ", headimage="
				+ headimage + ", livimage=" + livimage + ", birthday=" + birthday + ", avatarcolor=" + avatarcolor
				+ ", phone=" + phone + ", province=" + province + ", city=" + city + ", signature=" + signature
				+ ", registip=" + registip + ", registtime=" + registtime + ", registchannel=" + registchannel
				+ ", subregistchannel=" + subregistchannel + ", registos=" + registos + ", registimei=" + registimei
				+ ", livestatus=" + livestatus + ", opentime=" + opentime + ", recommend=" + recommend + ", videoline="
				+ videoline + ", verified=" + verified + ", verifiedReason=" + verifiedReason + ", contrrq=" + contrrq
				+ "]";
	}
    
}