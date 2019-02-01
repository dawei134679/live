package com.mpig.api.dictionary.lib;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mpig.api.dictionary.UrlConfig;
import com.mpig.api.utils.FileUtils;

import java.util.HashMap;
import java.util.Map;

public class UrlConfigLib {

    private static final Map<String, UrlConfig> mapUrl = new HashMap<String, UrlConfig>();

    public static void read(String configPath) {
        String strJson = FileUtils.ReadFileToString(configPath);

        JSONObject jsonObject = JSONObject.parseObject(strJson);
        loadUrlConfigFor(jsonObject);
    }

    public static UrlConfig getUrl(String url) {
        return mapUrl.get(url);
    }

    private static void loadUrlConfigFor(JSONObject jsonObject) {
        JSONArray artJson = jsonObject.getJSONArray("url");
        int len = artJson.size();
        if (len >= 0) {
            for (int i = 0; i < len; i++) {
                JSONObject jo = artJson.getJSONObject(i);
                mapUrl.put("url", new UrlConfig().initWith(jo));
            }
        }
    }
}
