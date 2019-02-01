package com.mpig.api.async;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import javapns.Push;
import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;
import javapns.notification.ResponsePacket;

import org.apache.log4j.Logger;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import com.mpig.api.dictionary.PushConfig;
import com.mpig.api.dictionary.lib.PushConfigLib;
import com.mpig.api.dictionary.lib.PushTempeleConfigLib;
import com.mpig.api.utils.LocalObject;

public class NotificationEngine{
    public static final int EXCE_THREAD_POOL_SIZE = 8;
    private ExecutorService exec;
	private static Logger log = Logger.getLogger(NotificationEngine.class);
	private String keystore;
	private String password;
	private boolean production;
	private int threadCount;
	private String iosTokenReids;
	private int batchCount;

	private final static NotificationEngine instance = new NotificationEngine();
	public static NotificationEngine getInstance() {
		return instance;
	}
	
	protected ShardedJedisPool jedisPool;

	public void setJedisPool(ShardedJedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}
	
    private void shutdownEngine(){
        if(!exec.isShutdown()){
            exec.shutdown();
        }
    }
    
    public NotificationEngine initialize(String apnConfigFile){
    	FileReader fileReader = null;
    	try {  
        	String rootPrefix = this.getClass().getResource("/").getPath();
            rootPrefix = rootPrefix.substring(0,rootPrefix.lastIndexOf("classes"));
        	Properties propertie = new Properties();
        	fileReader = new FileReader(new File(rootPrefix+apnConfigFile));
        	propertie.load(fileReader);  
        	String certificateFile = propertie.getProperty("certificateFile");
            keystore = rootPrefix + certificateFile; 
            password = propertie.getProperty("certificatePassword","");  
            production = Boolean.valueOf(propertie.getProperty("production", "true")); 
            String number = propertie.getProperty("threadCount");
            threadCount = Integer.parseInt(number);
            exec = Executors.newFixedThreadPool(threadCount);
            iosTokenReids = propertie.getProperty("iosTokenReids");
            batchCount = Integer.parseInt(propertie.getProperty("batchCount"));
        } catch (FileNotFoundException ex) {  
            ex.printStackTrace();  
        } catch (Exception ex) {  
            ex.printStackTrace();  
        }finally{ 
        	try {
        		if(fileReader != null){
        			fileReader.close();
        		}
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        return this;
    }
    
    public NotificationEngine initialize(PushConfig config){
        keystore = config.getCertificateFile(); 
        password = config.getCertificatePassword();  
        production = Boolean.valueOf(config.getProduction()); 
        threadCount = config.getThreadCount();
        exec = Executors.newFixedThreadPool(threadCount);
        iosTokenReids = config.getIosTokenReids();
        batchCount = config.getBatchCount();
        return this;
    }
    
    
    public void destroy(){
        Thread t = new Thread(){
            public void run() {
                NotificationEngine.this.shutdownEngine();
            }
        };
        t.start();
    }
    
	
    private FutureTask<List<PushedNotification>> doSendNotificationList(final String message, final int badge, final String sound, final String keystore, final String password, final boolean production, final List<String> tokens){    
        FutureTask<List<PushedNotification>> future = new FutureTask<List<PushedNotification>>(
                new Callable<List<PushedNotification>>() {
                    public List<PushedNotification> call() {
                        List<PushedNotification> notifications = null;
                        try {
                            notifications = Push.combined(message,badge,sound,keystore,password,production,tokens);
                        } catch (CommunicationException e) {
                            e.printStackTrace();
                        } catch (KeystoreException e) {
                            e.printStackTrace();
                        }
                        return notifications;
                    }
                });
        exec.submit(future);
        return future;
    }
    
    public void sendNotificationList(final String message, final int badge, final String sound,final List<String> tokens){    
        FutureTask<List<PushedNotification>> futrue = doSendNotificationList(message, badge,sound,keystore,password,production,tokens);
        try {
            List<PushedNotification> list = futrue.get();
            if(list != null){
            	for (PushedNotification notification : list) {
                    if (notification.isSuccessful()) {
                            /* Apple accepted the notification and should deliver it */  
                    		log.info("Push notification sent successfully to: " + notification.getDevice().getToken());
                            /* Still need to query the Feedback Service regularly */  
                    } else {
                            String invalidToken = notification.getDevice().getToken();
                            /* Add code here to remove invalidToken from your database */  

                            /* Find out more about what the problem was */  
                            Exception theProblem = notification.getException();
                            //theProblem.printStackTrace();
                            log.error(invalidToken +">>"+ theProblem.toString());

                            /* If the problem was an error-response packet returned by Apple, get it */  
                            ResponsePacket theErrorResponse = notification.getResponse();
                            if (theErrorResponse != null) {
                                    log.error(theErrorResponse.getMessage());
                            }
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    
    private FutureTask<List<PushedNotification>> doSendNotificationListWithData(final String data,final String message, final int badge, final String sound, final String keystore, final String password, final boolean production, final List<String> tokens){    
        FutureTask<List<PushedNotification>> future = new FutureTask<List<PushedNotification>>(
                new Callable<List<PushedNotification>>() {
                    public List<PushedNotification> call() {
                        List<PushedNotification> notifications = null;
                        try {
                        	PushNotificationPayload complexPayload = PushNotificationPayload.complex();
                        	complexPayload.addBadge(badge);
                			complexPayload.addSound(sound);
                			complexPayload.addCustomDictionary("data", Arrays.asList(data));
                			complexPayload.addAlert(message);
                			notifications = Push.payload(complexPayload, keystore, password, production, tokens);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return notifications;
                    }
                });
        exec.submit(future);
        return future;
    }
    
    /**
     * 主播开播时回调
     * 从开播池中随机取出一条模板
     * @param liverNickName 开播者昵称
     * @param data
     * @param message
     * @param tokens
     */
    public void sendNotificationListWithDataWhenLiveOn(final String liverNickName,final String data,final List<String> tokens){
    	if(tokens.size() <= 0){
    		return;
    	}
    	ArrayList<String> config = PushTempeleConfigLib.getConfig("ios");
    	if(config == null){
    		config = new ArrayList<String>();
    		log.error("sendNotificationListWithDataWhenLiveOn config ios is null or size is zero");
    	}
    	
    	if(config.size() <= 0){
    		config.add("你关注的%s正在直播，邀请你来看");
    	}
    	
    	int index = LocalObject.random.get().nextInt(config.size());
    	String templete = config.get(index);
    	String message = String.format(templete, liverNickName);
    	sendNotificationListWithData(data,message,1,"default",tokens);
    }
    
    /**
     * 组推
     * @param data
     * @param message
     * @param badge
     * @param sound
     * @param tokens
     */
    public void sendNotificationListWithData(final String data,final String message, final int badge, final String sound,final List<String> tokens){
    	if(tokens.size() <= 0){
    		return;
    	}
        FutureTask<List<PushedNotification>> futrue = doSendNotificationListWithData(data,message, badge,sound,keystore,password,production,tokens);
        try {
            List<PushedNotification> list = futrue.get();
            if(list != null){
            	for (PushedNotification notification : list) {
                    if (notification.isSuccessful()) {
                            /* Apple accepted the notification and should deliver it */  
                    		log.info("Push notification sent successfully to: " + notification.getDevice().getToken());
                            /* Still need to query the Feedback Service regularly */  
                    } else {
                            String invalidToken = notification.getDevice().getToken();
                            /* Add code here to remove invalidToken from your database */  

                            /* Find out more about what the problem was */  
                            Exception theProblem = notification.getException();
                            //theProblem.printStackTrace();
                            log.error(invalidToken + theProblem.toString());

                            /* If the problem was an error-response packet returned by Apple, get it */  
                            ResponsePacket theErrorResponse = notification.getResponse();
                            if (theErrorResponse != null) {
                                    log.error(theErrorResponse.getMessage());
                            }
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    
    private void hookShutdown(){
        Runtime.getRuntime().addShutdownHook(new Thread(){
            public void run() {
                shutdownEngine();
            }
        });
    }
    
    public void start(boolean readSpecialCfg) {
    	if(readSpecialCfg){
    		initialize("/apns.properties");
    	}else{
    		PushConfig config = PushConfigLib.getConfig();
    		initialize(config);
    	}
    	
		hookShutdown();
		log.warn(String.format("start notification server certificatePath=%s production=%s threadCount=%d", keystore,
				production==true?"pro":"dev", threadCount));
    } 
    
    public boolean pushToApnsFromRedisLoop(){
    	ShardedJedis jedis = jedisPool.getResource();
		List<String> deviceTokens = new ArrayList<String>();
		try {
			long start = 0;
			long end = start;
			while (true) {
				String clientId = jedis.lpop(iosTokenReids);
				List<String> lrange = jedis.lrange(iosTokenReids, start, end);
				if (clientId == null) {
					break;
				}
				String deviceToken = jedis.get(clientId);
				if (deviceToken != null) {
					deviceTokens.add(deviceToken);
				}
				if (deviceTokens.size() >= batchCount) {
					break;
				}
			}
		} finally {
			jedis.close();
		}
		if (deviceTokens.size() == 0) {
			return false;
		}
		
		NotificationEngine.getInstance().sendNotificationList("welcome xujin to tiny pig live app!" , 1, "default",deviceTokens);
		return true;
    }
    
    public static void main(String[] args) {
		NotificationEngine engine = NotificationEngine.getInstance();
		engine.start(true);
//		engine.sendNotificationList("welcome xujin to tiny pig live app!" , 1, "default", 
//				Arrays.asList("efef3c8b2aaa800610a0bb566b8d907f0fb38109d397416cac88f88e07ed4c69","4c8b13361ed6368563b5992a1f8a0dfa9b4192b06bdaa649ce10d9dcb6f2f7ee"));
//		String data = "{\"level\":1,\"msg\":\"您的关注：阿凡达正在直播，"
//				+ "邀您加入\",\"uid\":10000158,\"videoDomain\""
//				+ ":\"rtmp://videodownws.xiaozhutv.com:1935/xiaozhu\"}";
		String data = "{\"level\":1,\"uid\":10000012,\"nickname\":\"金秀\"}";
		engine.sendNotificationListWithData(data,"welcome to tiny pig!" , 1, "default", 
				Arrays.asList("c5da55688baac4465d2341dbfb1beced90580781dc80a98b4412561c8da1bb47"/*,"4c8b13361ed6368563b5992a1f8a0dfa9b4192b06bdaa649ce10d9dcb6f2f7ee"*/));
    }
}
