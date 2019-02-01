package com.tinypig.newadmin.web.entity;

import java.io.Serializable;

public class GameDoll implements Serializable{
    
		private static final long serialVersionUID = -2391409214414578626L;

		private Integer id;

	    private String name;

	    private String imageUrl;

	    private Double probability;

	    private Double multiple;

	    private Integer sort;

	    private Integer status;

	    private Long createTime;

	    private Long createUserId;

	    private Long updateTime;

	    private Long updateUserId;

	    public Integer getId() {
	        return id;
	    }

	    public void setId(Integer id) {
	        this.id = id;
	    }

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name == null ? null : name.trim();
	    }

	    public String getImageUrl() {
	        return imageUrl;
	    }

	    public void setImageUrl(String imageUrl) {
	        this.imageUrl = imageUrl == null ? null : imageUrl.trim();
	    }

	    public Double getProbability() {
	        return probability;
	    }

	    public void setProbability(Double probability) {
	        this.probability = probability;
	    }

	    public Double getMultiple() {
	        return multiple;
	    }

	    public void setMultiple(Double multiple) {
	        this.multiple = multiple;
	    }

	    public Integer getSort() {
	        return sort;
	    }

	    public void setSort(Integer sort) {
	        this.sort = sort;
	    }

	    public Integer getStatus() {
	        return status;
	    }

	    public void setStatus(Integer status) {
	        this.status = status;
	    }

	    public Long getCreateTime() {
	        return createTime;
	    }

	    public void setCreateTime(Long createTime) {
	        this.createTime = createTime;
	    }

	    public Long getCreateUserId() {
	        return createUserId;
	    }

	    public void setCreateUserId(Long createUserId) {
	        this.createUserId = createUserId;
	    }

	    public Long getUpdateTime() {
	        return updateTime;
	    }

	    public void setUpdateTime(Long updateTime) {
	        this.updateTime = updateTime;
	    }

	    public Long getUpdateUserId() {
	        return updateUserId;
	    }

	    public void setUpdateUserId(Long updateUserId) {
	        this.updateUserId = updateUserId;
	    }
}