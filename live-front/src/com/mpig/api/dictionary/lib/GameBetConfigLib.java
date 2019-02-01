package com.mpig.api.dictionary.lib;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mpig.api.dictionary.GameBetConfig;
import com.mpig.api.utils.FileUtils;

public class GameBetConfigLib {

	private static final Map<String, GameBetConfig> map = new HashMap<String,GameBetConfig>();
	
	public static GameBetConfig getGameBetConfig(String key){
		return map.get(key);
	}
	
	public static void read(String configPath){

		String strJson = FileUtils.ReadFileToString(configPath);
		JSONObject jsonObject = JSONObject.parseObject(strJson);
		
		JSONArray jsonInner = jsonObject.getJSONObject("game").getJSONObject("inner").getJSONArray("node");
		JSONArray jsonOuter = jsonObject.getJSONObject("game").getJSONObject("outer").getJSONArray("node");
		
		int lenInner = jsonInner.size();
		int lenOuter = jsonOuter.size();
		
		for(int i = 0; i < lenInner; i++){
			JSONObject json = jsonInner.getJSONObject(i);
			map.put("inner_"+json.getIntValue("id"), 
					new GameBetConfig().initWith(
							json.getIntValue("id"), 
							json.getString("name"), 
							json.getDoubleValue("times"), 
							json.getIntValue("rate")
						)
					);
		}
		for(int i = 0; i < lenOuter; i++){
			JSONObject json = jsonOuter.getJSONObject(i);
			map.put("outer_"+json.getIntValue("id"), 
					new GameBetConfig().initWith(
							json.getIntValue("id"), 
							json.getString("name"), 
							json.getDoubleValue("times"), 
							json.getIntValue("rate")
						)
					);
		}
		
	}
}
