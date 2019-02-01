package com.mpig.api.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.mpig.api.model.AuthenticationModel;
import com.mpig.api.model.ReturnModel;
import com.mpig.api.model.RobotBaseInfoModel;
import com.mpig.api.model.UserAccountModel;
import com.mpig.api.model.UserAssetModel;
import com.mpig.api.model.UserBaseInfoModel;
import com.mpig.api.model.UserXiaozhuAuthModel;
import com.mpig.api.modelcomet.BaseCMod;

public interface IUserService {

	/**
	 * 根据UID获取账户信息
	 *
	 * @param uid
	 * @param bl
	 *            =true 从数据库中读取， =false 从缓存中读取
	 * @return
	 */
	UserAccountModel getUserAccountByUid(int uid, Boolean bl);

	/**
	 * 根据第三方标志获取用户信息
	 *
	 * @param authKey
	 * @param bl
	 *            =true 从数据库中读取， =false 从缓存中读取
	 * @return
	 */
	UserAccountModel getUserAccountByAuthKey(String authKey, Boolean bl);
	

	/**
	 * 根据第三方标志获取用户信息
	 *
	 * @param authKey
	 * @param bl
	 *            =true 从数据库中读取， =false 从缓存中读取
	 * @return
	 */
	UserAccountModel getUserAccountByUnionId(String unionid, Boolean bl);

	/**
	 * 根据帐号获取用户信息
	 *
	 * @param accountname
	 * @param bl
	 * @return
	 */
	UserAccountModel getUserAccountByAccountName(String accountname, boolean bl);

	/**
	 * 插入用户账号信息
	 *
	 * @param userAccountModel
	 * @return
	 */
	int InsertUserAccount(Object... objects);

	/**
	 * 根据用户UID获取用户资产信息
	 *
	 * @param uid
	 * @param bl
	 *            =true 从数据库中读取， =false 从缓存中读取
	 * @return
	 */
	UserAssetModel getUserAssetByUid(int uid, Boolean bl);

	/**
	 * 插入用户资产信息
	 *
	 * @param userAssetModel
	 * @return
	 */
	int InsertUserAsset(Object... objects);

	/**
	 * 根据用户uid获取用户基本信息
	 *
	 * @param uid
	 * @param bl
	 *            =true 从数据库中读取， =false 从缓存中读取
	 * @return
	 */
	UserBaseInfoModel getUserbaseInfoByUid(int uid, Boolean bl);
	
	/**
	 * 根据用户uid获取机器人基本信息
	 * @param uid
	 * @param bl
	 * @return
	 */
	RobotBaseInfoModel getRobotbaseInfoByUid(int uid, Boolean bl);

	/**
	 * 根据用户uid获取用户基本信息
	 *
	 * @param uid
	 * @param bl
	 *            =true 从数据库中读取， =false 从缓存中读取
	 * @return
	 */
	Map<String, UserBaseInfoModel> getUserbaseInfoByUid(String... uids);
	
	/**
	 * 批量获取用户Account信息
	 * @param uids
	 * @return
	 */
	Map<String, UserAccountModel> getUserAccountByUid(String... uids);

	/**
	 * 插入用户基本信息
	 *
	 * @param userBaseInfoModel
	 * @return
	 */
	int InsertUserBaseInfo(Object... objects);

	/**
	 * 获取登录后的用户基本信息
	 *
	 * @param uid
	 * @return
	 */
	Map<String, Object> getAuthUserInfo(int uid);

	/**
	 * 生成uid
	 *
	 * @return
	 */
	int getUid();

	/**
	 * 生成靓号accountId
	 *
	 * @return
	 */
	int getAccountId();

	/**
	 * 获取用户简介
	 *
	 * @param uid
	 */
	Map<String, Object> getUserProfile(int uid);
	
	
	List<Map<String, Object>> getUserProfile(String... uids);

	/**
	 * 获取机器人简介
	 *
	 * @param uid
	 */
	Map<String, Object> getRobotProfile(int uid);

	/**
	 * 获取机器人简介
	 *
	 * @param uid
	 */
	List<Map<String, Object>> getRobotProfile(String... uids);

	/**
	 * 用户送礼变更信息
	 *
	 * @param uid
	 * @param money
	 * @param wealths
	 * @return
	 */
	int updUserAssetBySendUid(int uid, int money, int wealths);


	/**
	 * 用户内兑猪头
	 *
	 * @param uid
	 * @param money
	 * @return
	 */
	int updUserAssetByExchangeUid(int uid, int credit,int money);

	/**
	 * PC兑换 冻结声援值
	 * @param uid
	 * @param credit
	 * @return
	 */
	int updUserAssetByPcExchange(int uid, int credit);
	/**
	 * 用户发红包
	 * 
	 * @param uid
	 * @param money
	 * @return
	 */
	int updUserAssetByRedEnvelopUid(int uid, int money);

