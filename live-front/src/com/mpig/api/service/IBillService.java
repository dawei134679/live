package com.mpig.api.service;

import java.util.List;
import java.util.Map;

import com.mpig.api.model.BillRedenvelopeModel;

public interface IBillService {

	/**
	 * 插入送礼账单
	 * @param objs 参数严格按照sql中的占位符顺序
	 * @return
	 */
	public int insertBill(Object... objs);
	/**
	 * 插入送背包礼物账单
	 * @param objs 参数严格按照sql中的占位符顺序
	 * @return
	 */
	public int insertBagBill(Object... objs);
	
	/**
	 * 插入红包
	 * @param objects
	 * @return
	 */
	public int insertRedEnvelop(Object... objects);
	
	/**
	 * 根据红包id获取红包信息
	 * @param id
	 * @return
	 */
	public BillRedenvelopeModel getRedEnvelopById(int id);
	
	/**
	 * 根据红包id修改信息
	 * @param isfinish
	 * @param getmoney
	 * @param getcnts
	 * @param gettime
	 * @param id
	 * @return
	 */
	public int updRedEnvelopeById(int isfinish,int getmoney,int getcnts,int gettime, int id);
	/**
	 * 插入中奖账单记录
	 * @param uid 用户id
	 * @param gid 礼物id
	 * @param anchoruid 主播id
	 * @param multiples 倍率
	 * @param luckyCount 中奖次数
	 * @param gprice 礼物单价
	 * @param priceTotal 礼物总价
	 * @param wealthTotal 财富总值
	 * @param creditTotal 声援总值
	 * @param sendPrice 送出价格
	 * @param sendCount 送出数量
	 * @return
	 */
	public int insertPrizeBill(int uid,int gid, int anchoruid, int multiples,int luckyCount, int gprice, int priceTotal, int wealthTotal, int creditTotal, int sendPrice, int sendCount);
	
	/**
	 * 获取消费记录信息
	 * @param uid
	 * @param date
	 * @return
	 */
	List<Map<String, Object>> getCostRecord(int uid,String date,int page,int pageSize);

	/**
	 * 获取消费记录总条数
	 * @param uid
	 * @param date
	 * @return
	 */
	public long getCostRecordTotalCount(int uid,String date);
	
	/**
	 * 获取总钱数
	 * @param uid
	 * @param date
	 * @return
	 */
	public long getConBillSumaryByDate(int uid,String date);
	
	
	/**
	 * 获取收礼记录信息
	 * @param uid
	 * @param date
	 * @return
	 */
	List<Map<String, Object>> getRecBillListByDate(int uid,String date,int page,int pageSize);

	/**
	 * 获取收礼记录总条数
	 * @param uid
	 * @param date
	 * @return
	 */
	public long getRecTotalCount(int uid,String date);
	
	/**
	 * 获取声援数
	 * @param uid
	 * @param date
	 * @return
	 */
	public long getRecBillSumaryByDate(int uid,String date);
}
