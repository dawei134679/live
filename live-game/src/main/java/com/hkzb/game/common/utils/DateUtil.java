package com.hkzb.game.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期处理工具类
 */
public class DateUtil {
	/** 日期格式yyyy-MM-dd字符串常量 */
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	/** 日期格式HH:mm:ss字符串常量 */
	public static final String HOUR_FORMAT = "HH:mm:ss";
	/** 日期格式yyyy-MM-dd HH:mm:ss字符串常量 */
	public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	/** 日期格式yyyyMM字符串常量 */
	public static final String DATE_FORMAT_YYYYMM = "yyyyMM";
	/** 日期格式yyyy字符串常量 */
	public static final String DATE_FORMAT_YEAR = "yyyy";
	/** 日期格式MM字符串常量 */
	public static final String DATE_FORMAT_MONTH = "MM";
	/** 日期格式dd字符串常量 */
	public static final String DATE_DAY = "dd";
	/** 日期格式HH字符串常量 */
	public static final String DATE_HOUR = "HH";
	/** 日期格式mm字符串常量 */
	public static final String DATE_MINUTE = "mm";
	/** 日期格式yyyy-MM字符串常量 */
	public static final String DATE_FORMAT_YEAR_MONTH = "yyyy-MM";

	/**
	 * 格式化日期
	 * 
	 * @param date
	 * @param datetimeFormat
	 * @return
	 */
	public static String formatDate(Date date, String pattern) {
		SimpleDateFormat fmt = new SimpleDateFormat(pattern);
		return fmt.format(date);
	}

	/**
	 * 转化日期
	 * 
	 * @param timeto
	 * @param pattern
	 * @return
	 */
	public static Date parseDate(String timeto, String pattern) {
		SimpleDateFormat fmt = new SimpleDateFormat(pattern);
		try {
			return fmt.parse(timeto);
		} catch (ParseException e) {
			LogUtils.error(DateUtil.class, e);
		}
		return null;
	}

	/**
	 * 增加时间
	 * 
	 * @param d
	 *            要增加时间的日期
	 * @param field
	 *            Calendar.MONTH Calendar.DAY_OF_MONTH ...
	 * @param value
	 *            数量
	 * @return
	 */
	public static final Date addDate(Date d, int field, int value) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.add(field, value);
		return c.getTime();
	}

	/**
	 * 清除除了year后的时间
	 * 
	 * @param date
	 * @return
	 */
	public static final Date truncateToDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DATE);
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DATE, day);
		return calendar.getTime();
	}

	/**
	 * 清除除了year和month后的所有时间
	 * 
	 * @param date
	 * @return
	 */
	public static final Date truncateToMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		return calendar.getTime();
	}

	/**
	 * 清除除了year后的时间
	 * 
	 * @param date
	 * @return
	 */
	public static final Date truncateToYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		return calendar.getTime();
	}

	/**
	 * 获取月份最后一天
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static int getLastDayOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DATE, 1);
		calendar.add(Calendar.DATE, -1);
		SimpleDateFormat df = new SimpleDateFormat(DATE_DAY);
		return Integer.parseInt(df.format(calendar.getTime()));
	}

	/**
	 * 毫秒转化时分秒毫秒
	 * 
	 * @param ms
	 *            106200001
	 * @return example : 1天5小时30分1毫秒
	 */
	public static String formatTime(Long ms) {
		Integer ss = 1000;
		Integer mi = ss * 60;
		Integer hh = mi * 60;
		Integer dd = hh * 24;

		Long day = ms / dd;
		Long hour = (ms - day * dd) / hh;
		Long minute = (ms - day * dd - hour * hh) / mi;
		Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
		Long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

		StringBuffer sb = new StringBuffer();
		if (day > 0) {
			sb.append(day + "天");
		}
		if (hour > 0) {
			sb.append(hour + "小时");
		}
		if (minute > 0) {
			sb.append(minute + "分");
		}
		if (second > 0) {
			sb.append(second + "秒");
		}
		if (milliSecond > 0) {
			sb.append(milliSecond + "毫秒");
		}
		return sb.toString();
	}

	/**
	 * 获取某个时间所在的月份有多少天
	 * 
	 * @param date
	 * @return
	 */
	public static int getCountDayOfMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	public static Date NOW() {
		return new Date();
	}

	public static Long nowTime() {
		return System.currentTimeMillis();
	}

	/**
	 * 当前时间加24小时
	 * 
	 * @return uinx 时间戳
	 */
	public static Long getNowTimeAfter24H() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.HOUR, 24);
		return c.getTime().getTime() / 1000;
	}
}