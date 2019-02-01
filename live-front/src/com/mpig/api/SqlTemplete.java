package com.mpig.api;

public interface SqlTemplete {

	// 生成uid
	String SQL_GetUid = "INSERT INTO user_id(ADDTIME)VALUES(?)";
	// 生成靓号
	String SQL_GetAccountId = "INSERT INTO account_id(ADDTIME)VALUES(?)";

	// 根据用户UID，获取用户账户信息
	String SQL_GetUserAccountByUid = "SELECT uid,accountid,accountname,password,authkey,status,unionId FROM %s WHERE uid = ?";
	// 根据authkey获取用户账户信息
	String SQL_GetUserAccountByAuthKey = "SELECT uid,accountid,accountname,password,authkey FROM ? WHERE authkey=?";
	// 新增用户账户信息
	String SQL_InsertUserAccount = "INSERT INTO %s (uid,accountid,accountname,password,authkey,unionId)VALUES(?,?,?,?,?,?)";

	String SQL_updUserPwordByUid = "UPDATE %s SET password = ? WHERE uid = ?";

	String SQL_updAccountIdByUid = "update %s set accountid=? where uid=?";
	
	String SQL_updUnionIdByUid = "update %s set unionId=? where uid=?";

	// 根据用户UID，获取用户资产信息
	String SQL_GetUserAssetByUid = "SELECT uid,money,wealth,credit,creditTotal FROM %s WHERE uid=?";
	// 新增用户资产信息
	String SQL_InsertUserAsset = "INSERT INTO %s (uid,money,wealth,credit)VALUES(?,0,0,0);";

	// 用户送礼变更信息
	String SQL_UpdUserAssetBySendUid = "UPDATE %s SET money = money-? ,wealth = wealth+? WHERE uid = ? and money >= ?";

	// 增加用户资产
	String SQL_UpdUserAssetJia = "update %s set money = money + ?,credit = credit + ?,creditTotal = creditTotal + ? where uid = ?";
	// 消费用户资产（玩游戏）
	String SQL_UpdUserAssetJian = "UPDATE %s SET money = money-? ,wealth = wealth+? WHERE uid = ? and money >= ?";
	// 记录用户资产变更
	String SQL_InsertUserMoneyChange = "INSERT INTO user_money_change(uid,beforeMoney,type,money,createTime,memo)SELECT uid,money,%s,%s,%s,'%s' FROM %s WHERE uid=%s";

	// 用户送礼变更信息
	String SQL_UpdUserItemBySendUid = "UPDATE user_item SET num = num-?  WHERE uid = ? and gid = ? and num >= ?";
	
	//根据指定的分类查询用户背包礼物记录信息
	String SQL_SelUserItemLogBySubType = "SELECT uid,gid,num,subtype,addtime FROM user_item_log WHERE sourc = ? order by id desc limit ?,?";
	// 用户送红包更新信息
	String Sql_UpdUserAssetByRedEnevlopUid = "UPDATE %s SET money = money-? WHERE uid = ? and money >= ?";
	
	String Sql_UpdUserAssetCreditByUid = "UPDATE %s SET credit = credit-?,money=money+? WHERE uid = ? and credit >= ?";
	String Sql_UpdUserAssetCreditForExchange = "UPDATE %s SET credit = credit-?,frozenCredit=frozenCredit+? WHERE uid = ? and credit >= ?";

	// 收礼用户变更信息
	String SQL_UpdUserAssetByGetUid = "UPDATE %s SET credit = credit+? ,creditTotal = creditTotal+? WHERE uid = ?";
	// 充值后更新用户余额值
	String SQL_UpdAssetMondyByUid = "UPDATE %s SET money = money+? WHERE uid = ?";
	// 提现后更新用户收到的声援值
	String SQL_UpdAssetCreditByUid = "UPDATE %s SET credit = credit-? WHERE uid = ? and credit>=?";
	// 提现失败后归还声援值
	String SQL_UpdAssetRetCreditByUid = "UPDATE %s SET credit = credit + ? WHERE uid = ?";

	// 根据用户UID 获取用户基本信息
	String SQL_GetUserBaseInfoByUid = "SELECT uid,nickname,sex,anchorLevel,userLevel,exp,identity,headimage,livimage,birthday,phone,province,city,signature,registip,registtime,registchannel,subregistchannel,registos,registimei,liveStatus,opentime,recommend,videoline,familyId,verified,verified_reason,contrRq,constellation,hobby,pcimg1,pcimg2,grade,gameStatus,gameId,lianmaiStatus,lianmaiAnchorId FROM %s WHERE uid=?";
	String SQL_GetRobotList = "SELECT uid,nickname,sex,anchorLevel,userLevel,identity,headimage,livimage,birthday,phone,province,city,signature,registip,registtime,registchannel,subregistchannel,registos,registimei,liveStatus,opentime,recommend,videoline,familyId,verified,verified_reason,contrRq FROM robot_base_info";
	String SQL_GetRobotByUid = "SELECT uid,nickname,sex,anchorLevel,userLevel,identity,headimage,livimage,birthday,phone,province,city,signature,registip,registtime,registchannel,subregistchannel,registos,registimei,liveStatus,opentime,recommend,videoline,familyId,verified,verified_reason,contrRq FROM robot_base_info where uid=?";
	// 新增用户基本信息
	String SQL_InsertUserBaseInfo = "INSERT INTO %s (uid,nickname,sex,identity,headimage,livimage,birthday,phone,province,city,signature,registip,registtime,registchannel,subregistchannel,registos,registimei)VALUES(?,?,?,1,?,?,'',?,?,?,?,?,?,?,?,?,?)";
	// 修改用户等级
	String SQL_UpdUserBaseInfoUserLevelByUid = "UPDATE %s SET userLevel = ? WHERE uid = ?";
	// 修改用户的推荐值和人气值和评级
	String SQL_UpdUserRecommendByUid = "UPDATE %s SET recommend = ?,contrRq=?,grade=? WHERE uid = ?";

