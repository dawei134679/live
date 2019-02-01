package com.mpig.api.controller;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.Unirest;
import com.mpig.api.cache.game.BetRecordsCache;
import com.mpig.api.dictionary.GameAppConfig;
import com.mpig.api.dictionary.lib.GameAppConfigLib;
import com.mpig.api.dictionary.lib.GameBetConfigLib;
import com.mpig.api.dictionary.lib.GiftPromotionConfigLib;
import com.mpig.api.dictionary.lib.UrlConfigLib;
import com.mpig.api.model.ConfigGiftModel;
import com.mpig.api.model.ReturnModel;
import com.mpig.api.model.SmashedEggGiftConfigModel;
import com.mpig.api.model.UserAssetModel;
import com.mpig.api.model.UserBaseInfoModel;
import com.mpig.api.model.UserCarInfoModel;
import com.mpig.api.model.UserVipInfoModel;
import com.mpig.api.model.ValueaddPrivilegeModel;
import com.mpig.api.modelcomet.RunWayCMod;
import com.mpig.api.redis.service.OtherRedisService;
import com.mpig.api.service.IConfigService;
import com.mpig.api.service.IGameRecordService;
import com.mpig.api.service.IGameService;
import com.mpig.api.service.IMallService;
import com.mpig.api.service.ISmashedEggGiftConfigService;
import com.mpig.api.service.IUserCarInfoService;
import com.mpig.api.service.IUserItemService;
import com.mpig.api.service.IUserItemService.ItemSource;
import com.mpig.api.service.IUserService;
import com.mpig.api.service.IUserTransactionHisService;
import com.mpig.api.service.IUserVipInfoService;
import com.mpig.api.service.impl.UserServiceImpl;
import com.mpig.api.utils.ChatMessageUtil;
import com.mpig.api.utils.CodeContant;
import com.mpig.api.utils.DateUtils;
import com.mpig.api.utils.Encrypt;
import com.mpig.api.utils.EncryptUtils;
import com.mpig.api.utils.JsonUtil;
import com.mpig.api.utils.ParamHandleUtils;
import com.mpig.api.utils.RedisContant;
import com.mpig.api.utils.ValueaddServiceUtil;
import com.mpig.api.utils.VarConfigUtils;

@Controller
@Scope("prototype")
@RequestMapping("/game")
public class GameController extends BaseController {
	// =1鼠 =2牛 =3虎 =4兔 =5龙 =6蛇 =7马 =8羊 =9猴 =10鸡 =11狗 =12猪
	private static final Logger logger = Logger.getLogger(GameController.class);

	@Resource
	private IGameService gameService;
	@Resource
	private IUserService userService;
	@Resource
	private IConfigService configService;
	@Resource
	private IUserItemService userItemService;
	@Resource
	private ISmashedEggGiftConfigService smashedEggGiftConfigService;
	@Resource
	private IGameRecordService gameRecordService;
	@Resource
	private IUserTransactionHisService  userTransactionHisService;
	@Resource
	private IUserVipInfoService userVipInfoService;
	@Resource
	private IMallService mallService;
	@Resource
	private IUserCarInfoService userCarInfoService;
	
	private final static String SALT = "tinypig1&-vM*y;Y"; // 修改跟游戏同步
	private static final Charset utf8 = Charset.forName("utf8");
	private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	protected static String encodeHex(byte[] data) {
        int l = data.length;
        char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = DIGITS_LOWER[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS_LOWER[0x0F & data[i]];
        }
        return new String(out);
    }
	
	public static String signParams(String salt, String... params) {
		StringBuffer whole = new StringBuffer();
		for (int i = 0; i < params.length; i++) {
			whole.append(params[i]);
			if (i < params.length - 1) {
				whole.append('&');
			}
		}
		whole.append(salt);
		MessageDigest digest = com.mpig.api.utils.LocalObject.md5.get();
		return encodeHex((digest.digest(whole.toString().getBytes(
				utf8))));
	}

	public static boolean checkSign(String salt, String sign, String ...params) {
		return sign.equals(signParams(salt, params));
	}
	
