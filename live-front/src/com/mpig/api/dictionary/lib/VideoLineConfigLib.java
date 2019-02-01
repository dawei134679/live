package com.mpig.api.dictionary.lib;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mpig.api.dictionary.VideoLineConfig;
import com.mpig.api.utils.FileUtils;

/**
 * @author jack.zhang
 * @date 2016-3-17
 */
public class VideoLineConfigLib {
	/**
	 * 视频开播类型
	 */
	private enum VideoFor {
		Advanced, Normal
	}

	private static final Map<Integer, VideoLineConfig> videoLineMapForAdvanced = new HashMap<Integer, VideoLineConfig>();
	private static final Map<Integer, VideoLineConfig> videoLineMapForNormal = new HashMap<Integer, VideoLineConfig>();

	/**
	 * 装载配置文件
	 */
	public static void read(String configPath) {
		String strJson = FileUtils.ReadFileToString(configPath);
		JSONObject jsonObject = JSONObject.parseObject(strJson);
		loadVideoLineFor(VideoFor.Advanced, jsonObject);
		loadVideoLineFor(VideoFor.Normal, jsonObject);
	}

	/**
	 * 获取主播视频线路
	 * 
	 * @param id
	 * @return
	 */
	public static VideoLineConfig getForAdvanced(int id) {
		return videoLineMapForAdvanced.get(id);
	}

	/**
	 * 获取普通用户视频线路
	 * 
	 * @param id
	 * @return
	 */
	public static VideoLineConfig getForNormal(int id) {
		return videoLineMapForNormal.get(id);
	}

	private static void loadVideoLineFor(final VideoFor videoFor, JSONObject jsonObject) {
		String videoForNode = videoFor == VideoFor.Advanced ? "video_anchors" : "video_users";
		JSONArray artJson = jsonObject.getJSONObject("videos").getJSONObject(videoForNode).getJSONArray("line");
		;
		int len = artJson.size();
		if (len >= 0) {
			Map<Integer, VideoLineConfig> containerMap = videoFor == VideoFor.Advanced ? videoLineMapForAdvanced
					: videoLineMapForNormal;
			for (int i = 0; i < len; i++) {
				JSONObject jo = artJson.getJSONObject(i);
				Integer id = jo.getInteger("id");
				containerMap.put(id, new VideoLineConfig().initWith(id, jo.getString("domain"), jo.getString("pageUrl"),
						jo.getString("shareUrl"), jo.getString("host"), jo.getString("port"), jo.getString("key"),
						jo.getString("hls")));
			}
		}
	}
}
