package com.qiniu.pili;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import com.mpig.api.dictionary.lib.UrlConfigLib;
import com.mpig.api.utils.Constant;

public class QnStreamUtil {
	private static final Logger logger = Logger.getLogger(QnStreamUtil.class);
			
	static private QnStreamUtil ins = new QnStreamUtil();

	static public final String HUBNAME = Constant.qn_hub;

	static public QnStreamUtil getInstance() {
		return ins;
	}

	private QnStreamUtil() {}

	public String getRoomToken(Integer roomid,Integer uid){
		String token = getToken(roomid, uid);
		if(StringUtils.isNotBlank(token)) {
			return token;
		}
		return getToken(roomid, uid);
	}
	private String getToken(Integer roomid,Integer uid) {
		String perm = (roomid!=null&&uid!=null&&roomid.intValue()==uid)?"admin":"user";
		String accessKey = UrlConfigLib.getUrl("url").getQn_accessKey();
		String secretKey =UrlConfigLib.getUrl("url").getQn_secretKey();
        
	    Client cli = new Client(accessKey, secretKey);
	    Meeting meeting = cli.newMeeting();

	    String roomName = roomid.toString();
	    Meeting.Room room = null;
        try {
			room = meeting.getRoom(roomName);
		} catch (PiliException e) {
			//如果是首次开播 七牛服务获取到流信息 就会抛异常
			logger.error("QN getRoom error : " + e.getMessage());
		}
        try {
        	if(room==null) {
        		meeting.createRoom(roomName,roomName);
        	}
		} catch (PiliException e) {
			logger.error(e.getMessage(),e);
		}
	    String rt = null;
    	try {
    		Date d = new Date(System.currentTimeMillis()+1000*60*60*24);	    	
			rt = meeting.roomToken(roomName, uid.toString(), perm,d);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}    	
		return rt;
	}
}