	/**
	 * 收礼用户变更信息
	 *
	 * @param uid
	 * @param gets 声援值
	 * @param getsTotal 声援总值
	 * @return
	 */
	int updUserAssetByGetUid(int uid, Double gets, Double getsTotal);

	/**
	 * 充值后更新余额
	 *
	 * @param uid
	 * @param money
	 * @return
	 */
	int updAssetMoneyByUid(int uid, Double money);

	/**
	 * 更新用户等级
	 *
	 * @param level
	 * @param uid
	 * @param type
	 *            =1表示用户 =2表示主播
	 * @return
	 */
	int updUserBaseInfoLevelByUid(int level, int uid, int type);

	/**
	 *  修改开播状态 和游戏状态信息
	 * @param uid 
	 * @param gameId
	 * @param status
	 * @return
	 */
	 
	int updUserBaseInfoLiveStatusByUid(Integer uid, Boolean status);

	/**
	 * 获取用户资料 弹层使用
	 *
	 * @param dstUid
	 *            资料用户
	 * @param uid
	 *            当前用户
	 * @return
	 */
	Map<String, Object> getUserDataMap(int dstUid, int uid);
	

	/**
	 * 房间内用户信息 弹层使用
	 *
	 * @param curUid
	 *            当前用户
	 * @param dstUid
	 *            获取资料用户
	 * @param anchorUid
	 *            主播
	 * @return
	 */
	Map<String, Object> getUserDataInRoomMap(int curUid, int dstUid, int anchorUid);

	/**
	 * 获取用户卡片上的信息
	 * 
	 * @param srcUid
	 * @param dstUid
	 * @return
	 */
	Map<String, Object> getUserCardMap(int srcUid, int dstUid,int anchorUid);

	/**
	 * 关注后rpc通知聊天服务器
	 */
	void rpcAdminFollow(Integer uid, Integer dstUid, Boolean bfollow);

	/**
	 * 修改用户昵称
	 *
	 * @param uid
	 * @param nickName
	 * @return
	 */
	int updUserNickName(int uid, String nickName);

	/**
	 * 修改签名
	 *
	 * @param uid
	 * @param signature
	 * @return
	 */
	int updUserSignature(int uid, String signature);
	
	/**
	 * 修改星座
	 * @param uid
	 * @param constellation 星座
	 * @return
	 */
	int updUserConstellation(int uid, String constellation);
	/**
	 * 修改爱好
	 * @param uid
	 * @param hobby 爱好
	 * @return
	 */
	int updUserHobby(int uid, String hobby);

	/**
	 * 修改密码
	 *
	 * @param uid
	 * @param pword
	 * @return
	 */
	int updUserPwordByUid(int uid, String pword);

	/**
	 * 获取下个等级的值
	 *
	 * @param level
	 * @param type
	 *            =user =anchor
	 * @return
	 */
	Long getNextLevel(int level, String type);

	/**
	 * 关注
	 *
	 * @param type
	 * @param srcUid
	 *            关注了 dstUid
	 * @param dstUid
	 * @return
	 */
	void addFollows(String type, Integer srcUid, Integer dstUid);

	/**
	 * 用户中心关注列表
	 *
	 * @param uid
	 */
	void getFollowsCenter(Integer curUid, Integer dstUid,ReturnModel returnModel);


	/**
	 * 用户中心关注列表
	 *
	 * @param uid
	 */
	void getPCFollowsCenter(Integer curUid,ReturnModel returnModel);
	
	/**
	 * 设置关注主播 是否有推送信息的权利
	 *
	 * @param srcUid
	 * @param dstUid
	 * @param type
	 */
	void setPushSwitch(Integer srcUid, Integer dstUid, int type);

	/**
	 * 发送验证码
	 *
	 * @param mobile
	 * @return
	 */
	Boolean sendMobileCode(String mobile, String code);

	/**
	 * 获取声援榜
	 *
	 * @param dstUid
	 *            该用户的声援
	 * @param uid
	 *            操作的用户
	 * @param page
	 * @param mode /null/day/week
	 */
	void getSupportByUid(Integer dstUid, Integer uid, Integer page, ReturnModel returnModel,String mode);
	
	/**
	 * 获取声援榜
	 * @param dstUid 该用户的声援
	 * @param uid 操作的用户
	 * @param page
	 */
	List<Map<String, Object>> getSupportByUid(Integer dstUid, Integer start, Integer rows);
	/**
	 * 绑定手机号码
	 *
	 * @param uid
	 * @param mobile
	 * @return
	 */
	int bindMobileByUid(Integer uid, String mobile);

	/**
	 * 修改性别
	 *
	 * @param uid
	 * @param sex
	 * @return
	 */
	int updUserSex(Integer uid, Boolean sex);