	// 修改主播等级
	String SQL_UpdUserBaseInfoAnchorLevelByUid = "UPDATE %s SET anchorLevel = ? WHERE uid = ?";
	// 修改昵称
	String SQL_UpdUserNickNameByUid = "UPDATE %s SET nickname = ? WHERE uid = ?";
	// 修改开播状态
	String SQL_UpdUserBaseLivingByUid = "UPDATE %s SET liveStatus = ?,opentime=? WHERE uid = ?";
	// 修改开播状态 和 游戏信息  add by 2017-11-10
	String SQL_UpdUserBaseLiveAndGameByUid = "UPDATE %s SET liveStatus = ?,gameStatus = ?,gameId=?,opentime=? WHERE uid = ?";
	// 修改下播状态
	String SQL_UpdUserBaseOffByUid = "UPDATE %s SET liveStatus = ? WHERE uid = ?";
	// 修改签名
	String SQL_UpdUserSignatureByUid = "UPDATE %s SET signature = ? WHERE uid = ?";
	// 修改星座
	String SQL_UpdUserConstellationByUid = "UPDATE %s SET constellation = ? WHERE uid = ?";
	// 修改爱好
	String SQL_UpdUserHobbyByUid = "UPDATE %s SET hobby = ? WHERE uid = ?";
	// 修改经验值
	String SQL_UpdUserExpByUid = "UPDATE %s SET exp = ? WHERE uid = ?";
	// 增加经验值
	String SQL_AddUserExpByUid = "UPDATE %s SET exp = exp+?  WHERE uid = ?";
	// 修改图像
	String SQL_UpdUserHeadimageByUid = "UPDATE %s SET headimage = ?,livimage=? WHERE uid = ?";
	// 修改性别
	String SQL_UpdUserSexByUid = "UPDATE %s SET sex = ? WHERE uid = ?";
	// 绑定手机号码
	String SQL_UpdUserMobileByUid = "UPDATE %s SET phone = ? WHERE uid = ?";
	//更新游戏状态
	String SQL_UpdUserGameInfo = "UPDATE %s SET gameStatus = ?,gameId=? WHERE uid = ?";
	
	//更新PK状态
	String SQL_UpdUserPKStatus = "UPDATE %s SET lianmaiStatus = ?,lianmaiAnchorId=? WHERE uid = ?";

	// 更新用户认证信息
	String SQL_UpdateUserVerifiedByUid = "UPDATE %s SET verified = ?,verified_reason = ? WHERE uid = ?";
	
	//查询新浪认证状态及认证内容
	String SQL_selSinaVerifiedByUid = "SELECT verified,verified_reason FROM %s WHERE uid = ?";
	
	// 更新用户身份
	String SQL_UpdateUserIdentity = "UPDATE %s SET identity = ? WHERE uid = ?";

	// 更新用户位置
	String SQL_UpdateUserCity = "UPDATE %s SET city = ? WHERE uid = ?";

	// 更新用户账户状态
	String SQL_UpdateUserAccountStatus = "UPDATE %s SET status = ? WHERE uid = ?";
	
	// 获取开播的主播列表
	String SQL_GetLivingList = "SELECT * FROM live_info WHERE status=1 limit ?,?";
	// 非第一次开播
	String SQL_UpdLiveInfoByUid = "UPDATE live_info SET slogan = ?,province = ?,city = ?,status = ?,opentime = ? WHERE uid = ?";
	// 修改开播相片
	String SQL_UpdLivInfoPicByUid = "UPDATE live_info SET mobileliveimg = ? WHERE uid = ?";
	// 下播
	String SQL_UpdLiveInfoStatusByUid = "UPDATE live_info SET status = ? WHERE uid = ?";
	// 修改开播其他信息
	String SQL_UpdLiveInfoOtherByUid = "UPDATE live_info SET ,recommend = ?,videoline = ?, sort = ? WHERE uid = ?";

	// 获取开播信息
	String SQL_GetLiveInfoByUid = "SELECT id,uid,familyId,slogan,province,city,mobileliveimg,status,opentime,recommend,videoline,sort FROM live_info WHERE uid=?";
	// 新增开播信息
	String SQL_InsertLiveInfo = "INSERT INTO live_info(uid,mobileliveimg,status,opentime,recommend,videoline,sort)VALUES(?,?,?,?,?,?,?)";

	// 下播修改结束时间和状态
	String SQL_UpdLiveMicTimeById = "UPDATE live_mic_time SET endtime = ? ,TYPE = ?,audience = ? ,views = ? ,likes = ? ,credit = ? WHERE id = ?";
	String SQL_InsertLiveMicTime = "INSERT INTO live_mic_time (uid,os,slogan,province,city,starttime,addtime)VALUES(?,?,?,?,?,?,?)";
	String SQL_GetLiveMicTimeById = "SELECT starttime FROM live_mic_time WHERE id=?";

	String SQL_GetLiveMicInfoByUid = "SELECT id,uid,slogan,province,city,starttime,endtime,audience,views,likes,credit,TYPE,ADDTIME FROM live_mic_time WHERE uid=? AND TYPE=0 Order by starttime desc limit 1";
	String SQL_GetLiveMicInfoOpenedByUid = "SELECT id,uid,slogan,province,city,starttime,endtime,audience,views,likes,credit,TYPE,ADDTIME FROM zhu_live.live_mic_time WHERE uid=? AND TYPE=1 Order by starttime desc limit 1";
	// 房间的管理员列表
	String SQL_GetLiveManageListOfAnchor = "SELECT anchorUid,userUid,isvalid,creatAt,modifyAt from live_manage where anchorUid=? and isvalid=1";
	// 用户主播 管理关系
	String SQL_GetLiveManageByAnchorUser = "SELECT anchorUid,userUid,isvalid,creatAt,modifyAt from live_manage where anchorUid=? AND userUid=?";
	// 用户管理的房间列表
	String SQL_GetLiveManageListByUser = "SELECT anchorUid,userUid,isvalid,creatAt,modifyAt from live_manage where userUid=? and isvalid=1";
	// 删除管理员
	String SQL_DelLiveManageByAnchorUser = "update live_manage set isvalid =? where  anchorUid=? AND userUid=?";
	// 新增管理员
	String SQL_AddLiveManageByAnchorUser = "insert into live_manage(anchorUid,userUid,isvalid,creatAt,modifyAt)value(?,?,?,?,?)";

