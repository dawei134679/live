package com.tinypig.admin.model;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.tinypig.admin.dao.UnionDao;
import com.tinypig.admin.util.StringUtil;

public class AnchorModel implements PopulateTemplate<AnchorModel> {
	private int uid;
	private String nickname;
	private String headimage;
	private int anchorLevel;
	private int userLevel;
	private String sex;
	private String opentime;
	private String verified;
	private String verified_reason;
	private int contrRq;
	private int recommend;
	private String recommendName;
	private String identity;
	private String familyName = "自由人";
	private int familyId;
	private String timeFormat = null; // opentime数据转换格式
	private int hms = 0;// 用于统计开播时长，存储开播时间戳即opentime为转换前的值
	
	private int grade;	//评级

	public String getNickname() {
		return nickname;
	}

	public String getHeadimage() {
		return headimage;
	}

	public int getAnchorLevel() {
		return anchorLevel;
	}

	public int getUserLevel() {
		return userLevel;
	}

	public String getSex() {
		return sex;
	}

	public String getOpentime() {
		return opentime;
	}

	public String getVerified() {
		return verified;
	}

	public String getVerified_reason() {
		return verified_reason;
	}

	public int getContrRq() {
		return contrRq;
	}

	public String getIdentity() {
		return identity;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setHeadimage(String headimage) {
		this.headimage = headimage;
	}

	public void setAnchorLevel(int anchorLevel) {
		this.anchorLevel = anchorLevel;
	}

	public void setUserLevel(int userLevel) {
		this.userLevel = userLevel;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setOpentime(String opentime) {
		this.opentime = opentime;
	}

	public void setVerified(String verified) {
		this.verified = verified;
	}

	public void setVerified_reason(String verified_reason) {
		this.verified_reason = verified_reason;
	}

	public void setContrRq(int contrRq) {
		this.contrRq = contrRq;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public int getFamilyId() {
		return familyId;
	}

	public void setFamilyId(int familyId) {
		this.familyId = familyId;
	}

	@Override
	public AnchorModel populateFromResultSet(ResultSet rs) {
		try {
			this.nickname = rs.getString("nickname");
			this.headimage = rs.getString("headimage");
			this.anchorLevel = rs.getInt("anchorLevel");
			this.userLevel = rs.getInt("userLevel");
			this.sex = rs.getBoolean("sex") ? "男" : "女";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			this.opentime = sdf.format(new Date(rs.getInt("opentime") * 1000));
			this.verified_reason = rs.getBoolean("verified") ? rs.getString("verified_reason") : "否";
			this.contrRq = rs.getInt("contrRq");
			this.familyId = rs.getInt("familyId");
			this.uid = rs.getInt("uid");
			this.recommend = rs.getInt("recommend");
			this.grade = rs.getInt("grade");
			if (this.recommend == 0) {
				this.recommendName = "普通";
			} else if (this.recommend == 1) {
				this.recommendName = "热门";
			} else if (this.recommend == 2) {
				this.recommendName = "推荐";
			} else if (this.recommend == 3) {
				this.recommendName = "推荐";
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public String toString() {
		String jsonString = JSON.toJSONString(this);
		return jsonString;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getRecommend() {
		return recommend;
	}

	public void setRecommend(int recommend) {
		this.recommend = recommend;
	}

	public String getRecommendName() {
		return recommendName;
	}

	public void setRecommendName(String recommendName) {
		this.recommendName = recommendName;
	}

	public AnchorModel populateFromUserBaseInfoModel(UserBaseInfoModel user) {
		if (user != null) {
			this.nickname = user.getNickname();
			this.headimage = user.getHeadimage();
			this.anchorLevel = user.getAnchorLevel();
			this.userLevel = user.getUserLevel();
			this.sex = user.getSex() ? "男" : "女";
			if (StringUtil.isEmpty(timeFormat)) {
				timeFormat = "yyyy-MM-dd hh:mm:ss";
			}
			SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
			this.opentime = user.getOpentime() == 0 ? "0" : sdf.format(new Date(user.getOpentime() * 1000L));
			this.hms = user.getLiveStatus()?user.getOpentime():0;
			this.verified_reason = user.isVerified() ? user.getVerified_reason() : "否";
			this.contrRq = user.getContrRq();
			this.familyId = user.getFamilyId();
			this.uid = user.getUid();
			this.recommend = user.getRecommend();
			if (this.recommend == 0) {
				this.recommendName = "普通";
			} else if (this.recommend == 1) {
				this.recommendName = "最新";
			} else if (this.recommend == 2) {
				this.recommendName = "热门";
			} else if (this.recommend == 3) {
				this.recommendName = "头牌";
			}
			this.grade = user.getGrade();
			if (familyId > 0) {
				UnionModel unionModel = UnionDao.getIns().getUnionById(familyId);
				if (unionModel != null) {
					this.familyName = unionModel.getUnionname();
				}
			}
		}
		return this;
	}

	public AnchorModel setTimeFormat(String format) {
		this.timeFormat = format;
		return this;
	}

	public String getHms() {

		if (hms <= 0) {
			return "0";
		}
		long curTime = System.currentTimeMillis() / 1000;
		int seconds = (int) (curTime - hms);

		int hour = seconds / 3600;
		int minute = seconds % 3600 / 60;
		int second = seconds % 3600 % 60;
		String strHour = "" + hour;
		String strMin = "" + minute;
		String strSec = "" + second;
		if (hour < 10) {
			strHour = "0" + hour;
		}
		if (minute < 10) {
			strMin = "0" + minute;
		}
		if (second < 10) {
			strSec = "0" + second;
		}
		return strHour + ":" + strMin + ":" + strSec;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

}
