package com.mpig.api.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.Unirest;
import com.mpig.api.SqlTemplete;
import com.mpig.api.controller.GameController;
import com.mpig.api.db.DBHelper;
import com.mpig.api.db.DataSource;
import com.mpig.api.dictionary.lib.GameBetConfigLib;
import com.mpig.api.dictionary.lib.UrlConfigLib;
import com.mpig.api.model.GameUserBetLogMdoel;
import com.mpig.api.model.ReturnModel;
import com.mpig.api.model.UserAssetModel;
import com.mpig.api.model.UserBaseInfoModel;
import com.mpig.api.modelcomet.RunWayCModNew;
import com.mpig.api.redis.service.RedisCommService;
import com.mpig.api.redis.service.UserRedisService;
import com.mpig.api.service.IBillService;
import com.mpig.api.service.IGameService;
import com.mpig.api.service.IRoomService;
import com.mpig.api.service.IUserService;
import com.mpig.api.utils.ChatMessageUtil;
import com.mpig.api.utils.CodeContant;
import com.mpig.api.utils.DateUtils;
import com.mpig.api.utils.EncryptUtils;
import com.mpig.api.utils.RedisContant;
import com.mpig.api.utils.StringUtils;
import com.mpig.api.utils.VarConfigUtils;

@Service
public class GameServiceImpl implements IGameService,SqlTemplete {
	
	private static final Logger logger = Logger.getLogger(GameController.class);
	
	@Resource
	private IUserService userService;
	@Resource
	private IBillService billService;
	@Resource
	private IRoomService roomService;
	
	@Override
	public void addBets(String orderId, int roomid, int srcUid, int money, int nid, int type,ReturnModel returnModel) {
		
		UserAssetModel userAsset = userService.getUserAssetByUid(srcUid, false);
		if (userAsset == null) {
			returnModel.setCode(CodeContant.USERASSETEXITS);
			returnModel.setMessage("该用户不存在. CODE:" + CodeContant.USERASSETEXITS);
		}else {
			if (userAsset.getMoney() < money) {
				returnModel.setCode(CodeContant.MONEYLESS);
				returnModel.setMessage("金额不足，请充值. CODE:" + CodeContant.MONEYLESS);
			}else {
				
				Connection conn = null;
				PreparedStatement stmt = null;
				try {
					conn = DataSource.instance.getPool(VarConfigUtils.dbZhuGame).getConnection();

					String strDeductSql = StringUtils.getSqlString(SqlTemplete.SQL_UpdUserAssetBySendUid, "zhu_user.user_asset_", srcUid);
					stmt = conn.prepareStatement(strDeductSql);
					stmt.setInt(1, money);
					stmt.setInt(2, money*100);
					stmt.setInt(3, srcUid);
					stmt.setInt(4, money);
					
					int deductOk = stmt.executeUpdate();
					if (deductOk > 0) {
						
						userAsset = userService.getUserAssetByUid(srcUid, true);
						UserBaseInfoModel userbase = userService.getUserbaseInfoByUid(srcUid, false);
						
						// 扣费成功
						String strLogSql = "insert into game_user_bet_log(order_id,uid,rid,nid,type,deduct_money,add_time)value(?,?,?,?,?,?,?)";
						stmt.close();
						stmt = conn.prepareStatement(strLogSql);
						stmt.setString(1, orderId);
						stmt.setInt(2, srcUid);
						stmt.setInt(3, roomid);
						stmt.setInt(4, nid);
						stmt.setInt(5, type);
						stmt.setInt(6, money);
						stmt.setLong(7, System.currentTimeMillis()/1000);
						
						stmt.addBatch();
						
						String tbname = "zhu_bill.bill_"+DateUtils.dateToString(null, "YMM");
						String insertBill = "INSERT INTO "+ tbname
								+ "(gid,count,price,srcuid,srcleftmoney,srcwealth,srccredit,dstuid,dstleftmoney,dstwealth,dstcredit,addtime,type,getmoney,os,bak,srcnickname,dstnickname,familyid)VALUE"
								+ "(0,1,"+money+","+srcUid+","+userAsset.getMoney()+","+userAsset.getWealth()+","+userAsset.getCredit()+",0,0,0,0,"+System.currentTimeMillis()/1000+",2,0,0,'十二生肖押注','"+userbase.getNickname()+"','',"+userbase.getFamilyId()+")";
						stmt.addBatch(insertBill);
						
						int[] logOk = stmt.executeBatch();

						// 日榜、周榜、月榜、总榜
						UserRedisService.getInstance().setRank(String.valueOf(srcUid), "99001", money,0);
						
						// 用户升级
						roomService.updUserLevel(srcUid, 0, 99001, 1, 1);
						
						System.out.println("logOk:"+logOk.length);
						
						boolean bl = true;
						for(int i=0; i<logOk.length;i++){
							if (logOk[i] <= 0) {
								bl = false;
							}
						}
						if (!bl) {
							logger.info("insertBill:" + insertBill);
							returnModel.setCode(CodeContant.gameBetLogAddErr);
							returnModel.setMessage("下注记录添加失败:" + CodeContant.gameBetLogAddErr);
							logger.info(String.format("addBets下注扣费成功，添加日志失败：orderid:%s,roomid:%d,srcUid:%d,money:%d,nid:%d,type:%s", orderId,roomid,srcUid,money,nid,type));
						}
						returnModel.setData(userAsset.getMoney());
					}else {
						returnModel.setCode(CodeContant.gameBetDeductErr);
						returnModel.setMessage("下注失败:" + CodeContant.gameBetDeductErr);
						
						logger.info(String.format("addBets下注扣费失败：orderid:%s,roomid:%d,srcUid:%d,money:%d,nid:%d,type:%s", orderId,roomid,srcUid,money,nid,type));
					}
				} catch (Exception e) {
					returnModel.setCode(CodeContant.ERROR);
					returnModel.setMessage("系统繁忙，请稍后再试");
					logger.error("addBets-exception:", e );
				}finally {
					try {
						if (stmt != null) {
							stmt.close();
						}
						if (conn != null) {
							conn.close();
							
						}
					} catch (Exception e2) {
						logger.error("addBets-finally-exception:", e2 );
					}
				}
			}
		}
	}
	
