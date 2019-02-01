package com.hkzb.game.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hkzb.game.common.utils.CModProtocol;
import com.hkzb.game.common.utils.Constants;
import com.hkzb.game.common.utils.DateUtil;
import com.hkzb.game.common.utils.JsonUtil;
import com.hkzb.game.common.utils.RedisShardClient;
import com.hkzb.game.common.utils.ResultUtil;
import com.hkzb.game.dao.GameCarRecordMapper;
import com.hkzb.game.dao.GameCarStakeRecordMapper;
import com.hkzb.game.dto.GameCarConfigResultDto;
import com.hkzb.game.dto.GameCarMessageDto;
import com.hkzb.game.dto.GameCarStakeRecordDto;
import com.hkzb.game.dto.GameCarTimeDto;
import com.hkzb.game.dto.LotteryRecordParamDto;
import com.hkzb.game.dto.MessageCMod;
import com.hkzb.game.dto.ResultDto;
import com.hkzb.game.dto.RoomGameInfoModel;
import com.hkzb.game.dto.StakeRecordParamDto;
import com.hkzb.game.dto.UserBaseDto;
import com.hkzb.game.dto.WinningResultDto;
import com.hkzb.game.model.BetRecord;
import com.hkzb.game.model.GameCarRecord;
import com.hkzb.game.model.GameCarStakeRecord;
import com.hkzb.game.model.GameRecord;
import com.hkzb.game.model.UserTransactionHis;
import com.hkzb.game.timer.AsyncManager;
import com.hkzb.game.timer.MessagePushTask;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

import redis.clients.jedis.Tuple;

@Service
@Transactional
public class GameCarService implements IGameCarService {

	@Autowired
	private GameCarStakeRecordMapper gameCarStakeRecordMapper;

	@Autowired
	private GameCarRecordMapper gameCarRecordMapper;

	@Autowired
	private RedisShardClient redisShardClient;
	
	@Autowired
	private IMessageService messageService;
	
	@Autowired
	private IAllBetRecordService iAllBetRecordService;

	@Autowired
	private IGameRecordService gameRecordService;
	
	@Autowired
	private IUserTransactionHisService userTransactionHisService;

	private static Logger log = Logger.getLogger(GameCarService.class);

	@Value("${business.server.url}")
	private String businessServerUrl;

	@Override
	@Transactional(readOnly = true)
	public ResultDto<List<GameCarConfigResultDto>> getGameCarConfig() {
		String str = redisShardClient.get(Constants.gameCarConfig);
		if (StringUtils.isBlank(str)) {
			return ResultUtil.fail();
		}
		List<GameCarConfigResultDto> result = JsonUtil.toListBean(str, GameCarConfigResultDto.class);
		return ResultUtil.success(result);
	}

	@Override
	@Transactional(readOnly = true)
	public ResultDto<Map<String, Long>> getGameCarTime() {
		String json = redisShardClient.get(Constants.gameCarTime);
		if(StringUtils.isBlank(json)) {
			return ResultUtil.fail();
		}
		GameCarTimeDto gameCarTimeDto = JsonUtil.toBean(json, GameCarTimeDto.class);
		Map<String, Long> map = new HashMap<String, Long>();
		map.put("time", Constants.time-((DateUtil.nowTime()/1000)-gameCarTimeDto.getTime()));
		map.put("periods", gameCarTimeDto.getPeriods());
		return ResultUtil.success(map);
	}

	@Override
	public Map<String,Object> getUserStakeAndTotalStake(Long uid){
		if(uid==null||uid==0) {
			return null;
		}
		Map<String,Object> result = new HashMap<String,Object>();
		List<Map<String,Long>> stakeList = new ArrayList<Map<String,Long>>();
		String s_idx = redisShardClient.hget(Constants.gameCarUsertakeIdx,String.valueOf(uid));
		if(!StringUtils.isBlank(s_idx)) {
			String strList = redisShardClient.lindex(Constants.gameCarUserStake,Long.valueOf(s_idx)-1);
			List<GameCarStakeRecord> list = JsonUtil.toListBean(strList, GameCarStakeRecord.class);
			for (GameCarStakeRecord bean : list) {
				Map<String,Long> map = new HashMap<String,Long>();
				map.put("carId", bean.getCarId());
				map.put("money", bean.getMoney());
				stakeList.add(map);
			}
		}
		result.put("stake", stakeList);
		
		List<Map<String,Object>> totalList = new ArrayList<Map<String,Object>>();
		Set<Tuple> tuples = redisShardClient.zrangeWithScores(Constants.gameCarUserTotalStake, 0, -1);
		for (Tuple tuple : tuples) {
			Map<String,Object> map =  new HashMap<String,Object>();
			map.put("carId",tuple.getElement());
			map.put("money",tuple.getScore());
			totalList.add(map);
		}
		result.put("total", totalList);
		return result;
	}
	
