package com.mpig.api.modelcomet;

/**
 * @author tosy
 *房间礼物赠送结果	通过comet到手机端
 *
 *gid	礼物id
 *name	礼物名
 *type	礼物类型
 *combo	连送
 *count	本次赠送数量
 */
public class GiftSendCMod extends BaseCMod{

	public GiftSendCMod(){
		this.setCometProtocol(CModProtocol.GIFT_SEND);
	}
	
	private Integer gid;
    private String name;
    private Integer type;
    private Integer combo;
    private Integer count;
    private Integer gets;
    private String stamp;
    private Integer getTotal;
    private int rq;
    private String dstNickname;
    private Long monthSupport;

    private Integer dstuid;
    
    private Integer gpctype;
    
    public Integer getGpctype() {
		return gpctype;
	}
	public void setGpctype(Integer gpctype) {
		this.gpctype = gpctype;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
    
	public Integer getGid() {
		return gid;
	}
	public void setGid(Integer gid) {
		this.gid = gid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getCombo() {
		return combo;
	}
	public void setCombo(Integer combo) {
		this.combo = combo;
	}
	public Integer getDstuid() {
		return dstuid;
	}
	public void setDstuid(Integer dstuid) {
		this.dstuid = dstuid;
	}
	public Integer getGets() {
		return gets;
	}
	public void setGets(Integer gets) {
		this.gets = gets;
	}
	public String getStamp() {
		return stamp;
	}
	public void setStamp(String stamp) {
		this.stamp = stamp;
	}
	public Integer getGetTotal() {
		return getTotal;
	}
	public void setGetTotal(Integer getTotal) {
		this.getTotal = getTotal;
	}
	public int getRq() {
		return rq;
	}
	public void setRq(int rq) {
		this.rq = rq;
	}
	public String getDstNickname() {
		return dstNickname;
	}
	public void setDstNickname(String dstNickname) {
		this.dstNickname = dstNickname;
	}
	public Long getMonthSupport() {
		return monthSupport;
	}
	public void setMonthSupport(Long monthSupport) {
		this.monthSupport = monthSupport;
	}
}
