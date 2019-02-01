package com.mpig.api.utils;

import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateUtils {
	/**
	 * 通用日期格式化方法
	 * 
	 * @param date
	 *            传入的日期
	 * @param strFormat
	 *            格式化字符串（yyyy年MM月dd日hh时mm分ss秒）
	 * @return
	 */
	public static String dateFormat(Date date, String strFormat) {
		if (date != null) {
			SimpleDateFormat format = new SimpleDateFormat(strFormat, Locale.getDefault());
			return format.format(date);
		} else {
			return "";
		}
	}

	/**
	 * 把日期转换为字符串,
	 * 
	 * @param date
	 *            要转换的日期,如果date时间参数为null,默认取当前时间；
	 * @param par
	 *            日期格式,如果参数为null，默认取"yyyy-MM-dd HH:mm:ss"
	 * @return 转换后的字符串
	 * @since 2009-11-9
	 */
	public static String dateToString(Date date, String par) {
		String string = "";

		if (date == null) {
			Calendar cal = Calendar.getInstance();
			date = cal.getTime();
		}
		if (par == null)
			par = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat dateformat = new SimpleDateFormat(par, Locale.getDefault());
		string = dateformat.format(date);
		return string;
	}

	// 获得本月第一天0点时间
	public static String getTimesMonthmorning(String par) {
		SimpleDateFormat dateformat = new SimpleDateFormat(par);
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		return dateformat.format(cal.getTime());
	}

	/**
	 * 功能：获得当前时间
	 * 
	 * @return yyyy-MM-dd HH:mm:ss Date
	 */
	public static Date getCurrentDateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		Date now = new Date();
		String tmp = sdf.format(now);
		try {
			return sdf.parse(tmp);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 字符串日期转为Date类型
	 * 
	 * @param strdate
	 *            需转换的字符串
	 * @return 转换后的java.util.Date
	 */
	public static Date parseYMDHM(String strdate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
		Date date = new Date();
		try {
			date = sdf.parse(strdate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 字符串日期转为Date
	 * 
	 * @param sDate
	 *            需转换的字符串
	 * @param format
	 *            格式，如:yyyy-MM-dd HH:mm:ss
	 * @return java.util.Date
	 */
	public static Date parseDate(String sDate, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
		Date date = new Date();
		try {
			date = sdf.parse(sDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 获取当天时间戳，以秒为单位
	 * 
	 * @return
	 */
	public static long getTimeStamp(Date date) {
		Long lg;
		if (date == null) {
			lg = System.currentTimeMillis() / 1000;
		} else {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			lg = cal.getTimeInMillis() / 1000;
		}

		return lg.longValue();
	}

	public static long getTimeStamp(String date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			Date d = sdf.parse(date);
			return d.getTime() / 1000;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public static String md5Encrypt(String data) {
		String resultString = null;
		try {
			resultString = new String(data);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byte2hexString(md.digest(resultString.getBytes()));
		} catch (Exception ex) {

		}
		return resultString;
	}

	private static String byte2hexString(byte[] bytes) {
		StringBuffer bf = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			if ((bytes[i] & 0xff) < 0x10) {
				bf.append("T0");
			}
			bf.append(Long.toString(bytes[i] & 0xff, 16));
		}
		return bf.toString();
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
	 * 获取今天的0点的时间戳
	 * 
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
	 * 获取今天的23:59:59点的时间戳
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static Long getDayEnd() {

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.MILLISECOND, 999);

		return cal.getTimeInMillis() / 1000;
	}

	/**
	 * 取date前几天或后后几天的日期
	 * 
	 * @param date
	 *            n
	 * @return
	 */
	public static Date getNDaysAfterDate(Date date, int n) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, n);
		return c.getTime();
	}

	/**
	 * 获取前几月或后几月
	 * 
	 * @param date
	 * @param n
	 * @return
	 */
	public static Date getMonthsAfterDate(Date date, int n) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, n);
		return c.getTime();
	}

	/**
	 * 获取指定日期 的前后天的 日期
	 * 
	 * @param date
	 * @param n
	 * @param format
	 * @return
	 */
	public static String getDate(Date date, int n, String format) {
		Calendar calendar = Calendar.getInstance();
		if (date != null) {
			calendar.setTime(date);
		}
		calendar.add(Calendar.DATE, n);

		return new SimpleDateFormat(format).format(calendar.getTime());
	}

	/**
	 * 获取距离下一天的秒数
	 * 
	 * @return
	 */
	public static int getSecondeToNextDay() {

		Long lgNow = System.currentTimeMillis() / 1000;

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);

		Long lgNextDay = cal.getTimeInMillis() / 1000;
		return lgNextDay.intValue() - lgNow.intValue();
	}

	public static Date getNMonthAfterDate(Date date, int n) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, n);
		return c.getTime();
	}

	/**
	 * 计算两段时间相差的天数
	 * 
	 * @param from
	 *            /秒
	 * @param to
	 *            /秒
	 * @return
	 */
	public static Long getDaysBetweenTime(Long from, Long to) {
		Long days = (to - from) / (60 * 60 * 24);
		return days;
	}

	/**
	 * 获取某月 第一天 零点 零分 零秒
	 * 
	 * @param date
	 * @param month
	 * @return
	 */
	public static Date getMonthStartDay(Date date, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 获取某月 最后一天 23:59:59
	 * 
	 * @param date
	 * @param month
	 * @return
	 */
	public static Date getMonthEndDay(Date date, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();
	}
	
	/**
	 * 获取当前系统时间 精确到毫秒
	 * @return
	 */
	public static long getCurrentTimeMillis() {
		return new Date().getTime();
	}
	
	/**
	 * 获取当前日期字符串
	 * @return
	 */
	public static String getCurrentDayStr() {
		Date date = new Date();
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = sdFormat.format(date);
		return dateStr;
	}
}
