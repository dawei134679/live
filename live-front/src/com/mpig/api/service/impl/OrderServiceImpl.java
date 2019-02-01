package com.mpig.api.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.mpig.api.SqlTemplete;
import com.mpig.api.async.AsyncManager;
import com.mpig.api.async.task.AsyncTask;
import com.mpig.api.db.DBHelper;
import com.mpig.api.db.DataSource;
import com.mpig.api.dictionary.lib.PayConfigLib;
import com.mpig.api.dictionary.lib.TaskConfigLib;
import com.mpig.api.model.PayAccountModel;
import com.mpig.api.model.PayOrderModel;
import com.mpig.api.model.ReturnModel;
import com.mpig.api.model.UserAssetModel;
import com.mpig.api.pay.AlipayCore;
import com.mpig.api.redis.service.OtherRedisService;
import com.mpig.api.redis.service.RedisCommService;
import com.mpig.api.redis.service.UserRedisService;
import com.mpig.api.service.IOrderService;
import com.mpig.api.service.IUserInfoService;
import com.mpig.api.service.IUserItemService;
import com.mpig.api.service.IUserService;
import com.mpig.api.service.IUserTransactionHisService;
import com.mpig.api.task.TaskService;
import com.mpig.api.utils.CodeContant;
import com.mpig.api.utils.DateUtils;
import com.mpig.api.utils.RedisContant;
import com.mpig.api.utils.VarConfigUtils;

@Service
public class OrderServiceImpl implements IOrderService, SqlTemplete {
	private static final Logger logger = Logger.getLogger(OrderServiceImpl.class);

	private final static OrderServiceImpl instance = new OrderServiceImpl();

	public static OrderServiceImpl getInstance() {
		return instance;
	}

	@Resource
	private IUserService userService;
	@Resource
	private IUserItemService userItemService;
	@Resource
	private IUserTransactionHisService userTransactionHisService;
	@Resource
	private IUserInfoService userInfoService;

	private static final Object obj = new Object();

	/**
	 * 生成订单号
	 *
	 * @return
	 */
	@Override
	public String CreateOrderNo(int os) {
		String orderNo = "";
		synchronized (obj) {
			orderNo = os + DateUtils.dateToString(null, "yyMMddHHmmssSSS");
		}
		return orderNo;
	}

	@Override
	public Boolean CheckBrush(Integer uid) {
		int icnt = OtherRedisService.getInstance().getPayRecord(uid);
		if (icnt >= 5) {
			return true;
		}
		return false;
	}

