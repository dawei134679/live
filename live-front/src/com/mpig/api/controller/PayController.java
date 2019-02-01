package com.mpig.api.controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mpig.api.model.PayOrderModel;
import com.mpig.api.model.ReturnModel;
import com.mpig.api.model.UserBaseInfoModel;
import com.mpig.api.reapal.ResponseUtils;
import com.mpig.api.reapal.RongpayService;
import com.mpig.api.redis.service.RelationRedisService;
import com.mpig.api.service.IOrderService;
import com.mpig.api.service.IUserService;
import com.mpig.api.utils.AESCipher;
import com.mpig.api.utils.CodeContant;
import com.mpig.api.utils.Constant;
import com.mpig.api.utils.DateUtils;
import com.mpig.api.utils.HttpKit;
import com.mpig.api.utils.JsonUtil;
import com.mpig.api.utils.MD5Encrypt;
import com.mpig.api.utils.ParamHandleUtils;
import com.mpig.api.utils.RandomUtil;
import com.mpig.api.utils.XmlUtil;

@RestController
@Scope("prototype")
@RequestMapping("/pay")
public class PayController extends BaseController {

	private static Logger log = Logger.getLogger(PayController.class);

	@Autowired
	private IOrderService orderService;
	@Autowired
	private IUserService userService;

