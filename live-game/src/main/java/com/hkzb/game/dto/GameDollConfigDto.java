package com.hkzb.game.dto;

import java.io.Serializable;

public class GameDollConfigDto implements Serializable{
	private static final long serialVersionUID = -3986595295362446959L;

	private Integer id;

    private String name;

    private String imageUrl;

    private Double probability;

    private Double multiple;

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
		this.name = name;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
