package com.tinypig.newadmin.web.entity;

import java.io.Serializable;

public class ExtensionCenterParamDto implements Serializable {
	private static final long serialVersionUID = -2830366545956416360L;

	private Long id;
	private String name;
	private String contactsPhone;// 查询条件
	private Long strategicPartnerId;
	private String strategicPartnerName;
	private Long stime;
	private Long etime;
	private Integer startIndex;
	private Integer pageSize;
	private String orderBy;
	private String orderSort;

	
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

	public String getContactsPhone() {
		return contactsPhone;
	}

	public void setContactsPhone(String contactsPhone) {
		this.contactsPhone = contactsPhone;
	}

	public Long getStrategicPartnerId() {
		return strategicPartnerId;
	}

	public void setStrategicPartnerId(Long strategicPartnerId) {
		this.strategicPartnerId = strategicPartnerId;
	}

	public String getStrategicPartnerName() {
		return strategicPartnerName;
	}

	public void setStrategicPartnerName(String strategicPartnerName) {
		this.strategicPartnerName = strategicPartnerName;
	}

	public Long getStime() {
		return stime;
	}

	public void setStime(Long stime) {
		this.stime = stime;
	}

	public Long getEtime() {
		return etime;
	}

	public void setEtime(Long etime) {
		this.etime = etime;
	}

	public Integer getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getOrderSort() {
		return orderSort;
	}

	public void setOrderSort(String orderSort) {
		this.orderSort = orderSort;
	}

}