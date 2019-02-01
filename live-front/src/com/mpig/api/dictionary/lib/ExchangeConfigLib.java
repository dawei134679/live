package com.mpig.api.dictionary.lib;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mpig.api.utils.FileUtils;

public class ExchangeConfigLib {

	private static final List<ConcurrentHashMap<String, Object>> configList = new ArrayList<ConcurrentHashMap<String, Object>>();
	
	public static void read(String configPath) {
		String strJson = FileUtils.ReadFileToString(configPath);
		JSONObject jsonObject = JSONObject.parseObject(strJson);
		loadExchangeConfig(jsonObject);
	}
	
	private static void loadExchangeConfig(JSONObject jsonObject){
		JSONArray artJson = jsonObject.getJSONArray("exchange");
		int len = artJson.size();
		if (len > 0) {
			for (int i = 0; i < len; i++) {
				JSONObject jo = artJson.getJSONObject(i);
				ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<String,Object>();
				map.put("money", jo.getIntValue("money"));
				map.put("zhutou", jo.getIntValue("zhutou"));
				configList.add(map);
			}
		}
	}
	/**
	 * 根据金额返回内兑猪头数
	 * @param money
	 * @return
	 */
	public static int getZhutou(int money){
		for (ConcurrentHashMap<String, Object> map:configList) {
			if ((int)map.get("money") == money) {
				return (int) map.get("zhutou");
			}
		}
		return 0;
	}
	
	public static List<ConcurrentHashMap<String, Object>> getConfig(){
		return configList;
	}
}
