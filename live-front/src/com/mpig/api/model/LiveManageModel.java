package com.mpig.api.model;

import java.sql.ResultSet;

public class LiveManageModel implements PopulateTemplate<LiveManageModel> {
	private int anchorUid;
	private int userUid;
	private boolean isvalid;
	private int creatAt;
	private int modifyAt;
	
	public int getAnchorUid() {
		return anchorUid;
	}
	public int getUserUid() {
		return userUid;
	}
	public boolean isIsvalid() {
		return isvalid;
	}
	public int getCreatAt() {
		return creatAt;
	}
	public int getModifyAt() {
		return modifyAt;
	}
	public void setAnchorUid(int anchorUid) {
		this.anchorUid = anchorUid;
	}
	public void setUserUid(int userUid) {
		this.userUid = userUid;
	}
	public void setIsvalid(boolean isvalid) {
		this.isvalid = isvalid;
	}
	public void setCreatAt(int creatAt) {
		this.creatAt = creatAt;
	}
	public void setModifyAt(int modifyAt) {
		this.modifyAt = modifyAt;
	}
	
	@Override
	public LiveManageModel populateFromResultSet(ResultSet rs) {
		
		try{
			this.anchorUid = rs.getInt("anchorUid");
			this.userUid = rs.getInt("userUid");
			this.isvalid = rs.getBoolean("isvalid");
			this.creatAt = rs.getInt("creatAt");
			this.modifyAt = rs.getInt("modifyAt");
		}catch(Exception ex){
			
		}
		return this;
	}
}
