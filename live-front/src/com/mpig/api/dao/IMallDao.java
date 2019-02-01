package com.mpig.api.dao;

public interface IMallDao {

	/**
	 * 插入商城订单详情
	 * @param gid 商品id
	 * @param gname 商品名称 
	 * @param srcuid 操作人id
	 * @param srcnickname 操作人昵称
	 * @param dstuid 受益人id
	 * @param dstnickname 受益人昵称
	 * @param count 购买数量 
	 * @param price 单价
	 * @param realprice 实际支付单价
	 * @param pricetotal 总价
	 * @param realpricetotal 实际支付总价
	 * @param credit 声援值
	 * @param starttime 
	 * @param endtime
	 * @param type 1守护 2vip 3商城道具 4座驾
	 * @return
	 */
	int addMallInfo(int gid, String gname, int srcuid, String srcnickname, int dstuid, String dstnickname, int count, int price, int realprice, int pricetotal, int realpricetotal, int credit, int starttime, int endtime, int type);
}
