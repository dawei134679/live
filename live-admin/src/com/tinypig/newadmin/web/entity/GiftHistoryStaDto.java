package com.tinypig.newadmin.web.entity;

import java.io.Serializable;
import java.util.List;

public class GiftHistoryStaDto implements Serializable {

	private static final long serialVersionUID = 6161751045446484493L;

	private Integer page;
	private Integer rows;
	private Integer startIndex;
	private Integer pageSize;
	private Long srcuid;
	private String srcnickname;
	private Long dstuid;
	private String dstnickname;
	private String startTime;
	private String endTime;
	private Long gid;
	private String gname;
	private Integer gflag;
	private Integer supportUserFlag;
	private List<String> tableNames;

	private Long agentUserId;
	private Long promotersId;
	private Long salesmanId;
	private Long extensionCenterId;
	private Long strategicPartnerId;

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
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

	public Long getSrcuid() {
		return srcuid;
	}

	public void setSrcuid(Long srcuid) {
		this.srcuid = srcuid;
	}

	public String getSrcnickname() {
		return srcnickname;
	}

	public void setSrcnickname(String srcnickname) {
		this.srcnickname = srcnickname;
	}

	public Long getDstuid() {
		return dstuid;
	}

	public void setDstuid(Long dstuid) {
		this.dstuid = dstuid;
	}

	public String getDstnickname() {
		return dstnickname;
	}

	public void setDstnickname(String dstnickname) {
		this.dstnickname = dstnickname;
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

	public Long getGid() {
		return gid;
	}

	public void setGid(Long gid) {
		this.gid = gid;
	}

	public String getGname() {
		return gname;
	}

	public void setGname(String gname) {
		this.gname = gname;
	}

	public Integer getGflag() {
		return gflag;
	}

	public void setGflag(Integer gflag) {
		this.gflag = gflag;
	}

	public Integer getSupportUserFlag() {
		return supportUserFlag;
	}

	public void setSupportUserFlag(Integer supportUserFlag) {
		this.supportUserFlag = supportUserFlag;
	}

	public List<String> getTableNames() {
		return tableNames;
	}

	public void setTableNames(List<String> tableNames) {
		this.tableNames = tableNames;
	}

	public Long getAgentUserId() {
		return agentUserId;
	}

	public void setAgentUserId(Long agentUserId) {
		this.agentUserId = agentUserId;
	}

	public Long getPromotersId() {
		return promotersId;
	}

	public void setPromotersId(Long promotersId) {
		this.promotersId = promotersId;
	}

	public Long getSalesmanId() {
		return salesmanId;
	}

	public void setSalesmanId(Long salesmanId) {
		this.salesmanId = salesmanId;
	}

	public Long getExtensionCenterId() {
		return extensionCenterId;
	}

	public void setExtensionCenterId(Long extensionCenterId) {
		this.extensionCenterId = extensionCenterId;
	}

	public Long getStrategicPartnerId() {
		return strategicPartnerId;
	}

	public void setStrategicPartnerId(Long strategicPartnerId) {
		this.strategicPartnerId = strategicPartnerId;
	}

}