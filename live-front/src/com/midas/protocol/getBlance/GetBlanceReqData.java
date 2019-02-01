package com.midas.protocol.getBlance;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class GetBlanceReqData {
	private String openid = "";  //从手Q登录态或微信登录态中获取的openid的值
	private String openkey = ""; //手Q登陆时传手Q登陆回调里获取的paytoken值，微信登陆时传微信登陆回调里获取的传access_token值。
	private Integer appid = 0; //offerid，offerid即支付结算页面里的应用id，用于支付接口。
	private Long ts = 0l; //UNIX时间戳（从格林威治时间1970年01月01日00时00分00秒起至现在的总秒数）。
	private String sig = ""; //请求串的签名（参考YSDK支付接口签名说明）。
	private String pf = ""; //平台来源，登录获取的pf值
	private String pfkey = ""; //登录获取的pfkey值
	private String zoneid = ""; //账户分区ID_角色ID。每个应用都有一个分区ID为1的默认分区，分区可以在cpay.qq.com/mpay上自助配置。如果应用选择支持角色，则角色ID接在分区ID号后用"_"连接，角色ID需要进行urlencode。
	private String userip = ""; //（可选）用户的外网IP accounttype：（可选）帐户类型ID。基础货币（common）；安全货币（security）；不填，默认common
	private String format = ""; //（可选）json、jsonp_$func。默认json。如果jsonp，前缀为：$func 例如：format=jsonp_sample_pay，返回格式前缀为：sample_pay()
	
	public Map<String,String> GetBlanceReqDataMap(String openid, String openkey, Integer appid,
			Long ts, String pf, String pfkey, String zoneid,String sig,
			String userip, String format) {
		setAppid(appid);
		setFormat(format);
		setOpenid(openid);
		setOpenkey(openkey);
		setPf(pf);
		setPfkey(pfkey);
		setTs(ts);
		setUserip(userip);
		setZoneid(zoneid);
		setSig(sig);
		return toMap();
	}


	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getOpenkey() {
		return openkey;
	}
	public void setOpenkey(String openkey) {
		this.openkey = openkey;
	}
	public Integer getAppid() {
		return appid;
	}
	public void setAppid(Integer appid) {
		this.appid = appid;
	}
	public Long getTs() {
		return ts;
	}
	public void setTs(Long ts) {
		this.ts = ts;
	}
	public String getSig() {
		return sig;
	}
	public void setSig(String sig) {
		this.sig = sig;
	}
	public String getPf() {
		return pf;
	}
	public void setPf(String pf) {
		this.pf = pf;
	}
	public String getPfkey() {
		return pfkey;
	}
	public void setPfkey(String pfkey) {
		this.pfkey = pfkey;
	}
	public String getZoneid() {
		return zoneid;
	}
	public void setZoneid(String zoneid) {
		this.zoneid = zoneid;
	}
	public String getUserip() {
		return userip;
	}
	public void setUserip(String userip) {
		this.userip = userip;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	
	public Map<String,String> toMap(){
        Map<String,String> map = new HashMap<String, String>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            Object obj;
            try {
                obj = field.get(this);
                if(obj!=null){
                    map.put(field.getName(), obj.toString());
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
	
}
