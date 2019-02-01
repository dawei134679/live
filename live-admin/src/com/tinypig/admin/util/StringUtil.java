package com.tinypig.admin.util;

public class StringUtil {

	public static boolean isEmpty(String str){
		if("".equals(str)|| str==null){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean isNotEmpty(String str){
		if(!"".equals(str)&&str!=null){
			return true;
		}else{
			return false;
		}
	}
	public static String getSqlString(String sqlString,String dbName,Integer uid) {
		
		int sufix = uid%100;
		return String.format(sqlString,sufix<10? dbName+"0"+sufix:dbName+sufix);		
	}
}
