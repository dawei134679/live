package com.mpig.api.utils;

/**
 * java 概率计算
 * 
 * @author zyl
 * @time 2016-7-30
 */
public class MathRandomUtil {
	/**
	 * 1出现的概率为%60
	 */
	public static double rate1 = 0.5;
	/**
	 * 2出现的概率为20%
	 */
	public static double rate2 = 0.2;
	/**
	 * 3出现的概率为10%
	 */
	public static double rate3 = 0.125;
	/**
	 * 4出现的概率为5%
	 */
	public static double rate4 = 0.075;
	/**
	 * 5出现的概率为3%
	 */
	public static double rate5 = 0.015;
	/**
	 * 6出现的概率为1.5%
	 */
	public static double rate6 = 0.05;
	/**
	 *  7的概率0.5%
	 */
	public static double rate7 = 0.03;
	/**
	 * 
	 */
	public static double rate8 = 0.005;

	/**
	 * 获取充值抽奖的概率
	 * Math.random()产生一个double型的随机数，判断一下 例如1出现的概率为%60，则介于0到0.60中间的返回1
	 * 
	 * @return int
	 * 
	 */
	public static int PercentageRandom() {
		double randomNumber;
		randomNumber = Math.random();
		if (randomNumber >= 0 && randomNumber <= rate1) {
			return 1;
		} else if (randomNumber >= rate1 && randomNumber <= rate1 + rate2) {
			return 2;
		} else if (randomNumber >= rate1 + rate2 && randomNumber <= rate1 + rate2 + rate3) {
			return 3;
		} else if (randomNumber >= rate1 + rate2 + rate3 && randomNumber <= rate1 + rate2 + rate3 + rate4) {
			return 4;
		} else if (randomNumber >= rate1 + rate2 + rate3 + rate4 && randomNumber <= rate1 + rate2 + rate3 + rate4 + rate5) {
			return 5;
		} else if (randomNumber >= rate1 + rate2 + rate3 + rate4 + rate5 && randomNumber <= rate1 + rate2 + rate3 + rate4 + rate5+ rate6) {
			return 6;
		}else if (randomNumber >= rate1 + rate2 + rate3 + rate4 + rate5 + rate6 && randomNumber <= rate1 + rate2 + rate3 + rate4 + rate5 + rate6 + rate7) {
			return 7;
		}else if (randomNumber >= rate1 + rate2 + rate3 + rate4 + rate5 + rate6+rate7 && randomNumber <= rate1 + rate2 + rate3 + rate4 + rate5 + rate6 + rate7 + rate8) {
			return 8;
		}
		return -1;
	}
	
	public static double n_rate1 = 0.5;
	public static double n_rate2 = 0.47;
	public static double n_rate3 = 0.02;
	public static double n_rate4 = 0.005;
	public static double n_rate5 = 0.003;
	public static double n_rate6 = 0.0015;
	public static double n_rate7 = 0.0005;
	
	public static int newYearRandom() {
		double randomNumber;
		randomNumber = Math.random();
		if (randomNumber >= 0 && randomNumber <= n_rate1) {
			return 8;
		} else if (randomNumber >= n_rate1 && randomNumber <= n_rate1 + n_rate2) {
			return 6;
		} else if (randomNumber >= n_rate1 + n_rate2 && randomNumber <= n_rate1 + n_rate2 + n_rate3) {
			return 2;
		} else if (randomNumber >= n_rate1 + n_rate2 + n_rate3 && randomNumber <= n_rate1 + n_rate2 + n_rate3 + n_rate4) {
			return 3;
		} else if (randomNumber >= n_rate1 + n_rate2 + n_rate3 + n_rate4 && randomNumber <= n_rate1 + n_rate2 + n_rate3 + n_rate4 + n_rate5) {
			return 5;
		} else if (randomNumber >= n_rate1 + n_rate2 + n_rate3 + n_rate4 + n_rate5 && randomNumber <= n_rate1 + n_rate2 + n_rate3 + n_rate4 + n_rate5+ n_rate6) {
			return 4;
		}else if (randomNumber >= n_rate1 + n_rate2 + n_rate3 + n_rate4 + n_rate5 + n_rate6 && randomNumber <= n_rate1 + n_rate2 + n_rate3 + n_rate4 + n_rate5 + n_rate6 + n_rate7) {
			return 1;
		}
		return 0;
	}
	
	
	
	public static double rote1_1 = 0.01;
	public static double rote1_2 = 0.99;
	
