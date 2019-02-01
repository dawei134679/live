package com.mpig.api.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.Unirest;
import com.mpig.api.model.PayOrderModel;
import com.mpig.api.model.ReturnModel;
import com.mpig.api.model.UserBaseInfoModel;
import com.mpig.api.service.IOrderService;
import com.mpig.api.service.IUserService;
import com.mpig.api.utils.CodeContant;
import com.mpig.api.utils.Constant;
import com.mpig.api.utils.JsonUtil;
import com.mpig.api.utils.ParamHandleUtils;

@RestController
@Scope("prototype")
@RequestMapping("/appleInnerPay")
public class AppleInnerPayController extends BaseController {

	private static final Logger logger = Logger.getLogger(AppleInnerPayController.class);

	@Autowired
	private IOrderService orderService;
	@Autowired
	private IUserService userService;

	/**
	 * 保存订单记录并且验证交易是否有效
	 */
	@RequestMapping("/saveAndCheckOrder")
	public ReturnModel saveAndCheckOrder(HttpServletRequest req, HttpServletResponse res) {
		try {
			if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "dstuid", "amount", "details",
					"productid")) {
				logger.info("saveAndCheckOrder 缺少参数");
				returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
				returnModel.setMessage("缺少参数或参数为空");
				return returnModel;
			}
			if (!ParamHandleUtils.isInt(req, "os", "reqtime", "dstuid", "amount", "paytime")) {
				logger.info("saveAndCheckOrder 参数类型错误");
				returnModel.setCode(CodeContant.ConParamTypeIsErr);
				returnModel.setMessage("参数类型错误");
				return returnModel;
			}
			// 校验码验证及token
			authToken(req, false);
			if (returnModel.getCode() != CodeContant.OK) {
				logger.info("saveAndCheckOrder 校验码验证及token失败");
				return returnModel;
			}
			String token = req.getParameter("token");
			int srcUid = authService.decryptToken(token, returnModel);
			if (srcUid <= 0) {
				logger.info("saveAndCheckOrder srcUid 等于 0!");
				return returnModel;
			}

			int orderStatus = 3;// 支付失败
			String orderString = req.getParameter("orderString");
			if (StringUtils.isNotBlank(orderString)) {
				logger.info("准备验证订单有效性");
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("receipt-data", orderString);
				map.put("password", Constant.apple_inner_pay_password);
				map.put("exclude-old-transactions", "");

				String jsonRespose = Unirest.post(Constant.apple_inner_pay_url).body(JsonUtil.toJson(map)).asJson().getBody().toString();
				logger.debug("苹果支付正式环境验证订单返回:"+jsonRespose);
				JSONObject json = JSONObject.parseObject(jsonRespose);
				Integer checkStatus = json.getInteger("status");
				
				if (checkStatus == 0) {
					orderStatus = 2;// 已支付
				}
				else if(checkStatus == 21007) {//receipt是Sandbox receipt，但却发送至生产系统的验证服务 
					logger.debug("去Sandbox环境验证");
					String sandboxRespose = Unirest.post("https://sandbox.itunes.apple.com/verifyReceipt").body(JsonUtil.toJson(map)).asJson().getBody().toString();
					logger.debug("苹果支付Sandbox环境验证订单返回:"+sandboxRespose);
					JSONObject sandboxJson = JSONObject.parseObject(sandboxRespose);
					checkStatus = sandboxJson.getInteger("status");
					if (checkStatus == 0) {
						orderStatus = 2;// 已支付
					}
				}
				
			}

			// 获取必要信息并保存订单信息
			logger.info("准备保存订单");
			String dstUid = req.getParameter("dstuid");
			int os = Integer.valueOf(req.getParameter("os"));
			int paytime = Integer.valueOf(req.getParameter("paytime"));
			String total_amount = req.getParameter("amount");
			int creatAt = (int) (System.currentTimeMillis() / 1000);
			String inpour_no = req.getParameter("transactionID");
			String paytype = "apple_innerpay";// req.getParameter("paytype");
			String details = req.getParameter("details");
			String channel = req.getParameter("channel") == null ? "" : req.getParameter("channel");
			String orderNo = orderService.CreateOrderNo(os);

			// 获取用户信息
			UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(srcUid, false);
			int registtime = userBaseInfoModel.getRegisttime();
			
			PayOrderModel payOrderModel = orderService.existByInpourNo(inpour_no);
			if(payOrderModel != null) {
				logger.info("订单已存在");
				if(payOrderModel.getStatus() != 2) {
					if(orderService.updStatusByInpourNo(inpour_no)) {
						logger.info("更新订单状态失败");
						returnModel.setCode(CodeContant.PayOrderUpdStatusFail);
						returnModel.setMessage("更新订单状态失败");
						return returnModel;
					};
				}
				returnModel.setCode(CodeContant.OK);
				returnModel.setMessage("保存订单成功");
				return returnModel;
			}
			//删除已存在的订单(支付失败再次支付会出现问题)
			orderService.delByInpourNo(inpour_no);
			//创建订单
			int ires = orderService.CreateOrder(null, orderNo, srcUid, Integer.valueOf(dstUid), Float.parseFloat(total_amount), creatAt,
					paytime, os, paytype, false, inpour_no, details, userBaseInfoModel.getRegistchannel(), channel,
					registtime, returnModel);
			if (ires == 0) {
				returnModel.setCode(CodeContant.PayOrder);
				returnModel.setMessage("订单生成失败，保存订单数据失败");
				return returnModel;
			}

			// 更新订单状态
			logger.info("准备更新订单状态");
			Double imoney = new BigDecimal(total_amount).doubleValue();
			Double zhutous = imoney * Integer.parseInt(Constant.recharge_multiple);// 一块钱等于100个金币
			int zhutou = zhutous.intValue();
			int usres = orderService.updPayStatus(orderNo, inpour_no, imoney, zhutou, orderStatus,
					new Date().getTime() / 1000);
			if (usres == 0) {
				logger.info("更新订单状态失败");
				returnModel.setCode(CodeContant.PayOrderUpdStatusFail);
				returnModel.setMessage("更新订单状态失败");
				return returnModel;
			}
			
			returnModel.setCode(CodeContant.OK);
			returnModel.setMessage("保存订单成功");
			logger.info("保存订单成功");
			return returnModel;
		} catch (Exception e) {
			e.printStackTrace();
			returnModel.setCode(CodeContant.PayOrderException);
			returnModel.setMessage("保存订单失败");
			logger.error("方法：saveAndCheckOrder 异常：", e);
		}
		return returnModel;
	}
}
