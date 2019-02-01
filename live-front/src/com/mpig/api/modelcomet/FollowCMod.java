package com.mpig.api.modelcomet;

/**
 * @author tosy
 *
 *	
 *	dstUid	被关注的人id
 *	bFollow	true	关注		false	取消关注
 *	
 */
public class FollowCMod extends BaseCMod{

	public FollowCMod(){
		this.setCometProtocol(CModProtocol.FOLLOW);
	}
	
	Integer	dstUid;
	Boolean bFollow;
	
	public Integer getDstUid() {
		return dstUid;
	}
	public void setDstUid(Integer dstUid) {
		this.dstUid = dstUid;
	}
	public Boolean getbFollow() {
		return bFollow;
	}
	public void setbFollow(Boolean bFollow) {
		this.bFollow = bFollow;
	}

//	Boolean	dstSex;
//	String	dstNick;
//	Integer	dstLevel;
//	Integer	dstAnchorLevel;	
//	String	dstAvatar;
}
