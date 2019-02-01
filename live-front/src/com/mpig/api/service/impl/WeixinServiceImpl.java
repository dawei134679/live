package com.mpig.api.service.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeSet;

import javax.annotation.Resource;
import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.XML;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.AlipaySignature;
import com.ipaynow.utils.MD5;
import com.mpig.api.dictionary.lib.PayConfigLib;
import com.mpig.api.dictionary.lib.UrlConfigLib;
import com.mpig.api.model.PayAccountModel;
import com.mpig.api.redis.service.OtherRedisService;
import com.mpig.api.service.IOrderService;
import com.mpig.api.service.IPayService;
import com.mpig.api.utils.HttpUtil;

/**
 * 微信支付服务 Created by YX on 2016/4/12.
 */
@Service("weixinService")
public class WeixinServiceImpl implements IPayService {
	private static final Logger logger = Logger.getLogger("paylog");
	private Random random = new Random();

	private static SSLConnectionSocketFactory sslsf;

	@Resource
	private IOrderService orderService;

	public synchronized static void buildSSLClient() {
//		KeyStore keyStore;
//		FileInputStream instream = null;
//		try {
//			keyStore = KeyStore.getInstance("PKCS12");
//			
//	        String rootPrefix = WeixinServiceImpl.class.getResource("/").getPath();
//	        
//	        rootPrefix = rootPrefix.substring(0,rootPrefix.lastIndexOf("classes"));
//			instream = new FileInputStream(rootPrefix+"apiclient_cert.p12");
//			keyStore.load(instream, PayConfigLib.getConfig().getWeixin_mchid().toCharArray());
//			instream.close();
//			SSLContext sslcontext = SSLContexts.custom()
//					.loadKeyMaterial(keyStore, PayConfigLib.getConfig().getWeixin_mchid().toCharArray()).build();
//			sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,
//					SSLConnectionSocketFactory.getDefaultHostnameVerifier());
//
//		} catch (Exception e) {
//			logger.error("error-buildSSLClient->Exception:", e);
//		} finally {
//			if (instream != null) {
//				try {
//					instream.close();
//				} catch (IOException e) {
//					logger.error("error-buildSSLClient->finally->IOException:", e);
//				}
//			}
//		}
	}

	@Override
	public String buildSign(Map<String, String> param) {
		return null;
	}

	@Override
	public boolean checkNotify(HttpServletRequest req) {
		return false;
	}

	/**
	 * 支付回调验签
	 *
	 * @param jsonObject
	 * @return
	 */
	public boolean checkNotify(JSONObject jsonObject) {
		StringBuilder content = new StringBuilder();
		String sign = jsonObject.getString("sign");
		jsonObject.remove("sign");

		TreeSet<String> strings = new TreeSet<>(jsonObject.keySet());
		int index = 0;

		for (String key : strings) {
			String value = jsonObject.getString(key);

			content.append(index == 0 ? "" : "&").append(key).append("=").append(value);
			++index;

		}
		content.append("&key=").append(PayConfigLib.getConfig().getWeixin_mchKey());

		try {
			String s = MD5.md5(content.toString(), "UTF-8");
			if (s.equals(sign)) {
				return true;
			}
		} catch (Exception e) {
			logger.error("<error-checkNotify->Exception:>" + e.toString());
			return false;
		}
		return true;
	}