	/**
	 * 生成订单号
	 *
	 * @param
	 * @return
	 */
	@Override
	public void CreateAliOrder(String subject, String orderNo, Integer srcUid, Integer dstUid, Integer amount,
			Integer creatAt, Integer paytime, int os, String paytype, Boolean status, String inpour_no, String details,String userSource,String channel,int registtime,
			ReturnModel returnModel) {
		try {
			if(StringUtils.isEmpty(channel)){
				channel = "";
			}
			int ires = DBHelper.execute(VarConfigUtils.dbZhuPay, SQL_InsertPayOrder, false, orderNo, srcUid, dstUid,
					amount, creatAt, paytime, os, paytype, status, inpour_no, details,userSource,channel,registtime);
			if (ires == 1) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("service", PayConfigLib.getConfig().getAlipay_service());
				map.put("partner", PayConfigLib.getConfig().getAlipay_partner());
				map.put("seller_id", PayConfigLib.getConfig().getAlipay_sellerId());
				map.put("_input_charset", PayConfigLib.getConfig().getAlipay_inputCharset());
				map.put("payment_type", PayConfigLib.getConfig().getAlipay_paymentType());
				map.put("notify_url", PayConfigLib.getConfig().getAlipay_notifyUrl());
				// map.put("return_url", PayConfigLib.getConfig()
				// .getAlipay_returnUrl());

				map.put("out_trade_no", orderNo);
				map.put("subject", subject);
				map.put("total_fee", String.valueOf(amount));
				// map.put("body","小猪头");

				map = AlipayCore.paraFilter(map);
				String mysign = AlipayCore.buildRequestMysign(map);
				map = new HashMap<String, String>();
				map.put("sign", mysign);
				map.put("sign_type", PayConfigLib.getConfig().getAlipay_signType());
				map.put("out_trade_no", orderNo);
				returnModel.setData(map);
				OtherRedisService.getInstance().addPayRecord(srcUid);
			} else {
				returnModel.setCode(CodeContant.PayOrder);
				returnModel.setMessage("订单生成失败");
			}
		} catch (Exception e) {
			logger.error("<CreateOrder->Exception>" + e.toString());

			returnModel.setCode(CodeContant.PayOrder);
			returnModel.setMessage("订单生成失败");
		}
	}
	
	@Override
	public int updPayStatus(String orderNo, String trade_no, Double total_fee, int zhutou, Integer status) {
		int ires = 0;
		try {
			PayOrderModel payOrderModel = this.getPayOrderByOrderNo(orderNo);
			if (payOrderModel != null) {
				if (payOrderModel.getStatus() != 2 && payOrderModel.getAmount() == total_fee.doubleValue()) {
					// 未支付
					ires = DBHelper.execute(VarConfigUtils.dbZhuPay, SQL_UpdPayOrderStatus, false, total_fee, zhutou,
							System.currentTimeMillis() / 1000, status, trade_no, orderNo);
					if (ires == 1) {
						// TOSY TASK
						TaskService.getInstance().taskProcess(payOrderModel.getSrcuid(), TaskConfigLib.Prepaid, 1);
						TaskService.getInstance().taskProcess(payOrderModel.getSrcuid(), TaskConfigLib.ChargeOk, 1);

						//首冲任务
						if (total_fee >= 500) {
							TaskService.getInstance().taskProcess(payOrderModel.getSrcuid(), TaskConfigLib.Charge500,
									1);
							TaskService.getInstance().taskProcess(payOrderModel.getSrcuid(), TaskConfigLib.Daily_charge500,
									1);
						}

						if (total_fee >= 10) {
							TaskService.getInstance().taskProcess(payOrderModel.getSrcuid(), TaskConfigLib.Charge10,
									1);
						}

						if (total_fee >= 30) {
							TaskService.getInstance().taskProcess(payOrderModel.getSrcuid(), TaskConfigLib.Daily_charge30,
									1);
						}

						if (total_fee >= 298) {
							TaskService.getInstance().taskProcess(payOrderModel.getSrcuid(), TaskConfigLib.Daily_charge298,
									1);
						}

						try{
							// 充值活动异步处理
							AsyncManager.getInstance().execute(new AsyncTask.PayActAsyncTask(payOrderModel.getSrcuid(),total_fee));
						}catch (Exception ex){
							logger.error("PayActAsyncTask>>>",ex);
						}



						if (status == 2) {
							ires = userService.updAssetMoneyByUid(payOrderModel.getDstuid(), (double) zhutou);
							OtherRedisService.getInstance().delPayRecord(payOrderModel.getSrcuid());
							
							// 充值活动
							this.actReceiveMoney(payOrderModel.getSrcuid(),payOrderModel.getOrderId(),total_fee,zhutou);
							
						}
					} else {
						// 更新失败
						logger.error("<aliPayStatus->订单状态更新失败：>" + orderNo);
					}
				} else {
					// 已支付
					ires = 1;
				}
			} else {
				// 无单号
				logger.error("<aliPayStatus->单号不存在：>" + orderNo);
			}

		} catch (Exception e) {
			logger.error("<aliPayStatus->Exception>" + e.toString());
		}
		return ires;
	}
	
	@Override
	public int updPayType(String payType,String orderNo){
		int ires = 0;
		try {
			ires = DBHelper.execute(VarConfigUtils.dbZhuPay, SQL_UpdPayType, false,payType , orderNo);
		}catch (Exception e) {
			logger.error(" 修改支付方式失败 ： "+e);
		}
		return ires; 
	}
	@Override
	public int updPayStatus(String orderNo, String trade_no, Double total_fee, int zhutou, Integer status,Long paytime) {
		int ires = 0;
		try {
			PayOrderModel payOrderModel = this.getPayOrderByOrderNo(orderNo);
			if (payOrderModel != null) {
				if (payOrderModel.getStatus() != 2 && payOrderModel.getAmount() == total_fee.doubleValue()) {
					// 未支付 且 支付金额和订单金额一致。
					String sql = SQL_UpdPayOrderStatus;
					ires = DBHelper.execute(VarConfigUtils.dbZhuPay, sql, false, total_fee, zhutou,
							paytime, status, trade_no, orderNo);
					if (ires == 1) {

						// TOSY TASK
						TaskService.getInstance().taskProcess(payOrderModel.getSrcuid(), TaskConfigLib.Prepaid, 1);

						TaskService.getInstance().taskProcess(payOrderModel.getSrcuid(), TaskConfigLib.ChargeOk, 1);
						if (total_fee >= 500) {
							TaskService.getInstance().taskProcess(payOrderModel.getSrcuid(), TaskConfigLib.Charge500,1);
						}
						try{
							// 充值活动异步处理
							AsyncManager.getInstance().execute(new AsyncTask.PayActAsyncTask(payOrderModel.getSrcuid(),total_fee));
						}catch (Exception ex){
							logger.error("PayActAsyncTask>>>",ex);
						}

						if (status == 2) {
							ires = userService.updAssetMoneyByUid(payOrderModel.getDstuid(), (double) zhutou);
							OtherRedisService.getInstance().delPayRecord(payOrderModel.getSrcuid());
							// 充值活动
							this.actReceiveMoney(payOrderModel.getSrcuid(),payOrderModel.getOrderId(),total_fee,zhutou);
							//充值统计
							userTransactionHisService.saveUserTransactionHis(12, payOrderModel.getDstuid(), total_fee, 0L, paytime*1000, "", 1);
							//记录用户充值总金额(RMB)
							userService.updateUserMoneyRbm(payOrderModel.getDstuid()+"",total_fee);
							//记录数据（user_info）
							userInfoService.updateUserInfoMoneyRMB(payOrderModel.getDstuid(), total_fee);
						}
					} else {
						// 更新失败
						logger.error("<aliPayStatus->订单状态更新失败：>" + orderNo);
					}
				} else {
					// 已支付
					ires = 1;
				}
			} else {
				// 无单号
				logger.error("<aliPayStatus->单号不存在：>" + orderNo);
			}

		} catch (Exception e) {
			logger.error("<aliPayStatus->Exception>" + e.toString());
		}
		return ires;
	}

	@Override
	public PayOrderModel getPayOrderByOrderNo(String orderNo) {
		Connection conn = null;
		PreparedStatement statement = null;
		PayOrderModel payOrderModel = null;
		ResultSet rs = null;
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuPay).getConnection();
			String sql = SQL_GetPayOrderByOrderNo;
			statement = conn.prepareStatement(sql);
			DBHelper.setPreparedStatementParam(statement, orderNo);
			rs = statement.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					payOrderModel = new PayOrderModel().populateFromResultSet(rs);
					break;
				}
			}
		} catch (Exception e) {
			logger.error("<getPayOrderByOrderNo->Exception>" + e.toString());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				logger.error("<getPayOrderByOrderNo->finally->Exception>" + e2.toString());
			}
		}
		return payOrderModel;
	}

	@Override
	public PayAccountModel getPayAccountByUid(int uid, Boolean bl) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		PayAccountModel payAccountModel = null;
		if (!bl) {
			// 读缓存
			String payAccount = UserRedisService.getInstance().getPayAccount(uid);
			if (StringUtils.isNotEmpty(payAccount)) {
				payAccountModel = (PayAccountModel) JSONObject.parseObject(payAccount, PayAccountModel.class);
			}
		}
		if (payAccountModel == null) {
			try {
				conn = DataSource.instance.getPool(VarConfigUtils.dbZhuPay).getConnection();
				statement = conn.prepareStatement(SQL_GetPayAcountByUid);
				DBHelper.setPreparedStatementParam(statement, uid);
				rs = statement.executeQuery();
				if (rs != null) {
					while (rs.next()) {
						payAccountModel = new PayAccountModel().populateFromResultSet(rs);
						// 缓存
						UserRedisService.getInstance().setPayAccount(uid, JSONObject.toJSONString(payAccountModel));
						break;
					}
				}
			} catch (Exception e) {
				logger.error("<getPayAccountByUid->Exception>" + e.toString());
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
					if (statement != null) {
						statement.close();
					}
					if (conn != null) {
						conn.close();
					}
				} catch (Exception e2) {
					logger.error("<getPayAccountByUid->finally->Exception>" + e2.toString());
				}
			}
		}
		return payAccountModel;
	}

	@Override
	public int insertPayAccount(Object... objs) {
		int ires = 0;
		try {
			ires = DBHelper.execute(VarConfigUtils.dbZhuPay, SQL_InsertPayAccount, false, objs);
			if (ires == 1) {
				this.getPayAccountByUid((int) objs[0], true);
			}
		} catch (Exception e) {
			logger.error("<insertPayAccount->Exception>" + e.toString());
		}
		return ires;
	}

	@Override
	public int updPayAccountByUid(int uid, String weixin, String alipay) {
		int ires = 0;
		if (weixin == "" && alipay == "") {
			return 1;
		}
		try {
			PayAccountModel payAccountModel = this.getPayAccountByUid(uid, false);
			if (weixin == "") {
				weixin = payAccountModel.getWx_openid();
			}
			if (alipay == "") {
				alipay = payAccountModel.getAlipay();
			}
			ires = DBHelper.execute(VarConfigUtils.dbZhuPay, SQL_UpdPayAccount, false, weixin, alipay, uid);
			if (ires == 1) {
				this.getPayAccountByUid(uid, true);
			}
		} catch (Exception e) {
			logger.error("<updPayAccountByUid->Exception>" + e.toString());
		}
		return ires;
	}

	@Override
	public int CreateOrder(String notiryUrl, String orderNo, Integer srcUid, Integer dstUid, Float amount,
			Integer creatAt, Integer paytime, int os, String paytype, Boolean status, String inpour_no, String details,String userSource,String channel,int registtime,
			ReturnModel returnModel) {
		try {
			if(StringUtils.isEmpty(channel)){
				channel = "";
			}
			String sql = SQL_InsertPayOrder;
			return DBHelper.execute(VarConfigUtils.dbZhuPay, sql, false, orderNo, srcUid, dstUid, amount,
					creatAt, paytime, os, paytype, status, inpour_no, details,userSource,channel,registtime);
		} catch (Exception e) {
			logger.error("<CreateOrder->Exception>" + e.toString());
			returnModel.setCode(CodeContant.PayOrder);
			returnModel.setMessage("订单生成失败");
			return 0;
		}
	}

	@Override
	public int CreateOrderApp(String orderNo, Integer srcUid, Integer dstUid, Float amount, int zhutou, Integer creatAt,
			Integer paytime, int os, String paytype, int status, String inpour_no, String details,String userSource,String channel,int registtime,
			ReturnModel returnModel) {
		int ires = 0;
		try {
			if(StringUtils.isEmpty(channel)){
				channel = "";
			}
			ires = DBHelper.execute(VarConfigUtils.dbZhuPay, SQL_InsertPayOrderByApple, false, orderNo, srcUid, dstUid,
					amount, zhutou, creatAt, paytime, os, paytype, status, inpour_no, details,userSource,channel,registtime);
			if (ires == 1 && zhutou > 0) {
				ires = userService.updAssetMoneyByUid(srcUid, (double) zhutou);
			}
		} catch (Exception e) {
			logger.error("<CreateOrder->Exception>" + e.toString());
			returnModel.setCode(CodeContant.PayOrder);
			returnModel.setMessage("订单生成失败");
		}
		return ires;
	}

	@Override
	public int bindWeixin(String unionid, String openid) {
		int ires = 0;
		try {
			ires = DBHelper.execute(VarConfigUtils.dbZhuPay, SQL_BindWeixin, false, openid, unionid);
		} catch (Exception e) {
			logger.error("<bindWeixin->Exception>" + e.toString());
		}
		return ires;
	}

	@Override
	public List<Integer> getUidByUnionid(String unionid) {

		ArrayList<Integer> uids = new ArrayList<>();
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuPay).getConnection();
			statement = conn.prepareStatement(SQL_GetUidByUnionid);
			DBHelper.setPreparedStatementParam(statement, unionid);
			rs = statement.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					uids.add(rs.getInt("uid"));
				}
			}
			return uids;
		} catch (Exception e) {
			logger.error("<getUidByUnionid->Exception>" + e.toString());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				logger.error("<getUidByUnionid->finally->Exception>" + e2.toString());
			}
		}

		return uids;
	}

	/**
	 * 添加活动产生的猪头记录
	 * 
	 * @param uid
	 *            用户UID
	 * @param source
	 *            =1任务 =2活动
	 * @param act_id
	 *            活动id
	 * @param act_name
	 *            活动名称
	 * @param zhutou
	 *            猪头数
	 * @return =1成功，其他则失败
	 */
	@Override
	public int insertPayActivity(int uid, int source, int act_id, String act_name, int zhutou) {
		int ires = 0;
		try {
			ires = DBHelper.execute(VarConfigUtils.dbZhuPay, SQL_InsertPayActivity, false, uid, act_id, act_name,
					zhutou,0, System.currentTimeMillis() / 1000, source);
			if (ires == 1) {
				try {
					String sql = com.mpig.api.utils.StringUtils.getSqlString(SQL_UpdAssetMondyByUid, "user_asset_",
							uid);
					ires = DBHelper.execute(VarConfigUtils.dbZhuUser, sql, false, (double) zhutou, uid);
					// 现在7倍
					if (ires == 1) {
						UserServiceImpl.getInstance().getUserAssetByUid(uid, true);
					}
				} catch (Exception e) {
					logger.error("<updAssetMoneyByUid->Exception>" + e.toString());
				}
				// return ires;

				// ires = userService.updAssetMoneyByUid(uid, (double) zhutou);
			}
		} catch (Exception e) {
			logger.error("<insertPayActivity->Exception>" + e.toString());
		}
		return ires;
	}

	/**
	 * 判断是否首充
	 * 
	 * @param uid
	 * @return false未充值，true已充值
	 */
	@Override
	public boolean checkFirst(int uid) {

		Boolean blFirstPay = true;
		String firstPayStatus = UserRedisService.getInstance().getFirstPayStatus(uid);
		if (StringUtils.isEmpty(firstPayStatus)) {
			
			Connection conn = null;
			PreparedStatement statement = null;
			ResultSet rs = null;
			try {
				conn = DataSource.instance.getPool(VarConfigUtils.dbZhuPay).getConnection();
				statement = conn.prepareStatement(SQL_GetFirstPayByUid);
				DBHelper.setPreparedStatementParam(statement, uid);
				rs = statement.executeQuery();
				if (rs != null) {
					while (rs.next()) {
						if (rs.getInt("cnts") > 1) {
							UserRedisService.getInstance().setFirstPayStatus(uid, System.currentTimeMillis() / 1000);
							blFirstPay = false;
						}else {
							
						}
					}
				}else {
					blFirstPay = false;
				}
			} catch (Exception e) {
				blFirstPay = false;
				logger.error("<checkFirst->Exception>", e);
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
					if (statement != null) {
						statement.close();
					}
					if (conn != null) {
						conn.close();
					}
				} catch (Exception e2) {
					logger.error("<checkFirst->finally->Exception>", e2);
				}
			}
		}else {
			blFirstPay = false;
		}
		return blFirstPay;
	}

	@Override
	public void ReceiveMoney(int uid,int type,ReturnModel returnModel) {
		returnModel.setCode(400);
		returnModel.setMessage("领取失败");
		
		Long dayBegin = DateUtils.getDayBegin();
		Long dayEnd = dayBegin + 24*3600;
		
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			
			String actName = "国庆活动";
			int total = 0;
			int rate = 0;
			
			String  sql = "select order_id,zhutou from pay_order where status=2 and paytime>=? and paytime<? and srcuid=?";
			if (type == 1) {
				sql = sql + " and amount >= 100 and amount < 500";
				rate = 5;
				actName = actName + "：充值大于等于100，小于500 返5%";
			}else if (type == 2) {
				sql = sql + " and amount >= 500 and amount < 1000";
				rate = 8;
				actName = actName + "：充值大于等于500，小于1000 返8%";
			}else if (type == 3) {
				sql = sql + " and amount >= 1000";
				rate = 10;
				actName = actName + "：充值大于等于1000 返10%";
			}
			
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuPay).getConnection();
			statement = conn.prepareStatement(sql);
			statement.setLong(1, dayBegin);
			statement.setLong(2, dayEnd);
			statement.setInt(3, uid);
			
			rs = statement.executeQuery();
			
			if (rs != null) {
				String str = null;
				int rst = 0;
				while (rs.next()) {
					str = RedisCommService.getInstance().get(RedisContant.RedisNameUser, RedisContant.nationalReceive + rs.getString("order_id"));
					if (StringUtils.isNotEmpty(str)) {
						// 已经领过了
						continue;
					}else {
						rst = this.insertPayActivity(uid,2,0,actName,(int)(rs.getInt("zhutou")*rate/100));
						if (rst > 0) {
							// 标示今天已经领取过去了
							RedisCommService.getInstance().set(RedisContant.RedisNameUser, RedisContant.nationalReceive + rs.getString("order_id"), "1", 129600);
						}
						total += (int)(rs.getInt("zhutou")*rate/100);
					}
				}
			}
			
			if (total > 0) {
				UserAssetModel userAssetModel = userService.getUserAssetByUid(uid, false);
				Map<String, Object> map = new HashMap<String,Object>();
				map.put("money", userAssetModel == null?0:userAssetModel.getMoney());
				returnModel.setCode(200);
				returnModel.setMessage("成功领取了："+total+" 猪头");
				returnModel.setData(map);
			}else {
				returnModel.setCode(200);
				returnModel.setMessage("你还没有达到领取条件");
			}
			
		} catch (Exception e) {
			logger.error("<ReceiveMoney->Exception>", e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				logger.error("<ReceiveMoney->finally->Exception>", e2);
			}
		}
	}
	
	/**
	 * 获取国庆活动
	 * @param uid
	 * @param order_id
	 * @param amount
	 * @param zhutou
	 */
	private void actReceiveMoney(int uid,String order_id,Double amount,int zhutou) {
		
		Long lgNow = System.currentTimeMillis()/1000;
		
		// 从9月24号到9月29号  
		if (lgNow >= 1474646400 && lgNow < 1475164800) {
			
			String actName = order_id+",国庆活动";
			int rate = 0;
			
			if (amount >= 100 && amount < 500) {
				
				rate = 5;
				actName = actName + "：充值大于等于100，小于500 返5%";
			}else if (amount >= 500 && amount < 1000) {
				
				rate = 8;
				actName = actName + "：充值大于等于500，小于1000 返8%";
			}else if (amount >= 1000) {
				
				rate = 10;
				actName = actName + "：充值大于等于1000 返10%";
			}else {
				return;
			}
			
			String str = RedisCommService.getInstance().get(RedisContant.RedisNameUser, RedisContant.nationalReceive + order_id);
			if (StringUtils.isNotEmpty(str)) {
				// 已经领过了
			}else {
				int rst = this.insertPayActivity(uid,2,0,actName,(int)(zhutou*rate/100));
				if (rst > 0) {
					// 标示今天已经领取过去了
					RedisCommService.getInstance().set(RedisContant.RedisNameUser, RedisContant.nationalReceive + order_id, "1", 129600);
				}
			}
		}
	}

	@Override
	public boolean delByInpourNo(String inpour_no) {
		int ires;
		try {
			ires = DBHelper.execute(VarConfigUtils.dbZhuPay, SQL_DelOrderByInpourNo, false, inpour_no);
			if (ires == 1) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public PayOrderModel existByInpourNo(String inpour_no) {
		Connection conn = null;
		PreparedStatement statement = null;
		PayOrderModel payOrderModel = null;
		ResultSet rs = null;
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuPay).getConnection();
			String sql = SQL_GetExchangeList;
			statement = conn.prepareStatement(sql);
			DBHelper.setPreparedStatementParam(statement, inpour_no);
			rs = statement.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					payOrderModel = new PayOrderModel().populateFromResultSet(rs);
					break;
				}
			}
		} catch (Exception e) {
			logger.error("<existByInpourNo->Exception>" + e.toString());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				logger.error("<existByInpourNo->finally->Exception>" + e2.toString());
			}
		}
		return payOrderModel;
	}

	@Override
	public boolean updStatusByInpourNo(String inpour_no) {
		int ires;
		try {
			ires = DBHelper.execute(VarConfigUtils.dbZhuPay, SQL_UpdStatusByInpourNo, false, inpour_no);
			if (ires == 1) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}	
}
