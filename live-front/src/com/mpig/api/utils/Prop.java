package com.mpig.api.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.log4j.Logger;

public class Prop {

	private static final Logger logger = Logger.getLogger(Prop.class);

	protected static final String DEFAULT_ENCODING = "UTF-8";

	private Properties properties = null;

	public Prop(String fileName) {
		this(fileName, DEFAULT_ENCODING);
	}

	public Prop(String fileName, String encoding) {
		InputStream inputStream = null;
		try {
			properties = new Properties();
			inputStream = getClassLoader().getResourceAsStream(fileName); 
			if (inputStream == null) {
				logger.error("加载配置文件失败，文件可能不存在");
				return;
			}
			properties.load(new InputStreamReader(inputStream, encoding));
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		} finally {
			if (inputStream != null)
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.error(e.getMessage(),e);
				}
		}
	}

	private ClassLoader getClassLoader() {
		ClassLoader ret = Thread.currentThread().getContextClassLoader();
		return ret != null ? ret : getClass().getClassLoader();
	}

	public String get(String key) {
		return properties.getProperty(key);
	}

	public String get(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}

	public Integer getInt(String key) {
		return getInt(key, null);
	}

	public Integer getInt(String key, Integer defaultValue) {
		String value = properties.getProperty(key);
		if (value != null) {
			return Integer.parseInt(value.trim());
		}
		return defaultValue;
	}

	public Long getLong(String key) {
		return getLong(key, null);
	}

	public Long getLong(String key, Long defaultValue) {
		String value = properties.getProperty(key);
		if (value != null) {
			return Long.parseLong(value.trim());
		}
		return defaultValue;
	}

	public Boolean getBoolean(String key) {
		return getBoolean(key, null);
	}

	public Boolean getBoolean(String key, Boolean defaultValue) {
		String value = properties.getProperty(key);
		if (value != null) {
			value = value.toLowerCase().trim();
			if ("true".equals(value)) {
				return true;
			} else if ("false".equals(value)) {
				return false;
			}
			throw new RuntimeException("The value can not parse to Boolean : " + value);
		}
		return defaultValue;
	}

	public boolean containsKey(String key) {
		return properties.containsKey(key);
	}

	public Properties getProperties() {
		return properties;
	}
}
