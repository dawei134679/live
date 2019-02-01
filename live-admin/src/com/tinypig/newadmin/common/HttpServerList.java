package com.tinypig.newadmin.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

public class HttpServerList {

	private static HttpServerList instance = null;
	private static Object lock = new Object();
	
	private static Logger logger = Logger.getLogger(HttpServerList.class);

	private HttpServerList() {

	}

	public static HttpServerList getInstance() {
		synchronized (lock) {
			if (instance == null) {
				instance = new HttpServerList();
			}
		}
		return instance;
	}

	private static ArrayList<String> serverList = new ArrayList<String>();

	public ArrayList<String> getServerList() {
		return serverList;
	}

	public void loadFromFile(String filePath) {
		BufferedReader bufferedReader = null;
		try {
			serverList.clear();
			String encoding = "UTF-8";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) {
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
				bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					serverList.add(lineTxt);
				}
			} else {
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}finally {
			if(bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					logger.error("关闭流异常",e);
				}
			}
		}
	}

}