	/**
	 * 获取主播信息（进房间的时候获取）
	 *
	 * @param srcUid
	 * @param dstUid
	 * @return
	 */
	Map<String, Object> getLivIngInfo(Integer srcUid, Integer dstUid);

	/**
	 * 修改图像
	 *
	 * @param uid
	 * @param urlImg
	 * @return
	 */
	int updUserBaseHeadimg(Integer uid, String headimage, String livimg);

	/**
	 * 获取可提现的金额
	 *
	 * @param uid
	 */
	void getWithDraw(int uid, ReturnModel returnModel);

	/**
	 * 提现
	 *
	 * @param type
	 *            =alipay =weixin
	 * @param amount
	 *            单位 元
	 * @param uid
	 */
	void tixian(String type, int amount, Integer uid, ReturnModel returnModel);

	/**
	 * 获取提现记录
	 *
	 * @param uid
	 */
	void getTixianList(int uid, ReturnModel returnModel);
	
	/**
	 * 根据id获取该笔提现数据详细
	 * @param id
	 * @return
	 */
	Map<String, Object> getTixianInfoById(int id);

	int updateWeiboVrified(int uid, boolean vrified, String verified_reason);

	int updateUserIdentity(int uid, int identity);

	int updateUserAccountStatus(int uid, int status, ReturnModel returnModel);

	void pushUserMessage(int uid, BaseCMod cMod);

	JSONObject getUserWXToken(String code);

	JSONObject getUserInfoByToken(String token, String openid);


	/**
	 * 封号
	 * 
	 * @param uid
	 */
	void forbidAccountByManage(int uid, ReturnModel returnModel);

	/**
	 * 解封
	 * 
	 * @param uid
	 */
	void unForbidAccountByManage(int uid, ReturnModel returnModel);

	/**
	 * 修改用户位置
	 * 
	 * @param uid
	 * @param city
	 */
	void changeCityByUid(int uid, String city, ReturnModel returnModel);

	/**
	 * 获取机器人
	 * 
	 * @return
	 */
	List<Map<String, Object>> getRobotList();

	/**
	 * 获取热门主播
	 * 
	 * @return
	 */
	List<Map<String, Object>> getAnchorList();

	/**
	 * 设置用户推荐值 和 人气
	 * 
	 * @param uid
	 * @param recommend
	 * @param rq
	 */
	void setRecomendRQ(int uid, int recommend, int rq,int grade, ReturnModel returnModel);

	/**
	 * 获取用户的充值记录
	 * 
	 * @param uid
	 * @param returnModel
	 */
	void getPaylist(int uid, ReturnModel returnModel);
	

	/**
	 * 添加银行卡认证
	 * 
	 * @param uid
	 * @param realName
	 *            用户真实姓名
	 * @param cardID
	 *            身份证ID
	 * @param cardNo
	 *            银行卡号
	 * @param bankAccount
	 *            开户银行
	 * @param provinceOfBank
	 *            开户行省份
	 * @param cityOfBank
	 *            开户行城市
	 * @param branchBank
	 *            支行名称
	 * @param positiveImage
	 *            正面照
	 * @param negativeImage
	 *            反面照
	 * @param handImage
	 *            手持照
	 * @return
	 */
	int insertAuthentication(int uid, String realName, String cardID, String cardNo, String bankAccount,
			String provinceOfBank, String cityOfBank, String branchBank, String positiveImage, String negativeImage,
			String handImage);

	/**
	 * 查询是否已实名认证
	 * @param uid
	 * @return
	 */
	int getAuthenticationForStatus(int uid);
	/**
	 * 获取身份认证信息
	 * 
	 * @param uid
	 * @param returnModel
	 */
	AuthenticationModel getAuthentication(int uid);
	
	Map<String, Object> getUserCard(int srcUid,int dstUid);

	/**
	 * 更新用户经验
	 * @param uid
	 * @param exp
	 * @return
	 */
	int updateUserExp(int uid, int exp);

	/**
	 * 新增用户经验
	 * @param uid
	 * @param exp
	 * @return
	 */
	Long addUserExpByTask(int uid, int exp);

	/**
	 * 获取用户经验值
	 * @param uid
	 * @return
	 */
	Long getUserExp(int uid);
	
	/**
	 * 获取手机号码
	 * @param uid
	 * @return
	 */
	String getUserPhone(int uid);
	
	/**
	 * 获取用户等级值
	 * @param uid
	 * @return
	 */
	int getUserLevel(int uid);
	
	/**
	 * 新增用户财富值
	 * @param uid
	 * @param wealth
	 * @param reason 
	 * @return
	 */
	int addUserWealth(int uid, int zhutou,int reason,int taskId,String taskName);
	