	String SQL_GETLiveManageByAnchorUser = "select isvalid from live_manage where anchorUid=? and userUid=?";

	//获取所有礼物列表（有效的）
	String SQL_GetGiftConfigAllNew = "select * from config_giftlist where isvalid=1 order by gprice asc,createAt desc";
	
	//获取全部任务列表
	String SQL_GetTaskConfigAll = "SELECT * FROM task_config";
	
	//获取等级列表
	String SQL_GetLevelConfigAll = "SELECT * FROM level_config";
	
	//获取签到列表
	String SQL_GetSignConfigAll = "SELECT * FROM sign_config";

	// 新增账单
	String SQL_InsertBill = "INSERT INTO %s (gid,count,price,srcuid,srcleftmoney,srcwealth,srccredit,dstuid,dstleftmoney,dstwealth,dstcredit,addtime,type,getmoney,os,bak,srcnickname,dstnickname,familyid,gflag)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,0)";
	//新增背包礼物账单
	String SQL_InsertBagBill = "INSERT INTO %s (gid,count,price,srcuid,srcleftmoney,srcwealth,srccredit,dstuid,dstleftmoney,dstwealth,dstsupport,addtime,type,getmoney,os,bak,srcnickname,dstnickname,familyid,support,totalsupport,gflag)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,1)";
	//新增背包礼物账单
	//String SQL_InserBagBill = "INSERT INTO %s (gid,count,price,srcuid,srcleftmoney,srcwealth,srccredit,dstuid,dstleftmoney,dstwealth,dstsupport,addtime,type,getmoney,os,bak,srcnickname,dstnickname,familyid,support,totalsupport)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	// 新增红包账单
	String SQL_InsertBillRedEnvelope = "insert into bill_redenvelope (roomId,reddesc,srcUid,dstUid,sendmoney,sendcnts,sendtime)value(?,?,?,?,?,?,?)";
	// 修改红包信息
	String SQL_UpdBillRedEnvelopeById = "update bill_redenvelope set isfinish=?,getmoney=getmoney+?,getcnts=getcnts+?,gettime=? where id=?";

	String SQL_GetBiilRedEnvelopeById = "select id,roomId,reddesc,isfinish,srcUid,dstUid,sendmoney,sendcnts,getmoney,getcnts,sendtime,gettime from bill_redenvelope where id=?";
	// 新增反馈信息
	String SQL_InsertFeedback = "INSERT INTO web_feedback (uid,cls,des,mobile,createAt)VALUES(?,?,?,?,?)";
	// 获取banner数据
	String SQL_GetBanners = "SELECT id,picUrl,webPicUrl,jumpUrl,sort,startShow,endShow,switch,createAT,platform,name,type,roomId,roomType FROM web_banner WHERE switch=1 and type=1 order by sort desc;";

	String SQL_GetWebVer = "select iosVer,iosAt,androidVer,androidAt from web_ver";

	String SQL_DelOrderByInpourNo = "delete from pay_order where inpour_no=?";
	String SQL_InsertPayOrder = "INSERT INTO pay_order (order_id,srcuid,dstuid,amount,creatAt,paytime,os,paytype,status,inpour_no, details,userSource,channel,registtime)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	String SQL_SelectPayOrder = "select order_id, paytime,paytype,amount,zhutou,os from pay_order where status=2 and srcuid=? and paytime>0 order by paytime desc";
	String sQL_SelectPayOrderTotalByMonth = "select date_format(from_unixtime(paytime),'%Y-%m') as paytimes,sum(amount) as amount,sum(zhutou) as zhutou from pay_order where status=2 and srcuid=? and paytime>0 group by paytimes order by paytimes desc";
	String SQL_InsertPayOrderByApple = "INSERT INTO pay_order (order_id,srcuid,dstuid,amount,zhutou,creatAt,paytime,os,paytype,status,inpour_no, details,userSource,channel,registtime)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	String SQL_UpdPayOrderStatus = "UPDATE pay_order SET amount = ? ,zhutou=? , paytime = ? ,status = ? ,inpour_no = ? WHERE order_id = ? and status in(0,1)";
	String SQL_UpdPayType = "UPDATE pay_order SET paytype = ? WHERE order_id = ? and status in(0,1)";
	String SQL_GetPayOrderByOrderNo = "SELECT order_id,srcuid,dstuid,amount,creatAt,paytime,os,paytype, STATUS, inpour_no, details,userSource FROM pay_order WHERE order_id=?";
	String SQL_GetFirstPayByUid = "SELECT count(*) as cnts FROM pay_order WHERE status=2 and srcuid=? and paytime>1470412800";
	String SQL_ExistOrderByInpourNo = "SELECT status FROM pay_order WHERE inpour_no=?";
	String SQL_UpdStatusByInpourNo = "UPDATE pay_order SET status = 2 WHERE inpour_no=?";

	
	String SQL_InsertExchange = "insert into pay_exchange(uid,credit,money,zhutou,createAT,rate)value(?,?,?,?,?,?)";
	String SQL_GetExchangeList = "SELECT order_id,srcuid,dstuid,amount,creatAt,paytime,os,paytype, STATUS, inpour_no, details,userSource FROM pay_order WHERE inpour_no=?";

	// 添加抢红包记录
	String SQL_InsertPayGetRedEnvelop = "insert into pay_get_redenvelop (envelopeid,srcUid,dstUid,roomId,money,getTime)value(?,?,?,?,?,?)";

