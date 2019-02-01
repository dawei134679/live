package com.hkzb.game.common.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtils {

	public enum Level {
		DEBUG, ERROR, INFO, WARN
	}

	/**
	 * 调试
	 * 
	 * @param clz
	 * @param msg
	 */
	public static void debug(Class clz, String msg) {
		log(clz, msg, Level.DEBUG);
	}

	/**
	 * 错误
	 * 
	 * @param clz
	 * @param msg
	 */
	public static void error(Class clz, String msg) {
		log(clz, msg, Level.ERROR);
	}

	public static void error(Class clz, Throwable t) {
		error(clz, null, t);
	}

	public static void error(Class clz, String msg, Throwable t) {
		StringWriter sw = new StringWriter();
		t.printStackTrace(new PrintWriter(sw));
		String smsg = sw.toString();
		if (null != msg) {
			smsg = msg + smsg;
		}
		log(clz, smsg, Level.ERROR);
	}

	/**
	 * 信息
	 * 
	 * @param clz
	 * @param msg
	 */
	public static void info(Class clz, String msg) {
		log(clz, msg, Level.INFO);
	}

	/**
	 * 警告
	 * 
	 * @param clz
	 * @param msg
	 */
	public static void warn(Class clz, String msg) {
		log(clz, msg, Level.WARN);
	}

	public static void warn(Class clz, Throwable t) {
		StringWriter sw = new StringWriter();
		t.printStackTrace(new PrintWriter(sw));
		log(clz, sw.toString(), Level.WARN);
	}

	private static void log(Class clz, String msg, Level level) {
		// Log log = LogFactory.getLog(clz);
		Logger log = LoggerFactory.getLogger(clz);

		switch (level) {
		case DEBUG:
			if (log.isDebugEnabled()) {
				log.debug(msg);
			}
			break;
		case INFO:
			if (log.isInfoEnabled()) {
				log.info(msg);
			}
			break;
		case WARN:
			if (log.isWarnEnabled()) {
				log.warn(msg);
			}
			break;
		case ERROR:
			if (log.isErrorEnabled()) {
				log.error(msg);
			}
			break;
		default:
			break;
		}
	}

}
