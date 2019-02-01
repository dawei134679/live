package com.mpig.api.model;

/**
 * 主播列表首页上使用 自定义
 * @author fang
 *
 */
public class LiveListModel implements Comparable<LiveListModel> {
	
	private Integer uid;		//用户UID
	private String nickName;	//用户昵称
	private String headimage;	//用户小图标
	private String slogan;		//视频slogan
	private String city;		//城市 
	private Long count;			//观看人数
	private String liveimg;		//用户大图标
	private Byte status;		//开播状态
	private Integer levels;		//主播等级
    private Integer sort;
	
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getHeadimage() {
		return headimage;
	}
	public void setHeadimage(String headimage) {
		this.headimage = headimage;
	}
	public String getSlogan() {
		return slogan;
	}
	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public String getLiveimg() {
		return liveimg;
	}
	public void setLiveimg(String liveimg) {
		this.liveimg = liveimg;
	}
	public Integer getLevels() {
		return levels;
	}
	public void setLevels(Integer levels) {
		this.levels = levels;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	@Override
	public int compareTo(LiveListModel arg0) {
		
		int isort = this.sort.compareTo(arg0.getSort());
		if (isort == 0) {
			isort = this.count.compareTo(arg0.getCount());
		}
		return isort;
	}
}
