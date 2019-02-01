package com.hkzb.game.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hkzb.game.common.utils.Constants;
import com.hkzb.game.common.utils.DateUtil;
import com.hkzb.game.common.utils.HMap;
import com.hkzb.game.common.utils.JsonUtil;
import com.hkzb.game.common.utils.RedisShardClient;
import com.hkzb.game.common.utils.ResultUtil;
import com.hkzb.game.dto.GameDollConfigDto;
import com.hkzb.game.dto.ResultDto;
import com.hkzb.game.dto.RoomNoticeTxtCmod;
import com.hkzb.game.dto.RunWayCMod;
import com.hkzb.game.dto.UserBaseDto;
import com.hkzb.game.model.GameGraspDollRecord;
import com.hkzb.game.model.GameRecord;
import com.hkzb.game.model.UserTransactionHis;
import com.hkzb.game.timer.AsyncManager;
import com.hkzb.game.timer.GameDollMsgPushTask;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

@Service
@Transactional
public class GameDollService implements IGameDollService {
	private static Logger log = Logger.getLogger(GameDollService.class);

	@Value("${business.server.url}")
	private String businessServerUrl;

	@Autowired
	private RedisShardClient redisShardClient;

	@Autowired
	private IGameGraspDollRecordService gameGraspDollRecordService;

	@Autowired
	private IGameRecordService gameRecordService;
	
	@Autowired
	private IUserTransactionHisService userTransactionHisService;

	@Override
	public ResultDto<List<GameDollConfigDto>> getGameDollConfig() {
		String str = redisShardClient.get(Constants.gameDollConfig);
		if (StringUtils.isBlank(str)) {
			return ResultUtil.fail();
		}
		List<GameDollConfigDto> result = JsonUtil.toListBean(str, GameDollConfigDto.class);
		return ResultUtil.success(result);
	}

