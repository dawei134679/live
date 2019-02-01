package com.tinypig.admin.job;

public class SmartObject {
	private String color;
	private int size;
	private String txt;
	
	public SmartObject(String color,int size,String txt){
		this.color = color;
		this.size = size;
		this.txt = txt;
	}
	
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public String getTxt() {
		return txt;
	}
	public void setTxt(String txt) {
		this.txt = txt;
	}
}
