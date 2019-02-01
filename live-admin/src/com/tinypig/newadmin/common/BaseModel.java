package com.tinypig.newadmin.common;

/**
  * @ClassName: BaseModel 
  * @Description: 公共类
  * @author Wangshaocheng
  * @date 2016-6-29 上午11:12:41 
  *
 */
public class BaseModel {
	
	//从第几条开始
	private Integer rowNumStart;
	//每页显示多少条
	private Integer pageSize;
	// 添加或修改状态
	private String isAddOrEdit;
	
	public String getIsAddOrEdit() {
		return isAddOrEdit;
	}

	public void setIsAddOrEdit(String isAddOrEdit) {
		this.isAddOrEdit = isAddOrEdit;
	}

	public Integer getRowNumStart() {
		return rowNumStart;
	}

	public void setRowNumStart(Integer rowNumStart) {
		this.rowNumStart = rowNumStart;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
}
