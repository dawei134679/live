package com.tinypig.admin.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserBaseInfo implements PopulateTemplate<UserBaseInfo> {
	 private Integer uid;
	    private String nickname;
	    private Integer familyId;
	    private Boolean sex;
	    private Integer anchorLevel;
	    private Integer userLevel;
	    private Integer identity;
	    private String headimage;
	    private String livimage;
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
	    private long credit;//持有声援
	    private long creditTotal;//累计声援
	    private long totalLiveTime;//直播总时长
	    private int validLiveDay;//有效直播天数
	    private int unionid;//公会id
	    private String unionname;//公会名
	    private String username;//所属人名字
	    private long totalmoney;//主播房间总消耗
	    private int status;//用户状态
	    private String remarks;//用户备注
	    
	    
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

	    @Override
	    public UserBaseInfo populateFromResultSet(ResultSet rs) {

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
	        } catch (SQLException e) {
	            // TODO Auto-generated catch block
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

		public long getTotalLiveTime() {
			return totalLiveTime;
		}

		public void setTotalLiveTime(long totalLiveTime) {
			this.totalLiveTime = totalLiveTime;
		}

		public int getValidLiveDay() {
			return validLiveDay;
		}

		public void setValidLiveDay(int validLiveDay) {
			this.validLiveDay = validLiveDay;
		}

		public long getCredit() {
			return credit;
		}

		public void setCredit(long credit) {
			this.credit = credit;
		}

		public long getCreditTotal() {
			return creditTotal;
		}

		public void setCreditTotal(long creditTotal) {
			this.creditTotal = creditTotal;
		}

		public String getUnionname() {
			return unionname;
		}

		public void setUnionname(String unionname) {
			this.unionname = unionname;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public long getTotalmoney() {
			return totalmoney;
		}

		public void setTotalmoney(long totalmoney) {
			this.totalmoney = totalmoney;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public String getRemarks() {
			return remarks;
		}

		public void setRemarks(String remarks) {
			this.remarks = remarks;
		}

		public int getUnionid() {
			return unionid;
		}

		public void setUnionid(int unionid) {
			this.unionid = unionid;
		}
		
		
}