	/**
	 * 声援值兑换
	 * @param uid 用户uid
	 * @param amount 兑换金额
	 * @return
	 */
	void exchange(int uid,int amount,ReturnModel returnModel);
	
	/**
	 * 内兑配置
	 * @param returnModel
	 */
	void exchangeConfig(ReturnModel returnModel);
	
	/**
	 * 内兑记录
	 * @param uid
	 * @param returnModel
	 */
	void exchangeList(int uid,ReturnModel returnModel);
	
	/**
	 * 查询是否已小猪认证
	 * @param uid
	 * @return
	 */
	int selectXiaozhuAuthForStatus(int uid);
	/**
	 * 插入小猪认证资料
	 * @param uid 用户id
	 * @param nickname 用户认证资料
	 * @param authContent 用户认证内容
	 * @param authPics 用户提交的认证图片
	 * @param authURLs 用户提交的认证链接地址
	 * @return
	 */
	int insertXiaozhuAuth(int uid, String nickname, String authContent, String authPics, String authURLs);
	
	/**
	 * 根据uid获取小猪认证
	 * @param uid
	 * @return
	 */
	public UserXiaozhuAuthModel getXiaozhuAuth(int uid);
	
	/**
	 * 删除认证相关数据
	 * @param uid
	 * @return
	 */
	public int cannelXzAuth(int uid);
	
	/**
	 * 获取用户当前经验值 及下一级对应的经验值
	 * @param uid
	 * @param returnModel
	 */
	public void getUserExpNextLevel(int uid,ReturnModel returnModel);
	
	/**
	 * 根据昵称获取认证昵称是否存在
	 * @param nikename
	 * @return
	 */
	public int getAuthNikenameCount(String nikename);
	
	/**
	 * 获取当前用户的新浪认证数据
	 * @param uid
	 * @return
	 */
	public Map<String, Object> getSinaVerified(Integer uid);

	/**
	 * 获取uid的相互关注的开播中的好友列表
	 * @param uid
	 * @param returnModel
	 */
	void getUserFriendsInliving(int uid,int os,int page, ReturnModel returnModel);

	/**
	 * 获取uid所有录像视频列表
	 * @param uid
	 * @return
	 */
	public  Map<String,String> getRecordAllByUid(String uid,ReturnModel returnModel);

	/**
	 * 删除uid中对应time的record
	 * @param uid
	 * @param time
	 * @param returnModel
	 */
	public void delmyRecordByTime(int uid, String time, ReturnModel returnModel);
	
	/**
	 * 插入user_cover_check表数据
	 *
	 * @param userAccountModel
	 * @return
	 */
	int InsertUserCover(int uId,String picCover,String picCover1,String picCover2);
	
	Map<String, Object> isNullId(int id,int status);
	
	boolean updUserCover(int uId,String picCover,String picCover1,String picCover2);
	
	public Map<String, Object> getStatus(int uId);
	
	public Map<String, Object> getNewestRecord(int uId);
	/**
	 * @param uid	被操作的人uid
	 * @param zhutou 要修改的资产数量
	 * @param credit 声援值（type=0此参数无效）
	 * @param type	1增加 0消费
	 * @return
	 */
	ReturnModel modifyMoney(int uid,int zhutou,int credit,int type,String desc);
	
	/**
	 * 修改直播间游戏信息
	 * @param gameStatus  游戏状态 （0:没有;1;有）
	 * @param gameId 游戏ID
	 * @return
	 */
	public int updUserBaseGameStatusById(int uid ,Integer gameStatus,Long gameId);

	/**
	 * 更新PK状态（一组）
	 * @param firstUid 主播ID
	 * @param secodUid 第二个主播ID
	 * @return
	 */
	public boolean updUserBasePKStatusById(int firstUid,int secodUid,int lianmaiStatus);

	/**
	 * 更新或添加用户金额
	 * @param dstUid
	 * @param imoney
	 * @return
	 */
	int updateUserMoneyRbm(String dstUid, Double imoney);
	
	/**
	 * 获取该主播的新声援值贡献榜
	 * @param dstuid 主播ID
	 * @param uid 当前用户ID
	 * @param page 页码
	 * @param type 1:当天;2:当周;3:当月;4:总值;
	 * @return
	 */
	public Map<String, Object> getSupportContributionRankByDstuid(int dstuid,int uid, int type, int page);

	/**
	 * 修改用户昵称、签名、爱好、头像、封面
	 * @param uid 用户UID
	 * @param nickname 昵称
	 * @param signature 签名
	 * @param hobby 爱好
	 * @param headimage 头像
	 * @param livimage 封面
	 * @return
	 */
	public int  updUserBaseInfoByUid(Integer uid, String nickname,Boolean sex,String signature, String hobby, String headimage,String livimage);
}
