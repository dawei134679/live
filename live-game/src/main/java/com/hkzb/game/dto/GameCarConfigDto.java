package com.hkzb.game.dto;

import java.io.Serializable;

public class GameCarConfigDto implements Serializable {

	private static final long serialVersionUID = 6000446782797747657L;

	private Long id;

	private String name;

	private String img;

	private Double probability1;

	private Double multiple1;

	private Double probability2;

	private Double multiple2;

	private Double probability3;
	
	private Double multiple3;

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

	public Double getProbability1() {
		return probability1;
	}

	public void setProbability1(Double probability1) {
		this.probability1 = probability1;
	}

	public Double getMultiple1() {
		return multiple1;
	}

	public void setMultiple1(Double multiple1) {
		this.multiple1 = multiple1;
	}

	public Double getProbability2() {
		return probability2;
	}

	public void setProbability2(Double probability2) {
		this.probability2 = probability2;
	}

	public Double getMultiple2() {
		return multiple2;
	}

	public void setMultiple2(Double multiple2) {
		this.multiple2 = multiple2;
	}

	public Double getProbability3() {
		return probability3;
	}

	public void setProbability3(Double probability3) {
		this.probability3 = probability3;
	}

	public Double getMultiple3() {
		return multiple3;
	}

	public void setMultiple3(Double multiple3) {
		this.multiple3 = multiple3;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}