	@Override
	public ResultDto<Object> grab(GameGraspDollRecord gameGraspDollRecord) {
		if (gameGraspDollRecord.getRoomId() == null || gameGraspDollRecord.getRoomId() == 0) {
			return ResultUtil.fail("直播间不能为空");
		}
		if (gameGraspDollRecord.getAnchorId() == null || gameGraspDollRecord.getAnchorId() == 0) {
			return ResultUtil.fail("主播不能为空");
		}
		if (gameGraspDollRecord.getUid() == null || gameGraspDollRecord.getUid() == 0) {
			return ResultUtil.fail("用户不能为空");
		}
		if (gameGraspDollRecord.getGraspdollId() == null) {
			return ResultUtil.fail("娃娃不能为空");
		}
		if (gameGraspDollRecord.getMultiple() == null) {
			return ResultUtil.fail("倍数不能为空");
		}
		if (gameGraspDollRecord.getPawsPrice() == null || gameGraspDollRecord.getPawsPrice() == 0) {
			return ResultUtil.fail("爪子价格不能为空");
		}

		// 计算总价
		BigDecimal pawsPrice = new BigDecimal(gameGraspDollRecord.getPawsPrice());
		BigDecimal multiple = new BigDecimal(gameGraspDollRecord.getMultiple());
		Long totalPrice = pawsPrice.multiply(multiple).setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
		gameGraspDollRecord.setTotalPrice(Double.valueOf(totalPrice));
		gameGraspDollRecord.setCreateAt(DateUtil.nowTime());

		// 减金币
		JsonNode res = modifyMoney(gameGraspDollRecord.getUid(), gameGraspDollRecord.getPawsPrice(), 0);
		if (res == null) {
			log.error("抓娃娃修改用户金额失败");
			return ResultUtil.fail("抓娃娃遇到网络问题");
		}
		JSONObject json = res.getObject();
		if (!"200".equals(String.valueOf(json.get("code")))) {
			return ResultUtil.fail(String.valueOf(json.get("code")), json.getString("message"));
		}
		
		//dollId 大于0 表示用户抓到了娃娃 需要后台计算能能否将娃娃抓到手里;如果等于 0 表示没有抓到娃娃 爪子抓空了;
		Long dollId = gameGraspDollRecord.getGraspdollId();
		if (dollId > 0) {
			// 获取抓娃娃配置
			String str = redisShardClient.get(Constants.gameDollConfig);
			if (StringUtils.isBlank(str)) {
				return ResultUtil.fail("抓娃娃遇到网络问题.");
			}

			// 根据概率计算是否抓到娃娃
			List<GameDollConfigDto> result = JsonUtil.toListBean(str, GameDollConfigDto.class);
			Double probability = 0.0d;
			for (GameDollConfigDto gameDollConfigDto : result) {
				if (gameDollConfigDto.getId().longValue() == dollId.longValue()) {
					probability = gameDollConfigDto.getProbability();
					break;
				}
			}
			double random = Math.random();
			if (random <= probability) {// 抓到啦
				// 加金币
				JsonNode jjb = modifyMoney(gameGraspDollRecord.getUid(), totalPrice, 1);
				if (jjb == null) {
					log.info("抓娃娃修改用户金额失败");
					return ResultUtil.fail("抓娃娃遇到网络问题");
				}
				JSONObject jjbJson = res.getObject();
				if (!"200".equals(String.valueOf(jjbJson.get("code")))) {
					log.info("抓娃娃修改用户金额：" + jjbJson.getString("code") + "" + jjbJson.getString("message"));
					return ResultUtil.fail(jjbJson.getString("code"), jjbJson.getString("message"));
				}
				int row = gameGraspDollRecordService.saveGameGraspDollRecord(gameGraspDollRecord);

				if (row > 0) {
					//记录游戏历史(抓娃娃)
					GameRecord gameRecord = new GameRecord();
					gameRecord.setMoney(gameGraspDollRecord.getPawsPrice());
					gameRecord.setType(3);
					gameRecord.setRoomid(gameGraspDollRecord.getAnchorId());
					gameRecord.setUid(gameGraspDollRecord.getUid());
					gameRecord.setCtime(System.currentTimeMillis() / 1000);
					gameRecord.setProfit(gameGraspDollRecord.getPawsPrice() - totalPrice);//盈亏针对平台
					gameRecordService.saveGameRecord(gameRecord);
					
					//抓娃娃游戏下注统计
					UserTransactionHis  userTransactionHis = new UserTransactionHis();
					userTransactionHis.setUid(Integer.parseInt(gameGraspDollRecord.getUid().toString()));
					userTransactionHis.setTransType(4);//投注
					userTransactionHis.setMoney(gameGraspDollRecord.getPawsPrice());
					userTransactionHis.setCreateTime(gameRecord.getCtime()*1000);
					userTransactionHisService.saveUserTransactionHis(userTransactionHis);
					
					//抓娃娃游戏胜出统计
					userTransactionHis.setMoney(totalPrice);
					userTransactionHis.setTransType(5);//胜出
					userTransactionHisService.saveUserTransactionHis(userTransactionHis);
					
					ResultDto<Object> data = ResultUtil.success("抓到啦");
					HMap map = HMap.by("totalPrice", totalPrice);
					map.set("recordId", gameGraspDollRecord.getId());
					data.setData(map);
					return data;
				}
			}
		} 
		//没抓到娃娃
		//记录游戏历史(抓娃娃)
		gameGraspDollRecordService.saveGameGraspDollRecord(gameGraspDollRecord);
		GameRecord gameRecord = new GameRecord();
		gameRecord.setMoney(gameGraspDollRecord.getPawsPrice());
		gameRecord.setType(3);
		gameRecord.setRoomid(gameGraspDollRecord.getAnchorId());
		gameRecord.setUid(gameGraspDollRecord.getUid());
		gameRecord.setCtime(System.currentTimeMillis() / 1000);
		gameRecord.setProfit(gameGraspDollRecord.getPawsPrice());//盈亏针对平台
		gameRecordService.saveGameRecord(gameRecord);
		
		//抓娃娃游戏下注统计
		UserTransactionHis  userTransactionHis = new UserTransactionHis();
		userTransactionHis.setUid(Integer.parseInt(gameGraspDollRecord.getUid().toString()));
		userTransactionHis.setTransType(4);//下注
		userTransactionHis.setMoney(gameGraspDollRecord.getPawsPrice());
		userTransactionHis.setCreateTime(gameRecord.getCtime()*1000);
		userTransactionHisService.saveUserTransactionHis(userTransactionHis);
		
		return ResultUtil.fail("没抓到");
	}

