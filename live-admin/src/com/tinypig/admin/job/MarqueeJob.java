package com.tinypig.admin.job;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.tinypig.admin.config.GlobalConfig;
import com.tinypig.admin.protocol.Protocol;

/**
 * 跑马灯任务
 * @author jackzhang
 *
 */
public class MarqueeJob implements Job {
	private static final Logger logger = LoggerFactory.getLogger(MarqueeJob.class);
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		int speed = jobDataMap.getInt("speed");
		int count = jobDataMap.getInt("count");
		String content = jobDataMap.getString("content");
		
		JSONObject command = new JSONObject();
		command.put("appKey", GlobalConfig.getInstance().getAppKey());
		JSONObject msgBody = new JSONObject();
		command.put("msgBody", msgBody);
		msgBody.put("cometProtocol", Protocol.Notice_Marquee);
		msgBody.put("speed", speed);
		msgBody.put("count", count);
		msgBody.put("content", content);
		/**
		 * 红色r 黄色y 绿色g 默认白色w
		 * jsonArray jsonObject key:color|content
		 */
		String url = GlobalConfig.getInstance().getPublishWordUrl();
		HttpResponse<JsonNode> response;
		try {
			response = Unirest.get(url).asJson();
			 if (response.getStatus() != 200) {
		            logger.error("<MarqueeJob>--" + response.getBody().toString());
		        }
		} catch(UnirestException e) {
			e.printStackTrace();
		}
	}
	

	public static void main(String[] args) {
		JSONObject command = new JSONObject();
		command.put("appKey", GlobalConfig.getInstance().getAppKey());
		JSONObject msgBody = new JSONObject();
		command.put("msgBody", msgBody);
		msgBody.put("cometProtocol", Protocol.Notice_Marquee);
		msgBody.put("speed", 80);
		msgBody.put("count", 1);
		List<SmartObject> groupList = new ArrayList<SmartObject>();
		groupList.add(new SmartObject("y",20,"yellow group"));
//		groupList.add(new SmartObject("g",20,"green group"));
//		groupList.add(new SmartObject("r",20,"red group"));
		groupList.add(new SmartObject("w",20,"white group"));
		
		msgBody.put("content", groupList);
		
		String url = "http://101.201.211.24:8001/publish/world";
		HttpResponse<JsonNode> response;
		try {
		 	response = Unirest.post(url)
					.field("appKey","tinypig")
					.field("msgBody",msgBody.toString())
					.asJson();
			int status = response.getStatus();
			if (status != 200) {
		            logger.error("<MarqueeJob>--" + response.getBody().toString());
		        }
		} catch (UnirestException e) {
			e.printStackTrace();
		}
	}

}