	// 根据红包ID查询 红包被抢的记录
	String SQL_GetPayGetRedEnvelopByenvelopId = "select id,envelopeid,srcUid,dstUid,roomId,money,getTime from pay_get_redenvelop where envelopeid=?";
	String SQL_GetPayGetRedEnvelopByEIdUid = "select id,envelopeid,srcUid,dstUid,roomId,money,getTime from pay_get_redenvelop where envelopeid=? and dstUid=?";

	// 获取用户提现的账号
	String SQL_GetPayAcountByUid = "SELECT uid,wx_openid,wx_unionid,alipay,isUse,createAt FROM pay_account WHERE uid=?";
	String SQL_InsertPayAccount = "INSERT INTO pay_account(uid,wx_unionid,alipay,createAt)VALUES(?,?,?,?)";
	String SQL_InsertPayActivity = "insert into pay_activity(uid,act_id,act_name,zhutou,credit,createAt,source)value(?,?,?,?,?,?,?)";
	String SQL_UpdPayAccount = "UPDATE pay_account SET wx_unionid = ?,alipay = ? WHERE uid = ? ";

	String SQL_BindWeixin = "UPDATE pay_account SET wx_openid = ? where wx_unionid = ?";
	String SQL_GetUidByUnionid = "SELECT uid FROM pay_account WHERE wx_unionid = ?";
	String SQL_updisUsePayAccountByUid = "update pay_account  set isUse = ? where uid= ? ";

	// 添加提现单
	String SQL_InsertWithDraw = "INSERT INTO zhu_pay.pay_withdraw (billno,uid,type,amount,credit,sendListid,isSecc,createAt,sendTime,openid)VALUES(?,?,?,?,?,?,?,?,?,?)";
	String SQL_UpdWithDraw = "UPDATE pay_withdraw SET sendListid = ?, isSecc = ? ,sendTime = ? WHERE billno= ? AND openid=? AND TYPE=?";
	String SQL_UpdWithDrawById = "UPDATE pay_withdraw SET sendListid = ?, isSecc = ? ,sendTime = ?,adminid = ? WHERE id = ? ";
	String SQL_GetWithDrawList = "SELECT id,billno,uid,type,amount,credit,sendListid,isSecc,createAt,sendTime FROM pay_withdraw WHERE uid = ? order by createAt desc ";
	String SQL_GetWithDrawINfoById = "SELECT id,billno,uid,type,amount,credit,sendListid,isSecc,createAt,sendTime,openid FROM pay_withdraw WHERE id = ? ";

	String SQL_InsertAuthen = "insert into user_authentication(realName,cardID,cardNo,bankAccount,provinceOfBank,cityOfBank,branchBank,positiveImage,negativeImage,handImage,createAt,auditStatus,uid)value(?,?,?,?,?,?,?,?,?,?,?,?,?)";
	String SQL_selAuthen = "select id, realName,cardID,cardNo,bankAccount,provinceOfBank,cityOfBank,branchBank,createAt,auditAt,uid,auditStatus,cause,positiveImage,negativeImage,handImage from user_authentication where uid=?";
	String SQL_UpdateAuthen = "update user_authentication set realName=?,cardID=?,cardNo=?,bankAccount=?,provinceOfBank=?,cityOfBank=?,branchBank=?,positiveImage=?,negativeImage=?,handImage=?,createAt=?,auditStatus=? where uid=?";
	String SQL_selAuthenForStatus = "SELECT id FROM user_authentication WHERE auditStatus = 3 AND uid=?";
	String SQL_UpdateAuthenStatus = "update user_authentication set auditStatus=?,cause=?,auditAt=? where uid=?";
	String SQL_getLoginDetail = "select id,uid,os,loginMethod,mobileModel,channel,mobileVersion,isType,loginTime FROM user_login_detail where uid=? and isType=?";
	String SQL_InsertLoginDetail = "insert into user_login_detail (uid,os,loginMethod,mobileModel,channel,mobileVersion,isType,loginTime)value(?,?,?,?,?,?,?,?)";
	
	String SQL_giftlist = "select gid,gname,gprice,wealth,credit,gcover,gtype,gframeurl,gframeurlios,simgs,bimgs,pimgs,gnumtype,gduration,gver,sver,gsort,createAt,category from config_giftlist where isshow=1 and isvalid=1 and skin=gid";
	String SQL_giftlistNew = "select isshow,type,subtype,gid,gname,gprice,wealth,credit,gcover,gtype,gframeurl,simgs,bimgs,pimgs,gnumtype,gduration,gver,sver,gsort,createAt,icon,skin,category from config_giftlist where isvalid=1";
	String SQL_giftinfonew = "select gid,gname,gprice,wealth,credit,gcover,gtype,gframeurl,gframeurlios,simgs,bimgs,pimgs,gnumtype,gduration,gver,sver,gsort,createAt,isshow,isvalid,category from config_giftlist where isshow=1 and isvalid=1 and gid=?";
	String SQL_giftact = "select id,gid,atype,stime,etime,isvalid from config_gift_activity where isvalid=1 and etime>unix_timestamp(now())";
	
