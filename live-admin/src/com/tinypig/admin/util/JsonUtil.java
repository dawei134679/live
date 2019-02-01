package com.tinypig.admin.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class JsonUtil {

	public static JSONArray formatRsToJsonArray(ResultSet rs)throws Exception{
		ResultSetMetaData md=rs.getMetaData();
		int num=md.getColumnCount();
		JSONArray array=new JSONArray();
		while(rs.next()){
			JSONObject mapOfColValues=new JSONObject();
			for(int i=1;i<=num;i++){
				Object o=rs.getObject(i);
				if(o instanceof Date){
					mapOfColValues.put(md.getColumnName(i), DateUtil.formatDate((Date)o, "yyyy-MM-dd"));
				}else{
					mapOfColValues.put(md.getColumnName(i), rs.getObject(i));					
				}
			}
			array.add(mapOfColValues);
		}
		return array;
	}
	
	/**
	 * javaBean转换为json
	 */
	public static String toJson(Object obj) {
		/*
		 * QuoteFieldNames———-输出key时是否使用双引号,默认为true
		 * WriteMapNullValue——–是否输出值为null的字段,默认为false
		 * WriteNullNumberAsZero—-数值字段如果为null,输出为0,而非null
		 * WriteNullListAsEmpty—–List字段如果为null,输出为[],而非null
		 * WriteNullStringAsEmpty—字符类型字段如果为null,输出为”“,而非null
		 * WriteNullBooleanAsFalse–Boolean字段如果为null,输出为false,而非null
		 */
		// ValueFilter filter = new ValueFilter() {
		// @Override
		// public Object process(Object obj, String k, Object v) {
		// if (v == null) {
		// return "";
		// }
		// return v;
		// }
		// };
		return JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty,
				SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullBooleanAsFalse,
				SerializerFeature.WriteNonStringKeyAsString);
	}

	/**
	 * 转换json为javaBean
	 */
	public static <T> T toBean(String json, Class<T> t) {
		return JSONObject.parseObject(json, t);
	}

	/**
	 * 转换json数组为list集合
	 */
	public static List<Object> toList(String json) {
		return JSON.parseArray(json);
	}

	/**
	 * 转换Json数组为List<bean>
	 */
	public static <T> List<T> toListBean(String json, Class<T> t) {
		List<T> list = new ArrayList<T>();
		if (org.apache.commons.lang.StringUtils.isNotBlank(json)) {
			JSONArray parseArray = JSON.parseArray(json);
			for (Object object : parseArray) {
				String j = object.toString();
				list.add((T) toBean(j, t));
			}
		}
		return list;
	}
}
