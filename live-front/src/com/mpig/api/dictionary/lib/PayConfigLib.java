package com.mpig.api.dictionary.lib;

import com.mpig.api.dictionary.PayConfig;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PayConfigLib {

    private static final Map<String, PayConfig> mapUrl = new HashMap<String, PayConfig>();

    public static void read(String configPath) {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader(configPath));

            mapUrl.put(
                    "payConfig",
                    new PayConfig().initWith(
                            properties.getProperty("alipay.submit"),
                            properties.getProperty("alipay.service"),
                            properties.getProperty("alipay.partner"),
                            properties.getProperty("alipay.sellerId"),
                            properties.getProperty("alipay.inputCharset"),
                            properties.getProperty("alipay.paymentType"),
                            properties.getProperty("alipay.notifyUrl"),
                            properties.getProperty("alipay.return_url"),
                            properties.getProperty("alipay.signType"),
                            properties.getProperty("alipay.privateKey"),
                            properties.getProperty("alipay.publicKey"),
                            properties.getProperty("alipay.verifyUrl"),
                            properties.getProperty("ipayNow.notiryUrl"),
                            properties.getProperty("ipayNow.appKey"),
                            properties.getProperty("ipayNow.signType"),
                            properties.getProperty("ipayNow.charset"),
                            properties.getProperty("alipay.appid"),
                            properties.getProperty("weixin.appid"),
                            properties.getProperty("weixin.appKey"),
                            properties.getProperty("weixin.mch_id"),
                            properties.getProperty("weixin.mch_key"),
                            properties.getProperty("apple.sandbox"),
                            properties.getProperty("apple.buy"),
                            properties.getProperty("pay.notifyUrl")

                    ));
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public static PayConfig getConfig() {
        return mapUrl.get("payConfig");
    }
}
