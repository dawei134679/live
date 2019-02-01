package com.mpig.api.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class SignUtil {
	

	public static String getSign(Map<String,Object> map, String key, boolean removeSign) throws Exception{
        String result = ascSort(map,removeSign);
        result += "key=" + key;
        result = MD5Encrypt.encrypt(result).toUpperCase();
        return result;
    }
	
	/**
	 * ASC排序
	 * @param map
	 * @return
	 */
	public static String ascSort(Map<String,Object> map, boolean removeSign){
        ArrayList<String> list = new ArrayList<String>();
        for(Map.Entry<String,Object> entry:map.entrySet()){
        	if(removeSign){
        		 if(!entry.getValue().equals("")&& !entry.getKey().equals("sign")){
                     list.add(entry.getKey() + "=" + entry.getValue() + "&");
                 }
        	}else{
        		if(!entry.getValue().equals("")){
                    list.add(entry.getKey() + "=" + entry.getValue() + "&");
                }
        	}
        }
        int size = list.size();
        String [] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i ++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        return result;
	}
}
