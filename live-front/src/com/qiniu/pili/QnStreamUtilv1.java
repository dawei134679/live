package com.qiniu.pili;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.mpig.api.async.IAsyncTask;
import com.mpig.api.dictionary.lib.UrlConfigLib;
import com.mpig.api.model.RecordItem;
import com.mpig.api.redis.service.OtherRedisService;
import com.mpig.api.utils.Constant;
import com.mpig.api.utils.EncryptUtils;
import com.pili.Hub;
import com.pili.Stream.SaveAsResponse;
import com.pili.Stream.SnapshotResponse;
import com.qiniu.Credentials;


public class QnStreamUtilv1 {

	private static final Logger logger = Logger.getLogger(QnStreamUtilv1.class);

	static public final int recLimitSec = 30;
	
	static private QnStreamUtilv1 insv1 = new QnStreamUtilv1();
	static final String PRE	= Constant.qn_streamId_pre;

	private QnStreamUtilv1(){}
	
	static public QnStreamUtilv1 getInstance() {
		return insv1;
	}
	
//test
//	public static void main(String[] args) {
//		QnStreamUtilv1.getInstance().getStreamv1("testkey");
//	}
	
	/**
	 * 获取开播key
	 * @param uid
	 * @param key	null，时间戳更新key	 != null	固定key
	 * @return
	 */
	public String updateStreamKeyV1(String uid,String key,boolean bOnlyGetkey){
		String rt = null;

		String accessKey = UrlConfigLib.getUrl("url").getQn_accessKey();
		String secretKey =UrlConfigLib.getUrl("url").getQn_secretKey();
		
		Credentials credentials = new Credentials(accessKey,secretKey); // Credentials Object
        Hub hub = new Hub(credentials, QnStreamUtil.HUBNAME);
		
        com.pili.Stream stream = null;

        if(null == key){
        	key = EncryptUtils.md5Encrypt(String.valueOf(System.currentTimeMillis()*100+ (long)(Math.random()*(100-1+1))));
        }
        
        try{
			stream = hub.getStream(PRE + uid);	
			if(false == bOnlyGetkey){
				logger.debug("qn update stream 1... ... " + uid + " key:" + stream.getPublishKey());
				stream = stream.update(key, "static", false);
				logger.debug("qn update stream 2... ... " + uid + " key:" + stream.getPublishKey());
			}
        } catch( Exception e){
        	logger.debug("qn get stream failed ... need create a stream " + uid);
        }
        
        if(null == stream){
        	try{
    	        String title           = uid;     // optional, auto-generated as default
    	        String publishKey      = key;
    	        String publishSecurity = "static";     // optional, can be "dynamic" or "static", "dynamic" as default
    	        stream = hub.createStream(title, publishKey, publishSecurity);
            	logger.debug("qn create stream ok ... " + uid);
        	}catch(Exception e){
            	logger.debug("qn create stream failed ... " + uid);
        	}
        }
        
        if(null != stream){
        	rt = stream.getPublishKey();
        }
        
        return rt;
	}
	
	static public class Qnv1CreateDelRecords implements IAsyncTask{

		String	uid;
		long	starttime;
		long	endtime;
		String	title;
		
		public Qnv1CreateDelRecords(String strUid,long nStarttime,long nEndtime,String strTitle){
			uid = strUid;
			starttime = nStarttime;
			endtime = nEndtime;
			title = strTitle;
		}
		
		@Override
		public void runAsync() {
			String accessKey = UrlConfigLib.getUrl("url").getQn_accessKey();
			String secretKey =UrlConfigLib.getUrl("url").getQn_secretKey();

			Credentials credentials = new Credentials(accessKey,secretKey); // Credentials Object
	        Hub hub = new Hub(credentials, QnStreamUtil.HUBNAME);
	        com.pili.Stream stream = null;
	        try{
				stream = hub.getStream(PRE + uid);	
	        } catch( Exception e){
	        	logger.debug("qn get stream failed ... need create a stream " + uid);
	        }
	        
	        if(null != stream){
	            String saveAsFormat    = "mp4";                            // required
	            String saveAsName      = "rec_" + starttime+ "_" + uid + "." + saveAsFormat; // required
	            String saveAsNotifyUrl = null;                             // optional
	            String pipeline = null;
	            try {
					SaveAsResponse response = stream.saveAs(saveAsName, saveAsFormat, starttime, endtime, saveAsNotifyUrl,pipeline);
					if(null != response){
						String mp4url = response.getTargetUrl();
						String m3u8url = response.getUrl();
						String id = response.getPersistentId();
						logger.debug("streamv1 saveas:"+ mp4url + "  " + m3u8url + "  " + id);
						
						RecordItem data = new RecordItem(String.valueOf(starttime),title,mp4url,m3u8url,(endtime - starttime),null);
						
						//snapshot
				        String formatpic    = "jpg";                      // required
				        String namepic      = "reccover_" + starttime + "_" + uid + "." + formatpic; // required
				        long timepic        = starttime + 30;                 // optional, in second, unix timestamp
				        String notifyUrl = null; 
				        try {        
				        	SnapshotResponse responsePic = stream.snapshot(namepic, formatpic, timepic, notifyUrl);
				        	if(null != responsePic){
				        		data.setCoverPic(responsePic.getTargetUrl());
				        	}
				        } catch (Exception e) {
							e.printStackTrace();
						}
						
						//snapshot
						String jsondata = JSONObject.toJSONString(data);
						OtherRedisService.getInstance().addUidRecordByTime(uid, Long.toString(starttime), jsondata);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
	        	
	        }
		}

		@Override
		public void afterOk() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterError(Exception e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return null;
		}
		
		
	}
}
