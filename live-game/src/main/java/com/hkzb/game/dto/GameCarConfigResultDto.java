package com.hkzb.game.dto;

import java.io.Serializable;

public class GameCarConfigResultDto implements Serializable {

	private static final long serialVersionUID = -8834684224842425752L;

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