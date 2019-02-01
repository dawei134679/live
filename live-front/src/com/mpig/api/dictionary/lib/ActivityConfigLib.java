package com.mpig.api.dictionary.lib;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.mpig.api.dictionary.FirstPayConfig;
import com.mpig.api.dictionary.RechargeLotteryConfig;

public class ActivityConfigLib {

	private static final List<FirstPayConfig> firstPayList = new ArrayList<FirstPayConfig>();
	private static ConcurrentHashMap<Integer, RechargeLotteryConfig> rechargeLotteryConfig= new ConcurrentHashMap<Integer, RechargeLotteryConfig>();
	private static ConcurrentHashMap<Integer, RechargeLotteryConfig> newYearLotteryConfig= new ConcurrentHashMap<Integer, RechargeLotteryConfig>();
	public static void readFirstPay(String configPath) {/*

		// 1~100		//首冲活动配置
		FirstPayConfig firstPayConfig = new FirstPayConfig();
		firstPayConfig.setStart(1);
		firstPayConfig.setEnd(100);

		List<ActGiftConfig> giftList = new ArrayList<ActGiftConfig>();
		ActGiftConfig actGiftConfig = new ActGiftConfig();
		actGiftConfig.setGid(30); // 徽章
		actGiftConfig.setNum(1);
		actGiftConfig.setTimes(14);
		actGiftConfig.setType(3); // 时效礼物
		giftList.add(actGiftConfig);
		
		actGiftConfig.setGid(27); // 心动
		actGiftConfig.setNum(10);
		actGiftConfig.setTimes(0);
		actGiftConfig.setType(2); // 普通礼物
		giftList.add(actGiftConfig);

		actGiftConfig = new ActGiftConfig();
		actGiftConfig.setGid(24); // 人气猪
		actGiftConfig.setNum(10);
		actGiftConfig.setTimes(0);
		actGiftConfig.setType(2); // 普通礼物
		giftList.add(actGiftConfig);

		actGiftConfig = new ActGiftConfig();
		actGiftConfig.setGid(1); // 弹幕
		actGiftConfig.setNum(1);
		actGiftConfig.setTimes(0);
		actGiftConfig.setType(1); // 弹幕
		giftList.add(actGiftConfig);
		firstPayConfig.setConfigs(giftList);

		firstPayList.add(firstPayConfig);

		// 100~500
		firstPayConfig = new FirstPayConfig();
		firstPayConfig.setStart(100);
		firstPayConfig.setEnd(500);

		giftList = new ArrayList<ActGiftConfig>();
		actGiftConfig = new ActGiftConfig();
		actGiftConfig.setGid(31); // 徽章
		actGiftConfig.setNum(1);
		actGiftConfig.setTimes(30);
		actGiftConfig.setType(3); // 时效礼物
		giftList.add(actGiftConfig);
		
		actGiftConfig.setGid(27); // 心动
		actGiftConfig.setNum(20);
		actGiftConfig.setTimes(0);
		actGiftConfig.setType(2); // 普通礼物
		giftList.add(actGiftConfig);

		actGiftConfig = new ActGiftConfig();
		actGiftConfig.setGid(24); // 人气猪
		actGiftConfig.setNum(20);
		actGiftConfig.setTimes(0);
		actGiftConfig.setType(2); // 普通礼物
		giftList.add(actGiftConfig);

		actGiftConfig = new ActGiftConfig();
		actGiftConfig.setGid(1); // 弹幕
		actGiftConfig.setNum(5);
		actGiftConfig.setTimes(0);
		actGiftConfig.setType(1); // 弹幕
		giftList.add(actGiftConfig);

		actGiftConfig = new ActGiftConfig();
		actGiftConfig.setGid(43); // 白金VIP
		actGiftConfig.setNum(1);
		actGiftConfig.setTimes(7);
		actGiftConfig.setType(3); // 时效徽章
		giftList.add(actGiftConfig);
		
		firstPayConfig.setConfigs(giftList);

		firstPayList.add(firstPayConfig);

		// 500~1000
		firstPayConfig = new FirstPayConfig();
		firstPayConfig.setStart(500);
		firstPayConfig.setEnd(1000);

		giftList = new ArrayList<ActGiftConfig>();
		actGiftConfig = new ActGiftConfig();
		actGiftConfig.setGid(31); // 徽章
		actGiftConfig.setNum(1);
		actGiftConfig.setTimes(30);
		actGiftConfig.setType(3); // 时效礼物
		giftList.add(actGiftConfig);

		actGiftConfig = new ActGiftConfig();
		actGiftConfig.setGid(24); // 人气猪
		actGiftConfig.setNum(50);
		actGiftConfig.setTimes(0);
		actGiftConfig.setType(2); // 普通礼物
		giftList.add(actGiftConfig);
		
		actGiftConfig.setGid(36); // 猪头
		actGiftConfig.setNum(50);
		actGiftConfig.setTimes(0);
		actGiftConfig.setType(2); // 普通礼物
		giftList.add(actGiftConfig);

		actGiftConfig = new ActGiftConfig();
		actGiftConfig.setGid(1); // 弹幕
		actGiftConfig.setNum(10);
		actGiftConfig.setTimes(0);
		actGiftConfig.setType(1); // 弹幕
		giftList.add(actGiftConfig);
		firstPayConfig.setConfigs(giftList);

		actGiftConfig = new ActGiftConfig();
		actGiftConfig.setGid(23); // 喇叭
		actGiftConfig.setNum(2);
		actGiftConfig.setTimes(0);
		actGiftConfig.setType(1); // 喇叭
		giftList.add(actGiftConfig);

		actGiftConfig = new ActGiftConfig();
		actGiftConfig.setGid(43); // 白金VIP
		actGiftConfig.setNum(1);
		actGiftConfig.setTimes(14);
		actGiftConfig.setType(3); // 时效徽章
		giftList.add(actGiftConfig);
		
		firstPayConfig.setConfigs(giftList);

		firstPayList.add(firstPayConfig);

		// 1000~
		firstPayConfig = new FirstPayConfig();
		firstPayConfig.setStart(1000);
		firstPayConfig.setEnd(0);

		giftList = new ArrayList<ActGiftConfig>();
		actGiftConfig = new ActGiftConfig();
		actGiftConfig.setGid(31); // 徽章
		actGiftConfig.setNum(1);
		actGiftConfig.setTimes(30);
		actGiftConfig.setType(3); // 时效礼物
		giftList.add(actGiftConfig);

		actGiftConfig.setGid(35); // 猫咪
		actGiftConfig.setNum(100);
		actGiftConfig.setTimes(0);
		actGiftConfig.setType(2); // 普通礼物
		giftList.add(actGiftConfig);

		actGiftConfig = new ActGiftConfig();
		actGiftConfig.setGid(24); // 人气猪
		actGiftConfig.setNum(100);
		actGiftConfig.setTimes(0);
		actGiftConfig.setType(2); // 普通礼物
		giftList.add(actGiftConfig);

		actGiftConfig = new ActGiftConfig();
		actGiftConfig.setGid(1); // 弹幕
		actGiftConfig.setNum(20);
		actGiftConfig.setTimes(0);
		actGiftConfig.setType(1); // 弹幕
		giftList.add(actGiftConfig);
		firstPayConfig.setConfigs(giftList);

		actGiftConfig = new ActGiftConfig();
		actGiftConfig.setGid(23); // 喇叭
		actGiftConfig.setNum(5);
		actGiftConfig.setTimes(0);
		actGiftConfig.setType(1); // 喇叭
		giftList.add(actGiftConfig);

		actGiftConfig = new ActGiftConfig();
		actGiftConfig.setGid(43); // 白金VIP
		actGiftConfig.setNum(1);
		actGiftConfig.setTimes(30);
		actGiftConfig.setType(3); // 时效徽章
		giftList.add(actGiftConfig);
		
		firstPayConfig.setConfigs(giftList);

		firstPayList.add(firstPayConfig);
	*/
	}