	//查询开屏图片
	String SQL_GetOpenScreemConfig = "SELECT * FROM web_banner WHERE startShow <= UNIX_TIMESTAMP(NOW()) AND endShow >= UNIX_TIMESTAMP(NOW()) AND type = 0 AND switch =1";
	//查询直播公告
	String SQL_GetLiveNotice = "SELECT content FROM system_notice LIMIT 1";
	String SQL_getUserItemByUid = "SELECT a.id,a.uid,a.gid,a.type,a.subtype,a.num,b.gprice FROM user_item a,zhu_config.config_giftlist b WHERE a.gid=b.gid AND a.uid=?";
	String SQL_getUserItemSpecialByUid = "select id,uid,gid,type,subtype,num,starttime,endtime from user_item_special where uid=?";
	String SQL_getBadgeListByUid = "select id,uid,gid,type,subtype,num,starttime,endtime from user_item_special where uid=? and subtype=6 and starttime<=unix_timestamp(now()) and endtime>unix_timestamp(now()) ";
	//查询用户背包指定道具的数量
	String SQL_getBadgeItemCount = "SELECT count(id) AS itemCount FROM user_item WHERE uid = ? AND gid = ?";
	//删除指定背包指定的道具
	String SQL_delBadgeForItem = "DELETE FROM user_item WHERE uid = ? AND gid = ?"; 
	//小猪认证相关
	String SQL_selXiaozhuAuth = "SELECT id, uid,nickname,authContent,authPics,authURLs,status,cause,createAt FROM user_xiaozhu_auth WHERE uid=? AND isdel = 0";
	String SQL_selXiaozhuAuth_STATUS = "SELECT id FROM user_xiaozhu_auth WHERE uid=? AND status = 3 AND isdel = 0";
	String SQL_insXiaozhuAuth = "INSERT INTO user_xiaozhu_auth(uid,nickname,authContent,authPics,authURLs,status,createAt,isdel)VALUE(?,?,?,?,?,?,?,?)";
	String SQL_updXiaozhuAuthStatus = "update user_xiaozhu_auth set status=? where uid=? AND isdel = 0";
	//取消认证
	String SQL_delXiaozhuAuth = "UPDATE user_xiaozhu_auth SET isdel = 1 WHERE uid=? AND isdel = 0";
	//查找认证昵称是否唯一
	String SQL_selAuthNickname = "SELECT id FROM user_xiaozhu_auth WHERE nickname=? AND isdel = 0 AND status = 3";
	
	//查询幸运礼物名单
	String SQL_selLuckyGift = "SELECT gid,stime,etime FROM config_gift_activity WHERE stime <= UNIX_TIMESTAMP(NOW()) AND etime >= UNIX_TIMESTAMP(NOW()) AND isvalid = 1 AND atype = 2";
	
	//查询幸运礼物概率
	String SQL_selLuckyGiftProbabilitys = "SELECT * FROM gift_lucky_probabilitys";
	
	//插入中奖记录
	String SQL_insPrize = "INSERT INTO bill_prize_list(uid,gid,anchoruid,multiples,luckyCount,gprice,priceTotal,wealthTotal,creditTotal,sendPrice,sendCount,createAt)VALUE(?,?,?,?,?,?,?,?,?,?,?,?)";
	String SQL_selPrizeList = "SELECT * FROM bill_prize_list WHERE uid = ?";
	
	//增值服务权限相关
	String SQL_valueaddPrivilegeList = "select * from valueadd_privilege";
	String SQL_valueaddLevelConfList = "select * from valueadd_level_conf";
	String SQL_selUserGardInfo = "select * from user_guard_info where roomid = ? and uid = ? and isdel = 0 and cushiontime >= UNIX_TIMESTAMP(NOW())";
	String SQL_selUserGardInfoByRoomId = "select * from user_guard_info where roomid = ? and isdel = 0 and endtime >= UNIX_TIMESTAMP(NOW()) ORDER BY gid DESC";
	String SQL_selUserAllGardInfo = "select * from user_guard_info where uid = ? and isdel = 0 and cushiontime >= UNIX_TIMESTAMP(NOW())";
	String SQL_selUserGardInfoByUid = "select * from user_guard_info where roomid = ? and uid = ? and gid = ?";
	String SQL_selUserVipInfo = "select * from user_vip_info where uid = ? and isdel = 0 and endtime >= UNIX_TIMESTAMP(NOW())";
	String SQL_selUserVipInfoByUid = "select * from user_vip_info where uid = ? AND gid = ?";
	//查询一个用户所有有效的座驾
	String SQL_selUserValidCarInfo = "select * from user_car_info where endtime >= UNIX_TIMESTAMP(NOW()) and uid = ?";
	//根据用户查找某个座驾是否存在记录
	String SQL_selUserCarInfoByGid = "select * from user_car_info where uid = ? and gid = ?";
	//新增座驾
	String SQL_insUserCarInfo = "INSERT INTO user_car_info(uid,gid,starttime,endtime,status)VALUE(?,?,?,?,?)";
	//修改某个座驾的有效期并启用
	String SQL_updUserCarInfo = "UPDATE user_car_info SET starttime = ? , endtime = ? , status = ? WHERE uid = ? AND gid = ?";
	//启用或者停用某个座驾
	String SQL_updUserCarInfoStatus = "UPDATE user_car_info SET status = ? WHERE uid = ? AND gid = ? and status != ?";
	//停用所有座驾
	String SQL_updUserCarInfoUnStatus = "UPDATE user_car_info SET status = 0 WHERE uid = ? and status != 0";
	
	//增加守护的经验值
	String SQL_AddGuardExpByUid = "UPDATE user_guard_info SET exp = exp+?  WHERE uid = ? AND roomid = ? AND gid = ?";
	//增加守护的经验值获取记录
	String SQL_insUserGuardExpRecord = "INSERT INTO user_guard_exp_record(uid,roomid,gid,date,type,exp)VALUE(?,?,?,?,?,?)";
	//修改守护等级
	String SQL_UpdGuardLevel = "UPDATE user_guard_info SET level = ? WHERE uid = ? AND roomid = ? AND gid = ?";
	//增加守护身份
	String SQL_insUserGuardInfo = "INSERT INTO user_guard_info(roomid,uid,gid,level,exp,starttime,endtime,cushiontime,isdel)VALUE(?,?,?,?,?,?,?,?,?)";
	//修改守护时间
	String SQL_updUserGuardInfo = "UPDATE user_guard_info SET exp = exp+? , starttime = ? ,endtime = ?, cushiontime = ? , isdel = ? WHERE uid = ? AND roomid = ? AND gid = ?";
	//增加vip身份
	String SQL_insUserVipInfo = "INSERT INTO user_vip_info(uid,gid,starttime,endtime,isdel)VALUE(?,?,?,?,?)";
	//修改vip时间
	String SQL_updUserVipInfo = "UPDATE user_vip_info SET starttime = ? , endtime = ? , isdel = ? WHERE uid = ? AND gid = ?";
	//增加商城购买记录
	String SQL_insBillMall = "INSERT INTO pay_mall_list(gid,gname,srcuid,srcnickname,dstuid,dstnickname,count,price,realprice,pricetotal,realpricetotal,credit,starttime,endtime,type,createAt)VALUE(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	//增加商城道具到背包 
	String SQL_insUserMallItem = "INSERT INTO user_mall_item(uid,gid,type,subtype,num,starttime,endtime)VALUE(?,?,?,?,?,?,?)";
	//修改用户指定类型的道具的有效期
	String SQL_updUserMallItem = "UPDATE user_mall_item SET starttime=?,endtime=? WHERE uid=? AND subtype=?";
	//根据类型查询用户有效的道具
	String SQL_getUserMallItem = "SELECT * FROM user_mall_item WHERE uid=? and subtype=? and endtime >= UNIX_TIMESTAMP(NOW())";
	//根据类型查询用户的道具(包含无效)
	String SQL_getUserAllMallItemBySubtype = "SELECT * FROM user_mall_item WHERE uid=? and subtype=?";
	
