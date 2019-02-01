package com.mpig.api.dao;

public interface IPayMallListDao {

	/**
	 * 增加购买记录
	 * @param gid 商品id
	 * @param gname 商品名称
	 * @param srcuid 购买人id
	 * @param srcnickname 购买人昵称
	 * @param dstuid 受益人id
	 * @param dstnickname 受益人昵称
	 * @param count 购买数量 
	 * @param price 订单原价
	 * @param realprice 实际支付价格
	 * @param pricetotal 订单原总价
	 * @param realpricetotal 实际支付总价
	 * @param credit 声援值
	 * @return
	 */
	public int insert(int gid, int gname, int srcuid, String srcnickname, int dstuid, String dstnickname, int count, int price, int realprice, int pricetotal, int realpricetotal, int credit);
}
