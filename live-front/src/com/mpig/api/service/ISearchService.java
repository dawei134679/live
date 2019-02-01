package com.mpig.api.service;

import java.util.List;
import java.util.Map;

import com.mpig.api.model.SearchModel;


public interface ISearchService {
	/**
	 * 插入用户可搜索信息
	 */
	void insertUserInfosAnsyc(List<SearchModel> data, boolean isSync);
	void insertUserInfo(SearchModel d);

	/**
	 * 更新用户es信息
	 */
	void updateUsersAnsyc(SearchModel data);
	
	/**
	 * 更新用户可搜索信息
	 */
	void updateUserInfosAnsyc(List<SearchModel> data);
	/**
	 * 更新用户的某条信息
	 * @param strUid	uid
	 * @param field		字段		字段名参考SearchModel
	 * @param value		值
	 */
	void updateUserFieldsAnsyc(String strUid, String field,Object value);
	/**
	 * 删除用户可搜索信息
	 */
	void deleteUserInfosAnsyc(List<SearchModel> data);
	
	/**
	 * 搜索用户信息
	 */
	List<SearchModel> searchUserInfo(String searchData,int uid);
	
	List<SearchModel> searchUserInfoForPc(String searchData, int uid);
	
	List<Map<String, Object>> getSearchRecommand();
}