	/**
	 * 下注记录
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/bet/deductBetPrice")
	@ResponseBody
	public Map<String, Object> deductBetPrice(HttpServletRequest request, HttpServletResponse response) {
		int code = 0;
		if (ParamHandleUtils.isBlank(request, "sign","orderid","nid","type","rid","money","uid")) {
			code = CodeContant.ConParamIsEmptyOrNull;
		}
		
		String queryString = request.getQueryString();
		String[] queryStringSplit = queryString.split("&");
		List<String> asList = new ArrayList<String>(queryStringSplit.length - 1);
		for (String arg : queryStringSplit) {
			if(!arg.startsWith("sign")){
				asList.add(arg);
			}
		}
		queryStringSplit = asList.toArray(new String[0]);
		Arrays.sort(queryStringSplit);
		String sign = request.getParameter("sign");
		if(!checkSign(SALT, sign, queryStringSplit)){
			code = CodeContant.GameSignatureFail;
		}
		
		Map<String, Object> msghead = new HashMap<String,Object>();
		msghead.put("msgid", 103);
		msghead.put("serverid", 0);
		msghead.put("transmode", 0);
		
		Map<String, Object> msgbody = new HashMap<String,Object>();
		msgbody.put("code", code);
		
		Map<String, Object> _map = new HashMap<String,Object>();
		if(code == 0){
			int uid = Integer.valueOf(request.getParameter("uid"));
			int rid = Integer.valueOf(request.getParameter("rid"));
			String orderid = request.getParameter("orderid");
			int type = Integer.valueOf(request.getParameter("type"));
			int nid = Integer.valueOf(request.getParameter("nid"));
			int money = Integer.valueOf(request.getParameter("money"));
			int contextid = request.getParameter("contextid") == null?0:Integer.valueOf(request.getParameter("contextid"));
			
			msghead.put("rid", rid);
			msghead.put("uid", uid);

			gameService.addBets(orderid, rid, uid, money, nid, type, returnModel);
			msgbody.put("code", returnModel.getCode());
			
			msgbody.put("nid", nid);
			msgbody.put("type", type);
			msgbody.put("money", money);
			msgbody.put("curMoney", (Integer)returnModel.getData());
			msgbody.put("contextid", contextid);
		}
		
		_map.put("msghead", msghead);
		_map.put("msgbody", msgbody);
		return _map;
	}

	/**
	 * 开奖
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/bet/getBetPrice")
	@ResponseBody
	public Map<String, Object> getBetPrice(HttpServletRequest request, HttpServletResponse response) {
		int code = 200;
		if (ParamHandleUtils.isBlank(request, "sign","orderid","innerid","outerid","rid")) {
			code = CodeContant.ConParamIsEmptyOrNull;
		}

		String queryString = request.getQueryString();
		String[] queryStringSplit = queryString.split("&");
		List<String> asList = new ArrayList<String>(queryStringSplit.length - 1);
		for (String arg : queryStringSplit) {
			if(!arg.startsWith("sign")){
				asList.add(arg);
			}
		}
		queryStringSplit = asList.toArray(new String[0]);
		Arrays.sort(queryStringSplit);
		String sign = request.getParameter("sign");
		if(!checkSign(SALT, sign, queryStringSplit)){
			code = CodeContant.GameSignatureFail;
		}
		
		String order_id = request.getParameter("orderid");
		int inner_id = Integer.valueOf(request.getParameter("innerid"));
		int outer_id = Integer.valueOf(request.getParameter("outerid"));
		int rid = Integer.valueOf(request.getParameter("rid"));
		
		Map<String, Object> msghead = new HashMap<String,Object>();
		msghead.put("msgid", 104);
		msghead.put("rid", rid);
		msghead.put("uid", 0);
		msghead.put("serverid", 0);
		msghead.put("transmode", 1);
		Map<String, Object> mapResult = new HashMap<String,Object>();
		mapResult.put("msghead", msghead);
		
		Map<String, Object> msgbody = new HashMap<String,Object>();
		mapResult.put("msgbody", msgbody);
		msgbody.put("result", 200);
		
		if (code != 200) {
			msgbody.put("result", code);
		}else if (inner_id == 0 || outer_id == 0 || rid == 0) {
			msgbody.put("result", 500);
		}else {
			List<Map<String, Object>> betResult = gameService.BetResult(order_id, rid, inner_id, outer_id);
			msgbody.put("users", betResult);
			msgbody.put("inner_id", inner_id);
			msgbody.put("inner_name", GameBetConfigLib.getGameBetConfig("inner_"+inner_id).getName());
			msgbody.put("outer_id", outer_id);
			msgbody.put("outer_name", GameBetConfigLib.getGameBetConfig("outer_"+outer_id).getName());
			msgbody.put("time", System.currentTimeMillis()/1000);
		}
		return mapResult;
	}
	
	/**
	 * 下注记录
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/bet/sendRunWay")
	@ResponseBody
	public void sendTmpRunway(HttpServletRequest request,HttpServletResponse response){
		int code = 0;
		if (ParamHandleUtils.isBlank(request, "sign","orderid")) {
			logger.error("sendTmpRunway no orderid");
		}
		String queryString = request.getQueryString();
		String[] queryStringSplit = queryString.split("&");
		List<String> asList = new ArrayList<String>(queryStringSplit.length - 1);
		for (String arg : queryStringSplit) {
			if(!arg.startsWith("sign")){
				asList.add(arg);
			}
		}
		queryStringSplit = asList.toArray(new String[0]);
		Arrays.sort(queryStringSplit);
		String sign = request.getParameter("sign");
		if(!checkSign(SALT, sign, queryStringSplit)){
			code = CodeContant.GameSignatureFail;
		}
		String order_id = request.getParameter("orderid");
		if (order_id == null || code > 0) {
			return;
		}else {
			gameService.sendRunway(order_id);
		}
	}

	/**
	 * 获取游戏列表
	 * 
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public void list(HttpServletRequest req, HttpServletResponse resp) {

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}

		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			writeJson(resp, returnModel);
			return;
		}

		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
//		if ("1".equals(req.getParameter("os"))) {
//			returnModel.setData(GameAppConfigLib.allGameApp());
//		}
		returnModel.setData(GameAppConfigLib.allGameApp());
		writeJson(resp, returnModel);
	}

	/**
	 * 获取进房的签名
	 * 
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/signature", method = RequestMethod.GET)
	public void signature(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "appId")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}

		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			writeJson(resp, returnModel);
			return;
		}

		String token = req.getParameter("token");
		final int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		
		final String appId = req.getParameter("appId");
		GameAppConfig gameAppFor = GameAppConfigLib.getGameAppFor(Integer.parseInt(appId));
		if(gameAppFor == null){
			returnModel.setCode(CodeContant.GameAppNotExist);
			returnModel.setMessage("没有此游戏");
			writeJson(resp, returnModel);
			return;
		}
		
		if(gameAppFor.requireLv > 0){
			UserBaseInfoModel userBaseInfoModel = UserServiceImpl.getInstance().getUserbaseInfoByUid(uid, false);
			if(userBaseInfoModel.getUserLevel() < gameAppFor.requireLv){
				returnModel.setCode(CodeContant.GameRequireFail);
				returnModel.setMessage("游戏进入条件不足,继续加油");
				writeJson(resp, returnModel);
				return;
			}
		}
		
		returnModel.setData(new HashMap<Object, Object>() {
			private static final long serialVersionUID = -5183139375153395276L;
			{
				put("signature", Encrypt.encode(String.valueOf(uid) + ":" + System.currentTimeMillis() + ":" + appId));
			}
		});
		writeJson(resp, returnModel);
	}
	
	/**
	 * 获取进房的签名
	 * 
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/bet/getBetRecord", method = RequestMethod.GET)
	public void getBetRecord(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "appId")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}

		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			writeJson(resp, returnModel);
			return;
		}

		String token = req.getParameter("token");
		final int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		
		final String appId = req.getParameter("appId");
		boolean presentThisGame = GameAppConfigLib.containGameAppFor(Integer.parseInt(appId));
		if(!presentThisGame){
			returnModel.setCode(CodeContant.GameAppNotExist);
			returnModel.setMessage("没有此游戏");
			writeJson(resp, returnModel);
			return;
		}
		returnModel.setData(BetRecordsCache.instatnce.get().getLatestRecords());
		writeJson(resp, returnModel);
	}
	
	/**
	 * 获取砸蛋游戏的头奖
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(value = "/gettopprize")
	@ResponseBody
	public ReturnModel gettopprize(HttpServletRequest req, HttpServletResponse resp){
		/**
		Map<String,Object> amap = new TreeMap<String, Object>();
		Map<String,String> bmap = new HashMap<String, String>();
		bmap.put("giftname", "跑车");
		bmap.put("giftcount", "1");
		amap.put("1", bmap);
		bmap = new HashMap<String, String>();
		bmap.put("giftname", "兰博基尼");
		bmap.put("giftcount", "1");
		amap.put("2", bmap);
		bmap = new HashMap<String, String>();
		bmap.put("giftname", "飞机");
		bmap.put("giftcount", "3");
		amap.put("3", bmap);
		**/
		List<Map<String, Object>> result = smashedEggGiftConfigService.getSmashedEggFirstPrize();
		returnModel.setData(result);
		return returnModel;
	}
	
	/**
	 * 砸蛋游戏
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(value = "/smashedEgg")
	@ResponseBody
	public ReturnModel smashedEgg(Integer hammer, Integer count, Integer roomId, HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "hammer", "count", "roomId")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		if(count <=0){
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}

		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			return returnModel;
		}

		String token = req.getParameter("token");
		final int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			return returnModel;
		}
		
		String pricestr = OtherRedisService.getInstance().hget(RedisContant.SmashedEggMoneyCfgList, ""+hammer);
		int price = (StringUtils.isNotBlank(pricestr)? Integer.valueOf(pricestr) : 10);
		
		int allPrice = price * count;
		
		UserAssetModel sendUserAssetModel = userService.getUserAssetByUid(uid, false);
		if (sendUserAssetModel == null) {
			returnModel.setCode(CodeContant.USERASSETEXITS);
			returnModel.setMessage("用户不存在");
			return returnModel;
		}
		if (allPrice > sendUserAssetModel.getMoney()) {
			returnModel.setCode(CodeContant.MONEYLESS);
			returnModel.setMessage("金额不足");
			return returnModel;
		}
		
		Map<Integer, Integer> luckyGiftMap = new HashMap<Integer, Integer>();
		//获取当前锤子的奖品列表
		String smash = OtherRedisService.getInstance().hget(RedisContant.SmashedEggGiftCfgList, ""+hammer);//礼物列表
		if (StringUtils.isBlank(smash)) {
			returnModel.setCode(CodeContant.gameEggNoGiftCfg);
			returnModel.setMessage("砸蛋失败，没有奖品");
			return returnModel;
		}
		
		int res = userService.updUserAssetBySendUid(uid, allPrice, 0);
		if (res == 0) {
			returnModel.setCode(CodeContant.MONEYDEDUCT);
			returnModel.setMessage("扣费失败");
			return returnModel;
		}
		
		List<SmashedEggGiftConfigModel> smashs = JsonUtil.toListBean(smash, SmashedEggGiftConfigModel.class);
		
		//砸10次 总中奖礼物
		long giftTotalPriceAll = 0;
		
		//Map<Integer, RechargeLotteryConfig> rechargemap = ActivityConfigLib.getGoldEggLotteryConfig().get(hammer);
		for (int i = 0; i < count; i++) {
			//获取砸金蛋的概率
			//int prize = MathRandomUtil.goldEggRandom();
			
			double randomNumber = Math.random();
			boolean flag = false;
			SmashedEggGiftConfigModel selectSmashs = null;
			for (int j = 0; j < smashs.size(); j++) {
				int k = 0;
				double startNumber = 0;
				while(k <= j) {
					SmashedEggGiftConfigModel sm = smashs.get(k++);
					if (randomNumber >= startNumber && randomNumber <= (startNumber+sm.getProbability())) {
						flag = true;
						break;
					}
					startNumber += sm.getProbability();
				}
				if(flag) {
					selectSmashs = smashs.get(j);
					break;
				}
			}
			
			//根据概率获取奖品
			//RechargeLotteryConfig lotteryConfig = rechargemap.get(prize);
			
			if(selectSmashs == null) {
				continue;
			}
			Integer gid = selectSmashs.getGiftId().intValue();
			Integer num = selectSmashs.getGiftNum();
			Integer gtype = selectSmashs.getGiftType();
			
			ConfigGiftModel giftConfigModel = configService.getGiftInfoByGidNew(gid.intValue());
			int giftTotalPrice =  giftConfigModel.getGprice() * num;
			userItemService.insertUserItem(uid, giftConfigModel.getGid(), num, ItemSource.smashedEgg);
			gameService.addSmashedEggLog(uid, price, 1, roomId, giftConfigModel.getGid(), giftConfigModel.getGname(), giftConfigModel.getGprice(), num, giftTotalPrice,gtype);
			
			Long time = System.currentTimeMillis();
			gameRecordService.addGameRecord(Long.valueOf(uid), Long.valueOf(roomId), 1, Long.valueOf(price),
					Long.valueOf(price) - Long.valueOf(giftTotalPrice),  time/ 1000);//盈亏情况针对平台
			
			giftTotalPriceAll = giftTotalPriceAll + giftTotalPrice;
			
			if(luckyGiftMap.get(gid) == null){
				luckyGiftMap.put(gid, num);
			}else{
				luckyGiftMap.put(gid, num+luckyGiftMap.get(gid));
			}
		}
		
		Long time = System.currentTimeMillis();
		userTransactionHisService.saveUserTransactionHis(7, uid, 0.00, Long.valueOf(allPrice), time, "", 1);
		userTransactionHisService.saveUserTransactionHis(8, uid, 0.00, giftTotalPriceAll, time, "", 1);
		
		List<Map<String, Object>> giftList = new ArrayList<Map<String, Object>>();
		if(luckyGiftMap.size() > 0) {//如果中奖了就推送中奖信息
			UserBaseInfoModel dstUserBaseinfo = userService.getUserbaseInfoByUid(uid, false);
			UserBaseInfoModel anchorBaseinfo = userService.getUserbaseInfoByUid(roomId, false);
	
			//增加跑道
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			HashMap<String, Object> map = new HashMap<String, Object>();
			HashMap<String, Object> toUserMap = new HashMap<String, Object>();
			List<Map<String, Object>> toUserList = new ArrayList<Map<String, Object>>();
			
			map.put("name", "喜讯：");
			map.put("color", "#ff2d55");
			list.add(map);
			toUserList.add(map);
	
			map = new HashMap<String, Object>();
			map.put("name", " 恭喜 ");
			map.put("color", "");
			list.add(map);
			toUserList.add(map);
	
			map = new HashMap<String, Object>();
			map.put("name", dstUserBaseinfo.getNickname());
			map.put("color", "#fff08c");
			list.add(map);
			
			toUserMap.put("name", "您");
			toUserMap.put("color", "");
			toUserList.add(toUserMap);
			
			map = new HashMap<String, Object>();
			map.put("name", "在 ");
			map.put("color", "");
			list.add(map);
			toUserList.add(map);
			
			map = new HashMap<String, Object>();
			map.put("name", "砸金蛋");
			map.put("color", "#ffadeb");
			list.add(map);
			toUserList.add(map);
			
			map = new HashMap<String, Object>();
			map.put("name", " 中获得");
			map.put("color", "");
			list.add(map);
			toUserList.add(map);
			
			Iterator<Entry<Integer, Integer>> entries = luckyGiftMap.entrySet().iterator();
			Integer giftPriceTotal = 0;
			while (entries.hasNext()){
				Entry<Integer, Integer> entry = entries.next();
				int gid = entry.getKey();
				int num = entry.getValue();
				ConfigGiftModel giftConfigModel = configService.getGiftInfoByGidNew(gid);
				
				//给用户增加道具、VIP属性
				if(giftConfigModel.getType() == 3) {//时效道具
					if(giftConfigModel.getSubtype() == 3) {//VIP
						this.addVipToUser(sendUserAssetModel,giftConfigModel);
					}else if(giftConfigModel.getSubtype() == 5) {//座驾
						this.addCarToUser(sendUserAssetModel,giftConfigModel);
					}
				}
				
				//构造中奖消息
				
				HashMap<String, Object> giftMap = new HashMap<String, Object>();
				giftMap.put("gid", gid);
				giftMap.put("gname", giftConfigModel.getGname());
				giftMap.put("num", num);
				giftPriceTotal = giftConfigModel.getGprice()*num +giftPriceTotal;
				giftList.add(giftMap);
				
				map = new HashMap<String, Object>();
				map.put("gid", gid);
				map.put("color", "");
				list.add(map);
				toUserList.add(map);
				
				map = new HashMap<String, Object>();
				map.put("name", " * ");
				map.put("color", "");
				list.add(map);
				toUserList.add(map);
				
				map = new HashMap<String, Object>();
				map.put("name", num);
				map.put("color", "#fff08c");
				list.add(map);
				toUserList.add(map);
				if(entries.hasNext()){
					map = new HashMap<String, Object>();
					map.put("name", "、");
					map.put("color", "");
					list.add(map);
					toUserList.add(map);
				}
			}
			
			//if(giftPriceTotal >= 10000000) {//审核版本
				//左侧聊天区
				ChatMessageUtil.roomLuckyNotice(roomId, list,0,null,uid,dstUserBaseinfo.getNickname(),null,null,null);
			//}
			
			if(giftPriceTotal >= 18000){
			//if(giftPriceTotal >= 10000000){//审核版本
				RunWayCMod msgBody = new RunWayCMod();
				map = new HashMap<String, Object>();
				map.put("list", list);
				msgBody.setData(map);
				msgBody.setAnchorUid(anchorBaseinfo.getUid());
				msgBody.setAnchorName(anchorBaseinfo.getNickname());
				logger.info("跑道的发送内容 : "+map);
				String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
						"appKey=" + VarConfigUtils.ServiceKey,
						"msgBody=" + JSONObject.toJSONString(msgBody));
				//顶部跑道
				Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_room_all()).field("appKey", VarConfigUtils.ServiceKey).field("msgBody", JSONObject.toJSONString(msgBody)).field("sign", signParams).asJsonAsync();
			}
			toUserMap = new HashMap<String, Object>();
			toUserMap.put("list", toUserList);
			ChatMessageUtil.sendGiftUpdateAssetAndBag(uid,null, null, toUserMap, giftList,null,null,null);
		}
		UserAssetModel userAssetModel = userService.getUserAssetByUid(uid, false);
		Map<String, Object> datamap = new HashMap<String, Object>();
		datamap.put("surplus", userAssetModel.getMoney());
		datamap.put("giftList", giftList);
		returnModel.setData(datamap);
		return returnModel;
	}


	/**
	 * 给某个用户增加VIP，砸金蛋砸出来的
	 */
	private void addVipToUser(UserAssetModel sendUserAssetModel, ConfigGiftModel giftConfigModel) {
		int baijinVipId = 43;
		int zuanshiVipId = 44;
		int uid = sendUserAssetModel.getUid().intValue();
		int gid = giftConfigModel.getGid();
		
		if(giftConfigModel.getGid() != zuanshiVipId) {//只能是钻石VIP
			logger.error(String.format("%s砸金蛋砸中VIP，运营配置有问题，只能是钻石VIP！",uid,gid));
			return;
		}
		
		int count = 1;
		Long nowtime = System.currentTimeMillis()/1000;
		Date date = new Date();
		Date startdate = new Date();
		int price = giftConfigModel.getGprice(); //单价
		int realprice = price; //实际支付单价
		int pricetotal = giftConfigModel.getGprice()*count; //总价
		int realpricetotal = pricetotal; //实际支付总价格(实际需要支付)
		String gname = giftConfigModel.getGname();
		int wealths = giftConfigModel.getWealth() * count;
		
		
		UserVipInfoModel userVipInfoModel = ValueaddServiceUtil.getVipInfo(uid);
		if(userVipInfoModel != null && nowtime.intValue() <= userVipInfoModel.getEndtime().intValue()) {//当前已经开通了VIP
			if(userVipInfoModel.getGid().intValue() == baijinVipId) {//开通了白金VIP，替换掉
				userVipInfoService.updUserVipInfo(uid, userVipInfoModel.getStarttime(), userVipInfoModel.getEndtime(), 1, userVipInfoModel.getGid());
			}else if(userVipInfoModel.getGid().intValue() == zuanshiVipId) {//开通了钻石VIP，延长之
				long endtime = userVipInfoModel.getEndtime();
				long starttime = userVipInfoModel.getStarttime();
				ValueaddPrivilegeModel privilegeModel = ValueaddServiceUtil.getPrivilege(userVipInfoModel.getGid(),1);
				double discount = privilegeModel.getRenewalDiscount();
				double discountprice = realprice*discount;
				realprice = (int)discountprice;
				double discountrealpricetotal = realpricetotal*discount;
				realpricetotal = (int)discountrealpricetotal;
				date = new Date(endtime*1000);
				startdate = new Date(starttime*1000);
			}
		}else {//当前没开通VIP，暂不处理，service里新增
		}
		realprice = GiftPromotionConfigLib.getDisPrice(gid, realprice);//实际支付单价
		realpricetotal = GiftPromotionConfigLib.getDisPrice(gid, realpricetotal);//实际支付总价
		
		realprice = 0;//强制设置砸金蛋单价，不要钱!!!
		realpricetotal = 0;//强制设置砸金蛋总价，不要钱!!!
		
		Date newEndtime = DateUtils.getNMonthAfterDate(date, count);//开通时长1个月
		ReturnModel rm = new ReturnModel();
		mallService.buyVip(gid, gname, uid, uid, new Long(startdate.getTime()/1000).intValue(), new Long(newEndtime.getTime()/1000).intValue(), count, wealths, price, pricetotal, realprice, realpricetotal, rm);
		//billcvgDao.insBillcvgList(uid, uid, gid, gname, count, new Long(new Date().getTime()/1000).intValue(), new Long(startdate.getTime()/1000).intValue(), new Long(newEndtime.getTime()/1000).intValue(), 2);
		logger.info("砸金蛋砸中钻石VIP结果："+rm.toString());
	}

	/**
	 * 给某个用户增加座驾，砸金蛋砸出来的，给他啊
	 */
	private void addCarToUser(UserAssetModel sendUserAssetModel, ConfigGiftModel giftConfigModel) {
		int uid = sendUserAssetModel.getUid().intValue();
		int gid = giftConfigModel.getGid();
		int count = 1;
		int price = giftConfigModel.getGprice(); //单价
		int realprice = price; //实际支付单价
		int pricetotal = giftConfigModel.getGprice()*count; //总价
		int realpricetotal = pricetotal; //实际支付总价格(实际需要支付)
		String gname = giftConfigModel.getGname();
		int wealths = giftConfigModel.getWealth() * count;
		Long nowtime = System.currentTimeMillis()/1000;

		UserCarInfoModel userCarInfo = userCarInfoService.getUserCarInfo(uid, gid, true);
		Date date = new Date();
		Date startdate = new Date();
		if(userCarInfo !=null && nowtime.intValue() <= userCarInfo.getEndtime().intValue()){
			long endtime = userCarInfo.getEndtime();
			long starttime = userCarInfo.getStarttime();
			double discountprice = realprice;
			realprice = (int)discountprice;
			date = new Date(endtime*1000);
			startdate = new Date(starttime*1000);
		}
		Date newEndtime = DateUtils.getNMonthAfterDate(date, count);//count个月
		realprice = GiftPromotionConfigLib.getDisPrice(gid, realprice);
		realpricetotal = GiftPromotionConfigLib.getDisPrice(gid, realpricetotal);
		
		realprice = 0;//实际支付单价，强制归零，砸蛋砸出来不花钱
		realpricetotal = 0;//实际支付总价，强制归零，砸蛋砸出来不花钱
		
		ReturnModel rm = new ReturnModel();
		mallService.buyCar(gid, gname, uid, uid, 1, 1, new Long(startdate.getTime()/1000).intValue(), new Long(newEndtime.getTime()/1000).intValue(), count, wealths, price, pricetotal, realprice, realpricetotal, rm);
		//billcvgDao.insBillcvgList(uid, dstuid, gid, gname, count, new Long(new Date().getTime()/1000).intValue(), new Long(startdate.getTime()/1000).intValue(), new Long(newEndtime.getTime()/1000).intValue(), 1);
		logger.info("砸金蛋砸出座驾处理结果"+rm.toString());
	}
	
	
}