	@Override
	public  ResultDto<Object> stake(GameCarStakeRecord param) {
		int modifyMoneyFlag = 0;//状态标识
		try {
			if (param.getRoomId() == null || param.getRoomId() == 0) {
				return ResultUtil.fail("直播间不能为空");
			}
			if (param.getAnchorId() == null || param.getAnchorId() == 0) {
				return ResultUtil.fail("主播不能为空");
			}
			if (param.getCarId() == null || param.getCarId() == 0) {
				return ResultUtil.fail("押注图标不能为空");
			}
			if (param.getUid() == null || param.getUid() == 0) {
				return ResultUtil.fail("用户ID不能为空");
			}
			if (param.getMoney() == null || param.getMoney() == 0) {
				return ResultUtil.fail("押注筹码不能为空");
			}
			if (param.getPeriods() == null || param.getPeriods() == 0) {
				return ResultUtil.fail("押注的期数不正确");
			}
			ResultDto<Map<String, Long>> rd = getGameCarTime();
			if (!ResultUtil.SUCCESS_CODE.equals(rd.getCode())) {
				return ResultUtil.fail("押注失败");
			}
			//三秒内不能押注
			if (rd.getData().get("time")<=5) {
				return ResultUtil.fail("开奖中或已经开过奖,不能押注");
			}
			if (param.getPeriods().longValue() != rd.getData().get("periods").longValue()) {
				return ResultUtil.fail("该期已经结束,不能押注");
			}
			param.setStakeTime(DateUtil.nowTime());
			param.setStatus(Constants.status_1);

			JsonNode res = modifyMoney(param.getUid(), param.getMoney(), 0);
			if(res==null) {
				return ResultUtil.fail("押注失败");
			}
			JSONObject json = res.getObject();
			if (!"200".equals(String.valueOf(json.get("code")))) {
				return ResultUtil.fail(String.valueOf(json.getInt("code")),json.getString("message"));
			}
			
			modifyMoneyFlag = 1;
			
			GameCarRecord gcr = gameCarRecordMapper.getLotteryByPeriods(param.getPeriods());
			param.setRefId(gcr.getId());
			
			//判断用户是否押注过
			boolean exists = redisShardClient.hexists(Constants.gameCarUsertakeIdx, String.valueOf(param.getUid()));
			if(!exists) {
				int i = gameCarStakeRecordMapper.insertSelective(param);
				if (i == 0) {
					return ResultUtil.fail("押注失败");
				}
				List<GameCarStakeRecord> list = new ArrayList<GameCarStakeRecord>();
				list.add(param);
				Long idx = redisShardClient.rpush(Constants.gameCarUserStake, JsonUtil.toJson(list));
				if(idx==null||idx==0) {
					throw new RuntimeException("押注异常");
				}
				redisShardClient.hset(Constants.gameCarUsertakeIdx ,String.valueOf(param.getUid()), String.valueOf(idx));
				calculateTotalStakeAndPushMsg(param);
				return ResultUtil.success();
			}
			Long index = Long.valueOf(redisShardClient.hget(Constants.gameCarUsertakeIdx,String.valueOf(param.getUid())));
			//拿到已经押注的信息
			String strList = redisShardClient.lindex(Constants.gameCarUserStake,index-1);
			List<GameCarStakeRecord> list = JsonUtil.toListBean(strList, GameCarStakeRecord.class);
			
			//当前用户有押注信息的情况
			if(list!=null&&!list.isEmpty()) {
				//判断缓存中第一条数据的用户id是否与当前用户id相等,若不相等 退币 结束本次押注
				if(param.getUid().longValue()!=list.get(0).getUid().longValue()) {
					modifyMoney(param.getUid(), param.getMoney(), 1);
					return ResultUtil.fail("押注失败"); 
				}
			}
			
			boolean flag = false;
			GameCarStakeRecord bean = null;
			Long oldStakeTime = null;
			//遍历处理 相同图标押注情况
			for (int i = 0; i < list.size(); i++) {
				 bean = list.get(i);
				//如果图标ID相同  更新
				if(param.getCarId().longValue()==bean.getCarId().longValue()) {
					oldStakeTime = bean.getStakeTime();
					bean.setStakeTime(param.getStakeTime());
					bean.setMoney(bean.getMoney()+param.getMoney());
					list.remove(i);
					flag = true;
					break;
				}
			}
			if(flag) {
				int row = gameCarStakeRecordMapper.updateByPrimaryKeyAndStakeTimeSelective(bean,oldStakeTime);
				if (row == 0) {
					modifyMoney(param.getUid(), param.getMoney(), 1);
					return ResultUtil.fail("押注失败");
				}
				list.add(bean);
			}else {
				int row = gameCarStakeRecordMapper.insertSelective(param);
				if (row == 0) {
					modifyMoney(param.getUid(), param.getMoney(), 1);
					return ResultUtil.fail("押注失败");
				}
				list.add(param);
			}
			String redisResult = redisShardClient.lset(Constants.gameCarUserStake, index-1, JsonUtil.toJson(list));
			if(StringUtils.isBlank(redisResult)) {
				throw new RuntimeException("押注异常");
			}
			calculateTotalStakeAndPushMsg(param);
			return ResultUtil.success();
		} catch (Exception e) {
			//执行退币操作
			if(modifyMoneyFlag==1) {
				log.error("押注异常,执行退币操作");
				modifyMoney(param.getUid(), param.getMoney(), 1);
			}
			throw new RuntimeException("押注异常",e);
		}
	}
	
