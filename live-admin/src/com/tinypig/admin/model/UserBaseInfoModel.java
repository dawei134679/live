package com.tinypig.admin.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserBaseInfoModel implements PopulateTemplate<UserBaseInfoModel> {
    private Integer uid;
    private String nickname;
    private Integer familyId;
    private Boolean sex;
    private Integer anchorLevel;
    private Integer userLevel;
    private Integer identity;
    private String headimage;
    private String livimage;
    private String pcimg1;
    private String pcimg2;
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
    private Long exp;
    private Integer grade = 0; //主播评级
    
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
        this.nickname = nickname == null ? null : nickname.trim();
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
        this.headimage = headimage == null ? null : headimage.trim();
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
        this.birthday = birthday == null ? null : birthday.trim();
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
        this.registchannel = registchannel == null ? null : registchannel
                .trim();
    }

    public String getSubregistchannel() {
        return subregistchannel;
    }

    public void setSubregistchannel(String subregistchannel) {
        this.subregistchannel = subregistchannel == null ? null
                : subregistchannel.trim();
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

    public Boolean getLiveStatus() {
        return liveStatus;
    }

    public void setLiveStatus(Boolean liveStatus) {
        this.liveStatus = liveStatus;
    }
    
    public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	@Override
    public UserBaseInfoModel populateFromResultSet(ResultSet rs) {

        try {
            uid = rs.getInt("uid");
            nickname = rs.getString("nickname");
            sex = rs.getBoolean("sex");
            userLevel = rs.getInt("userLevel");
            anchorLevel = rs.getInt("anchorLevel");
            identity = rs.getInt("identity");
            headimage = rs.getString("headimage");
            birthday = rs.getString("birthday");
            phone = rs.getString("phone");
            province = rs.getString("province");
            city = rs.getString("city");
            signature = rs.getString("signature");
            registip = rs.getLong("registip");
            registtime = rs.getInt("registtime");
            registchannel = rs.getString("registchannel");
            subregistchannel = rs.getString("subregistchannel");
            registos = rs.getByte("registos");
            registimei = rs.getString("registimei");
            this.familyId = rs.getInt("familyId");
            this.liveStatus = rs.getBoolean("liveStatus");
            this.livimage = rs.getString("livimage");
            this.opentime = rs.getInt("opentime");
            this.recommend = rs.getInt("recommend");
            this.videoline = rs.getInt("videoline");
            verified = rs.getBoolean("verified");
            verified_reason = rs.getString("verified_reason");
            this.contrRq = rs.getInt("contrRq");
            this.pcimg1 = rs.getString("pcimg1");
            this.pcimg2 = rs.getString("pcimg2");
            exp = rs.getLong("exp");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
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

	public Long getExp() {
		return exp;
	}

	public void setExp(Long exp) {
		this.exp = exp;
	}
}