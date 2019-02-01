package com.hkzb.game.common.utils;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({ "rawtypes", "serial", "unchecked" })
public class HMap extends HashMap {

	public HMap() {
	}

	public static HMap by(Object key, Object value) {
		return new HMap().set(key, value);
	}

	public static HMap create() {
		return new HMap();
	}

	public HMap set(Object key, Object value) {
		super.put(key, value);
		return this;
	}

	public HMap set(Map map) {
		super.putAll(map);
		return this;
	}

	public HMap set(HMap okv) {
		super.putAll(okv);
		return this;
	}

	public HMap delete(Object key) {
		super.remove(key);
		return this;
	}

	public <T> T getAs(Object key) {
		return (T) get(key);
	}

	public String getStr(Object key) {
		Object s = get(key);
		return s != null ? s.toString() : null;
	}

	public Integer getInt(Object key) {
		Object val = get(key);
		if (val == null) {
			return null;
		}
		return Integer.valueOf(val.toString());
	}

	public Long getLong(Object key) {
		Object val = get(key);
		if (val == null) {
			return null;
		}
		return Long.valueOf(val.toString());
	}

	public Double getDouble(Object key) {
		Object val = get(key);
		if (val == null) {
			return null;
		}
		return Double.valueOf(val.toString());
	}

	public Number getNumber(Object key) {
		return (Number) get(key);
	}

	public Boolean getBoolean(Object key) {
		return (Boolean) get(key);
	}

	/**
	 * key 存在，并且 value 不为 null
	 */
	public boolean notNull(Object key) {
		return get(key) != null;
	}

	/**
	 * key 不存在，或者 key 存在但 value 为null
	 */
	public boolean isNull(Object key) {
		return get(key) == null;
	}

	/**
	 * value 为 null 或 “”
	 */
	public boolean isBlank(Object key) {
		String value = this.getStr(key);
		return null == value || "".equals(value);
	}

	/**
	 * key 存在，并且 value 为 true，则返回 true
	 */
	public boolean isTrue(Object key) {
		Object value = get(key);
		return (value instanceof Boolean && ((Boolean) value == true));
	}

	/**
	 * key 存在，并且 value 为 false，则返回 true
	 */
	public boolean isFalse(Object key) {
		Object value = get(key);
		return (value instanceof Boolean && ((Boolean) value == false));
	}

	public String toJson() {
		return JsonUtil.toJson(this);
	}

	public boolean equals(Object okv) {
		return okv instanceof HMap && super.equals(okv);
	}
}