	/**
	 * 获取获奖用户信息
	 * @param order_id
	 * @param rid
	 * @return
	 */
	public List<GameUserBetLogMdoel> getWinUsers(String order_id,int rid){
		
		Connection conn = null;
		PreparedStatement stmtWinUsers = null;
		ResultSet rs = null;
		
		List<GameUserBetLogMdoel> list = new ArrayList<GameUserBetLogMdoel>();
		try {

			String strBet = "select id,order_id,uid,rid,nid,type,deduct_money,add_price,add_time,update_time from game_user_bet_log where order_id=? and rid=?";
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuGame).getConnection();
			stmtWinUsers = conn.prepareStatement(strBet);
			stmtWinUsers.setString(1, order_id);
			stmtWinUsers.setInt(2, rid);
			rs = stmtWinUsers.executeQuery();
			
			if (rs != null) {
				GameUserBetLogMdoel gameUserBetLogMdoel = null;
				while(rs.next()){
					gameUserBetLogMdoel = new GameUserBetLogMdoel().populateFromResultSet(rs);
					list.add(gameUserBetLogMdoel);
				}
			}
		} catch (Exception e) {
			logger.error("getWinUsers exception:",e);
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmtWinUsers != null) {
					stmtWinUsers.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				logger.error("getWinUsers finally exception:",e2);
			}
		}
		
		return list;
	}

	@Override
	public List<Map<String, Object>> BetResult(String orderId, int roomid, int innerid, int outerid) {

		Long nowtime = System.currentTimeMillis()/1000;

		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		Connection conn = null;
		PreparedStatement stmtBetResult = null;
		PreparedStatement stmtUpdBetLog = null;
		PreparedStatement stmtUpdAsset = null;
		
		PreparedStatement stmtAddBill = null;
		
		try {

			// 记录开奖结果
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuGame).getConnection();
			String strSql = "insert into game_bet_result(order_id,rid,innernid,outernid,add_time)value(?,?,?,?,?)";
			stmtBetResult = conn.prepareStatement(strSql);
			stmtBetResult.setString(1, orderId);
			stmtBetResult.setInt(2, roomid);
			stmtBetResult.setInt(3, innerid);
			stmtBetResult.setInt(4, outerid);
			stmtBetResult.setLong(5, nowtime);
			
			int betResult = stmtBetResult.executeUpdate();
			if (betResult == 1) {
				System.out.println("开奖结果记录成功");
				// 查询中奖用户
				List<GameUserBetLogMdoel> winUsers = this.getWinUsers(orderId, roomid);

				System.out.println("获奖人数："+winUsers.size());
				
				if (winUsers != null && winUsers.size() > 0) {
					
					Map<Integer, Integer> totalAmount = new HashMap<Integer,Integer>();
					
					// 更新单次中奖记录
					String strUpdBetLog = "update game_user_bet_log set add_price=?,update_time=? where id=?";
					stmtUpdBetLog = conn.prepareStatement(strUpdBetLog);
					
					// 计算中奖
					for(GameUserBetLogMdoel betLogMdoel:winUsers){
						// 判断是否在同一房间里
						if (betLogMdoel.getRid() ==  roomid) {
							int amount = 0;
							if (outerid == 14) {
								//内圈全部中奖，外圈不中奖（小丰收）
								if (betLogMdoel.getType() == 1) {
									amount = (int) (betLogMdoel.getDeduct_money()*GameBetConfigLib.getGameBetConfig("inner_"+betLogMdoel.getNid()).getTimes());
								}
							}else {
								if (betLogMdoel.getType() == 1 && betLogMdoel.getNid() == innerid) {
									// 内圈中奖
									amount = (int) (betLogMdoel.getDeduct_money()*GameBetConfigLib.getGameBetConfig("inner_"+betLogMdoel.getNid()).getTimes());
								}else if (betLogMdoel.getType() == 2) {
									// 外圈
									if (outerid == 13 || betLogMdoel.getNid() == outerid) {
										// 大丰收 或 外圈中奖
										amount = (int) (betLogMdoel.getDeduct_money()*GameBetConfigLib.getGameBetConfig("outer_"+betLogMdoel.getNid()).getTimes());
									}
								}
							}
							
							if (amount > 0 ) {
								stmtUpdBetLog.setInt(1, amount);
								stmtUpdBetLog.setLong(2, nowtime);
								stmtUpdBetLog.setInt(3, betLogMdoel.getId());
								stmtUpdBetLog.addBatch();
								
								// 中奖累计
								if (totalAmount.containsKey(betLogMdoel.getUid())) {
									totalAmount.put(betLogMdoel.getUid(), amount + totalAmount.get(betLogMdoel.getUid()));
								}else {
									totalAmount.put(betLogMdoel.getUid(), amount);
								}
							}
						}
					}
					stmtUpdBetLog.executeBatch();
					// 计算中奖 结束  *****************
					System.out.println("开始计算中奖结果----中奖人数："+totalAmount.size());
					List<List<Map<String, Object>>> listRunWay = new ArrayList<List<Map<String,Object>>>();
					if (totalAmount.size() > 0) {
						
						String tbname = "zhu_bill.bill_"+DateUtils.dateToString(null, "YMM");
						String insertBill = "INSERT INTO "+ tbname
								+ "(gid,count,price,srcuid,srcleftmoney,srcwealth,srccredit,dstuid,dstleftmoney,dstwealth,dstcredit,addtime,type,getmoney,os,bak,srcnickname,dstnickname,familyid)VALUE"
								+ "(0,0,0,0,0,0,0,?,?,?,?,?,3,?,0,'十二生肖中奖','',?,?)";
						stmtAddBill = conn.prepareStatement(insertBill);
						
						// 更新资产
						for(Map.Entry<Integer, Integer> map:totalAmount.entrySet()){
							
							if (stmtUpdAsset != null) {
								stmtUpdAsset.close();
							}
							String updAsset = StringUtils.getSqlString("update %s set money=money+"+map.getValue()+" where uid="+map.getKey(), "zhu_user.user_asset_", map.getKey());
							stmtUpdAsset = conn.prepareStatement(updAsset);
							
							int executeUpdate = stmtUpdAsset.executeUpdate();
							if (executeUpdate != 1) {
								logger.info(String.format("update asset is err. uid:%d,money:$d",map.getKey(),map.getValue()));
							}else {
								userService.getUserAssetByUid(map.getKey(), true);
							}

							// 获取用户信息
							UserBaseInfoModel userBase = userService.getUserbaseInfoByUid(map.getKey(), false);
							UserAssetModel userAsset = userService.getUserAssetByUid(map.getKey(), false);
							
							stmtAddBill.setInt(1, userBase.getUid());
							stmtAddBill.setInt(2, userAsset.getMoney());
							stmtAddBill.setLong(3, userAsset.getWealth());
							stmtAddBill.setInt(4, userAsset.getCredit());
							stmtAddBill.setLong(5, nowtime);
							stmtAddBill.setInt(6, map.getValue());
							stmtAddBill.setString(7, userBase.getNickname());
							stmtAddBill.setInt(8, userBase.getFamilyId());
							stmtAddBill.addBatch();
							
							List<List<Map<String, Object>>> setTwelveRunWayInfo = this.setTwelveRunWayInfo(orderId, map.getKey(), map.getValue(),userBase,userAsset);
							
							if (setTwelveRunWayInfo != null && setTwelveRunWayInfo.size() > 0) {
								// 公聊区 信息推送
								ChatMessageUtil.gameWinNotice(roomid,map.getKey(),userAsset.getMoney(), setTwelveRunWayInfo);

								// 设置待上跑道数据  上线设置500
								if (map.getValue() > 500) {
									listRunWay.addAll(setTwelveRunWayInfo);
								}
							}
						}
						stmtAddBill.executeBatch();
					}
					
					if (listRunWay.size() > 0) {
						// 预设 广播信息
						RedisCommService.getInstance().set(RedisContant.RedisNameOther, RedisContant.tmpRunway+orderId, JSONObject.toJSONString(listRunWay), 3600);
					}
				}
			}else {
				logger.info(String.format("BetResult insert game_bet_result err orderId:%s,roomid:%d,innerid:%d,outerid:%d,betTime:%s", orderId,roomid,innerid,outerid,nowtime));
			}
		} catch (Exception e) {
			logger.error("BetResult exception:", e);
		}finally {
			try {
				if (stmtBetResult != null) {
					stmtBetResult.close();
				}
				if (stmtUpdBetLog != null) {
					stmtUpdBetLog.close();
				}
				if (stmtUpdAsset != null) {
					stmtUpdAsset.close();
				}
				if (stmtAddBill != null) {
					stmtAddBill.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				logger.error("BetResult finally exception:", e2);
			}
		}
		
		return list;
	}
	
	/**
	 * 设置中奖跑道数据
	 * @param orderId
	 * @param uid
	 * @param money
	 * @return
	 */
	private List<List<Map<String, Object>>> setTwelveRunWayInfo(String orderId,int uid,int money,UserBaseInfoModel userBase,UserAssetModel userAsset){
		
		if (userBase == null || userAsset == null) {
			return null;
		}else {
			
			Map<String, Object> _map = new HashMap<String,Object>();
			// 跑道list
			List<List<Map<String, Object>>> allList = new ArrayList<List<Map<String, Object>>>();
			// 一条跑道信息
			List<Map<String, Object>>  list = new ArrayList<Map<String,Object>>();

			int inum = money/3000;
			int isurplus = money%3000;
			//恭喜
			//“用户昵称”
			//在
			//“十二生肖”
			//中获得 
			//1 辆兰博基尼
			if ( inum > 0) {
				
				 _map.put("name", "恭喜");
				 _map.put("color", "#ff00e3");
				 list.add(_map);

				 _map = new HashMap<String,Object>();
				 _map.put("name", userBase.getNickname());
				 _map.put("color", "#fff08c");
				 list.add(_map);

				 _map = new HashMap<String,Object>();
				 _map.put("name", "在");
				 _map.put("color", "");
				 list.add(_map);
				 
				 _map = new HashMap<String,Object>();
				 _map.put("name", "十二生肖");
				 _map.put("color", "#eb6100");
				 list.add(_map);
				 
				 _map = new HashMap<String,Object>();
				 _map.put("name", "中获得");
				 _map.put("color", "");
				 list.add(_map);
				 
				 _map = new HashMap<String,Object>();
				 _map.put("name", inum);
				 _map.put("color", "#fff08c");
				 list.add(_map);
				 
				 _map = new HashMap<String,Object>();
				 _map.put("name", "辆兰博基尼");
				 _map.put("color", "");
				 list.add(_map);
				 
				 _map = new HashMap<String,Object>();
				 _map.put("gid", 10);
				 _map.put("color", "");
				 list.add(_map);
				 allList.add(list);
			}

			if (isurplus > 0) {
				inum = isurplus/10;
				isurplus = isurplus%10;
				if ( inum > 0) {
					 list = new ArrayList<Map<String,Object>>();
					 
					 _map = new HashMap<String,Object>();
					 _map.put("name", "恭喜");
					 _map.put("color", "#ff00e3");
					 list.add(_map);

					 _map = new HashMap<String,Object>();
					 _map.put("name", userBase.getNickname());
					 _map.put("color", "#fff08c");
					 list.add(_map);

					 _map = new HashMap<String,Object>();
					 _map.put("name", "在");
					 _map.put("color", "");
					 list.add(_map);
					 
					 _map = new HashMap<String,Object>();
					 _map.put("name", "十二生肖");
					 _map.put("color", "#eb6100");
					 list.add(_map);
					 
					 _map = new HashMap<String,Object>();
					 _map.put("name", "中获得");
					 _map.put("color", "");
					 list.add(_map);
					 
					 _map = new HashMap<String,Object>();
					 _map.put("name", inum);
					 _map.put("color", "#fff08c");
					 list.add(_map);
					 
					 _map = new HashMap<String,Object>();
					 _map.put("name", "个情书");
					 _map.put("color", "");
					 list.add(_map);
					 
					 _map = new HashMap<String,Object>();
					 _map.put("gid", 16);
					 _map.put("color", "");
					 list.add(_map);
					 allList.add(list);
				}

				if (isurplus > 0) {

					 list = new ArrayList<Map<String,Object>>();
					 
					 _map = new HashMap<String,Object>();
					 _map.put("name", "恭喜");
					 _map.put("color", "#ff00e3");
					 list.add(_map);

					 _map = new HashMap<String,Object>();
					 _map.put("name", userBase.getNickname());
					 _map.put("color", "#fff08c");
					 list.add(_map);

					 _map = new HashMap<String,Object>();
					 _map.put("name", "在");
					 _map.put("color", "");
					 list.add(_map);
					 
					 _map = new HashMap<String,Object>();
					 _map.put("name", "十二生肖");
					 _map.put("color", "#eb6100");
					 list.add(_map);
					 
					 _map = new HashMap<String,Object>();
					 _map.put("name", "中获得");
					 _map.put("color", "");
					 list.add(_map);
					 
					 _map = new HashMap<String,Object>();
					 _map.put("name", isurplus);
					 _map.put("color", "#fff08c");
					 list.add(_map);
					 
					 _map = new HashMap<String,Object>();
					 _map.put("name", "个心动");
					 _map.put("color", "");
					 list.add(_map);
					 
					 _map = new HashMap<String,Object>();
					 _map.put("gid", 7);
					 _map.put("color", "");
					 list.add(_map);
					 allList.add(list);
				}
			}
			return allList;
		}
	}

	@Override
	public void sendRunway(String orderId) {
		
		String string = RedisCommService.getInstance().get(RedisContant.RedisNameOther, RedisContant.tmpRunway+orderId);
		if (org.apache.commons.lang.StringUtils.isNotEmpty(string)) {
			
			JSONArray parseArray = JSONObject.parseArray(string);
			
			if (parseArray != null && parseArray.size() > 0) {

				// 跑道 
				RunWayCModNew msgBody = new RunWayCModNew();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("list", parseArray);
				
				msgBody.setData(map);
				msgBody.setAnchorUid(99001);
				msgBody.setAnchorName("十二生肖");

				String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
						"appKey=" + VarConfigUtils.ServiceKey,
						"msgBody=" + JSONObject.toJSONString(msgBody));

				Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_room_all())
						.field("appKey", VarConfigUtils.ServiceKey)
						.field("msgBody", JSONObject.toJSONString(msgBody))
						.field("sign", signParams).asJsonAsync();
			}else {
				logger.info("sendRunway alllist is null or size is zero");
			}
		}else {
			logger.info("sendRunway is empty  orderId:"+orderId);
		}
	}
	
	@Override
	public int addSmashedEggLog(Integer uid, Integer hammerPrice, Integer hammerCount, Integer roomId, Integer rewardGiftId, String rewardGiftName, Integer rewardGiftPrice, Integer rewardGiftCount,Integer rewardGiftTotalPrice, Integer rewardGiftType) {
		try {
			int executeResult = DBHelper.execute(VarConfigUtils.dbZhuGame, SqlTemplete.SQL_insSmashedEggLog, false, uid, hammerPrice, hammerCount, roomId, rewardGiftId, rewardGiftName, rewardGiftPrice, rewardGiftCount, rewardGiftTotalPrice, rewardGiftType, System.currentTimeMillis()/1000);
			return executeResult;
		} catch (Exception e) {
			logger.error("<addSmashedEggLog-> Param => uid:"+uid+" hammerPrice :"+ hammerPrice+" hammerCount :"+hammerCount+" roomId :"+ roomId+ " rewardGiftId:"+rewardGiftId+ " rewardGiftName"+rewardGiftName+" rewardGiftPrice"+rewardGiftPrice+" rewardGiftCount"+rewardGiftCount+" rewardGiftTotalPrice"+rewardGiftTotalPrice+" Exception> " + e.toString());
		}
		return -1;
	}
}
