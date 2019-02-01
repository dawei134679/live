package com.mpig.api.dictionary.lib;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.mpig.api.dictionary.ClusterConfig;

public class ClusterConfigLib {

	private static final Map<String, ClusterConfig> mapUrl = new HashMap<String, ClusterConfig>();

	public static void read(String configPath) {
		Properties properties = new Properties();
		try {
			properties.load(new FileReader(configPath));
			String esHost = properties.getProperty("es.host");
			String esPort = properties.getProperty("es.port");
			Boolean esEnable = Boolean.parseBoolean(properties.getProperty("es.enable"));
			
			
			mapUrl.put("cluster",new ClusterConfig().initWith(esHost, Integer.valueOf(esPort),esEnable));
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	public static ClusterConfig getUrl() {
		return mapUrl.get("cluster");
	}
}
