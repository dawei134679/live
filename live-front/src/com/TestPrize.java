package com;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.hamcrest.core.IsInstanceOf;

import com.alibaba.fastjson.JSONObject;
import com.mpig.api.utils.MathRandomUtil;

public class TestPrize {

	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			demo();
		}
	}
	public static int allmoney() {
		long start = System.currentTimeMillis();
		Random random = new Random();
		Map<String,Object> probas = getProbabilitys();
		List<Object> probasList = new ArrayList<Object>(); 
		for (Map.Entry<String, Object> probaObj : probas.entrySet()) {
			Map<String,Object> proba = (Map<String, Object>)probaObj.getValue();
			Map<Integer, Object> luckyNumMap = new HashMap<Integer, Object>();
			int divisor = Integer.parseInt(proba.get("divisor").toString());
			for (int i = 0; i < divisor; i++) {
				int luckyNum = random.nextInt(Integer.parseInt(proba.get("dividend").toString()));
				luckyNumMap.put(luckyNum, luckyNum);
			}
			proba.put("luckyNums", luckyNumMap);
			probasList.add(proba);
		}
//		System.out.println(probasList);
		int allmoney = 0;
		for(Object probaObj : probasList){
			Map<String,Object> proba = (Map<String, Object>)probaObj;
			int luckyCount = 0;
			int maxcount = Integer.parseInt(proba.get("maxcount").toString());
			int max = 0;
			int dividend = Integer.parseInt(proba.get("dividend").toString());
			Map<Integer,Object> luckyNumMap =  (Map<Integer,Object>)proba.get("luckyNums");
			for (int i = 0; i < 1; i++) {
				if(max==maxcount){
					break;
				}
				int luckyNum = random.nextInt(dividend);
				if(luckyNumMap.get(luckyNum)!=null){
					max ++;
					luckyCount++;
				}
			}
			allmoney += 1*luckyCount*Integer.parseInt(proba.get("multiples").toString());
//			System.out.println("中奖概率 : "+proba.get("multiples")+"   中奖次数: "+luckyCount +"  中奖倍率 : "+ luckyCount*Integer.parseInt(proba.get("multiples").toString()));
		}
//		System.out.println(allmoney);
		long end = System.currentTimeMillis();
		long x = end-start;
//		System.out.println("共耗时 : "+x);
		return allmoney;
	}
	
	
	public static void demo(){
		long start = System.currentTimeMillis();
		Random random = new Random();
		Map<Integer, Object> lucky10 = new HashMap<Integer, Object>();
		Map<Integer, Object> lucky100 = new HashMap<Integer, Object>();
		Map<Integer, Object> lucky500 = new HashMap<Integer, Object>();
		Map<Integer, Object> lucky1000 = new HashMap<Integer, Object>();
		for (int i = 0; i < 9; i++) {
			int a = random.nextInt(1000);
			lucky10.put(a, a);
		}
		for (int i = 0; i < 3; i++) {
			int a = random.nextInt(10000);
			lucky100.put(a, a);
		}
		for (int i = 0; i < 3; i++) {
			int a = random.nextInt(10000);
			lucky500.put(a, a);
		}
		for (int i = 0; i < 3; i++) {
			int a = random.nextInt(100000);
			lucky1000.put(a, a);
		}
		int count10 = 0;
		int count100 = 0;
		int count500 = 0;
		int count1000 = 0;
//		System.out.println(lucky10);
//		System.out.println(lucky100);
//		System.out.println(lucky500);
//		System.out.println(lucky1000);
		for (int i = 0; i < 1000000; i++) {
			
			int a10 = random.nextInt(1000);
			Object o10 = lucky10.get(a10);
			if(o10!=null){
				count10++;
			}
			
			int a100 = random.nextInt(10000);
			Object o100 = lucky100.get(a100);
			if(o100!=null){
				count100++;
			}
			
			int a500 = random.nextInt(10000);
			Object o500 = lucky500.get(a500);
			if(o500!=null){
				count500++;
			}
			
			int a1000 = random.nextInt(100000);
			Object o1000 = lucky1000.get(a1000);
			if(o1000!=null){
				count1000++;
			}
		}
//		System.out.println("10倍 共中奖次数"+count10);
//		System.out.println("100倍 共中奖次数"+count100);
//		System.out.println("500倍 共中奖次数"+count500);
//		System.out.println("1000倍 共中奖次数"+count1000);
		int sum = count1000*1000+count100*100+count500*500+count10*10;
		System.out.println("总共送出去收益 : "+ sum);
		long end = System.currentTimeMillis();
		long x = end-start;
//		System.out.println("共耗时 : "+x);
	}
	//获取概率
	public static Map<String, Object> getProbabilitys(){
		Map<String, Object> probasMap = new HashMap<String, Object>();
		
		Map<String, String> probaMap10 = new HashMap<String, String>();
		probaMap10.put("multiples", "10");
		probaMap10.put("divisor", "9");
		probaMap10.put("dividend", "1000");
		probaMap10.put("isRunWay", "1");
		probaMap10.put("maxcount", "50");
		probasMap.put("10", probaMap10);
		
		Map<String, String> probaMap100 = new HashMap<String, String>();
		probaMap100.put("multiples", "100");
		probaMap100.put("divisor", "3");
		probaMap100.put("dividend", "10000");
		probaMap100.put("isRunWay", "1");
		probaMap100.put("maxcount", "5");
		probasMap.put("100", probaMap100);
		
		Map<String, String> probaMap500 = new HashMap<String, String>();
		probaMap500.put("multiples", "500");
		probaMap500.put("divisor", "3");
		probaMap500.put("dividend", "10000");
		probaMap500.put("isRunWay", "1");
		probaMap500.put("maxcount", "3");
		probasMap.put("500", probaMap500);
		
		Map<String, String> probaMap1000 = new HashMap<String, String>();
		probaMap1000.put("multiples", "1000");
		probaMap1000.put("divisor", "3");
		probaMap1000.put("dividend", "100000");
		probaMap1000.put("isRunWay", "1");
		probaMap1000.put("maxcount", "1");
		probasMap.put("1000", probaMap1000);
		return probasMap;
	}
}
