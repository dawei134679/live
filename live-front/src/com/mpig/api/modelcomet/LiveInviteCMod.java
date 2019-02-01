package com.mpig.api.modelcomet;

/**
 * 
 * @author tosy
 *	邀请或撤销邀请连麦
 */
public class LiveInviteCMod extends BaseCMod{
	public LiveInviteCMod(){
		this.setCometProtocol(CModProtocol.live_invite);
	}
	
	//是否取消邀请    0邀请 1取消邀请
	private Integer isCanncel;
	
	//申请人是否正在开播
	private Boolean isLiving;		
	
	public Integer getIsCanncel() {
		return isCanncel;
	}

	public void setIsCanncel(Integer isCanncel) {
		this.isCanncel = isCanncel;
	}

	public Boolean getIsLiving() {
		return isLiving;
	}

	public void setIsLiving(Boolean isLiving) {
		this.isLiving = isLiving;
	}
}