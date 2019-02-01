package com.mpig.api.dao;

import java.util.List;

import com.mpig.api.model.ValueaddPrivilegeModel;

/**
 * 增值身份特权相关信息
 * @author zyl
 *
 */
public interface IValueaddPrivilegeDao {
	/**
	 * 查询所有记录
	 * @return List<ValueaddPrivilegeModel> 
	 */
	List<ValueaddPrivilegeModel> getList();
}
