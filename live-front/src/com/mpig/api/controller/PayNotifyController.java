package com.mpig.api.controller;

import java.io.BufferedReader;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.AlipaySignature;
import com.mpig.api.reapal.DecipherH5;
import com.mpig.api.reapal.Md5Utils;
import com.mpig.api.reapal.RechargeNotifyDto;
import com.mpig.api.reapal.ResponseUtils;
import com.mpig.api.service.IOrderService;
import com.mpig.api.utils.Constant;
import com.mpig.api.utils.DateUtils;
import com.mpig.api.utils.JsonUtil;
import com.mpig.api.utils.MD5Encrypt;
import com.mpig.api.utils.XmlUtil;

@RestController
@Scope("prototype")
@RequestMapping("/notify")
public class PayNotifyController extends BaseController {

	private static final Logger logger = Logger.getLogger(PayNotifyController.class);

	@Autowired
	private IOrderService orderService;

	/**
	 * 充值回调
	 */
	@RequestMapping("/recharge")
	public void rechargeCallback(HttpServletRequest request, HttpServletResponse response) {
		Long reqFlag = System.currentTimeMillis();
		try {
			logger.info(reqFlag + "<融宝支付充值回调->");
			Map<String, String[]> parameterMap = request.getParameterMap();
			Iterator<String> it = parameterMap.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				logger.info("参数:" + key + "=" + parameterMap.get(key));
			}

			// 获取回调参数
			String paramStr = request.getParameter("data");

			if (StringUtils.isBlank(paramStr)) {
				logger.info("<融宝支付充值回调-> [data]参数为空");
				ResponseUtils.renderHtml(response, "fail");
				return;
			}

			logger.info("<融宝支付充值回调-> [data]" + JsonUtil.toJson(paramStr));

			RechargeNotifyDto params = JsonUtil.toBean(paramStr, RechargeNotifyDto.class);
			if (null == params) {
				logger.info("<融宝支付充值回调-> [params]为空");
				ResponseUtils.renderHtml(response, "fail");
				return;
			}

			// 按规则生成签名
			Map<String, String> signMap = new HashMap<String, String>();
			signMap.put("order_time", params.getOrder_time());
			signMap.put("order_no", params.getOrder_no());
			signMap.put("trade_no", params.getTrade_no());
			signMap.put("total_fee", params.getTotal_fee());
			signMap.put("status", params.getStatus());
			signMap.put("result_code", params.getResult_code());
			signMap.put("result_msg", params.getResult_msg());
			signMap.put("notify_id", params.getNotify_id());
			signMap.put("sign", params.getSign());

			String buildMysign = Md5Utils.BuildMysign(signMap, Constant.reapal_securityKey);

			// 校验签名
			if (!params.getSign().equals(buildMysign)) {
				logger.error(reqFlag + "<融宝支付充值回调->:签名校验失败");
				ResponseUtils.renderHtml(response, "fail");
				return;
			}

			double imoney = new BigDecimal(params.getTotal_fee())
					.divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
			Double zhutous = imoney * Integer.parseInt(Constant.recharge_multiple);// 一块钱等于100个金币
			int zhutou = zhutous.intValue();
			int status = "TRADE_FINISHED".equals(params.getStatus()) ? 2 : 3;// 支付状态 =0生成支付单 =1以到支付平台等待支付 =2已支付 =3取消

			// 更新订单状态
			logger.info("params:" + JsonUtil.toJson(params));

			String tradeNo = StringUtils.isBlank(params.getTrade_no()) ? "" : params.getTrade_no();

			int ires = orderService.updPayStatus(params.getOrder_no(), tradeNo, Double.valueOf(imoney), zhutou, status,
					DateUtils.getTimeStamp(params.getOrder_time(), "yyyy-MM-dd HH:mm:ss"));
			if (ires != 1) {
				logger.info(reqFlag + "<融宝支付充值回调->更新状态失败:" + request.getParameterMap().toString());
				ResponseUtils.renderHtml(response, "fail");
				return;
			}
			ResponseUtils.renderHtml(response, "success");
		} catch (Exception e) {
			logger.error(reqFlag + "<融宝支付充值回调-> 更新状态订单状态异常:", e);
			ResponseUtils.renderHtml(response, "fail");
		}
	}

	/**
	 * 充值回调
	 */
	@RequestMapping("/alinotify")
	public void alinotify(HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, String> params = new HashMap<String, String>();
			Map<String, String[]> requestParams = request.getParameterMap();
			for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
				}
				params.put(name, valueStr);
			}
			logger.info("支付宝充值回调：" + params);
			boolean flag = AlipaySignature.rsaCheckV1(params, Constant.alipay_publicKey, Constant.defaultCharset,
					"RSA2");
			if (!flag) {
				logger.info("支付宝验签失败,系统不做任何处理");
				ResponseUtils.renderHtml(response, "fail");
				return;
			}
			String trade_status = params.get("trade_status");
			String total_amount = params.get("total_amount");
			String trade_no = params.get("trade_no");
			String out_trade_no = params.get("out_trade_no");
			String gmt_payment = params.get("gmt_payment");
			if (StringUtils.isBlank(trade_status)) {
				ResponseUtils.renderHtml(response, "fail");
				return;
			}
			if (StringUtils.isBlank(total_amount)) {
				ResponseUtils.renderHtml(response, "fail");
				return;
			}
			if (StringUtils.isBlank(trade_no)) {
				ResponseUtils.renderHtml(response, "fail");
				return;
			}
			if (StringUtils.isBlank(out_trade_no)) {
				ResponseUtils.renderHtml(response, "fail");
				return;
			}
			if (StringUtils.isBlank(gmt_payment)) {
				ResponseUtils.renderHtml(response, "fail");
				return;
			}
			double imoney = Double.parseDouble(total_amount);
			Double temp = imoney * Integer.parseInt(Constant.recharge_multiple);
			int zhutou = temp.intValue();

			if (Constant.AlipayTradeStatus.WAIT_BUYER_PAY.equals(trade_status)) {
				int ires = orderService.updPayStatus(out_trade_no, trade_no, imoney, zhutou, 1);
				if (ires == 0) {
					logger.info("支付宝回调更新订单状态失败");
					ResponseUtils.renderHtml(response, "fail");
					return;
				}
			} else if (Constant.AlipayTradeStatus.TRADE_CLOSED.equals(trade_status)) {
				int ires = orderService.updPayStatus(out_trade_no, trade_no, imoney, zhutou, 3,
						DateUtils.getTimeStamp(gmt_payment, "yyyy-MM-dd HH:mm:ss"));
				if (ires == 0) {
					logger.info("支付宝回调更新订单状态失败");
					ResponseUtils.renderHtml(response, "fail");
					return;
				}
			} else if (Constant.AlipayTradeStatus.TRADE_SUCCESS.equals(trade_status)) {
				int ires = orderService.updPayStatus(out_trade_no, trade_no, imoney, zhutou, 2,
						DateUtils.getTimeStamp(gmt_payment, "yyyy-MM-dd HH:mm:ss"));
				if (ires == 0) {
					logger.info("支付宝回调更新订单状态失败");
					ResponseUtils.renderHtml(response, "fail");
					return;
				}
			}
			ResponseUtils.renderHtml(response, "success");
		} catch (Exception e) {
			logger.error("支付宝回调异常:", e);
			ResponseUtils.renderHtml(response, "fail");
		}
	}

	@RequestMapping("/unpayRecharge")
	public void unpayRecharge(HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, String> params = new HashMap<String, String>();
			Map<String, String[]> requestParams = request.getParameterMap();
			for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
				}
				params.put(name, valueStr);
			}
			logger.info("UNPay充值回调：" + params);

			String state = params.get("state"); // 1: 成功; 2:失败
			String sdcustomno = params.get("sdcustomno");
			String mark = params.get("mark");
			String sd51no = params.get("sd51no");
			String ordermoney = params.get("ordermoney");

			String sign = params.get("sign");
			String resign = params.get("resign");

			StringBuilder str = new StringBuilder();
			str.append("customerid=" + Constant.unpay_merchantId);
			str.append("&sd51no=" + sd51no);
			str.append("&sdcustomno=" + sdcustomno);
			str.append("&mark=" + mark);
			str.append("&key=" + Constant.unpay_key);

			String signStr = MD5Encrypt.encrypt(str.toString()).toUpperCase();

			if (!signStr.equals(sign)) {
				logger.info("UNPay回调sign失败");
				ResponseUtils.renderHtml(response, "<result>1</result>");
				return;
			}
			StringBuilder str2 = new StringBuilder();
			str2.append("sign=" + signStr);
			str2.append("&customerid=" + Constant.unpay_merchantId);
			str2.append("&ordermoney=" + ordermoney);
			str2.append("&sd51no=" + sd51no);
			str2.append("&state=" + state);
			str2.append("&key=" + Constant.unpay_key);

			String resignStr = MD5Encrypt.encrypt(str2.toString()).toUpperCase();

			if (!resignStr.equals(resign)) {
				logger.info("UNPay回调resignStr失败");
				ResponseUtils.renderHtml(response, "<result>1</result>");
				return;
			}

			double imoney = Double.parseDouble(ordermoney);
			Double zhutous = imoney * Integer.parseInt(Constant.recharge_multiple);// 一块钱等于100个金币
			int zhutou = zhutous.intValue();
			int status = "1".equals(state) ? 2 : 3;// 支付状态 =0生成支付单 =1以到支付平台等待支付 =2已支付 =3取消

			int ires = orderService.updPayStatus(sdcustomno, sd51no, imoney, zhutou, status,
					DateUtils.getTimeStamp(new Date()));
			if (ires != 1) {
				logger.info("UNPay充值回调更新订单状态失败");
				ResponseUtils.renderHtml(response, "<result>1</result>");
				return;
			}
			ResponseUtils.renderHtml(response, "<result>1</result>");
		} catch (Exception e) {
			logger.error("UNPay回调异常:", e);
			ResponseUtils.renderHtml(response, "<result>1</result>");
		}
	}

	@RequestMapping("/wechatH5PayRecharge")
	public void wechatH5PayRecharge(HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, String> params = new HashMap<String, String>();
			BufferedReader br = request.getReader();
			String tmpStr;
			StringBuilder wholeStr = new StringBuilder();
			while ((tmpStr = br.readLine()) != null) {
				wholeStr.append(tmpStr);
			}
			Map<String, String> requestParams = XmlUtil.xmlToMap(wholeStr.toString());
			for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String valueStr = requestParams.get(name);
				params.put(name, StringUtils.isBlank(valueStr) ? "" : valueStr);
			}
			logger.info("wechatH5Pay充值回调：" + params);
			if ((!params.containsKey("return_code"))
					|| (!StringUtils.equalsIgnoreCase("SUCCESS", params.get("return_code")))) {
				String return_msg = params.get("return_msg");
				logger.error("wechatH5Pay充值回调，参数错误：" + return_msg);
				Map<String, String> returnMap = new HashMap<String, String>();
				returnMap.put("result_code", "FAIL");
				returnMap.put("return_msg", "参数不正确");
				ResponseUtils.renderXml(response, XmlUtil.mapToXml(returnMap));
				return;
			}
			logger.info(">sign start");
			// sign
			Set<String> keySet = params.keySet();
			String[] keyArray = keySet.toArray(new String[keySet.size()]);
			Arrays.sort(keyArray);
			StringBuilder sb = new StringBuilder();
			for (String k : keyArray) {
				if ("sign".equals(k)) {
					continue;
				}
				if (params.get(k).trim().length() > 0) { // 参数值为空，则不参与签名
					sb.append(k).append("=").append(params.get(k).trim()).append("&");
				}
			}
			sb.append("key=").append(Constant.wechat_h5pay_signkey);
			String sign = DigestUtils.md5Hex(sb.toString()).toUpperCase();
			if (!StringUtils.equals(sign, params.get("sign"))) {
				logger.error("wechatH5Pay充值回调，签名失败：" + params.get("sign"));
				Map<String, String> returnMap = new HashMap<String, String>();
				returnMap.put("result_code", "FAIL");
				returnMap.put("return_msg", "签名失败");
				ResponseUtils.renderXml(response, XmlUtil.mapToXml(returnMap));
				return;
			}
			logger.info(">sign end");

			logger.info(">result code start");
			if (!params.containsKey("result_code")) {
				String err_code = params.get("err_code");
				String err_code_des = params.get("err_code_des");
				logger.error("wechatH5Pay充值回调，参数错误：" + err_code + " " + err_code_des);
				Map<String, String> returnMap = new HashMap<String, String>();
				returnMap.put("result_code", "FAIL");
				returnMap.put("return_msg", "参数错误");
				ResponseUtils.renderXml(response, XmlUtil.mapToXml(returnMap));
				return;
			}
			logger.info(">result code end");

			String ordermoney = params.get("total_fee");
			String transaction_id = params.get("transaction_id");
			String out_trade_no = params.get("out_trade_no");

			logger.info(">param3 start");
			if (StringUtils.isBlank(ordermoney) || StringUtils.isBlank(transaction_id)
					|| StringUtils.isBlank(out_trade_no)) {
				logger.error("wechatH5Pay充值回调，参数为空：" + ordermoney + " " + transaction_id + " " + out_trade_no);
				Map<String, String> returnMap = new HashMap<String, String>();
				returnMap.put("result_code", "FAIL");
				returnMap.put("return_msg", "参数为空");
				ResponseUtils.renderXml(response, XmlUtil.mapToXml(returnMap));
				return;
			}
			logger.info(">param3 end");

			double imoney = new BigDecimal(ordermoney).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP)
					.doubleValue();
			Double zhutous = imoney * Integer.parseInt(Constant.recharge_multiple);// 一块钱等于100个金币
			int zhutou = zhutous.intValue();
			int status = StringUtils.equalsIgnoreCase("SUCCESS", params.get("result_code")) ? 2 : 3;// 支付状态 =0生成支付单
																									// =1以到支付平台等待支付
																									// =2已支付 =3取消

			logger.info(">upd status start" + status);
			int ires = orderService.updPayStatus(out_trade_no, transaction_id, imoney, zhutou, status,
					DateUtils.getTimeStamp(new Date()));
			if (ires != 1) {
				logger.error("wechatH5Pay更新订单状态失败" + out_trade_no + " wechatOrder:" + transaction_id);
				Map<String, String> returnMap = new HashMap<String, String>();
				returnMap.put("result_code", "FAIL");
				returnMap.put("return_msg", "更新订单状态失败");
				ResponseUtils.renderXml(response, XmlUtil.mapToXml(returnMap));
				return;
			}
			logger.info("wechatH5Pay更新订单成功" + out_trade_no + " wechatOrder:" + transaction_id);
			logger.info(">upd status end");

			Map<String, String> returnMap = new HashMap<String, String>();
			returnMap.put("result_code", "SUCCESS");
			returnMap.put("return_msg", "OK");
			ResponseUtils.renderXml(response, XmlUtil.mapToXml(returnMap));
			return;
		} catch (Exception e) {
			logger.error("wechatH5Pay回调异常:", e);
			Map<String, String> returnMap = new HashMap<String, String>();
			returnMap.put("result_code", "FAIL");
			returnMap.put("return_msg", "更新订单状态异常");
			try {
				ResponseUtils.renderXml(response, XmlUtil.mapToXml(returnMap));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * 融宝快捷支付回调（异步）做业务用的
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/reapalQuickPayment")
	public void reapalQuickPayment(HttpServletRequest request, HttpServletResponse response) {
		try {
			//String merchantId = request.getParameter("merchant_id");
			String data = request.getParameter("data");
			String encryptkey = request.getParameter("encryptkey");
			// 解析密文数据
			String decryData = DecipherH5.decryptData(encryptkey, data);

			// 获取融宝支付的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
			JSONObject jsonObject = JSON.parseObject(decryData);
			logger.info("接收融宝快捷支付数据:"+jsonObject);
			String merchant_id = jsonObject.getString("merchant_id");
			String trade_no = jsonObject.getString("trade_no");
			String order_no = jsonObject.getString("order_no");
			String total_fee = jsonObject.getString("total_fee");
			String status = jsonObject.getString("status");
			String result_code = jsonObject.getString("result_code");
			String result_msg = jsonObject.getString("result_msg");
			String sign = jsonObject.getString("sign");
			String notify_id = jsonObject.getString("notify_id");

			Map<String, String> map = new HashMap<String, String>();
			map.put("merchant_id", merchant_id);
			map.put("trade_no", trade_no);
			map.put("order_no", order_no);
			map.put("total_fee", total_fee);
			map.put("status", status);
			map.put("result_code", result_code);
			map.put("result_msg", result_msg);
			map.put("notify_id", notify_id);
			// 将返回的参数进行验签
			String mysign = Md5Utils.BuildMysign(map, Constant.reapal_quick_payment_key);

			logger.info("融宝快捷支付签名校验:"+mysign+"="+sign);

			// 建议校验responseTxt，判读该返回结果是否由融宝发出
			if (mysign.equals(sign)) {
				// ——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
				if (status.equals("TRADE_FINISHED")) {
					int orderStatus = StringUtils.equalsIgnoreCase("TRADE_FINISHED", status) ? 2 : 3;// 支付状态 =0生成支付单
																										// =1以到支付平台等待支付
																										// =2已支付 =3取消
					// 支付成功，如果没有做过处理，根据订单号（out_trade_no）及金额（total_fee）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
					// 一定要做金额判断，防止恶意窜改金额，只支付了小金额的订单。
					// 如果有做过处理，不执行商户的业务程序
					double imoney = new BigDecimal(total_fee)
							.divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
					Double zhutous = imoney * Integer.parseInt(Constant.recharge_multiple);// 一块钱等于100个金币
					int zhutou = zhutous.intValue();
					int ires = orderService.updPayStatus(order_no, trade_no, imoney, zhutou, orderStatus,
							DateUtils.getTimeStamp(new Date()));
					if (ires == 0) {
						logger.info("融宝快捷支付回调更新订单状态失败");
						ResponseUtils.renderHtml(response, "fail");
						return;
					}
					ResponseUtils.renderHtml(response, "success");
					return;
				}
				// ——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
			}
			ResponseUtils.renderHtml(response, "fail");
		} catch (Exception e) {
			logger.error("融宝快捷支付回调异常:", e);
			ResponseUtils.renderHtml(response, "fail");
		}
	}
	
	/**
	 * 融宝快捷支付回调（同步）回跳到页面展示给用户
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/reapalReturnQuickPayment")
	public void reapalReturnQuickPayment(HttpServletRequest request, HttpServletResponse response) {
		try {
			//String merchantId = request.getParameter("merchant_id");
			String data = request.getParameter("data");
			String encryptkey = request.getParameter("encryptkey");
			// 解析密文数据
			String decryData = DecipherH5.decryptData(encryptkey, data);
			
			// 获取融宝支付的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
			JSONObject jsonObject = JSON.parseObject(decryData);
			logger.info("同步接收融宝快捷支付数据:"+jsonObject);
			String merchant_id = jsonObject.getString("merchant_id");
			String trade_no = jsonObject.getString("trade_no");
			String order_no = jsonObject.getString("order_no");
			String total_fee = jsonObject.getString("total_fee");
			String status = jsonObject.getString("status");
			String result_code = jsonObject.getString("result_code");
			String result_msg = jsonObject.getString("result_msg");
			String sign = jsonObject.getString("sign");
			String notify_id = jsonObject.getString("notify_id");
			
			Map<String, String> map = new HashMap<String, String>();
			map.put("merchant_id", merchant_id);
			map.put("trade_no", trade_no);
			map.put("order_no", order_no);
			map.put("total_fee", total_fee);
			map.put("status", status);
			map.put("result_code", result_code);
			map.put("result_msg", result_msg);
			map.put("notify_id", notify_id);
			// 将返回的参数进行验签
			String mysign = Md5Utils.BuildMysign(map, Constant.reapal_quick_payment_key);
			
			logger.info("融宝快捷支付同步签名校验:"+mysign+"="+sign);
			
			String payMsg = "fail";
			// 建议校验responseTxt，判读该返回结果是否由融宝发出
			if (mysign.equals(sign)) {
				// ——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
				if (status.equals("TRADE_FINISHED")) {
					payMsg = "success";
				}
				// ——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
			}
			request.setAttribute("success", payMsg);
			request.getRequestDispatcher("/reapal_quick_payment_return.jsp").forward(request, response);
		} catch (Exception e) {
			logger.error("融宝快捷支付回调异常:", e);
			ResponseUtils.renderHtml(response, "支付异常");
		}
	}
}