	/**
	 * 获取随机字符串
	 *
	 * @return
	 */
	private String getRandomString() {
		StringBuilder sb = new StringBuilder();
		String base = "QWERTYUIOPASDFGHJKLZXCVBNM0123456789";
		for (int i = 0; i < 30; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	/**
	 * 生成订单编号
	 *
	 * @return
	 */
	String buildMch_Billno() {
		String mch_id = PayConfigLib.getConfig().getWeixin_mchid();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String dataStr = format.format(new Date());
		return mch_id + dataStr + OtherRedisService.getInstance().getMchBillNo();
	}

	/**
	 * 拼接XML字符串
	 *
	 * @param map
	 * @return
	 */
	private String getXMLString(Map<String, String> map) {
		StringBuilder builder = new StringBuilder();
		builder.append("<xml>\n");
		for (String key : map.keySet()) {
			builder.append("<").append(key).append(">").append(map.get(key)).append("</").append(key).append(">\n");
		}
		builder.append("</xml>");
		return builder.toString();
	}

	/**
	 * 发送红包
	 *
	 * @param uid
	 *            uid
	 * @param amount
	 *            金额 单位元
	 * @param billno
	 *            订单号
	 * @return 返回
	 */
	public String sendRedPack(int uid, int amount, String billno) {

		PayAccountModel account = orderService.getPayAccountByUid(uid, false);
		if (account == null || StringUtils.isEmpty(account.getWx_openid())) {
			logger.error("<error-sendRedPack->account>:" + uid + "未找到账户信息");
			return null;
		}

		Map<String, String> map = new HashMap<>();
		map.put("nonce_str", getRandomString());
		map.put("mch_billno", billno);
		map.put("mch_id", PayConfigLib.getConfig().getWeixin_mchid());// TODO
		map.put("wxappid", PayConfigLib.getConfig().getWeixin_appid());
		map.put("send_name", "小猪直播");
		map.put("re_openid", account.getWx_openid());
		map.put("total_amount", String.valueOf(amount * 100)); // 单位元 转化为分
		map.put("total_num", "1");
		map.put("wishing", "恭喜发财，好运常来");
		map.put("client_ip", UrlConfigLib.getUrl("url").getWeixin_redpack_clientIP());// 需要填写服务器地址，微信做验证
		map.put("act_name", "全民直播");
		map.put("remark", "快乐直播，轻松赚钱");
		String stringSignTemp = AlipaySignature.getSignContent(map) + "&key="
				+ PayConfigLib.getConfig().getWeixin_mchKey();

		try {
			map.put("sign", MD5.md5(stringSignTemp, "UTF-8").toUpperCase());
		} catch (Exception e) {
			logger.error("<error-sendRedPack->sign:>" + e.toString());
			return null;
		}

		String postString = getXMLString(map);
		CloseableHttpResponse response = null;
		try {
			// if (sslsf == null) {
			// buildSSLClient();
			// }
			CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			HttpPost httpPost = new HttpPost(UrlConfigLib.getUrl("url").getWeixinTixianUrl());

			StringEntity stringEntity = new StringEntity(postString, "UTF-8");// 解决中文乱码问题
			stringEntity.setContentEncoding("UTF-8");
			stringEntity.setContentType("text/xml");
			httpPost.setEntity(stringEntity);
			response = httpclient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				return null;
			}
			HttpEntity entity = response.getEntity();
			if (entity == null) {
				return null;
			}
			return EntityUtils.toString(entity, "utf-8");
		} catch (Exception e) {
			logger.error("<error-sendRedPack->Exception:>" + e.toString());
			return null;
		} finally {
			if (response != null) {
				try {
					EntityUtils.consume(response.getEntity());
				} catch (IOException e) {
					logger.error("<error-sendRedPack->finally->Exception:>" + e.toString());
				}
			}
		}
	}

	/**
	 * 微信H5签名
	 *
	 * @param body
	 *            商品信息
	 * @param billNo
	 *            订单号
	 * @param amount
	 *            金额
	 * @param clientIP
	 *            客户端IP
	 * @return map
	 */
	public Map buildSign(String body, String billNo, String amount, String clientIP, String openid) {

		HashMap<String, String> data = new HashMap<>();
		data.put("appid", PayConfigLib.getConfig().getWeixin_appid());
		data.put("mch_id", PayConfigLib.getConfig().getWeixin_mchid());
		data.put("nonce_str", getRandomString());
		data.put("body", body);
		data.put("out_trade_no", billNo);
		data.put("total_fee", Float.valueOf(amount).intValue() * 100 + "");
		data.put("spbill_create_ip", clientIP);
		data.put("notify_url", UrlConfigLib.getUrl("url").getWeixin_callback());
		data.put("trade_type", "JSAPI");
		data.put("openid", openid);
		String stringSignTemp = AlipaySignature.getSignContent(data) + "&key="
				+ PayConfigLib.getConfig().getWeixin_mchKey();
		try {
			data.put("sign", MD5.md5(stringSignTemp, "UTF-8").toUpperCase());
		} catch (Exception e) {
			logger.error("<wxbuildSign->sign>" + "加密错误");
			return null;
		}
		String postString = getXMLString(data);
		logger.debug("<wxbuildSign->resp>" + postString);

		String s = HttpUtil.doPostSSL(UrlConfigLib.getUrl("url").getWeixin_unifiedorder(), postString);
		try {

			if (s == null) {
				logger.error("<wxbuildSign->返回空");
				return null;
			}

			logger.debug("<wxbuildSign->返回:" + s);

			String str = XML.toJSONObject(s).getJSONObject("xml").toString();
			JSONObject jsonObject = JSON.parseObject(str);
			String returnCode = jsonObject.get("return_code").toString();

			logger.debug("<wxbuildSign->resp>" + jsonObject);

			if (!"SUCCESS".equalsIgnoreCase(returnCode)) {
				logger.info("<wxbuildSign->resp> returnCode not success");
				return null;
			}
			if (!"SUCCESS".equals(jsonObject.get("result_code"))) {
				logger.info("<wxbuildSign->resp> result_code not success");
				return null;
			}

			String prepay_id = jsonObject.getString("prepay_id");

			HashMap<String, String> signMap = new HashMap<>();
			signMap.put("appId", PayConfigLib.getConfig().getWeixin_appid());
			signMap.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
			signMap.put("nonceStr", getRandomString());
			signMap.put("package", "prepay_id=" + prepay_id);
			signMap.put("signType", "MD5");

			stringSignTemp = AlipaySignature.getSignContent(signMap) + "&key="
					+ PayConfigLib.getConfig().getWeixin_mchKey();
			try {
				signMap.put("paySign", MD5.md5(stringSignTemp, "UTF-8").toUpperCase());
			} catch (Exception e) {
				logger.error("<wxbuildSign->sign>" + "加密错误");
				return null;
			}

			return signMap;
		} catch (Exception e) {
			logger.error("<wxbuildSign->Exception>" + e);
			return null;
		}
	}
}
