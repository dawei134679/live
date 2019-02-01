package com.mpig.api.dao;

public interface IUserItemDao {
	/**
	 * 根据用户和商品id查询背包里数量
	 * @param uid
	 * @param gid
	 * @return
	 */
	int getItemCountByGid(Integer uid, Integer gid);
	/**
	 * 根据用户和商品id删除
	 * @param uid
	 * @param gid
	 * @return
	 */
	int delItemByGid(Integer uid, Integer gid);
}