	/**
	 * 融宝微信支付
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/createOrder")
	public ReturnModel createReapalOrder(HttpServletRequest req, HttpServletResponse response) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "dstuid", "amount", "paytype",
				"details")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime", "dstuid", "amount")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		// 校验码验证及token
		authToken(req, false);
		if (returnModel.getCode() != CodeContant.OK) {
			return returnModel;
		}
		String token = req.getParameter("token");
		int srcUid = authService.decryptToken(token, returnModel);
		if (srcUid <= 0) {
			return returnModel;
		}

		Integer dstuid = Integer.valueOf(req.getParameter("dstuid"));
		if (null == dstuid || dstuid.intValue() <= 0) {
			returnModel.setCode(CodeContant.PayOrder);
			returnModel.setMessage("创建订单失败，用户ID不正确！");
			return returnModel;
		}
		int os = Integer.valueOf(req.getParameter("os"));
		Float amount = Float.valueOf(req.getParameter("amount"));

		String paytype = "wechat_native_reapal";
//		if (!"wechat_native_reapal".equals(paytype)) {
//			returnModel.setCode(CodeContant.PayOrder);
//			returnModel.setMessage("订单生成失败,支付方式不正确");
//			return returnModel;
//		}
		String details = req.getParameter("details");
		String channel = req.getParameter("channel") == null ? "" : req.getParameter("channel");

		String orderNo = orderService.CreateOrderNo(os);
		if (StringUtils.isEmpty(orderNo)) {
			returnModel.setCode(CodeContant.PayOrderNo);
			returnModel.setMessage("订单号生成失败");
			return returnModel;
		}

		int creatAt = (int) (System.currentTimeMillis() / 1000);
		UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(srcUid, false);
		int registtime = userBaseInfoModel.getRegisttime();
		int ires = orderService.CreateOrder(Constant.reapal_payStatus_notifyUrl, orderNo, srcUid, dstuid, amount,
				creatAt, 0, os, paytype, false, "", details, userBaseInfoModel.getRegistchannel(), channel, registtime,
				returnModel);

		if (ires == 0) {
			returnModel.setCode(CodeContant.PayOrder);
			returnModel.setMessage("订单生成失败");
			return returnModel;
		}

		Map<String, Object> orderInfo = new HashMap<String, Object>();
		orderInfo.put("notify_url", Constant.reapal_payStatus_notifyUrl);
		orderInfo.put("out_trade_no", orderNo);
		orderInfo.put("sub_appid", Constant.webChat_appId);
		orderInfo.put("merchant_id", Constant.reapal_merchantId);
		orderInfo.put("body", details);
		orderInfo.put("total_fee", amount * 100); // 融宝金额单位是 分
		orderInfo.put("clientType", "0"); // 固定值 PAY_WX=0
		orderInfo.put("securityKey", Constant.reapal_securityKey);
		orderInfo.put("certificatePwd", Constant.reapal_certificatePwd);
		orderInfo.put("privateKey", Constant.reapal_privateKey);
		orderInfo.put("seller_email", "zhuanzhuanzhibo123@163.com");

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("orderInfo", AESCipher.aesEncryptString(JsonUtil.toJson(orderInfo), Constant.aesKey));
		data.put("paytype", "wechat_native_reapal");
		returnModel.setData(data);

		return returnModel;
	}

	/**
	 * alipay 支付
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/createAlipayOrder")
	public ReturnModel createAlipayOrder(HttpServletRequest req, HttpServletResponse response) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "dstuid", "amount", "paytype",
				"details")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime", "dstuid", "amount")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		// 校验码验证及token
		authToken(req, false);
		if (returnModel.getCode() != CodeContant.OK) {
			return returnModel;
		}
		String token = req.getParameter("token");
		int srcUid = authService.decryptToken(token, returnModel);
		if (srcUid <= 0) {
			return returnModel;
		}

		Integer dstuid = Integer.valueOf(req.getParameter("dstuid"));
		if (null == dstuid || dstuid.intValue() <= 0) {
			returnModel.setCode(CodeContant.PayOrder);
			returnModel.setMessage("创建订单失败，用户ID不正确!");
			return returnModel;
		}
		int os = Integer.valueOf(req.getParameter("os"));
		Float amount = Float.valueOf(req.getParameter("amount"));

		String paytype = req.getParameter("paytype");
		if (!"alipay".equals(paytype)) {
			returnModel.setCode(CodeContant.PayOrder);
			returnModel.setMessage("订单生成失败,支付方式不正确");
			return returnModel;
		}
		String details = req.getParameter("details");
		String channel = req.getParameter("channel") == null ? "" : req.getParameter("channel");

		String orderNo = orderService.CreateOrderNo(os);
		if (StringUtils.isEmpty(orderNo)) {
			returnModel.setCode(CodeContant.PayOrderNo);
			returnModel.setMessage("订单号生成失败");
			return returnModel;
		}

		int creatAt = (int) (System.currentTimeMillis() / 1000);
		UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(srcUid, false);
		int registtime = userBaseInfoModel.getRegisttime();
		int ires = orderService.CreateOrder(Constant.alipay_payStatus_notifyUrl, orderNo, srcUid, dstuid, amount,
				creatAt, 0, os, paytype, false, "", details, userBaseInfoModel.getRegistchannel(), channel, registtime,
				returnModel);

		if (ires == 0) {
			returnModel.setCode(CodeContant.PayOrder);
			returnModel.setMessage("订单生成失败");
			return returnModel;
		}

		AlipayClient alipayClient = new DefaultAlipayClient(Constant.alipay_getway, Constant.alipay_appId,
				Constant.alipay_app_privateKey, "json", Constant.defaultCharset, Constant.alipay_publicKey, "RSA2");
		// 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
		// SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
		model.setBody(details);// 交易描述
		model.setSubject("麦芽账户充值");// 标题
		model.setOutTradeNo(orderNo);// 商户网站唯一订单号
		model.setTimeoutExpress("5m");// 超时时间
		model.setTotalAmount(String.valueOf(amount)); // 单位元 保留两位小数
		model.setProductCode("QUICK_MSECURITY_PAY"); // 销售产品码，商家和支付宝签约的产品码，为固定值 QUICK_MSECURITY_PAY
		request.setBizModel(model);
		request.setNotifyUrl(Constant.alipay_payStatus_notifyUrl);

		String orderInfo = null;
		AlipayTradeAppPayResponse alipayResponse = null;
		try {
			// 这里和普通的接口调用不同，使用的是sdkExecute
			alipayResponse = alipayClient.sdkExecute(request);
			orderInfo = alipayResponse.getBody();
			log.info(String.format("%s对应支付宝订单信息：%s", orderNo, orderInfo));
			if (StringUtils.isBlank(orderInfo)) {
				returnModel.setCode(CodeContant.PayOrder);
				returnModel.setMessage("订单生成失败");
				return returnModel;
			}
		} catch (AlipayApiException e) {
			log.error("创建支付宝订单异常", e);
			returnModel.setCode(CodeContant.PayOrder);
			returnModel.setMessage("订单生成失败");
			return returnModel;
		}

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("orderInfo", orderInfo);
		returnModel.setData(AESCipher.aesEncryptString(JsonUtil.toJson(data), Constant.aesKey));

		return returnModel;
	}

	/**
	 * alipay 支付跳转html5
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/createH5AlipayOrder")
	public ReturnModel createH5AlipayOrder(HttpServletRequest req, HttpServletResponse response) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "dstuid", "amount", "paytype",
				"details")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime", "dstuid", "amount")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		// 校验码验证及token
		authToken(req, false);
		if (returnModel.getCode() != CodeContant.OK) {
			return returnModel;
		}
		String token = req.getParameter("token");
		int srcUid = authService.decryptToken(token, returnModel);
		if (srcUid <= 0) {
			return returnModel;
		}

		Integer dstuid = Integer.valueOf(req.getParameter("dstuid"));
		if (null == dstuid || dstuid.intValue() <= 0) {
			returnModel.setCode(CodeContant.PayOrder);
			returnModel.setMessage("创建订单失败，用户ID不正确。");
			return returnModel;
		}
		int os = Integer.valueOf(req.getParameter("os"));
		Float amount = Float.valueOf(req.getParameter("amount"));

		String paytype = req.getParameter("paytype");
		if (!"alipay".equals(paytype)) {
			returnModel.setCode(CodeContant.PayOrder);
			returnModel.setMessage("订单生成失败,支付方式不正确");
			return returnModel;
		}
		String details = req.getParameter("details");
		String channel = req.getParameter("channel") == null ? "" : req.getParameter("channel");

		String orderNo = orderService.CreateOrderNo(os);
		if (StringUtils.isEmpty(orderNo)) {
			returnModel.setCode(CodeContant.PayOrderNo);
			returnModel.setMessage("订单号生成失败");
			return returnModel;
		}

		int creatAt = (int) (System.currentTimeMillis() / 1000);
		UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(srcUid, false);
		int registtime = userBaseInfoModel.getRegisttime();
		int ires = orderService.CreateOrder(Constant.alipay_payStatus_notifyUrl, orderNo, srcUid, dstuid, amount,
				creatAt, 0, os, paytype, false, "", details, userBaseInfoModel.getRegistchannel(), channel, registtime,
				returnModel);

		if (ires == 0) {
			returnModel.setCode(CodeContant.PayOrder);
			returnModel.setMessage("订单生成失败(1)");
			return returnModel;
		}

		returnModel.setData(orderNo);
		return returnModel;
	}

	@RequestMapping(value = "/getOrderAndPay")
	public void getOrderAndPay(HttpServletRequest httpRequest, HttpServletResponse response) {
		String orderNo = httpRequest.getParameter("orderno");

		if (StringUtils.isBlank(orderNo)) {
			returnModel.setCode(CodeContant.PayOrder);
			returnModel.setMessage("参数错误");
			ResponseUtils.renderHtml(response, JsonUtil.toJson(returnModel));
			return;
		}

		PayOrderModel payOrderModel = orderService.getPayOrderByOrderNo(orderNo);

		if (null == payOrderModel) {
			returnModel.setCode(CodeContant.PayOrder);
			returnModel.setMessage("获取订单失败");
			ResponseUtils.renderHtml(response, JsonUtil.toJson(returnModel));
			return;
		}

		AlipayClient alipayClient = new DefaultAlipayClient(Constant.alipay_getway, Constant.alipay_appId,
				Constant.alipay_app_privateKey, "json", Constant.defaultCharset, Constant.alipay_publicKey, "RSA2");
		// 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
		AlipayTradeWapPayRequest aliPayRequest = new AlipayTradeWapPayRequest();
		// SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
		// AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
		// model.setBody(payOrderModel.getDetails());//交易描述
		// model.setSubject("麦芽直播充值金币");//标题
		// model.setOutTradeNo(orderNo);//商户网站唯一订单号
		// model.setTimeoutExpress("5m");//超时时间
		// model.setTotalAmount(String.valueOf(payOrderModel.getAmount())); //单位元 保留两位小数
		// model.setTotalAmount("0.01"); //单位元 保留两位小数(测试 0.01)
		// model.setProductCode("QUICK_MSECURITY_PAY"); //销售产品码，商家和支付宝签约的产品码，为固定值 QUICK_MSECURITY_PAY
		// aliPayRequest.setBizModel(model);

		Map<String, String> bizContent = new HashMap<String, String>();
		bizContent.put("out_trade_no", orderNo);
		bizContent.put("total_amount", String.valueOf(payOrderModel.getAmount()));
		bizContent.put("subject", "麦芽账户充值");
		bizContent.put("product_code", "QUICK_WAP_PAY");

		aliPayRequest.setBizContent(JsonUtil.toJson(bizContent));

		aliPayRequest.setNotifyUrl(Constant.alipay_payStatus_notifyUrl);
		aliPayRequest.setReturnUrl(Constant.alipay_payStatus_returnUrl);

		String orderInfo = null;
		AlipayTradeWapPayResponse alipayResponse = null;
		try {
			alipayResponse = alipayClient.pageExecute(aliPayRequest);
			orderInfo = alipayResponse.getBody();
			log.info(String.format("%s对应h5支付宝订单信息：%s", orderNo, orderInfo));
			if (StringUtils.isBlank(orderInfo)) {
				returnModel.setCode(CodeContant.PayOrder);
				returnModel.setMessage("订单生成失败(1)");
				ResponseUtils.renderHtml(response, JsonUtil.toJson(returnModel));
				return;
			}
		} catch (AlipayApiException e) {
			log.error("创建h5支付宝订单异常", e);
			returnModel.setCode(CodeContant.PayOrder);
			returnModel.setMessage("订单生成失败(2)");
			ResponseUtils.renderHtml(response, JsonUtil.toJson(returnModel));
			return;
		}

		response.setContentType("text/html;charset=UTF-8");
		try {
			response.getWriter().write(orderInfo);// 直接将完整的表单html输出到页面
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			log.error("输出表单失败", e);
			returnModel.setCode(CodeContant.PayOrder);
			returnModel.setMessage("订单生成失败(4)");
			e.printStackTrace();
			ResponseUtils.renderHtml(response, JsonUtil.toJson(returnModel));
		}
	}

	/**
	 * 获取订单
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/getOrderByNo", method = RequestMethod.GET)
	public ReturnModel getOrderByNo(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "orderno")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		// 校验码验证及token
		authToken(req, false);
		if (returnModel.getCode() != CodeContant.OK) {
			return returnModel;
		}

		return getOrderByNoNew(req, resp);
	}

	@RequestMapping(value = "/getOrderByNoNew", method = RequestMethod.GET)
	private ReturnModel getOrderByNoNew(HttpServletRequest req, HttpServletResponse resp) {
		String orderNo = req.getParameter("orderno");
		PayOrderModel payOrderModel = orderService.getPayOrderByOrderNo(orderNo);
		if (payOrderModel == null) {
			returnModel.setCode(CodeContant.PayOrderExits);
			returnModel.setMessage("订单不存在");
			return returnModel;
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderno", payOrderModel.getOrderId());
		map.put("paytype", payOrderModel.getPaytype());
		map.put("amount", payOrderModel.getAmount());
		map.put("paytime", payOrderModel.getPaytime());
		map.put("status", payOrderModel.getStatus());

		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("data", map);
		returnModel.setData(returnMap);

		return returnModel;
	}

	/**
	 * 
	 * 选微信支付、统一下单(修改次接口注意，要修改两处，创建订单和去支付@see payUNPayOrder)
	 */
	@RequestMapping(value = "/createUNPayOrder", method = RequestMethod.GET)
	public ReturnModel createWechatOrder(HttpServletRequest req,HttpServletResponse resp) {
		String wxchannle = RelationRedisService.getInstance().getWechatPayChanel();
		if("unpay".equals(wxchannle)) {
			return createUNPayOrder(req);
		}else if("nativeh5".equals(wxchannle)){
			return createWechatPayOrder(req,"wechat_h5",Constant.wechat_h5pay_notify_url);
		}else {//默认使用unpay
			String os = req.getParameter("os");
			String ver = req.getParameter("ver");
			//ver 9 app版本号
			//nativereapal 融宝原生支付
			//os 1 android
			if(StringUtils.isNotBlank(ver) && "1".equals(os) && Integer.valueOf(ver).intValue() > 9 && "nativereapal".equals(wxchannle)) { 
				return createReapalOrder(req, resp);
			}
			return createWechatPayOrder(req,"wechat_h5",Constant.wechat_h5pay_notify_url);
		}
	}
	//@RequestMapping(value = "/createUNPayOrder", method = RequestMethod.GET)
	public ReturnModel createUNPayOrder(HttpServletRequest req) {
		try {
			if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "dstuid", "amount", "paytype",
					"details")) {
				returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
				returnModel.setMessage("缺少参数或参数为空");
				return returnModel;
			}
			if (!ParamHandleUtils.isInt(req, "os", "reqtime", "dstuid")) {
				returnModel.setCode(CodeContant.ConParamTypeIsErr);
				returnModel.setMessage("参数类型错误");
				return returnModel;
			}
			// 校验码验证及token
			authToken(req, false);
			if (returnModel.getCode() != CodeContant.OK) {
				return returnModel;
			}
			String token = req.getParameter("token");
			int srcUid = authService.decryptToken(token, returnModel);
			if (srcUid <= 0) {
				return returnModel;
			}