	@Override
	public ResultDto<Object> saveGameCarRecord(GameCarRecord param) {
		int i = gameCarRecordMapper.insert(param);
		if (i == 0) {
			return ResultUtil.fail();
		}
		return ResultUtil.success();
	}
	
	@Override
	public ResultDto<Object> saveGameCarRecordSelective(GameCarRecord param) {
		int i = gameCarRecordMapper.insertSelective(param);
		if (i == 0) {
			return ResultUtil.fail();
		}
		return ResultUtil.success();
	}
	
	@Override
	public ResultDto<Object> updateGameCarRecord(GameCarRecord param) {
		int i = gameCarRecordMapper.updateGameCarRecord(param);
		if (i == 0) {
			return ResultUtil.fail();
		}
		return ResultUtil.success();
	}

	@Override
	@Transactional(readOnly = true)
	public Long getMaxPeriods() {
		return gameCarRecordMapper.getMaxPeriods();
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public int updateCarStakeRecordById(List<GameCarStakeRecord> list) {
		return gameCarStakeRecordMapper.updateBatchByPrimaryKey(list);
	}

	@Override
	public ResultDto<List<GameCarStakeRecordDto>> getStakeRecord(StakeRecordParamDto param) {
		if (param.getPageNo() == 0) {
			param.setPageNo(Constants.defaultPageNo);
		}
		if (param.getPageSize() == 0) {
			param.setPageSize(Constants.defaultPageSize);
		}
		return ResultUtil.success(gameCarStakeRecordMapper.getStakeRecord(param));
	}

	@Override
	public ResultDto<List<GameCarRecord>> getLotteryRecord(LotteryRecordParamDto param) {
		if (param.getPageNo() == 0) {
			param.setPageNo(Constants.defaultPageNo);
		}
		if (param.getPageSize() == 0) {
			param.setPageSize(Constants.defaultPageSize);
		}
		return ResultUtil.success(gameCarRecordMapper.getLotteryRecord(param));
	}

	@Override
	public ResultDto<WinningResultDto> getLotteryByPeriods(Long periods, Long uid) {
		if(periods==null||periods==0) {
			return ResultUtil.fail("期数不能为空");
		}
		GameCarRecord gameCarRecord = gameCarRecordMapper.getLotteryByPeriods(periods);
		if(gameCarRecord==null) {
			return ResultUtil.fail();
		}
		if(gameCarRecord.getLotteryTime()==null||gameCarRecord.getLotteryTime()==0) {
			return ResultUtil.fail(Constants.code_3000,Constants.msg_3000);
		}
		WinningResultDto winningResultDto = new WinningResultDto();
		BetRecord betRecord = iAllBetRecordService.getBetRecord(periods, uid);
		if(betRecord==null) {
			winningResultDto.setTotalDeservedMoney(0L);
			winningResultDto.setTotalDeservedMoney(0L);
		}else {
			winningResultDto.setTotalStakeMoney(betRecord.getBetTotal());
			winningResultDto.setTotalDeservedMoney(betRecord.getDeservedTotal());
		}
		winningResultDto.setCarId1(gameCarRecord.getCarId1());
		winningResultDto.setCarId2(gameCarRecord.getCarId2());
		winningResultDto.setCarId3(gameCarRecord.getCarId3());
		
		return ResultUtil.success(winningResultDto);
	}

	/**
	 * 拿到开奖类型
	 * @return 1：中一   2：中二  3：中三
	 */
	private int getLotteryType(Map<String,String> gameCarSettingMap) {
		log.info("获取本次开奖类型");
		double random = getRandom();
		double temp = 0.00;
		for(Entry<String, String> entry : gameCarSettingMap.entrySet()) {
			double pr = StringUtils.isBlank(entry.getValue()) ? 0 : Double.parseDouble(entry.getValue());
			if (temp < random && random <= (temp + pr)) {
				log.info(String.format("%s%s", "本次开奖类型为:",entry.getKey()));
				if(Constants.PR3.equals(entry.getKey())) {
					return Constants.lotteryType3;
				}else if(Constants.PR2.equals(entry.getKey())) {
					return Constants.lotteryType2;
				}else {
					return Constants.lotteryType1;
				}
			}
			temp = temp + pr;
		}
		log.info(String.format("%s%s", "本次开奖类型,根据概率计算失败,返回:",Constants.PR1));
		return Constants.lotteryType1;
	}
	
	@Override
	public void lottery() {
		//拿到 中1    中2    中3  概率
		Map<String,String> gameCarSettingJson = redisShardClient.hgetAll(Constants.gameCarLotteryTypePR);

		int type = getLotteryType(gameCarSettingJson);
		
		//概率和倍数信息   暂时先使用 Map结构 然后转换 （不转换有类型错误）
		String gameCarPRAndBSStr = redisShardClient.get(Constants.gameCarPRAndBS);
		Map<String,Map<Long,Double>> gameCarPRAndBS = convertMap(gameCarPRAndBSStr);
		Map<String, String> map = redisShardClient.hgetAll(Constants.gameInformMoney);
		Long roomInformMoney = 0l;
		Long platformInformMoney = 0l;
		if(StringUtils.isNotBlank(map.get("roomInformMoney"))) {
			roomInformMoney = Long.valueOf(map.get("roomInformMoney"));
		}else {
			roomInformMoney = 10000l;
		}
		if(StringUtils.isNotBlank(map.get("platformInformMoney"))) {
			platformInformMoney = Long.valueOf(map.get("platformInformMoney"));
		}else {
			platformInformMoney = 100000l;
		}
		
		List<Long> carIdList = new ArrayList<Long>(3);
		if(type==Constants.lotteryType1) {
			Map<Long, Double> prMap1 = gameCarPRAndBS.get("probability1");
			for(int i=0;i<3;i++) {
				double random = getRandom();
				double temp = 0.00;
				long key = 0;
				for (Entry<Long, Double> entry : prMap1.entrySet()) {
					double pr = entry.getValue()==null?0:entry.getValue();
					if (temp < random && random <= (temp + pr)) {
						key = entry.getKey();
						carIdList.add(entry.getKey());
						break;
					}
					temp = temp + pr;
				}
				prMap1 = getNewPrMap(prMap1, key);
			}
		}else if(type==Constants.lotteryType2) {
			Map<Long, Double> prMap2 = gameCarPRAndBS.get("probability2");;
			double random = getRandom();
			double temp = 0.00;
			long key = 0;
			for (Entry<Long, Double> entry : prMap2.entrySet()) {
				double pr = entry.getValue()==null?0:entry.getValue();
				if (temp < random && random <= (temp + pr)) {
					key = entry.getKey();
					carIdList.add(entry.getKey());
					carIdList.add(entry.getKey());
					break;
				}
				temp = temp + pr;
			}
			
			Map<Long, Double> prMap1 = gameCarPRAndBS.get("probability1");
			prMap1 = getNewPrMap(prMap1, key);
			random = getRandom();
			temp = 0.00;
			for (Entry<Long, Double> entry : prMap1.entrySet()) {
				double pr = entry.getValue()==null?0:entry.getValue();
				if (temp < random && random <= (temp + pr)) {
					carIdList.add(entry.getKey());
					break;
				}
				temp = temp + pr;
			}
		}else if(type==Constants.lotteryType3) {
			Map<Long, Double> prMap3 = gameCarPRAndBS.get("probability3");
			double random = getRandom();
			double temp = 0.00;
			for (Entry<Long, Double> entry : prMap3.entrySet()) {
				double pr = entry.getValue()==null?0:entry.getValue();
				if (temp < random && random <= (temp + pr)) {
					carIdList.add(entry.getKey());
					carIdList.add(entry.getKey());
					carIdList.add(entry.getKey());
					break;
				}
				temp = temp + pr;
			}
		}
		log.info(String.format("本期开奖结果:%s", carIdList));
		
		GameCarRecord gameCarRecord = new GameCarRecord();
		gameCarRecord.setCarId1(carIdList.get(0));
		gameCarRecord.setCarId2(carIdList.get(1));
		gameCarRecord.setCarId3(carIdList.get(2));
		gameCarRecord.setAnchorId(0L);
		gameCarRecord.setRoomId(0L);
		gameCarRecord.setLotteryTime(DateUtil.nowTime());
		gameCarRecord.setLotteryType(type);
		
		String gameCarTimeStr = redisShardClient.get(Constants.gameCarTime);
		Long periods = null;
		if(StringUtils.isBlank(gameCarTimeStr)) {
			 periods = getMaxPeriods();
		}else {
			GameCarTimeDto gameCarTimeDto = JsonUtil.toBean(gameCarTimeStr, GameCarTimeDto.class);
			periods = gameCarTimeDto.getPeriods();
		}
		gameCarRecord.setPeriods(periods);
		updateGameCarRecord(gameCarRecord);
		
		int i = 0;
		List<GameCarStakeRecord> stakeList = null;
		while(true) {
			if (i == 0) {
				stakeList = new ArrayList<GameCarStakeRecord>();
			}
			//每次取一个用户押注信息
			List<String> list = redisShardClient.blpop(1,Constants.gameCarUserStake);
			if(list==null||list.size()==0) {
				break;
			}
			log.debug("用户押注信息："+list);
			
			//先获取倍数 防止期间有改动导致同期的倍数不一致的问题
			//中1 的倍数
			Map<Long, Double> multiple1 = gameCarPRAndBS.get("multiple1");
			//中2 的倍数
			Map<Long, Double> multiple2 = gameCarPRAndBS.get("multiple2");
			//中3 的倍数
			Map<Long, Double> multiple3 = gameCarPRAndBS.get("multiple3");
			
			//平台佣金比
			double gameCarCommission = 0.00;
			String commission = redisShardClient.get(Constants.gameCarCommission);
			if(!StringUtils.isBlank(commission)) {
				gameCarCommission = Double.parseDouble(commission);
			}
			//押注总金额
			long betTotal = 0;
			//中奖的本金
			long capitalTotal = 0;
			//中奖的筹码 不包含本金
			long awTotal = 0;
			long uid = 0;
			long anchorId = 0;
			for(int idx=1;idx<list.size();idx++) {
				String json = list.get(idx);
				if(StringUtils.isBlank(json)) {
					break;
				}
				List<GameCarStakeRecord> tempList = JsonUtil.toListBean(json, GameCarStakeRecord.class);
				for (int j = 0; j < tempList.size(); j++) {
					GameCarStakeRecord stake = tempList.get(j); 
					stake.setCommissionRate(gameCarCommission);
					stake.setAwardedMoney(0L);
					stake.setCommission(0L);
					stake.setDeservedMoney(0L);
					stake.setMultiple(0.00);
					
					uid = stake.getUid();
					anchorId = stake.getAnchorId();
					//中奖了
					if(carIdList.contains(stake.getCarId().longValue())) {
						stake.setStatus(Constants.status_2);
						//根据不同的 类型 计算最终获得的筹码
						if(type==Constants.lotteryType1) {
							Double beishu = 1.00;
							if(multiple1!=null&&multiple1.get(stake.getCarId())!=null) {
								beishu = multiple1.get(stake.getCarId());
							}
							//中奖筹码
							Long awardedMoney = Math.round(stake.getMoney()*beishu);
							
							BigDecimal b1 = new BigDecimal(stake.getMoney());
							BigDecimal b2 = new BigDecimal(beishu);
							BigDecimal b3 = new BigDecimal(gameCarCommission);
							
							//佣金
							Long dbCommission = b1.multiply(b2).multiply(b3)
									.setScale(0, RoundingMode.HALF_EVEN).longValue();
							//应得筹码
							Long deservedMoney = stake.getMoney() + awardedMoney - dbCommission; 
							
							stake.setAwardedMoney(awardedMoney);
							stake.setCommission(dbCommission);
							stake.setDeservedMoney(deservedMoney);
							stake.setMultiple(beishu);
						}else if(type==Constants.lotteryType2) {
							Double beishu = 1.00;
							int flag = 0;
							for(Long carId:carIdList) {
								if(carId.longValue()==stake.getCarId().longValue()) {
									flag++;
								}
								if(flag==2) {
									break;
								}
							}
							//中了2个
							if(flag==2) {
								if(multiple2!=null&&multiple2.get(stake.getCarId())!=null) {
									beishu = multiple2.get(stake.getCarId());
								}
							}else {
								if(multiple1!=null&&multiple1.get(stake.getCarId())!=null) {
									beishu = multiple1.get(stake.getCarId());
								}
							}
							//中奖筹码
							Long awardedMoney = Math.round(stake.getMoney()*beishu);
							
							BigDecimal b1 = new BigDecimal(stake.getMoney());
							BigDecimal b2 = new BigDecimal(beishu);
							BigDecimal b3 = new BigDecimal(gameCarCommission);
							
							//佣金
							Long dbCommission = b1.add(b1.multiply(b2)).multiply(b3)
									.setScale(0, RoundingMode.HALF_EVEN).longValue();
							//应得筹码
							Long deservedMoney = stake.getMoney() + awardedMoney - dbCommission; 
							
							stake.setAwardedMoney(awardedMoney);
							stake.setCommission(dbCommission);
							stake.setDeservedMoney(deservedMoney);
							stake.setMultiple(beishu);
						}else if(type==Constants.lotteryType3) {
							Double beishu = 1.00;
							if(multiple3!=null&&multiple3.get(stake.getCarId())!=null) {
								beishu = multiple3.get(stake.getCarId());
							}
							//中奖筹码
							Long awardedMoney = Math.round(stake.getMoney()*beishu);
							
							BigDecimal b1 = new BigDecimal(stake.getMoney());
							BigDecimal b2 = new BigDecimal(beishu);
							BigDecimal b3 = new BigDecimal(gameCarCommission);
							
							//佣金
							Long dbCommission = b1.multiply(b2).multiply(b3)
									.setScale(0, RoundingMode.HALF_EVEN).longValue();
							//应得筹码
							Long deservedMoney = stake.getMoney() + awardedMoney - dbCommission; 
							
							stake.setAwardedMoney(awardedMoney);
							stake.setCommission(dbCommission);
							stake.setDeservedMoney(deservedMoney);
							stake.setMultiple(beishu);
						}
						capitalTotal = capitalTotal + stake.getMoney();
						awTotal = awTotal + stake.getAwardedMoney();
					}else {
						stake.setStatus(Constants.status_3);
					}
					betTotal = betTotal+stake.getMoney();
					stakeList.add(stake);
					i++;
				}
			}
			modifyMoneyAndpushMsg(anchorId, periods, uid, betTotal, capitalTotal, awTotal, gameCarCommission, roomInformMoney, platformInformMoney);
			// 五百条更新一次
			if (i / 500 != 0) {
				updateCarStakeRecordById(stakeList);
				stakeList = new ArrayList<GameCarStakeRecord>();
				i=0;
			}
		}
		if (stakeList != null && stakeList.size() > 0) {
			updateCarStakeRecordById(stakeList);
		}
		
		redisShardClient.expire(String.format("%s%s", Constants.gameMsg,periods),60*10);
		AsyncManager.getInstance().execute(new MessagePushTask(periods));
		
		redisShardClient.del(Constants.gameCarUserStake);
		redisShardClient.del(Constants.gameCarUsertakeIdx);
		redisShardClient.del(Constants.gameCarUserTotalStake);
		
		GameCarTimeDto gameCarTimeDto = new GameCarTimeDto();
		gameCarTimeDto.setPeriods(periods+1);
		gameCarTimeDto.setTime(DateUtil.nowTime()/1000);
		redisShardClient.set(Constants.gameCarTime,JsonUtil.toJson(gameCarTimeDto));
		
		GameCarRecord record = new GameCarRecord();
		record.setAnchorId(0L);
		record.setRoomId(0L);
		record.setPeriods(periods+1);
		gameCarRecordMapper.insertSelective(record);
	}
	
	/**
	 * 获取一个[0,1)之间的随机数  如果循环100次随机数还是为0,返回 0.01
	 * @return
	 */
	private  double  getRandom() {
		double random = Math.random();
		int randomNum = 0;
		while(random==0) {
			random = Math.random();
			randomNum++;
			if(randomNum==100) {
				random = 0.1;
			}
		}
		return random;
	}
	
	@SuppressWarnings("unchecked")
	private Map<String,Map<Long,Double>> convertMap(String json){
		if(StringUtils.isBlank(json)) {
			return null;
		}
		Map<String,Map<String,Object>> tempMap = JsonUtil.toBean(json, Map.class);
		
		Map<String,Map<Long,Double>> resMap = new HashMap<String,Map<Long,Double>>();
		for (Entry<String, Map<String,Object>> entry : tempMap.entrySet()) {
			Map<Long,Double> data = new HashMap<Long,Double>();
			for (Entry<String,Object> entry1 : entry.getValue().entrySet()) {
				data.put(Long.valueOf(entry1.getKey()), Double.valueOf(entry1.getValue().toString()));
			}
			resMap.put(entry.getKey(), data);
		}
		return resMap;
	}
	
	/**
	 * 修改Map中的key和value 将参数key对应的值 平均分给其他的key对应的value 
	 * @param prMap  概率Map
	 * @param key  key
	 * @return  转换后的概率Map
	 */
	private  Map<Long,Double> getNewPrMap(Map<Long,Double> prMap,Long key){
		if(prMap==null) {
			return null;
		}
		if(key==null) {
			return prMap;
		}
		Double value = prMap.get(key);
		prMap.remove(key);
		if(value==null||value==0) {
			return prMap;
		}
		Double dValue = value/prMap.size();
		BigDecimal b = new BigDecimal(dValue); 
		double avgValue = b.setScale(5,BigDecimal.ROUND_HALF_UP).doubleValue();
		Map<Long,Double> resMap = new HashMap<Long,Double>();
		for (Entry<Long,Double> entry : prMap.entrySet()) {
			resMap.put(entry.getKey(), entry.getValue()+avgValue);
		}
		return resMap;
	}
	
	/**
	 * 根据ID修改用户账户金币
	 * @param uid  用户ID
	 * @param money  金币数
	 * @param type  1:加币; 2:减币
	 * @return
	 */
	private JsonNode modifyMoney(Long uid,Long money,int type) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("adminid", "admin");
			params.put("uid", uid);
			params.put("zhutou", money);
			params.put("type", type);
			params.put("credit", "0");
			if(type==1) {
				params.put("desc", "押注游戏中奖加币" + money);
			}else {
				params.put("desc", "押注游戏减币" + money);
			}
			HttpResponse<JsonNode> response = Unirest.post(businessServerUrl).fields(params).asJson();
			log.info("调用业务接口,返回：" + response.getBody());
			return response.getBody();
		} catch (Exception e) {
			log.error("调用业务接口异常",e);
			return null;
		}
	}
	
	/**
	 * 计算押注总额 、推送消息
	 * @param param 当前押注信息
	 */
	private void calculateTotalStakeAndPushMsg(GameCarStakeRecord param) {
		try {
			redisShardClient.zincrby(Constants.gameCarUserTotalStake, param.getMoney(), String.valueOf(param.getCarId()));
			Set<Tuple> tuples = redisShardClient.zrangeWithScores(Constants.gameCarUserTotalStake, 0, -1);
			Map<String,Double> map =  new HashMap<String,Double>();
			for (Tuple tuple : tuples) {
				map.put(tuple.getElement(), tuple.getScore());
			}
			MessageCMod message = new MessageCMod();
			message.setCometProtocol(CModProtocol.gameCarUserTotalStake);
			message.setMsg(JsonUtil.toJson(map));
			
			
			long gameId = 0;
			Map<String,String> gameMap =redisShardClient.hgetAll(Constants.gameHashList);
			for (Entry<String, String> entry : gameMap.entrySet()) {
				RoomGameInfoModel roomGameInfoModel = JsonUtil.toBean(entry.getValue(), RoomGameInfoModel.class);
				if(Constants.gameKey.equals(roomGameInfoModel.getGameKey())) {
					gameId = roomGameInfoModel.getGameId();
					break;
				}
			}
			Set<String> set = redisShardClient.zrange(Constants.gameRoomCredit + gameId, 0,-1);
			if(set!=null) {
				for (String uild : set) {
					messageService.pushRoomTotalStakeMsg(JsonUtil.toJson(message),Long.valueOf(uild));
				}
			}
		}catch (Exception e) {
			log.error("推送押注信息异常",e);
		}
	}
	
	private void modifyMoneyAndpushMsg(Long anchorId,Long periods,Long uid, Long betTotal, Long capitalTotal, Long awTotal, double gameCarCommission, Long roomInformMoney,Long platformInformMoney) {
		//未押注
		if(0==betTotal) {
			return;
		}
		//在押注了 没中奖
		if(capitalTotal==0) {
			BetRecord betRecord = new BetRecord();
			betRecord.setAnchorId(anchorId);
			betRecord.setUid(uid);
			betRecord.setBetTotal(betTotal);
			betRecord.setAwTotal(awTotal);
			betRecord.setCapitalTotal(capitalTotal);
			betRecord.setCommission(gameCarCommission);
			betRecord.setCommissionTotal(null);
			betRecord.setDeservedTotal(null);
			betRecord.setPeriods(periods);
			betRecord.setStatus(2);
			iAllBetRecordService.insertBetRecord(betRecord);
			
			//没中奖也要记录
			GameRecord gameRecord = new GameRecord();
			gameRecord.setMoney(betRecord.getBetTotal());
			gameRecord.setType(2);
			gameRecord.setRoomid(betRecord.getAnchorId());
			gameRecord.setUid(betRecord.getUid());
			gameRecord.setCtime(System.currentTimeMillis() / 1000);
			gameRecord.setProfit(betRecord.getBetTotal());//盈亏针对平台
			gameRecordService.saveGameRecord(gameRecord);
			
			//如果没有中奖 只记录押注信息
			UserTransactionHis  userTransactionHis = new UserTransactionHis();
			userTransactionHis.setUid(Integer.parseInt(betRecord.getUid().toString()));
			userTransactionHis.setTransType(1);//押注
			userTransactionHis.setMoney(betRecord.getBetTotal());
			userTransactionHis.setCreateTime(gameRecord.getCtime()*1000);
			userTransactionHis.setRefId(betRecord.getPeriods().toString());
			userTransactionHisService.saveUserTransactionHis(userTransactionHis);
			return;
		}
		
		Long commissionTotal = Math.round(awTotal*gameCarCommission);
		Long deservedTotal = capitalTotal + awTotal - commissionTotal;
		JsonNode jsonResult = modifyMoney(uid, deservedTotal, 1);
		
		BetRecord betRecord = new BetRecord();
		betRecord.setAnchorId(anchorId);
		betRecord.setUid(uid);
		betRecord.setBetTotal(betTotal);
		betRecord.setAwTotal(awTotal);
		betRecord.setCapitalTotal(capitalTotal);
		betRecord.setCommission(gameCarCommission);
		betRecord.setCommissionTotal(commissionTotal);
		betRecord.setDeservedTotal(deservedTotal);
		betRecord.setPeriods(periods);
		//成功和失败 都 需要记录
		if(jsonResult == null || !"200".equals(String.valueOf(jsonResult.getObject().get("code")))) {
			betRecord.setStatus(0);
		}else {
			betRecord.setStatus(1);
			if(deservedTotal>=roomInformMoney) {
				GameCarMessageDto msgDto = new GameCarMessageDto();
				msgDto.setUid(uid);
				msgDto.setMoney(deservedTotal);
				String uname = "";
				msgDto.setUname(uname);
				msgDto.setAnchorId(anchorId);
				msgDto.setMesgType(1);
				if(deservedTotal>platformInformMoney) {
					msgDto.setMesgType(2);
				}
				String strUserBase = redisShardClient.hget(Constants.keyBaseInfoList, String.valueOf(uid));
				if(!StringUtils.isBlank(strUserBase)) {
					UserBaseDto userBase = JsonUtil.toBean(strUserBase, UserBaseDto.class);
					if(userBase==null) {
						msgDto.setUname(String.valueOf(uid));
					}else {
						msgDto.setUname(userBase.getNickname());
					}
				}else {
					msgDto.setUname(String.valueOf(uid));
				}
				redisShardClient.lpush(String.format("%s%s", Constants.gameMsg,periods), JsonUtil.toJson(msgDto));
			}
		}
		iAllBetRecordService.insertBetRecord(betRecord);
		
		//记录游戏历史
		GameRecord gameRecord = new GameRecord();
		gameRecord.setMoney(betRecord.getBetTotal());
		gameRecord.setType(2);
		gameRecord.setRoomid(betRecord.getAnchorId());
		gameRecord.setUid(betRecord.getUid());
		gameRecord.setCtime(System.currentTimeMillis() / 1000);
		gameRecord.setProfit(betRecord.getBetTotal() - betRecord.getDeservedTotal());//盈亏针对平台
		gameRecordService.saveGameRecord(gameRecord);
		
		UserTransactionHis  userTransactionHis = new UserTransactionHis();
		userTransactionHis.setUid(Integer.parseInt(betRecord.getUid().toString()));
		userTransactionHis.setTransType(1);//押注
		userTransactionHis.setMoney(betRecord.getBetTotal());
		userTransactionHis.setCreateTime(gameRecord.getCtime()*1000);
		userTransactionHis.setRefId(betRecord.getPeriods().toString());
		userTransactionHisService.saveUserTransactionHis(userTransactionHis);
		
		userTransactionHis.setMoney(betRecord.getDeservedTotal());
		userTransactionHis.setTransType(2);//胜出
		userTransactionHisService.saveUserTransactionHis(userTransactionHis);
		
		userTransactionHis.setMoney(betRecord.getCommissionTotal());
		userTransactionHis.setTransType(3);//手续费
		userTransactionHisService.saveUserTransactionHis(userTransactionHis);
	}
}