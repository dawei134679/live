package com.mpig.api.model;

import java.sql.ResultSet;
/**
 * 增值服务相关特权
 * @author zyl
 *
 */
public class ValueaddPrivilegeModel implements PopulateTemplate<ValueaddPrivilegeModel> {
	private Integer id;
	private Integer gid; //守护/vip ID
	private Integer level; //守护/vip的等级
	private Integer permissType; //权限类别 : 1 守护 2VIP  仅在权限类别相同时才可操作
	private Integer permissLevel; //权限级别 : 等级高的可以操作等级低的
	private Integer kickUser; //是否能踢人 0/1
	private Integer gagUser; //是否能禁言 0/1
	private Integer addRankScore; //增加排名分数
	private Integer carId; //座驾id
	private Integer rqCount; //每日赠送人气礼物的数量
	private Double levelSpeedup; //等级加速
	private Integer cushionSecs; //保护期 单位秒
	private Integer firstLoginExp; //首次登陆成长值奖励
	private Integer firstSpendExp; //首次消费的成长值奖励
	private Integer cushionCarid; //保护期的座驾id
	private Integer cushionIcon; //保护期的徽章
	private Double renewalDiscount; //续费折扣 
	private Integer iconId; //图标id
	private Integer tid; //开通特效id
	private String joinEffects; //进场特效
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getGid() {
		return gid;
	}

	public void setGid(Integer gid) {
		this.gid = gid;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getPermissType() {
		return permissType;
	}

	public void setPermissType(Integer permissType) {
		this.permissType = permissType;
	}

	public Integer getPermissLevel() {
		return permissLevel;
	}

	public void setPermissLevel(Integer permissLevel) {
		this.permissLevel = permissLevel;
	}

	public Integer getKickUser() {
		return kickUser;
	}

	public void setKickUser(Integer kickUser) {
		this.kickUser = kickUser;
	}

	public Integer getGagUser() {
		return gagUser;
	}

	public void setGagUser(Integer gagUser) {
		this.gagUser = gagUser;
	}

	public Integer getAddRankScore() {
		return addRankScore;
	}

	public void setAddRankScore(Integer addRankScore) {
		this.addRankScore = addRankScore;
	}

	public Integer getCarId() {
		return carId;
	}

	public void setCarId(Integer carId) {
		this.carId = carId;
	}

	public Integer getRqCount() {
		return rqCount;
	}

	public void setRqCount(Integer rqCount) {
		this.rqCount = rqCount;
	}

	public Double getLevelSpeedup() {
		return levelSpeedup;
	}

	public void setLevelSpeedup(Double levelSpeedup) {
		this.levelSpeedup = levelSpeedup;
	}

	public Integer getCushionSecs() {
		return cushionSecs;
	}

	public void setCushionSecs(Integer cushionSecs) {
		this.cushionSecs = cushionSecs;
	}

	public Integer getFirstLoginExp() {
		return firstLoginExp;
	}

	public void setFirstLoginExp(Integer firstLoginExp) {
		this.firstLoginExp = firstLoginExp;
	}

	public Integer getFirstSpendExp() {
		return firstSpendExp;
	}

	public void setFirstSpendExp(Integer firstSpendExp) {
		this.firstSpendExp = firstSpendExp;
	}

	public Integer getCushionCarid() {
		return cushionCarid;
	}

	public void setCushionCarid(Integer cushionCarid) {
		this.cushionCarid = cushionCarid;
	}

	public Double getRenewalDiscount() {
		return renewalDiscount;
	}

	public void setRenewalDiscount(Double renewalDiscount) {
		this.renewalDiscount = renewalDiscount;
	}
	
	public Integer getIconId() {
		return iconId;
	}

	public void setIconId(Integer iconId) {
		this.iconId = iconId;
	}

	public String getJoinEffects() {
		return joinEffects;
	}

	public void setJoinEffects(String joinEffects) {
		this.joinEffects = joinEffects;
	}

	public Integer getTid() {
		return tid;
	}

	public void setTid(Integer tid) {
		this.tid = tid;
	}

	public Integer getCushionIcon() {
		return cushionIcon;
	}

	public void setCushionIcon(Integer cushionIcon) {
		this.cushionIcon = cushionIcon;
	}

	@Override
	public ValueaddPrivilegeModel populateFromResultSet(ResultSet rs) {
		try {
			this.id = rs.getInt("id");
			this.gid = rs.getInt("gid");
			this.level = rs.getInt("level");
			this.permissType = rs.getInt("permiss_type");
			this.permissLevel = rs.getInt("permiss_level");
			this.kickUser = rs.getInt("kick_user");
			this.gagUser = rs.getInt("gag_user");
			this.addRankScore = rs.getInt("add_rank_score");
			this.carId = rs.getInt("car_id");
			this.rqCount = rs.getInt("rq_count");
			this.levelSpeedup = rs.getDouble("level_speedup");
			this.cushionSecs = rs.getInt("cushion_secs");
			this.cushionCarid = rs.getInt("cushion_carid");
			this.cushionIcon = rs.getInt("cushion_icon");
			this.firstLoginExp = rs.getInt("first_login_exp");
			this.firstSpendExp = rs.getInt("first_spend_exp");
			this.renewalDiscount = rs.getDouble("renewal_discount");
			this.iconId = rs.getInt("icon_id");
			this.tid = rs.getInt("tid");
			this.joinEffects = rs.getString("join_effects");
		} catch (Exception e) {
			
		}
		return this;
	}
	
	
	
}
