package com.mpig.api.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.mpig.api.dictionary.lib.PayConfigLib;
import com.mpig.api.service.IPayService;

/**
 * 支付宝支付
 * Created by YX on 2016/4/12.
 */
@Service("aliPayService")
public class AliPayServiceImpl implements IPayService {

	private static final Logger logger = Logger.getLogger("paylog");

    @Override
    public String buildSign(Map<String, String> param) {

        Float amount = Float.valueOf(param.get("amount"));
        String subject = param.get("details");

        HashMap<String, String> sign = new HashMap<>();
        sign.put("service", PayConfigLib.getConfig().getAlipay_service());
        sign.put("partner", PayConfigLib.getConfig().getAlipay_partner());
        sign.put("_input_charset", PayConfigLib.getConfig().getAlipay_inputCharset());
        sign.put("notify_url", PayConfigLib.getConfig().getAlipay_notifyUrl());
        sign.put("out_trade_no", param.get("orderNo"));
        sign.put("subject", subject);
        sign.put("payment_type", "1");//默认1  商品购买
        sign.put("seller_id", PayConfigLib.getConfig().getAlipay_sellerId());
        sign.put("total_fee", amount.toString());//TODO 待修改
        sign.put("body", subject);
        sign.put("it_b_pay", "30m");

        logger.debug("<getAlipaySign->unsign>" + sign);
        try {
            String rsaSign = AlipaySignature.rsaSign(sign, PayConfigLib.getConfig().getAlipay_privateKey(), PayConfigLib.getConfig().getAlipay_inputCharset());
            sign.put("sign", URLEncoder.encode(rsaSign, "UTF-8"));
            sign.put("sign_type", PayConfigLib.getConfig().getAlipay_signType());
            String signContent = AlipaySignature.getSignContent(sign);
            logger.debug("<getAlipaySign->sign>" + signContent);
            return signContent;
        } catch (AlipayApiException e) {
            logger.info("<getAlipaySign->Exception>" + e.toString());
            return null;
        } catch (UnsupportedEncodingException e) {
            logger.info("<getAlipaySign->Exception>" + e.toString());
            return null;
        }
    }

    @Override
    public boolean checkNotify(HttpServletRequest req) {
        logger.info("<aliNotify-> 回调记录：>" + JSON.toJSONString(req.getParameterMap()));

        HashMap<String, String> temp = new HashMap<>();
        Map<String, String[]> requestParams = req.getParameterMap();

        for (String name : requestParams.keySet()) {
            String value = requestParams.get(name)[0];
            temp.put(name, value);
        }
        try {
            return AlipaySignature.rsaCheckV1(temp, PayConfigLib.getConfig().getAlipay_publicKey(), PayConfigLib.getConfig().getAlipay_inputCharset());
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return false;
        }

    }
}
