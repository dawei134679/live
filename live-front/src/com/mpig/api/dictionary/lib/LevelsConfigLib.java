package com.mpig.api.dictionary.lib;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mpig.api.dictionary.LevelsConfig;
import com.mpig.api.utils.FileUtils;

public class LevelsConfigLib {
	
	// 用户类型
	private enum UserFor{
		Advanced,
		Normal
	}
	private static final Map<Integer, LevelsConfig> levelMapForAdvanced = new HashMap<Integer, LevelsConfig>();
	private static final Map<Integer, LevelsConfig> levelMapForNormal = new HashMap<Integer, LevelsConfig>();
	
	public static void read(String configPath) {
		String strJson = FileUtils.ReadFileToString(configPath);
		JSONObject jsonObject = JSONObject.parseObject(strJson);
		loadUserlevelsFor(UserFor.Advanced,jsonObject);
		loadUserlevelsFor(UserFor.Normal,jsonObject);
	}
	
	public static LevelsConfig getForAdvanced(int level){
		return levelMapForAdvanced.get(level);
	}
	
	public static LevelsConfig getForNormal(int level){
		return levelMapForNormal.get(level);
	}
	
	private static void loadUserlevelsFor(final UserFor userFor, JSONObject jsonObject){
		String levelForNode = userFor == UserFor.Advanced?"anchors":"users";
		JSONArray artJson = jsonObject.getJSONObject(levelForNode).getJSONArray("level");
		int len = artJson.size();
		if(len >= 0){
			Map<Integer, LevelsConfig> containerMap = userFor == UserFor.Advanced?levelMapForAdvanced:levelMapForNormal;
			for (int i = 0; i < len; i++) {
				JSONObject jo = artJson.getJSONObject(i);
				Integer level = jo.getInteger("level");
				if (levelForNode == "anchors") {
					containerMap.put(level, new LevelsConfig().initWith(level,jo.getString("name"),jo.getLongValue("get"),jo.getIntValue("rq"),jo.getIntValue("rate")));
				}else {
					containerMap.put(level, new LevelsConfig().initWith(level,jo.getString("name"),jo.getLongValue("get"),0,0));
				}
			}
		}
	}
}
