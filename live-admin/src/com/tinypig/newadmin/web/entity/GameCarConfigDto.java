package com.tinypig.newadmin.web.entity;

import java.io.Serializable;

public class GameCarConfigDto implements Serializable {

	private static final long serialVersionUID = 6000446782797747657L;

	private Long id;

	private String name;

	private String img;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

}