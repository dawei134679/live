package com.tinypig.admin.TimerTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.tinypig.admin.dao.BillDao;
import com.tinypig.admin.dao.PayOrderDao;
import com.tinypig.admin.dao.UserDao;
import com.tinypig.admin.service.UnionServiceImpl;
import com.tinypig.admin.util.DateUtil;

public class ConsumeTimeService {

	@Resource
	private UnionServiceImpl unionService;
	
	/**
	 * 扶持号统计  每天 0:1:0 执行
	 */
	public void getUnionSupport(){

		System.out.println("getUnionSupport********start:"+DateUtil.convert2String(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
			Long etime = DateUtil.getDayBegin();
			Long stime = etime - 24*3600;
			
			String strYM = sdf.format(new Date(stime*1000));
			// 获取扶持号
			List<Map<String, Object>> supportList = unionService.getSupportList();
			if (supportList != null) {
				for(Map<String, Object> map : supportList){
					//TOSY 注：  函数内的support就是消费，不是扶持消费！！！！！！！！！！！！！！！！！！！！！！！
					// 支付
					PayOrderDao.getInstance().getSupportPay(Integer.valueOf(map.get("uid").toString()), Integer.valueOf(map.get("unionid").toString()), stime, etime);
					// 送礼消费
					BillDao.getInstance().getSupportConsume(Integer.valueOf(map.get("uid").toString()), Integer.valueOf(map.get("unionid").toString()), stime, etime, strYM);
					// 商城消费
					BillDao.getInstance().getMallConsume(Integer.valueOf(map.get("uid").toString()), Integer.valueOf(map.get("unionid").toString()), stime, etime, strYM);
					// 砸蛋消费
					BillDao.getInstance().getEggConsume(Integer.valueOf(map.get("uid").toString()), Integer.valueOf(map.get("unionid").toString()), stime, etime, strYM);
					// 抽奖消费
					BillDao.getInstance().getLottoryConsume(Integer.valueOf(map.get("uid").toString()), Integer.valueOf(map.get("unionid").toString()), stime, etime, strYM);
					// 打赏消费
					BillDao.getInstance().getFeedConsume(Integer.valueOf(map.get("uid").toString()), Integer.valueOf(map.get("unionid").toString()), stime, etime, strYM);
					// 红包消费，只要没有发给自己，就算消费
					BillDao.getInstance().getSupportRed(Integer.valueOf(map.get("uid").toString()), Integer.valueOf(map.get("unionid").toString()), stime, etime, strYM);
					// 剩余
					UserDao.getInstance().getSupportSurplus(Integer.valueOf(map.get("uid").toString()), Integer.valueOf(map.get("unionid").toString()), stime);
				}
			}
			
//			Long etime = DateUtil.dateToLong("2017-01-02", "yyyy-MM-dd", "other", 1);
//			Long stime = 0L;
			// 获取扶持号
			
//			List<Map<String, Object>> supportList = unionService.getSupportList();
//			while(etime <= 1486569600){
//				stime = etime - 24*3600;
//				String strYM = sdf.format(new Date(stime*1000));
//				
//				if (supportList != null) {
//					for(Map<String, Object> map : supportList){
//						// 支付
//						PayOrderDao.getInstance().getSupportPay(Integer.valueOf(map.get("uid").toString()), Integer.valueOf(map.get("unionid").toString()), stime, etime);
//						// 送礼消费
//						BillDao.getInstance().getSupportConsume(Integer.valueOf(map.get("uid").toString()), Integer.valueOf(map.get("unionid").toString()), stime, etime, strYM);
//						// 商城消费
//						BillDao.getInstance().getMallConsume(Integer.valueOf(map.get("uid").toString()), Integer.valueOf(map.get("unionid").toString()), stime, etime, strYM);
//						// 砸蛋消费
//						BillDao.getInstance().getEggConsume(Integer.valueOf(map.get("uid").toString()), Integer.valueOf(map.get("unionid").toString()), stime, etime, strYM);
//						// 抽奖消费
//						BillDao.getInstance().getLottoryConsume(Integer.valueOf(map.get("uid").toString()), Integer.valueOf(map.get("unionid").toString()), stime, etime, strYM);
//						// 打赏消费
//						BillDao.getInstance().getFeedConsume(Integer.valueOf(map.get("uid").toString()), Integer.valueOf(map.get("unionid").toString()), stime, etime, strYM);
//						// 剩余
//						UserDao.getInstance().getSupportSurplus(Integer.valueOf(map.get("uid").toString()), Integer.valueOf(map.get("unionid").toString()), stime);
//					}
//				}
//				
//				etime = etime+24*3600;
//			}
		} catch (Exception e) {
			System.out.println("getUnionSupport ******** fail:" + e.getMessage());
		}
		System.out.println("getUnionSupport********end:"+DateUtil.convert2String(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
	}
}
