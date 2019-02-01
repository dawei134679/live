package com.mpig.api.dictionary.lib;

import com.mpig.api.dictionary.PushConfig;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PushConfigLib {

    private static final Map<String, PushConfig> mapUrl = new HashMap<String, PushConfig>();

    public static void read(String rootPrefix) {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader(rootPrefix+"apns.properties"));
            String threadCountStr = properties.getProperty("threadCount");
            String batchCountStr = properties.getProperty("batchCount");
            mapUrl.put(
                    "pushConfig",
                    new PushConfig().initWith(
                    		rootPrefix+properties.getProperty("certificateFile"),
                            properties.getProperty("certificatePassword"),
                            properties.getProperty("production"),
                            Integer.parseInt(threadCountStr),
                            properties.getProperty("iosTokenReids"),
                            Integer.parseInt(batchCountStr)
                    ));
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public static PushConfig getConfig() {
        return mapUrl.get("pushConfig");
    }
}