	public static List<FirstPayConfig> getFirstPay() {
		return firstPayList;
	}
	
	public static void readRechargeLotteryConfig(){/*
		
		RechargeLotteryConfig lotteryConfig = new RechargeLotteryConfig();
		lotteryConfig.setGid(13); //棒棒糖*5
		lotteryConfig.setNum(5);
		lotteryConfig.setType(0);
		rechargeLotteryConfig.put(1, lotteryConfig);
		
		lotteryConfig = new RechargeLotteryConfig();
		lotteryConfig.setGid(13); //棒棒糖*10 
		lotteryConfig.setNum(10);
		lotteryConfig.setType(0);
		rechargeLotteryConfig.put(2, lotteryConfig);
		
		lotteryConfig = new RechargeLotteryConfig();
		lotteryConfig.setGid(13); //棒棒糖*30
		lotteryConfig.setNum(30);
		lotteryConfig.setType(0);
		rechargeLotteryConfig.put(3, lotteryConfig);
		
		lotteryConfig = new RechargeLotteryConfig();
		lotteryConfig.setGid(13); //棒棒糖*45
		lotteryConfig.setNum(45);
		lotteryConfig.setType(0);
		rechargeLotteryConfig.put(4, lotteryConfig);
		
		lotteryConfig = new RechargeLotteryConfig();
		lotteryConfig.setGid(26); //甲壳虫*1
		lotteryConfig.setNum(1);
		lotteryConfig.setType(0);
		rechargeLotteryConfig.put(5, lotteryConfig);
		
		lotteryConfig = new RechargeLotteryConfig();
		lotteryConfig.setGid(43); //白金VIP* 7 
		lotteryConfig.setNum(7);
		lotteryConfig.setType(3);
		rechargeLotteryConfig.put(6, lotteryConfig);
		
		lotteryConfig = new RechargeLotteryConfig();
		lotteryConfig.setGid(43); //白金VIP* 30
		lotteryConfig.setNum(30);
		lotteryConfig.setType(3);
		rechargeLotteryConfig.put(7, lotteryConfig);
		
		lotteryConfig = new RechargeLotteryConfig();
		lotteryConfig.setGid(10); //兰博基尼*1
		lotteryConfig.setNum(1);
		lotteryConfig.setType(0);
		rechargeLotteryConfig.put(8, lotteryConfig);
	*/
	}
	public static ConcurrentHashMap<Integer, RechargeLotteryConfig> getRechargeLotteryConfig() {
		return rechargeLotteryConfig;
	}
	
