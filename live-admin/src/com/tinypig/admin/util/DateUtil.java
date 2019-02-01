package com.tinypig.admin.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.time.DateUtils;

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

	public static void main(String[] args) {
		System.out.println(dateToLong("2018-05-18 00:00:00"));
	}

	public static String formatDate(Date date, String format) {
		String result = "";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		if (date != null) {
			result = sdf.format(date);
		}
		return result;
	}

	public static Date formatString(String str, String format) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(str);
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
		return Integer.valueOf(df.format(calendar.getTime()));
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
	 * 获取今天的0点的时间戳
	 * @param date
	 * @param format
	 * @return
	 */
	public static Long getDayBegin() {

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 001);

		return cal.getTimeInMillis() / 1000;
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
	 * 获取当天时间戳，以秒为单位
	 * @return
	 */
	public static int getTimeStamp(Date date) {
		Long lg;
		if (date == null) {
			lg = System.currentTimeMillis() / 1000;
		} else {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			lg = cal.getTimeInMillis() / 1000;
		}

		return lg.intValue();
	}

	/**
	  * 将长整型数字转换为日期格式的字符串
	  * 
	  * @param time
	  * @param format
	  * @return
	  */
	public static String convert2String(long time, String format) {
		if (time > 0l) {
			if (format == null || format.isEmpty()) {
				format = "yyyy-MM-dd HH:mm:ss";
			}

			SimpleDateFormat sf = new SimpleDateFormat(format);
			Date date = new Date(time);
			return sf.format(date);
		}
		return "";
	}

	/**
	 * 将长整型数字转换为任意日期格式的字符串
	 * 
	 * @param time
	 * @param format
	 * @return
	 */
	public static String datetime2String(long time, String format) {
		if (time > 0l) {
			if (format == null || format.isEmpty()) {
				format = "yyyy-MM-dd HH:mm:ss";
			}

			SimpleDateFormat sf = new SimpleDateFormat(format);
			Date date = new Date(time);
			return sf.format(date);
		}
		return "";
	}

	/**
	 * 获取本周开始时间
	 * 
	 * @param iweek
	 *            =0本周 =1上周
	 * @return yyyymmdd
	 */
	public static String getWeekStart(int iweek) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar cl = new GregorianCalendar();
		cl.setFirstDayOfWeek(Calendar.MONDAY);

		int dayOfWeek = 0;
		if (cl.get(Calendar.DAY_OF_WEEK) == 1) {
			dayOfWeek = 6;
		} else {
			dayOfWeek = cl.get(Calendar.DAY_OF_WEEK) - 2;
		}

		cl.add(Calendar.DATE, -dayOfWeek);
		if (iweek == 1) {
			cl.add(Calendar.DATE, -7);
		}
		return sdf.format(cl.getTime());
	}

	/**
	 * 时间转毫秒
	 * @param date
	 * @param fomart
	 * @param type
	 * @param val
	 * @return 毫秒
	 */
	public static Long dateToLong(String date, String fomart) {
		SimpleDateFormat sf = new SimpleDateFormat(fomart);
		Long lg = 0L;
		Date parse;
		try {
			parse = sf.parse(date);
			Calendar cal = Calendar.getInstance();
			cal.setTime(parse);
			lg = cal.getTimeInMillis();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return lg;
	}

	/**
	 * 时间转毫秒
	 * @param date
	 * @param parsePatterns
	 * @return 毫秒
	 */
	public static Long dateToLong(String str) {
		Long lg = 0L;
		try {
			// Date parseDate = DateUtils.parseDate(str, parsePatterns);
			Date parseDate = DateUtils.parseDate(str,
					new String[] { "yyyy", "yyyy-MM", "yyyyMM", "yyyy/MM", "yyyy-MM-dd", "yyyyMMdd", "yyyy/MM/dd",
							"yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss", "yyyy/MM/dd HH:mm:ss" });
			if (null != parseDate) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(parseDate);
				lg = cal.getTimeInMillis();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lg;
	}

	/**
	 * 时间转数字
	 * @param date
	 * @param fomart
	 * @param type
	 * @param val
	 * @return
	 */
	public static Long dateToLong(String date, String fomart, String type, int val) {
		SimpleDateFormat sf = new SimpleDateFormat(fomart);
		Long lg = 0L;
		Date parse;

		try {
			parse = sf.parse(date);
			Calendar cal = Calendar.getInstance();
			cal.setTime(parse);
			if ("day".equals(type)) {
				cal.add(Calendar.DATE, val);
			} else if ("month".equals(type)) {
				cal.add(Calendar.MONTH, val);
			} else if ("year".equals(type)) {
				cal.add(Calendar.YEAR, val);
			}
			lg = cal.getTimeInMillis() / 1000;

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lg;
	}

	public static int getDaysBetweenDates(Date startDate, Date endDate) {
		return (int) ((endDate.getTime() - startDate.getTime()) / (24 * 60 * 60 * 1000));
	}

	public static int getDayBetweenDates(String strStartDate, String strEndDate, String fomart) {
		int day = 0;
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(fomart);
		java.util.Date beginDate;
		java.util.Date endDate;
		try {
			beginDate = format.parse(strStartDate);
			endDate = format.parse(strEndDate);
			day = (int) ((endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return day;
	}

	/**
	 * 
	 * @param yearMonth  yyyy-MM
	 * @param type =0当前月 =1下个月
	 * @return
	 */
	public static int getYearMonthDay(String yearMonth, int type) {

		String[] split = yearMonth.split("-");
		int year = Integer.valueOf(split[0]);
		int month = Integer.valueOf(split[1]);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar cl = new GregorianCalendar();
		cl.set(year, month - 1, 1);
		if (type == 1) {
			cl.add(Calendar.MONTH, 1);
		}

		return Integer.valueOf(sdf.format(cl.getTime()));
	}

	/**
	 * 获取指定 年月日 的时间
	 * @param year
	 * @param month
	 * @param date
	 * @return
	 */
	public static Long getYMD(int year, int month, int date) {
		Calendar cal = new GregorianCalendar();
		cal.set(year, month, date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 001);

		return cal.getTimeInMillis() / 1000;
	}

}
