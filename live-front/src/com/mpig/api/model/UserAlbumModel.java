package com.mpig.api.model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author zyl
 * @date 2016-10-12 下午4:10:39
 */
public class UserAlbumModel implements PopulateTemplate<UserAlbumModel>{
	
	private Integer id;
	private Integer uid;
	private String fileName;
	private String photoUrl;
	private String photoThumbUrl;
	private Integer createAt;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public String getPhotoUrl() {
		return photoUrl;
	}
	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}
	public String getPhotoThumbUrl() {
		return photoThumbUrl;
	}
	public void setPhotoThumbUrl(String photoThumbUrl) {
		this.photoThumbUrl = photoThumbUrl;
	}
	public Integer getCreateAt() {
		return createAt;
	}
	public void setCreateAt(Integer createAt) {
		this.createAt = createAt;
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	@Override
	public UserAlbumModel populateFromResultSet(final ResultSet rs) {
		try {
			id = rs.getInt("id");
			uid = rs.getInt("uid");
			fileName = rs.getString("fileName");
			photoUrl = rs.getString("photoUrl");
			photoThumbUrl = rs.getString("photoThumbUrl");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this;
	}
}