	/**
	 * 推送中奖消息
	 * @return
	 */
	public ResultDto<Object> pushGameDollMsg(Long gameDollRecordId) {
		if (null != gameDollRecordId) {
			GameGraspDollRecord gameGraspDollRecord = gameGraspDollRecordService.selectByPrimaryKey(gameDollRecordId);
			if (null != gameGraspDollRecord) {
				// 观众名字
				String uname = "";
				String strUserBase = redisShardClient.hget(Constants.keyBaseInfoList,
						String.valueOf(gameGraspDollRecord.getUid()));
				if (!StringUtils.isBlank(strUserBase)) {
					UserBaseDto userBase = JsonUtil.toBean(strUserBase, UserBaseDto.class);
					if (userBase == null) {
						uname = String.valueOf(gameGraspDollRecord.getUid());
					} else {
						uname = userBase.getNickname();
					}
				} else {
					uname = String.valueOf(gameGraspDollRecord.getUid());
				}

				String roomInformMoney = redisShardClient.hget(Constants.gameDollInformMoney, "roomInformMoney");
				String platformInformMoney = redisShardClient.hget(Constants.gameDollInformMoney,
						"platformInformMoney");
				Long startPrice = StringUtils.isNotBlank(roomInformMoney) ? Long.valueOf(roomInformMoney) : 100L;
				Long endPrice = StringUtils.isNotBlank(platformInformMoney) ? Long.valueOf(platformInformMoney) : 1000L;

				Long totalPrice = gameGraspDollRecord.getTotalPrice().longValue();
				Double multiple = gameGraspDollRecord.getMultiple();
				String beishu = multiple == multiple.intValue() ? multiple.intValue() + "" : multiple + "";
				// 符合条件则推送消息
				if (totalPrice >= startPrice && totalPrice < endPrice) {
					// 恭喜***抓中*倍娃娃，赢得***金币！
					String msg = String.format("恭喜%s抓中%s倍娃娃，赢得%s金币！", uname, beishu, totalPrice.intValue());

					// 增加跑道
					List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
					HashMap<String, Object> map = new HashMap<String, Object>();

					map = new HashMap<String, Object>();
					map.put("name", msg);
					map.put("color", "");
					list.add(map);

					RoomNoticeTxtCmod msgBody = new RoomNoticeTxtCmod();
					msgBody.setData(list);
					msgBody.setUid(gameGraspDollRecord.getAnchorId().intValue());
					msgBody.setNickname(uname);
					msgBody.setLuckyMsg(null);

					AsyncManager.getInstance()
							.execute(new GameDollMsgPushTask(2, gameGraspDollRecord.getAnchorId(), msgBody));
				} else if (totalPrice >= endPrice) {
					// 金手达人**抓中*倍娃娃，赢得***金币！
					String msg = String.format("金手达人%s抓中%s倍娃娃，赢得%s金币！", uname, beishu, totalPrice.intValue());

					// 增加跑道
					List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
					HashMap<String, Object> map = new HashMap<String, Object>();
					List<Map<String, Object>> toUserList = new ArrayList<Map<String, Object>>();

					map = new HashMap<String, Object>();
					map.put("name", msg);
					map.put("color", "");
					list.add(map);
					toUserList.add(map);

					RunWayCMod msgBody = new RunWayCMod();
					map = new HashMap<String, Object>();
					map.put("list", list);
					msgBody.setData(map);

					AsyncManager.getInstance()
							.execute(new GameDollMsgPushTask(1, gameGraspDollRecord.getAnchorId(), msgBody));
				}
				return ResultUtil.success("推送成功");
			} else {
				log.info("推送游戏消息失败，没有找到游戏记录");
			}
		}
		return ResultUtil.fail("不推送");
	}
	
	/**
	 * 根据ID修改用户账户金币
	 * @param uid  用户ID
	 * @param money  金币数
	 * @param type  1:加币; other:减币
	 * @return
	 */
	private JsonNode modifyMoney(Long uid, Long money, int type) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("adminid", "admin");
			params.put("uid", uid);
			params.put("zhutou", money);
			params.put("type", type);
			params.put("credit", "0");
			if (type == 1) {
				params.put("desc", "抓娃娃游戏中奖加币" + money);
			} else {
				params.put("desc", "抓娃娃游戏减币" + money);
			}
			HttpResponse<JsonNode> response = Unirest.post(businessServerUrl).fields(params).asJson();
			log.info("调用业务接口,返回：" + response.getBody());
			return response.getBody();
		} catch (UnirestException e) {
			log.error("调用业务接口异常", e);
			return null;
		}
	}

	@Override
	public ResultDto<Map<String, Object>> getGameDollMoney() {
		String claw1 = redisShardClient.hget(Constants.gameDollTypeClaw, "claw1");
		String claw2 = redisShardClient.hget(Constants.gameDollTypeClaw, "claw2");
		String claw3 = redisShardClient.hget(Constants.gameDollTypeClaw, "claw3");
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("claw1", claw1);
		result.put("claw2", claw2);
		result.put("claw3", claw3);
		return ResultUtil.success(result);
	}

}