	//增加相片
	String SQL_insUserAlbum = "INSERT INTO user_album(uid,fileName,photoUrl,photoThumbUrl,createAt)VALUE(?,?,?,?,?)";
	String SQL_delUserAlbum = "DELETE FROM user_album WHERE id = ? AND uid = ?";
	String SQL_selUserAlbum = "SELECT * FROM user_album where uid = ?";
	String SQL_selUserAlbumSortDesc = "SELECT * FROM user_album where uid = ? order by id desc limit 1";
	String SQL_selUserAlbumById = "SELECT * FROM user_album where id = ?";
	String SQL_selUserAlbumCount = "SELECT count(id) as count FROM user_album where uid = ?";
	String SQL_updUserAlbumById = "UPDATE user_album SET fileName = ? , photoUrl = ? , photoThumbUrl = ? , createAt = ? WHERE id = ?";

	//举报相片
	String SQL_selReportAlbumByPid = "SELECT id,report_uid FROM report_album WHERE report_pid = ? and status = 0";
	String SQL_selReportAlbummUserByRid = "SELECT count(id) as count FROM report_album_user WHERE rid = ? and dstuid = ?";
	String SQL_insReportAlbumUser = "INSERT INTO report_album_user(rid,pid,report_reason,dstuid,dstAt)VALUE(?,?,?,?,?)";
	String SQL_insReportAlbum = "INSERT INTO report_album(report_uid,report_pid,copy_filename,copy_url,report_num,status,createAt,updateAt)VALUE(?,?,?,?,?,?,?,?)";
	String SQL_updReportAlbum = "UPDATE report_album SET report_num = report_num+1 WHERE report_pid = ? and status = 0";
	
	//砸蛋游戏
	String SQL_insSmashedEggLog = "INSERT INTO game_smashed_egg_log(uid,hammer_price,hammer_count,roomId,reward_gift_id,reward_gift_name,reward_gift_price,reward_gift_count,reward_gift_totalPrice,reward_gift_type,createAt)VALUE(?,?,?,?,?,?,?,?,?,?,?)";
	//增加游戏记录
	String SQL_insGameRecord = "INSERT INTO t_game_record(uid,roomId,type,money,profit,ctime) VALUES (?,?,?,?,?,?);";
	
	//邀请活动相关
	String SQL_selInvitationConfig = "SELECT * FROM invitation_config";
	String SQL_selInvitationRewardsConfig = "SELECT * FROM invitation_rewards_config";
	String SQL_selInviteUserInfoByUid = "SELECT * FROM invite_user_info where uid = ?";
	String SQL_insInviteUserInfo = "INSERT INTO invite_user_info(uid,invitecount,gets,createAt)VALUE(?,?,?,?)";
	String SQL_updInviteUserInfo = "UPDATE invite_user_info SET invitecount = invitecount+? WHERE uid = ?";
	String SQL_updInviteUserInfoGets = "UPDATE invite_user_info SET gets = gets+? WHERE uid = ?";
	String SQL_selInviteUserRewardInfoByInviteUid = "SELECT * FROM invite_user_reward_info where invite_uid = ?";
	String SQL_insInviteUserRewardInfo = "INSERT INTO invite_user_reward_info(uid,invite_uid,status,createAt,updateAt)VALUE(?,?,?,?,?)";
	String SQL_updInviteUserRewardInfo = "UPDATE invite_user_reward_info SET status = ? WHERE uid = ? AND status != ?";
	String SQL_updInviteUserRewardInfoByInviteUid = "UPDATE invite_user_reward_info SET status = ? WHERE invite_uid = ? AND status != ?";
	String SQL_selInviteUserPeckLog = "SELECT * FROM invite_user_peck_log where uid = ?";
	String SQL_selInviteUserPeckLogByInvitationId = "SELECT * FROM invite_user_peck_log where uid = ? AND invitation_id=?";
	String SQL_insInviteUserPeckLog = "INSERT INTO invite_user_peck_log(uid,invitation_id,status,createAt)VALUE(?,?,?,?)";
	
	//商城促销活动
	String SQL_selGiftPromotion  = "SELECT * FROM config_gift_promotion WHERE isvalid = 1 AND endtime >= UNIX_TIMESTAMP(NOW())";
	
