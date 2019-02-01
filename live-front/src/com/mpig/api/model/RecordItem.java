package com.mpig.api.model;

//tosy 用户视频录制item
public class RecordItem {
	
	String mp4Path = "";
	String m3u8Path = "";
	String coverPic = "";
	long lduration = 0;		//in ms
	String time = "";
	String title = "";
	boolean isShow = true;
	
	public RecordItem(){
		
	}
	
	public RecordItem(String time,String title,String mp4,String m3u8,long duration,String strCoverPic){
		this.setTime(time);
		this.setTitle(title);
		this.setMp4Path(mp4);
		this.setM3u8Path(m3u8);
		this.setLduration(duration);
		this.setCoverPic(strCoverPic);
	}
	
	public String getMp4Path() {
		return mp4Path;
	}
	public void setMp4Path(String mp4Path) {
		this.mp4Path = mp4Path;
	}
	public String getM3u8Path() {
		return m3u8Path;
	}
	public void setM3u8Path(String m3u8Path) {
		this.m3u8Path = m3u8Path;
	}
	public String getCoverPic() {
		return coverPic;
	}
	public void setCoverPic(String coverPic) {
		this.coverPic = coverPic;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public long getLduration() {
		return lduration;
	}
	public void setLduration(long lduration) {
		this.lduration = lduration;
	}
	public boolean isShow() {
		return isShow;
	}
	public void setShow(boolean isShow) {
		this.isShow = isShow;
	}
	
}