	public static int RandomQueq(){
		double randomNumber;
		randomNumber = Math.random();
		if (randomNumber >= 0 && randomNumber <= rote1_1) {
			return 1;
		} else if (randomNumber >= rote1_1 && randomNumber <= rote1_1 + rote1_2) {
			return 0;
		}
		return -1;
	}

	
	
	public static double no1 = 0.38;
	public static double no2 = 0.32;
	public static double no3 = 0.195;
	public static double no4 = 0.065;
	public static double no5 = 0.025;
	public static double no6 = 0.01;
	public static double no7 = 0.003;
	public static double no8 = 0.002;
	/**
	 * 扎金蛋的中奖概率
	 * @return
	 */
	@Deprecated
	public static int goldEggRandom() {
		double randomNumber;
		randomNumber = Math.random();
		if (randomNumber >= 0 && randomNumber <= no1) {
			return 1;
		} else if (randomNumber >= no1 && randomNumber <= no1 + no2) {
			return 2;
		} else if (randomNumber >= no1 + no2 && randomNumber <= no1 + no2 + no3) {
			return 3;
		} else if (randomNumber >= no1 + no2 + no3 && randomNumber <= no1 + no2 + no3 + no4) {
			return 4;
		} else if (randomNumber >= no1 + no2 + no3 + no4 && randomNumber <= no1 + no2 + no3 + no4 + no5) {
			return 5;
		} else if (randomNumber >= no1 + no2 + no3 + no4 + no5 && randomNumber <= no1 + no2 + no3 + no4 + no5+ no6) {
			return 6;
		}else if (randomNumber >= no1 + no2 + no3 + no4 + no5 + no6 && randomNumber <= no1 + no2 + no3 + no4 + no5 + no6 + no7) {
			return 7;
		}else if (randomNumber >= no1 + no2 + no3 + no4 + no5 + no6+no7 && randomNumber <= no1 + no2 + no3 + no4 + no5 + no6 + no7 + no8) {
			return 8;
		}
		return 1;
	}
	
	
	
	public static double share_1 = 0.463;
	public static double share_2 = 0.231;
	public static double share_3 = 0.006;
	/**
	 * 分享的中奖概率
	 * @return
	 */
	public static int shareRandom() {
		double randomNumber;
		randomNumber = Math.random();
		if (randomNumber >= 0 && randomNumber <= share_1) {
			return 95;
		} else if (randomNumber >= share_1 && randomNumber <= share_1 + share_2) {
			return 94;
		} else if (randomNumber >= share_1 + share_2 && randomNumber <= share_1 + share_2 + share_3) {
			return 93;
		} 
		return 0;
	}
	/**
	 * 测试主程序
	 * 
	 * @param agrs
	 */
	public static void main(String[] agrs) {
		int i = 0;
		MathRandomUtil a = new MathRandomUtil();
		int rate1count = 0;
		int rate2count = 0;
		int rate3count = 0;
		int rate4count = 0;
		int rate5count = 0;
		int rate6count = 0;
		int rate7count = 0;
		int getxqcount = 0;
		for (i = 0; i < 10000; i++)
		{
			int getRandom  = a.PercentageRandom();
			System.out.print(getRandom+"-");
			if(getRandom==1){
				rate1count++;
			}
			if(getRandom==2){
				rate2count++;
			}
			if(getRandom==3){
				rate3count++;
			}
			if(getRandom==4){
				rate4count++;
			}
			if(getRandom==5){
				rate5count++;
			}
			if(getRandom==6){
				rate6count++;
			}
			if(getRandom==7){
				rate7count++;
			}
			int getxq = RandomQueq();
			
			if(getxq==1){System.out.println("获得喜鹊 : "+getxq);getxqcount++;}
			
		}
		System.out.println();
		System.out.println("60% count : "+ rate1count);
		System.out.println("20% count : "+ rate2count);
		System.out.println("10% count : "+ rate3count);
		System.out.println("5% count : "+ rate4count);
		System.out.println("3% count : "+ rate5count);
		System.out.println("1.5% count : "+ rate6count);
		System.out.println("0.5% count : "+ rate7count);
		System.out.println("all counts : "+(rate1count+rate2count+rate3count+rate4count+rate5count+rate6count+rate7count));
		System.out.println("获得喜鹊的个数 : "+getxqcount);
		System.out.println(rate1+rate2+rate3+rate4+rate5+rate6+rate7+rate8);
	}
}
