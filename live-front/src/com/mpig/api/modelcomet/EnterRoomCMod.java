package com.mpig.api.modelcomet;

import java.util.List;

public class EnterRoomCMod extends BaseCMod{
	public EnterRoomCMod(){
		this.setCometProtocol(CModProtocol.ENTER_ROOM);
	}
	private int manage; //=1是 ＝0不是
	private List<Integer> badges; // 该用户的时效礼物gid的集合
	private int type; //用户身份 1:普通 2:守护
	private Object guardInfo;  //守护相关信息
	private Object vipInfo;  //守护相关信息
	private int carId; //座驾id
	private int joinPlace; //加入的位置(=1普通用户列表 =2守护列表)
	private int sort;
	
	public List<Integer> getBadges() {
		return badges;
	}
	public void setBadges(List<Integer> badges) {
		this.badges = badges;
	}
	public int getManage() {
		return manage;
	}
	public void setManage(int manage) {
		this.manage = manage;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Object getGuardInfo() {
		return guardInfo;
	}
	public void setGuardInfo(Object guardInfo) {
		this.guardInfo = guardInfo;
	}
	public int getCarId() {
		return carId;
	}
	public void setCarId(int carId) {
		this.carId = carId;
	}
	public Object getVipInfo() {
		return vipInfo;
	}
	public void setVipInfo(Object vipInfo) {
		this.vipInfo = vipInfo;
	}
	public int getJoinPlace() {
		return joinPlace;
	}
	public void setJoinPlace(int joinPlace) {
		this.joinPlace = joinPlace;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	
	
}