			Integer dstuid = Integer.valueOf(req.getParameter("dstuid"));
			if (null == dstuid || dstuid.intValue() <= 0) {
				returnModel.setCode(CodeContant.PayOrder);
				returnModel.setMessage("创建订单失败，用户ID不正确");
				return returnModel;
			}
			int os = Integer.valueOf(req.getParameter("os"));
			Float amount = Float.valueOf(req.getParameter("amount"));
			
			String paytype = "unpay_weixin";
			/**
			String paytype = req.getParameter("paytype");
			if (!"unpay_weixin".equals(paytype)) {
				returnModel.setCode(CodeContant.PayOrder);
				returnModel.setMessage("订单生成失败,支付方式不正确");
				return returnModel;
			}
			**/
			String details = req.getParameter("details");
			String channel = req.getParameter("channel") == null ? "" : req.getParameter("channel");

			String orderNo = orderService.CreateOrderNo(os);
			if (StringUtils.isEmpty(orderNo)) {
				returnModel.setCode(CodeContant.PayOrderNo);
				returnModel.setMessage("订单号生成失败");
				return returnModel;
			}

			int creatAt = (int) (System.currentTimeMillis() / 1000);
			UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(srcUid, false);
			int registtime = userBaseInfoModel.getRegisttime();
			int ires = orderService.CreateOrder(Constant.unpay_payStatus_notifyUrl, orderNo, srcUid, dstuid, amount,
					creatAt, 0, os, paytype, false, "", details, userBaseInfoModel.getRegistchannel(), channel,
					registtime, returnModel);
			if (ires == 0) {
				returnModel.setCode(CodeContant.PayOrder);
				returnModel.setMessage("订单生成失败");
				return returnModel;
			}
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("orderNo", orderNo);
			data.put("paytype", paytype);
			returnModel.setData(data);
			return returnModel;
		} catch (Exception e) {
			log.error("创建UNPayOrder异常", e);
			returnModel.setCode(CodeContant.PayOrderExits);
			returnModel.setMessage("订单不存在");
			return returnModel;
		}
	}

	/**
	 * 修改次接口请注意，要修改两个地方（创建订单和去支付） @see createWechatOrder
	 * @param httpRequest
	 * @param response
	 */
	@RequestMapping(value = "/payUNPayOrder")
	public void payUNPayOrder(HttpServletRequest httpRequest, HttpServletResponse response) {
		String wxchannle = RelationRedisService.getInstance().getWechatPayChanel();
		if("unpay".equals(wxchannle)) {
			paymentUNPayOrder(httpRequest, response);
		}else if("nativeh5".equals(wxchannle)) {
			payWxH5PayOrder(httpRequest, response);
		}else {//默认使用weixin原生h5 
			payWxH5PayOrder(httpRequest, response);
//			//校验是什么订单
//			{
//				String orderNo = httpRequest.getParameter("orderNo");
//				if (StringUtils.isBlank(orderNo)) {
//					returnModel.setCode(CodeContant.CONPARAMSEXCEPT);
//					returnModel.setMessage("参数错误");
//					ResponseUtils.renderHtml(response, JsonUtil.toJson(returnModel));
//					return;
//				}
//
//				PayOrderModel payOrderModel = orderService.getPayOrderByOrderNo(orderNo);
//				if (null == payOrderModel) {
//					returnModel.setCode(CodeContant.PayOrder);
//					returnModel.setMessage("获取订单失败");
//					ResponseUtils.renderHtml(response, JsonUtil.toJson(returnModel));
//					return;
//				}
//				if("wechat_h5".equals(payOrderModel.getPaytype())) {//wechat_h5
//					payWxH5PayOrder(httpRequest, response);
//				}else if("unpay_weixin".equals(payOrderModel.getPaytype())) {//unpay_weixin
//					paymentUNPayOrder(httpRequest, response);
//				}
//			}
		}
	}
	//@RequestMapping(value = "/payUNPayOrder")
	public void paymentUNPayOrder(HttpServletRequest httpRequest, HttpServletResponse response) {
		try {
			String orderNo = httpRequest.getParameter("orderNo");
			if (StringUtils.isBlank(orderNo)) {
				returnModel.setCode(CodeContant.CONPARAMSEXCEPT);
				returnModel.setMessage("参数错误");
				ResponseUtils.renderHtml(response, JsonUtil.toJson(returnModel));
				return;
			}

			PayOrderModel payOrderModel = orderService.getPayOrderByOrderNo(orderNo);
			if (null == payOrderModel) {
				returnModel.setCode(CodeContant.PayOrder);
				returnModel.setMessage("获取订单失败");
				ResponseUtils.renderHtml(response, JsonUtil.toJson(returnModel));
				return;
			}
			if (payOrderModel.getStatus() != 0) {
				returnModel.setCode(CodeContant.PayOrder);
				returnModel.setMessage("获取订单失败");
				ResponseUtils.renderHtml(response, JsonUtil.toJson(returnModel));
				return;
			}

			long orderAmount = (long) (payOrderModel.getAmount() * 100);
			StringBuilder sb = new StringBuilder();
			sb.append("customerid=" + Constant.unpay_merchantId);
			sb.append("&sdcustomno=" + orderNo);
			sb.append("&orderAmount=" + orderAmount);
			sb.append("&cardno=41");
			sb.append("&noticeurl=" + Constant.unpay_payStatus_notifyUrl);
			sb.append("&backurl=" + Constant.unpay_backurl);
			sb.append(Constant.unpay_key);

			String sign = MD5Encrypt.encrypt(sb.toString()).toUpperCase();

			Map<String, Object> queryMap = new HashMap<String, Object>();
			queryMap.put("customerid", Constant.unpay_merchantId);
			queryMap.put("sdcustomno", orderNo);
			queryMap.put("orderAmount", orderAmount);
			queryMap.put("cardno", 41);
			queryMap.put("noticeurl", Constant.unpay_payStatus_notifyUrl);
			queryMap.put("backurl", Constant.unpay_backurl);
			queryMap.put("mark", orderAmount);
			queryMap.put("remarks", "麦芽账户充值");
			queryMap.put("sign", sign);
			queryMap.put("zftype", 3);
			queryMap.put("loIp", getIpAddress(httpRequest));

			log.debug("UNPAY支付订单:" + queryMap);
			HttpResponse<JsonNode> unpayResult = Unirest.get(Constant.unpay_api_payPayMegerHandler)
					.queryString(queryMap).asJson();
			JSONObject jsonObject = unpayResult.getBody().getObject();
			log.debug("UNPAY支付返回:" + jsonObject);

			StringBuilder result = new StringBuilder();
			// 1:成功 ;其他失败
			if ("1".equals(jsonObject.getString("state")) && StringUtils.isNotBlank(jsonObject.getString("url"))) {
				result.append(jsonObject.getString("url"));
				httpRequest.setAttribute("url", result.toString());
				httpRequest.getRequestDispatcher("/jump_url.jsp").forward(httpRequest, response);
				return;
			} else {
				// 直接返回失败原因
				result.append(jsonObject.getString("errmsg"));
				ResponseUtils.renderHtml(response, result.toString());
				return;
			}
		} catch (Exception e) {
			log.error("UNPay订单生产失败", e);
			returnModel.setCode(CodeContant.PayOrder);
			returnModel.setMessage("订单生成失败");
			e.printStackTrace();
			ResponseUtils.renderHtml(response, JsonUtil.toJson(returnModel));
		}
	}

	//@RequestMapping(value = "/createWxH5PayOrder", method = RequestMethod.GET)
	public ReturnModel createWechatPayOrder(HttpServletRequest req,String paytype,String notifyUrl) {
		try {
			if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "dstuid", "amount", "paytype",
					"details")) {
				returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
				returnModel.setMessage("缺少参数或参数为空");
				return returnModel;
			}
			if (!ParamHandleUtils.isInt(req, "os", "reqtime", "dstuid")) {
				returnModel.setCode(CodeContant.ConParamTypeIsErr);
				returnModel.setMessage("参数类型错误");
				return returnModel;
			}
			// 校验码验证及token
			authToken(req, false);
			if (returnModel.getCode() != CodeContant.OK) {
				return returnModel;
			}
			String token = req.getParameter("token");
			int srcUid = authService.decryptToken(token, returnModel);
			if (srcUid <= 0) {
				return returnModel;
			}

			Integer dstuid = Integer.valueOf(req.getParameter("dstuid"));
			if (null == dstuid || dstuid.intValue() <= 0) {
				returnModel.setCode(CodeContant.PayOrder);
				returnModel.setMessage("创建订单失败，用户ID不正确");
				return returnModel;
			}
			int os = Integer.valueOf(req.getParameter("os"));
			Float amount = Float.valueOf(req.getParameter("amount"));
			
			//String paytype = "wechat_h5";
			/**
			String paytype = req.getParameter("paytype");
			if (!"wechat_h5".equals(paytype)) {
				returnModel.setCode(CodeContant.PayOrder);
				returnModel.setMessage("订单生成失败,支付方式不正确");
				return returnModel;
			}
			**/
			String details = req.getParameter("details");
			String channel = req.getParameter("channel") == null ? "" : req.getParameter("channel");

			String orderNo = orderService.CreateOrderNo(os);
			if (StringUtils.isEmpty(orderNo)) {
				returnModel.setCode(CodeContant.PayOrderNo);
				returnModel.setMessage("订单号生成失败");
				return returnModel;
			}

			int creatAt = (int) (System.currentTimeMillis() / 1000);
			UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(srcUid, false);
			int registtime = userBaseInfoModel.getRegisttime();
			int ires = orderService.CreateOrder(notifyUrl, orderNo, srcUid, dstuid, amount,
					creatAt, 0, os, paytype, false, "", details, userBaseInfoModel.getRegistchannel(), channel,
					registtime, returnModel);
			if (ires == 0) {
				returnModel.setCode(CodeContant.PayOrder);
				returnModel.setMessage("订单生成失败");
				return returnModel;
			}
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("orderNo", orderNo);
			data.put("paytype", paytype);
			returnModel.setData(data);
			return returnModel;
		} catch (Exception e) {
			log.error("创建createWxH5PayOrder异常", e);
			returnModel.setCode(CodeContant.PayOrderExits);
			returnModel.setMessage("订单不存在");
			return returnModel;
		}
	}

	//@RequestMapping(value = "/payWxH5PayOrder")
	public void payWxH5PayOrder(HttpServletRequest httpRequest, HttpServletResponse response) {
		try {
			String orderNo = httpRequest.getParameter("orderNo");
			if (StringUtils.isBlank(orderNo)) {
				returnModel.setCode(CodeContant.CONPARAMSEXCEPT);
				returnModel.setMessage("参数错误");
				ResponseUtils.renderHtml(response, JsonUtil.toJson(returnModel));
				return;
			}

			PayOrderModel payOrderModel = orderService.getPayOrderByOrderNo(orderNo);
			if (null == payOrderModel) {
				returnModel.setCode(CodeContant.PayOrder);
				returnModel.setMessage("获取订单失败");
				ResponseUtils.renderHtml(response, JsonUtil.toJson(returnModel));
				return;
			}
			if (payOrderModel.getStatus() != 0) {
				returnModel.setCode(CodeContant.PayOrder);
				returnModel.setMessage("获取订单失败");
				ResponseUtils.renderHtml(response, JsonUtil.toJson(returnModel));
				return;
			}
			long orderAmount = (long) (payOrderModel.getAmount() * 100);

			Map<String, String> xmlMap = new HashMap<String, String>();
			xmlMap.put("appid", Constant.wechat_h5pay_appid);
			xmlMap.put("mch_id", Constant.wechat_h5pay_mch_id);
			xmlMap.put("nonce_str", RandomUtil.generateNonceStr());
			xmlMap.put("body", "麦芽账户充值");
			xmlMap.put("out_trade_no", orderNo);
			xmlMap.put("total_fee", orderAmount + "");
			xmlMap.put("spbill_create_ip", getIpAddress(httpRequest));
			xmlMap.put("notify_url", Constant.wechat_h5pay_notify_url);
			xmlMap.put("trade_type", "MWEB");
			Map<String, Object> sceneInfo = new HashMap<String, Object>();
			Map<String, Object> h5Info = new HashMap<String, Object>();
			h5Info.put("type", "Wap");
			h5Info.put("wap_url", Constant.wechat_h5pay_wap_url);
			h5Info.put("wap_name", "麦芽账户充值");
			sceneInfo.put("h5_info", h5Info);
			xmlMap.put("scene_info", JsonUtil.toJson(sceneInfo));
			// sign
			Set<String> keySet = xmlMap.keySet();
			String[] keyArray = keySet.toArray(new String[keySet.size()]);
			Arrays.sort(keyArray);
			StringBuilder sb = new StringBuilder();
			for (String k : keyArray) {
				if (xmlMap.get(k).trim().length() > 0) { // 参数值为空，则不参与签名
					sb.append(k).append("=").append(xmlMap.get(k).trim()).append("&");
				}
			}
			String signKey = Constant.wechat_h5pay_signkey;
			sb.append("key=").append(signKey);
			String sign = DigestUtils.md5Hex(sb.toString()).toUpperCase();
			xmlMap.put("sign", sign);
			String sendXml = XmlUtil.mapToXml(xmlMap);

			Map<String, String> headers = new HashMap<String,String>();
			headers.put("Content-Type", "text/xml");
		    String USER_AGENT = "WXPaySDK/3.0.9" +
		            " (" + System.getProperty("os.arch") + " " + System.getProperty("os.name") + " " + System.getProperty("os.version") +
		            ") Java/" + System.getProperty("java.version") + " HttpClient/" + HttpClient.class.getPackage().getImplementationVersion();
			headers.put("User-Agent", USER_AGENT + " " + Constant.wechat_h5pay_mch_id);
			
			String returnXml = HttpKit.post(Constant.wechat_h5pay_unifiedOrder,null,sendXml,headers);
			Map<String, String> xmlResult = XmlUtil.xmlToMap(returnXml);
			String return_code = xmlResult.get("return_code");
			if(!StringUtils.equalsIgnoreCase("SUCCESS", return_code)) {
				String returnMsg = xmlResult.get("return_msg");
				log.error("创建微信H5订单失败，错误原因：" + returnMsg);
				ResponseUtils.renderHtml(response, returnMsg);
				return;
			}
			if(!StringUtils.equalsIgnoreCase("SUCCESS", xmlResult.get("result_code"))) {
				String returnMsg  = "错误码："+xmlResult.get("err_code")+"错误原因："+xmlResult.get("err_code_des");
				log.error("创建微信H5订单失败，" + returnMsg);
				ResponseUtils.renderHtml(response, returnMsg);
				return;
			}
			String mweb_url = xmlResult.get("mweb_url")+"&redirect_url="+URLEncoder.encode(Constant.wechat_h5pay_redirectUrl+"?orderNo="+orderNo,"utf-8");
			httpRequest.setAttribute("url", mweb_url);
			httpRequest.getRequestDispatcher("/jump_url.jsp").forward(httpRequest, response);
			return;
		} catch (Exception e) {
			log.error("payWxH5PayOrder订单生产失败", e);
			returnModel.setCode(CodeContant.PayOrder);
			returnModel.setMessage("订单生成失败");
			e.printStackTrace();
			ResponseUtils.renderHtml(response, JsonUtil.toJson(returnModel));
		}
	}
	
	/**
	 * 融宝快捷支付-创建订单
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/createReapalQuickPaymentOrder")
	public ReturnModel createReapalQuickPaymentOrder(HttpServletRequest req, HttpServletResponse response) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "dstuid", "amount", "paytype",
				"details")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime", "dstuid", "amount")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		// 校验码验证及token
		authToken(req, false);
		if (returnModel.getCode() != CodeContant.OK) {
			return returnModel;
		}
		String token = req.getParameter("token");
		int srcUid = authService.decryptToken(token, returnModel);
		if (srcUid <= 0) {
			return returnModel;
		}

		Integer dstuid = Integer.valueOf(req.getParameter("dstuid"));
		if (null == dstuid || dstuid.intValue() <= 0) {
			returnModel.setCode(CodeContant.PayOrder);
			returnModel.setMessage("创建订单失败，用户ID不正确。");
			return returnModel;
		}
		int os = Integer.valueOf(req.getParameter("os"));
		Float amount = Float.valueOf(req.getParameter("amount"));

		String paytype = req.getParameter("paytype");
		if (!"reapal_quick_payment".equals(paytype)) {
			returnModel.setCode(CodeContant.PayOrder);
			returnModel.setMessage("订单生成失败,支付方式不正确");
			return returnModel;
		}
		String details = req.getParameter("details");
		String channel = req.getParameter("channel") == null ? "" : req.getParameter("channel");

		String orderNo = orderService.CreateOrderNo(os);
		if (StringUtils.isEmpty(orderNo)) {
			returnModel.setCode(CodeContant.PayOrderNo);
			returnModel.setMessage("订单号生成失败");
			return returnModel;
		}

		int creatAt = (int) (System.currentTimeMillis() / 1000);
		UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(srcUid, false);
		int registtime = userBaseInfoModel.getRegisttime();
		int ires = orderService.CreateOrder(null, orderNo, srcUid, dstuid, amount,
				creatAt, 0, os, paytype, false, "", details, userBaseInfoModel.getRegistchannel(), channel, registtime,
				returnModel);
		if (ires == 0) {
			returnModel.setCode(CodeContant.PayOrder);
			returnModel.setMessage("订单生成失败(1)");
			return returnModel;
		}

		Map<String,String> map = new HashMap<String,String>();
		map.put("orderNo", orderNo);
		returnModel.setData(map);
		return returnModel;
	}
	
	/**
	 * 融宝-快捷支付-打开网页去支付
	 * @param httpRequest
	 * @param response
	 */
	@RequestMapping("/payReapalQuickPaymentOrder")
	public void payReapalQuickPaymentOrder(HttpServletRequest request, HttpServletResponse response) {
		try {
			String orderNo = request.getParameter("orderNo");
			if (StringUtils.isBlank(orderNo)) {
				returnModel.setCode(CodeContant.CONPARAMSEXCEPT);
				returnModel.setMessage("参数错误");
				ResponseUtils.renderHtml(response, JsonUtil.toJson(returnModel));
				return;
			}

			PayOrderModel payOrderModel = orderService.getPayOrderByOrderNo(orderNo);
			if (null == payOrderModel) {
				returnModel.setCode(CodeContant.PayOrder);
				returnModel.setMessage("获取订单失败");
				ResponseUtils.renderHtml(response, JsonUtil.toJson(returnModel));
				return;
			}
			if (payOrderModel.getStatus() != 0) {
				returnModel.setCode(CodeContant.PayOrder);
				returnModel.setMessage("获取订单失败");
				ResponseUtils.renderHtml(response, JsonUtil.toJson(returnModel));
				return;
			}
			long orderAmount = (long) (payOrderModel.getAmount() * 100);
			
			String seller_email = Constant.reapal_quick_payment_seller_email;
			String merchant_id = Constant.reapal_quick_payment_merchant_id;
			String notify_url = Constant.reapal_quick_payment_notify_url;
			String return_url = Constant.reapal_quick_payment_return_url;
			String transtime = DateUtils.dateFormat(new Date(), "yyyy-MM-dd HH:mm:ss");//交易时间
			String currency = "156";
			String member_id = DateUtils.dateFormat(new Date(), "yyyyMMddHHmmss");
			String member_ip = getIpAddress(request);
			String terminal_type = "mobile";
			String terminal_info = UUID.randomUUID().toString();
			String key = Constant.reapal_quick_payment_key;
			String sign_type = Constant.reapal_quick_payment_sign_type;
	        String title = "麦芽账户充值";
	        String body = "麦芽账户充值";
			String sHtmlText = RongpayService.BuildFormH5(seller_email, merchant_id, notify_url,  return_url,  transtime,  currency,
	    			 member_id,  member_ip,  terminal_type,  terminal_info, key,  sign_type,
	    			 orderNo,  title,  body,  orderAmount+"");
			request.setAttribute("submitForm", sHtmlText);
			request.getRequestDispatcher("/reapal_quick_payment_jump.jsp").forward(request, response);
			return;
		} catch (Exception e) {
			log.error("payQuickPaymentOrder订单生产失败", e);
			returnModel.setCode(CodeContant.PayOrder);
			returnModel.setMessage("订单生成失败");
			e.printStackTrace();
			ResponseUtils.renderHtml(response, JsonUtil.toJson(returnModel));
		}
	}
	

	private static String getIpAddress(HttpServletRequest request) {
		String ipAddress = request.getHeader("x-forwarded-for");
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknow".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
				// 根据网卡获取本机配置的IP地址
				InetAddress inetAddress = null;
				try {
					inetAddress = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				ipAddress = inetAddress.getHostAddress();
			}
		}
		// 对于通过多个代理的情况，第一个IP为客户端真实的IP地址，多个IP按照','分割
		if (null != ipAddress && ipAddress.length() > 15) {
			if (ipAddress.indexOf(",") > 0) {
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
			}
		}
		return ipAddress;
	}
	
}