	//动态相关
	String SQL_addFeed = "INSERT INTO user_feed(uid,content,status,createAt)VALUE(?,?,?,?)";
	String SQL_addFeedImgs = "UPDATE user_feed SET imgs = ? WHERE id = ?";
	String SQL_delFeed = "UPDATE user_feed SET status = 0 WHERE id = ?";
	String SQL_getFeed = "SELECT * FROM user_feed WHERE id = ? AND status = 1";
	String SQL_getFeedByUser = "SELECT * FROM user_feed WHERE uid = ? AND status = 1";
	String SQL_addFeedLaud = "INSERT INTO user_feed_laud(uid,feedid,createAt)VALUE(?,?,?)";
	String SQL_delFeedLaud = "DELETE FROM user_feed_laud WHERE uid = ?  AND feedid = ?";
	String SQL_getFeedReplyById = "select * from user_feed_reply where id = ?";
	String SQL_getFeedReplyByFeedId = "select * from user_feed_reply where feedid = ?";
	String SQL_addFeedReply = "INSERT INTO user_feed_reply(feedid,parentFrId,fromUid,toUid,content,createAt)VALUE(?,?,?,?,?,?)";
	String SQL_delFeedReply = "DELETE FROM user_feed_reply WHERE id = ?";
	String SQL_addFeedReward = "INSERT INTO user_feed_reward(feedId,feedCUid,rewardUid,zhutou,createAt)VALUE(?,?,?,?,?)";
	String SQL_addFeedRewardGift = "INSERT INTO user_feed_reward_gift(feedId,feedCUid,rewardUid,gid,count,createAt)VALUE(?,?,?,?,?,?)";
	
	//举报动态
	String SQL_selReportFeedByFid = "SELECT id,report_uid FROM report_feed WHERE report_fid = ? and status = 0";
	String SQL_selReportFeedUserByRid = "SELECT count(id) as count FROM report_feed_user WHERE rid = ? and dstuid = ?";
	String SQL_insReportFeedUser = "INSERT INTO report_feed_user(rid,fid,report_reason,dstuid,dstAt)VALUE(?,?,?,?,?)";
	String SQL_insReportFeed = "INSERT INTO report_feed(report_uid,report_fid,report_num,status,createAt,updateAt)VALUE(?,?,?,?,?,?)";
	String SQL_updReportFeed = "UPDATE report_feed SET report_num = report_num+1 WHERE report_fid = ? and status = 0";
	
	
	//动态推荐主播
	String SQL_getFeedRecommendAnchor = "SELECT uid FROM web_recommend_anchor WHERE isvalid=1 ORDER BY sort DESC";
	
	//PC兑换
	String SQL_getAnchorSalaryByTime = "SELECT count(id) as count FROM pay_anchor_credit WHERE uid = ? and ymtime = ?";
	String SQL_getAnchorSalaryByUid = "SELECT id,credits,status,addtime FROM pay_anchor_credit WHERE uid = ? order by addtime desc";
	String SQL_getUnionAnchorByAnchorid = "SELECT unionid,salary,rate,remarks FROM union_anchor_ref WHERE anchorid = ?";
	String SQL_insPayAnchorSalary = "INSERT INTO pay_anchor_credit(uid,unionid,credits,addtime)VALUE(?,?,?,?)";
	
	//插入抽奖消耗记录
	String SQL_insConsumeLottory = "INSERT INTO lottory_consume_list(uid,activityId,consume,des,addtime)VALUE(?,?,?,?,?)";
	//插入猪头中奖纪录
	String SQL_insRewardLottory = "INSERT INTO lottery_reward_list(uid,activityId,reward,des,addtime)VALUE(?,?,?,?,?)";
	
	String SQL_getRewardLottoryList = "SELECT * FROM lottery_reward_list WHERE activityId = ? ORDER BY addtime desc";
	//查询消费数
	String SQL_getCostRecord = "select b.dstnickname,c.gname,b.count,b.price from %s b,zhu_config.config_giftlist c where b.gid=c.gid and b.srcuid=? and b.addtime >=? and b.addtime<? order by b.srcuid asc,b.gid asc,b.addtime asc limit ?,?";
	//查询消费记录
	String SQL_getCostRecordTotalCount = "select count(b.id) totalCount from %s b,zhu_config.config_giftlist c where b.gid=c.gid and b.srcuid=? and b.addtime >=? and b.addtime<?";
	//查询消费记录
	String SQL_getCostRecordTotalSum = "select sum(count*price) totalSum from %s b,zhu_config.config_giftlist c where b.gid=c.gid and b.srcuid=? and b.addtime >=? and b.addtime<?";
	//查询收礼物总记录数
	String SQL_getRecBillList = "select b.srcnickname,c.gname,b.count,c.credit from %s b,zhu_config.config_giftlist c where b.gid=c.gid and b.dstuid=? and b.addtime >=? and b.addtime<? order by b.srcuid asc,b.gid asc,b.addtime asc limit ?,?";
	//查询收礼物总记录数
	String SQL_getRecBillTotalCount = "select count(b.id) totalCount from %s b,zhu_config.config_giftlist c where b.gid=c.gid and b.dstuid=? and b.addtime >=? and b.addtime<?";
	//查询 声援总数
	String SQL_getRecBillTotalCredit = "select SUM(credit*count) totalCredit from %s b,zhu_config.config_giftlist c where b.gid=c.gid and b.dstuid=? and b.addtime >=? and b.addtime<?";
	//查询麦时明细
	String SQL_getLivedTimeList = "SELECT starttime,endtime FROM live_mic_time WHERE uid = ? and addtime>=? and addtime<=? order by starttime desc limit ?,? ";
	//查询麦时总条数
	String SQL_getLivedTimeTotalCount = "SELECT count(id) totalCount FROM live_mic_time WHERE uid = ? and addtime>=? and addtime<=?";
	//查询麦时
	String SQL_getLivedTimeSumary = "SELECT * FROM zhu_analysis.analysis_wages WHERE anchorid=? and times=?";
	//保存用户关系表
	String SQL_insUserOrgRelation = "insert into t_user_org_relation(uid,phone,registtime,strategic_partner_id,extension_center_id,promoters_id,agent_user_id,salesman_id,create_time,create_user,update_time,update_user) value(?,?,?,?,?,?,?,?,?,?,?,?)";
	//根据ID获取用户关系信息
	String SQL_getUserOrgRelationCountByUid = "select count(id) from t_user_org_relation where uid=?";
	//根据ID获取业务信息
	String SQL_getSalemanById = "select * from t_salesman where id=? and status=0";
	//添加用户开通座驾,vip,守护记录
	String SQL_insBillcvgList = "insert into bill_cvg_list(uid,anchorid,gid,gname,realpricetotal,count,addtime,starttime,endtime,type) value(?,?,?,?,?,?,?,?,?,?)";
	//插入pk记录
	String SQL_insPkRecord = "insert into pk_record(first_uid,second_uid,pk_time,penalty_time,first_user_votes,second_user_votes,winner_uid,create_time) value(?,?,?,?,?,?,?,?)";
	//最后修改pk记录
	String SQL_updPkRecord = "UPDATE pk_record SET first_user_votes = ?,second_user_votes = ?, winner_uid = ?,update_time = ? WHERE id = ?";
	
