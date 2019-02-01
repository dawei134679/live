package com.tinypig.newadmin.web.entity;

import java.io.Serializable;

public class UserTransactionHisStaParam implements Serializable {

		private static final long serialVersionUID = -187964205069152228L;

		private Integer startIndex;

		private Integer pageSize;
		
		private String order;

		private String sort;
		
		private Integer transType;
		
		private Integer uid;
		
		private Integer dataType;
		
		private String startTime;

		private String endTime;

		private Long strategicPartnerId;
		
		private Long agentUserId;

		private Long extensionCenterId;

		private Long salesmanId;
		
		private Long promotersId;

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

		public String getOrder() {
			return order;
		}

		public void setOrder(String order) {
			this.order = order;
		}

		public String getSort() {
			return sort;
		}

		public void setSort(String sort) {
			this.sort = sort;
		}

		public Integer getTransType() {
			return transType;
		}

		public void setTransType(Integer transType) {
			this.transType = transType;
		}

		public Integer getUid() {
			return uid;
		}

		public void setUid(Integer uid) {
			this.uid = uid;
		}

		public Integer getDataType() {
			return dataType;
		}

		public void setDataType(Integer dataType) {
			this.dataType = dataType;
		}

		public String getStartTime() {
			return startTime;
		}

		public void setStartTime(String startTime) {
			this.startTime = startTime;
		}

		public String getEndTime() {
			return endTime;
		}

		public void setEndTime(String endTime) {
			this.endTime = endTime;
		}

		public Long getStrategicPartnerId() {
			return strategicPartnerId;
		}

		public void setStrategicPartnerId(Long strategicPartnerId) {
			this.strategicPartnerId = strategicPartnerId;
		}

		public Long getAgentUserId() {
			return agentUserId;
		}

		public void setAgentUserId(Long agentUserId) {
			this.agentUserId = agentUserId;
		}

		public Long getExtensionCenterId() {
			return extensionCenterId;
		}

		public void setExtensionCenterId(Long extensionCenterId) {
			this.extensionCenterId = extensionCenterId;
		}

		public Long getSalesmanId() {
			return salesmanId;
		}

		public void setSalesmanId(Long salesmanId) {
			this.salesmanId = salesmanId;
		}

		public Long getPromotersId() {
			return promotersId;
		}

		public void setPromotersId(Long promotersId) {
			this.promotersId = promotersId;
		}
	}
