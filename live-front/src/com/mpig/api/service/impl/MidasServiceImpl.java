package com.mpig.api.service.impl;

import java.net.URLEncoder;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.midas.common.Configure;
import com.midas.common.Signature;
import com.midas.protocol.getBlance.GetBlanceReqData;
import com.midas.protocol.getBlance.GetBlanceResData;
import com.midas.protocol.getBlance.pay.PayReqData;
import com.midas.protocol.getBlance.pay.PayResData;
import com.mpig.api.async.AsyncManager;
import com.mpig.api.async.task.AsyncTask;
import com.mpig.api.model.ReturnModel;
import com.mpig.api.model.UserBaseInfoModel;
import com.mpig.api.service.IMidasService;
import com.mpig.api.service.IOrderService;
import com.mpig.api.service.IUserService;
import com.mpig.api.utils.CodeContant;

@Service
public class MidasServiceImpl implements IMidasService{

	private static final Logger logger = Logger.getLogger(MidasServiceImpl.class);
	private static Integer APP_ID = Configure.APP_ID;
	private static String APP_KEY = Configure.OFFICIAL_APPKEY;
	private static String GET_BALANCE = Configure.OFFICIAL_GETBALANCE;
	private static String PAY = Configure.OFFICIAL_PAY;
	
	@Resource
	private IOrderService orderService;
	@Resource
	private IUserService userService;
	
	@Override
	public void pay(Integer uid, String openid, String openkey, String pf, String pfkey, String type, ReturnModel returnModel) {
		try {
			Long ts = System.currentTimeMillis()/1000;
			UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(uid, false);
			//生成签名
			String sign = Signature.getSign(GET_BALANCE,APP_KEY,new GetBlanceReqData().GetBlanceReqDataMap(openid, openkey, APP_ID, ts, pf, pfkey, "1",null, null, null));
			//按照指定格式转化成需要提交的参数
			String param = Signature.sortStr(new GetBlanceReqData().GetBlanceReqDataMap(openid, openkey, APP_ID, ts, pf, pfkey, "1",null, null, null));
			//拼接cookie参数
			String cookie = Signature.getCookieStr(type, GET_BALANCE);
			//发起余额账单请求
			GetBlanceResData blanceResData = getbalance(GET_BALANCE+"?"+param+"sig="+URLEncoder.encode(sign, "UTF-8"), cookie.substring(0,cookie.length()-1));
			if(blanceResData != null){
				int balance = blanceResData.getBalance();
				if(balance > 0){ //查询到余额大于0时做添加订单,添加账户余额,扣除支付宝余额操作
					int creatAt = (int) (System.currentTimeMillis() / 1000);
					String orderNo = orderService.CreateOrderNo(1);
					int registtime = userBaseInfoModel.getRegisttime();
					int ires = orderService.CreateOrderApp(orderNo, uid, uid, Float.valueOf(balance),
							(int) (balance), creatAt, creatAt, 1,
							"yingyongbao", 2, openid, balance+"",userBaseInfoModel.getRegistchannel(),"yingyongbao",registtime,returnModel);
					if(ires > 0){
						try{
							AsyncManager.getInstance().execute(new AsyncTask.PayActAsyncTask(uid,Float.valueOf(balance)));
						}catch (Exception ex){
							logger.error("PayActAsyncTask>>>",ex);
						}

						//生成签名
						String paySign = Signature.getSign(PAY,APP_KEY,new PayReqData().GetPayReqDataMap(openid, openkey, APP_ID, ts, pf, pfkey, "1", balance, orderNo));
						//按照指定格式转化成需要提交的参数
						String payParam = Signature.sortStr(new PayReqData().GetPayReqDataMap(openid, openkey, APP_ID, ts, pf, pfkey, "1", balance, orderNo));
						//拼接cookie参数
						String payCookie = Signature.getCookieStr(type, PAY);
						//发起余额账单请求
						PayResData payResData = pay(PAY+"?"+payParam+"sig="+URLEncoder.encode(paySign, "UTF-8"), payCookie.substring(0,payCookie.length()-1));
						if(payResData == null){
							returnModel.setCode(CodeContant.PayOrder);
							returnModel.setMessage("充值失败,请联系客服");
							return;
						}
					}else{
						returnModel.setCode(CodeContant.CONSYSTEMERR);
						returnModel.setMessage("充值失败,请联系客服");
						return;
					}
				}
			}else{
				returnModel.setCode(CodeContant.COnYYBCodeError);
				returnModel.setMessage("登录状态过期，请重新登录！");
				return;
			}
		} catch (Exception e) {
			logger.error(e);
		}
		
	}
	
	/**
	 * 获取账单查询请求
	 * @param url
	 * @param Cookie
	 * @return
	 */
	public GetBlanceResData getbalance(String url,String Cookie){
		HttpResponse<JsonNode> response = null;
		try {
			System.out.println(url);
			response = Unirest.get(url).header("Cookie", Cookie).asJson();
		} catch (Exception e) {
			logger.error("Error getbalance-exception:"+e.toString());
		}
		if (response == null || response.getStatus() != 200) {
			return null;
		}
		GetBlanceResData blanceResData = (GetBlanceResData)JSON.parseObject(response.getBody().toString(),GetBlanceResData.class);
		if (blanceResData != null) {
			if(blanceResData.getRet()!=0){
				logger.error("getbalance response ： "+response.getBody().toString());
				return null;	
			}
		}else{
			return null;
		}
		return blanceResData;
	}
	
	/**
	 * 支付扣钱请求
	 * @param url
	 * @param Cookie
	 * @return
	 */
	public PayResData pay(String url,String Cookie){
		HttpResponse<JsonNode> response = null;
		try {
			System.out.println(url);
			response = Unirest.get(url).header("Cookie", Cookie).asJson();
		} catch (Exception e) {
			logger.error("Error pay-exception:"+e.toString());
		}
		if (response == null || response.getStatus() != 200) {
			return null;
		}
		PayResData payResData = (PayResData)JSON.parseObject(response.getBody().toString(),PayResData.class);
		if (payResData != null) {
			if(payResData.getRet()!=0){
				logger.error("pay response ： "+response.getBody().toString());
				return null;	
			}
		}else{
			return null;
		}
		return payResData;
	}
}
