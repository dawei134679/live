package com.mpig.api.dictionary;

import java.util.HashMap;

/**
 * 版本信息配置
 * @ClassName:     VersionConfig.java
 * @Description:   TODO 
 * 
 * @author         jackzhang
 * @version        V1.0  
 * @Date           May 25, 2016 10:23:14 AM
 */
public class VersionConfig {
	private String lastVersion;
	private String updateDesc;
	private String uploadUrl;
	private boolean force;
	private int updateTime;


    public VersionConfig initWith(String lastVersion, String updateDesc,Boolean force,String uploadUrl,int updateTime) {
        this.setLastVersion(lastVersion);
        this.setForce(force);
        this.setUpdateDesc(updateDesc);
        this.setUploadUrl(uploadUrl);
        this.setUpdateTime(updateTime);
        return this;
    }

    public int getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(int updateTime) {
		this.updateTime = updateTime;
	}

	@SuppressWarnings("serial")
	public HashMap<String, Object> toHashMap(){
    	return new HashMap<String,Object>(){
    		{
    			put("ver", lastVersion);
    			put("desc", updateDesc);
    			put("force", force);
    			put("uploadUrl", uploadUrl);
    			put("updateTime", updateTime);
    		}
    	};
    }


	public String getLastVersion() {
		return lastVersion;
	}


	public void setLastVersion(String lastVersion) {
		this.lastVersion = lastVersion;
	}


	public String getUpdateDesc() {
		return updateDesc;
	}


	public void setUpdateDesc(String updateDesc) {
		this.updateDesc = updateDesc;
	}


	public boolean isForce() {
		return force;
	}


	public void setForce(boolean force) {
		this.force = force;
	}

	public String getUploadUrl() {
		return uploadUrl;
	}

	public void setUploadUrl(String uploadUrl) {
		this.uploadUrl = uploadUrl;
	}
}