	//插入发送验证码记录
	String SQL_InsertVerifyCode = "INSERT INTO t_verify_code (mobile,type,verifyCode,takeEffectTime,expiryTime,createTime) VALUES(?,?,?,?,?,?)";
	
	//查询用户定量信息记录是否存在
	String SQL_selUserRationInfo_Money = "select count(id) ct from user_ration_info where uid=?";
	//更新用户充值总金额（人民币）
	String SQL_updUserRationInfo_Money = "UPDATE user_ration_info SET money_rmb = money_rmb+? WHERE uid = ?";
	//插入用户充值总金额（人民币）
	String SQL_insUserRationInfo_Money = "insert into user_ration_info(uid,money_rmb) values(?,?)";
	//保存用户交易记录 统计
	String SQL_insUserTransactionHis = "insert into User_Transaction_His(trans_type,uid,amount,money,create_time,ref_id,data_type) value(?,?,?,?,?,?,?)";
	//查询当天是否举报
	String SQL_isReport = "select count(id) nums from t_report_info where uid = ? and rid = ? and createtime > ? and createtime < ?";
	//添加举报信息
	String SQL_insReportInfo = "INSERT INTO t_report_info(uid,content,rid,status,createtime) VALUES(?,?,?,?,?)";
	//根据ID获取业务员信息
	String SQL_getSalesman = "SELECT * FROM t_salesman WHERE id=?";
	
	//同步用户信息
	String SQL_insUserInfo = "insert into user_info (uid, nickname, familyId, anchorLevel, userLevel, sex, identity, headimage, livimage, pcimg1, pcimg2, birthday, exp, phone, province, city, signature, registip, registtime, registchannel, subregistchannel, registos, registimei, liveStatus, opentime, recommend, videoline, verified, verified_reason, contrRq, constellation, hobby, grade, gameStatus, gameId, accountid, accountname, password, authkey, unionId, status, money, wealth, credit, creditTotal, frozenCredit,salesman_id, salesman_name, salesman_contacts_name, salesman_contacts_phone, agent_user_id, agent_user_name, agent_user_contacts_name, agent_user_contacts_phone, promoters_id, promoters_name, promoters_contacts_name, promoters_contacts_phone, extension_center_id, extension_center_name, extension_center_contacts_name, extension_center_contacts_phone, strategic_partner_id, strategic_partner_name, strategic_partner_contacts_name, strategic_partner_contacts_phone, money_rmb) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	//查询用户所属信息(同步数据使用)
	String SQL_getUserOrgInfo = "SELECT  b.salesman_id salesmanId,  c.name salesmanName, c.contacts salesmanContactsName, c.contacts_phone salesmanContactsPhone,b.agent_user_id agentUserId, d.name agentUserName,d.contacts agentUserContactsName,d.contacts_phone agentUserContactsPhone,b.promoters_id promotersId,e.name promotersName,e.contacts promotersContactsName,e.contacts_phone promotersContactsPhone,b.extension_center_id extensionCenterId,  f.name extensionCenterName, f.contacts extensionCenterContactsName, f.contacts_phone extensionCenterContactsPhone, b.strategic_partner_id strategicPartnerId,  g.name strategicPartnerName,  g.contacts strategicPartnerContactsName, g.contacts_phone strategicPartnerContactsPhone FROM zhu_admin.t_user_org_relation b INNER JOIN zhu_admin.t_salesman c ON b.salesman_id = c.id INNER JOIN zhu_admin.t_agent_user d  ON b.agent_user_id = d.id INNER JOIN zhu_admin.t_promoters e  ON b.promoters_id = e.id  INNER JOIN zhu_admin.t_extension_center f  ON b.extension_center_id = f.id INNER JOIN zhu_admin.t_strategic_partner g  ON b.strategic_partner_id = g.id WHERE b.uid = ?"; 
	//更新user_info用户基本信息
	String SQL_updateUserInfoBase = "update user_info set nickname=?, familyId=?, anchorLevel=?, userLevel=?, sex=?, identity=?, headimage=?, livimage=?, pcimg1=?, pcimg2=?, birthday=?, exp=?, phone=?, province=?, city=?, signature=?, registip=?, registtime=?, registchannel=?, subregistchannel=?, registos=?, registimei=?, liveStatus=?, opentime=?, recommend=?, videoline=?, verified=?, verified_reason=?, contrRq=?, constellation=?, hobby=?, grade=?, gameStatus=?, gameId=? where uid = ?";
	//更新user_info用户账户
	String SQL_updUserInfoAccount = "update user_info set accountid=?, accountname=?, password=?, authkey=?, unionId=?, status=? where uid = ?";
	//更新user_info用户资产
	String SQL_updUserInfoAsset = "update user_info set  money=?, wealth=?, credit=?, creditTotal=?, frozenCredit=? where uid = ?";
	//更新user_info用户充值总金额
	String SQL_updUserInfoMoneyRmb = "update user_info set  money_rmb=money_rmb+? where uid = ?";
	
	// 修改用户昵称、签名、爱好、头像、封面
	String SQL_UpdUserNicknameAignatureAobbyHeadimageLivimageByUid = "UPDATE %s SET nickname=?,sex=?,signature=?,hobby=?,headimage=?,livimage=?WHERE uid =?";
	
	//增加窜礼物、消息信息
	String SQL_insStatisticalInfo = "INSERT INTO t_statistical_info(uid,anchorId,os,version,msg)VALUE(?,?,?,?,?)";
}