	public static ConcurrentHashMap<Integer, RechargeLotteryConfig> getNewYearLotteryConfig() {
		if(newYearLotteryConfig.size() == 0){
			readNewYearLotteryConfig();
		}
		return newYearLotteryConfig;
	}
	public static void readNewYearLotteryConfig(){/*
		
		RechargeLotteryConfig lotteryConfig = new RechargeLotteryConfig();
		lotteryConfig.setGid(100001); //猪头红包 888
		lotteryConfig.setNum(1);
		lotteryConfig.setType(888);
		newYearLotteryConfig.put(1, lotteryConfig);

		
		lotteryConfig = new RechargeLotteryConfig();
		lotteryConfig.setGid(43); //白金VIP
		lotteryConfig.setNum(10);
		lotteryConfig.setType(3);
		newYearLotteryConfig.put(2, lotteryConfig);
		
		lotteryConfig = new RechargeLotteryConfig();
		lotteryConfig.setGid(100001); //猪头红包 88
		lotteryConfig.setNum(4);
		lotteryConfig.setType(88);
		newYearLotteryConfig.put(3, lotteryConfig);
		
		lotteryConfig = new RechargeLotteryConfig();
		lotteryConfig.setGid(87); //兰博基尼
		lotteryConfig.setNum(2);
		lotteryConfig.setType(5);
		newYearLotteryConfig.put(4, lotteryConfig);
		
		lotteryConfig = new RechargeLotteryConfig();
		lotteryConfig.setGid(100001); //猪头红包 188 
		lotteryConfig.setNum(3);
		lotteryConfig.setType(188);
		newYearLotteryConfig.put(5, lotteryConfig);
		
		lotteryConfig = new RechargeLotteryConfig();
		lotteryConfig.setGid(100); //礼物新年快乐
		lotteryConfig.setNum(1000);
		lotteryConfig.setType(0);
		newYearLotteryConfig.put(6, lotteryConfig);
		
		lotteryConfig = new RechargeLotteryConfig();
		lotteryConfig.setGid(100001); //现金 8888元
		lotteryConfig.setNum(1);
		lotteryConfig.setType(99);
		newYearLotteryConfig.put(7, lotteryConfig);
		
		lotteryConfig = new RechargeLotteryConfig();
		lotteryConfig.setGid(0); //谢谢惠顾
		lotteryConfig.setNum(1);
		lotteryConfig.setType(-1);
		newYearLotteryConfig.put(8, lotteryConfig);
	*/}
}
