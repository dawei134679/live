package com.mpig.api.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mpig.api.model.ReturnModel;
import com.mpig.api.service.IActivityService;
import com.mpig.api.service.IConfigService;
import com.mpig.api.service.IOrderService;
import com.mpig.api.service.IUserItemService;
import com.mpig.api.service.IUserService;
import com.mpig.api.service.IUserVipInfoService;
import com.mpig.api.utils.CodeContant;

@Controller
@Scope("prototype")
@RequestMapping("/activity")
public class ActivityController extends BaseController {

	@Resource
	private IUserService userService;
	
	@Resource
	private IOrderService orderService;
	
	@Resource
	private IActivityService activityService;
	
	@Resource
	private IUserItemService userItemService;
	
	@Resource
	private IUserVipInfoService vipInfoService;
	
	@Resource IConfigService configService;
	

	private ReturnModel returnFail() {
		returnModel.setCode(CodeContant.CONPARAMSEXCEPT);
		returnModel.setMessage("该功能已关闭");
		return returnModel;
	}
	/**
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(value = "/swithenvelope", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel getEnvelopSwith(HttpServletRequest req, HttpServletResponse resp) {
		return returnFail();
		
		/*if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}

		// 验证auth及token
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			return returnModel;
		}
		Boolean get520Swith = OtherRedisService.getInstance().getEnvelopeSwith();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("swith", get520Swith);
		returnModel.setData(map);
		return returnModel;*/
	}


	@RequestMapping(value = "/swith520", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel get520Swith(HttpServletRequest req, HttpServletResponse resp) {
		return returnFail();
		/*
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}

		// 验证auth及token
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			return returnModel;
		}

		Map<String, Object> map = new HashMap<String, Object>();
		Boolean get520Swith = OtherRedisService.getInstance().get520Swith();
		map.put("swith", get520Swith);
		map.put("url", "http://www.xiaozhutv.com/activity/520/520.html");
		returnModel.setData(map);
		return returnModel;*/
	}

	/**
	 * 获取520 活动的 主播和用户的排行榜
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(value = "/rank520", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel getRank520(HttpServletRequest req, HttpServletResponse resp) {
		return returnFail();
		/*
		Set<Tuple> rankAnchor520 = UserRedisService.getInstance().getRankAnchor520(5);
		Set<Tuple> rankUser520 = UserRedisService.getInstance().getRankUser520(5);

		List<Map<String, Object>> anchorList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> userList = new ArrayList<Map<String, Object>>();
		Map<String, Object> result = new HashMap<String, Object>();

		for (Tuple tuple : rankAnchor520) {
			Map<String, Object> map = new HashMap<String, Object>();
			UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(Integer.valueOf(tuple.getElement()),
					false);
			if (userBaseInfoModel != null) {
				map.put("uid", tuple.getElement());
				map.put("headimage", userBaseInfoModel.getHeadimage());
				map.put("nickname", userBaseInfoModel.getNickname());
				map.put("score", tuple.getScore());
				anchorList.add(map);
			}
		}
		for (Tuple tuple : rankUser520) {
			Map<String, Object> map = new HashMap<String, Object>();
			UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(Integer.valueOf(tuple.getElement()),
					false);
			if (userBaseInfoModel != null) {
				map.put("uid", tuple.getElement());
				map.put("headimage", userBaseInfoModel.getHeadimage());
				map.put("nickname", userBaseInfoModel.getNickname());
				map.put("score", tuple.getScore());
				userList.add(map);
			}
		}
		result.put("anchor", anchorList);
		result.put("user", userList);
		returnModel.setData(result);
		return returnModel;*/
	}

	/**
	 * 获取活动
	 * 	  
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(value = "/switchact", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel getActivity(HttpServletRequest req,HttpServletResponse resp){
		return returnFail();
		/*Map<String, Object> map = new HashMap<String,Object>();
		if (System.currentTimeMillis()/1000 > 1465660800) {
			// 端午 2016-06-12 0：00：00结束
			map.put("activity", new JSONObject());
			returnModel.setData(map);
			return returnModel;
		}
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}

		// 验证auth及token
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			return returnModel;
		}

		Boolean bl = OtherRedisService.getInstance().getActivitySwith();
		map.put("on", bl);
		map.put("pic_bannner", "http://7xsddj.com1.z0.glb.clouddn.com/activity/pic_banner.png");
		map.put("pic_btn", "http://7xsddj.com1.z0.glb.clouddn.com/activity/pic_bnt.png");
		map.put("pic_main", "http://7xsddj.com1.z0.glb.clouddn.com/activity/pic_main.png");
		map.put("act_url", "http://www.xiaozhutv.com/duanwu/");
		map.put("actName", "duanwu");
		map.put("expireat", 1465660800);
		map.put("position", 1);//支付页添加图片
		map.put("pic_ext","http://7xsddj.com1.z0.glb.clouddn.com/activity/pic_pay.png");
		
		Map<String, Object> _map = new HashMap<String,Object>();
		_map.put("activity", map);
		
		returnModel.setData(_map);
		return returnModel;*/
	}
	
	
    /**
     * 获取初选赛排行榜
     * @param req
     * @param resp
     * @return
     */
    @RequestMapping(value="/priAudition2016", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public ReturnModel getRankAnchorPriAudition2016(HttpServletRequest req, HttpServletResponse resp){
    	return returnFail();
    	/*Long lg = System.currentTimeMillis() / 1000;
    	if(lg<1467388800){//初选赛开始前直接返回空
    		return returnModel;
    	}
    	//获得初选赛的排名结果
		Set<Tuple> priAudition = UserRedisService.getInstance().getRank(RedisContant.RankAnchorPriAudition2016, 0, 19);
		List<Map<String, Object>> anchorList = new ArrayList<Map<String, Object>>();
		Map<String, Object> result = new HashMap<String, Object>();
		
		for (Tuple tuple : priAudition) {
			Map<String, Object> map = new HashMap<String, Object>();
			//获取用户缓存信息
			UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(Integer.valueOf(tuple.getElement()),
					false);
			if (userBaseInfoModel != null) {
				map.put("uid", tuple.getElement());
				map.put("headimage", userBaseInfoModel.getLivimage());
				map.put("nickname", userBaseInfoModel.getNickname());
				map.put("ticketcount", (int)tuple.getScore());
				map.put("liveStatus", userBaseInfoModel.getLiveStatus());
				anchorList.add(map);
			}
		}
		result.put("anchor", anchorList);
		returnModel.setData(result);
    	return returnModel;*/
    }
    
    /**
     * 获取决赛排行榜
     * @param req
     * @param resp
     * @return
     */
    @RequestMapping(value="/finalAudition2016", method = RequestMethod.GET)
    @ResponseBody
    public ReturnModel getRankAnchorFinalAudition2016(HttpServletRequest req, HttpServletResponse resp){
    	return returnFail();
    	/*
    	Long lg = System.currentTimeMillis() / 1000;
    	Set<Tuple> finalAudition = new HashSet<Tuple>();
    	//决赛开始前直接返回空
    	if(lg<1468252800){
    		return returnModel;
    	}else{
    		finalAudition = UserRedisService.getInstance().getRank(RedisContant.RankAnchorFinalAudition2016, 0, 9); //查找排名前十的结果
    		if(finalAudition.size()==0){ //如果没有数据 则从初赛里拿出排名前十的结果赋值
    			Set<Tuple> priAuditions = UserRedisService.getInstance().getRank(RedisContant.RankAnchorPriAudition2016, 0, 9);
    			for(Tuple tuple : priAuditions){
    				UserRedisService.getInstance().setRank(RedisContant.RankAnchorFinalAudition2016, tuple.getElement(), (double)0);
    			}
    			finalAudition = UserRedisService.getInstance().getRank(RedisContant.RankAnchorFinalAudition2016, 0, 9);
    		}
    	}
		List<Map<String, Object>> anchorList = new ArrayList<Map<String, Object>>();
		Map<String, Object> result = new HashMap<String, Object>();
		for (Tuple tuple : finalAudition) {
			Map<String, Object> map = new HashMap<String, Object>();
			//查询用户缓存信息
			UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(Integer.valueOf(tuple.getElement()),
					false);
			if (userBaseInfoModel != null) {
				map.put("uid", tuple.getElement());
				map.put("headimage", userBaseInfoModel.getLivimage());
				map.put("nickname", userBaseInfoModel.getNickname());
				map.put("score", tuple.getScore());
				map.put("liveStatus", userBaseInfoModel.getLiveStatus());
				anchorList.add(map);
			}
		}
		result.put("anchor", anchorList);
		returnModel.setData(result);
    	return returnModel;
    */}
    
    /**
     * 获取牛郎排行榜
     * @param req
     * @param resp
     * @return
     */
    @RequestMapping(value="/qx/niulangTop", method = RequestMethod.GET)
    @ResponseBody
	public ReturnModel getRankNiulangTop(HttpServletRequest req,HttpServletResponse resp) {
    	return returnFail();
    	/*Long lg = System.currentTimeMillis() / 1000;
		if (lg < 1470412800) {// 活动开始前直接返回空
			return returnModel;
		}
		// 获得初选赛的排名结果
		String niuTopKey = "niu";
		Set<Tuple> niulangTop = UserRedisService.getInstance().getRank(RedisContant.RankActivityTop+niuTopKey, 0, 9);
		List<Map<String, Object>> anchorList = new ArrayList<Map<String, Object>>();
		Map<String, Object> result = new HashMap<String, Object>();
		for (Tuple tuple : niulangTop) {
			Map<String, Object> map = new HashMap<String, Object>();
			// 获取用户缓存信息
			UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(Integer.valueOf(tuple.getElement()), false);
			if (userBaseInfoModel != null) {
				map.put("uid", tuple.getElement());
				map.put("headimage", userBaseInfoModel.getLivimage());
				map.put("nickname", userBaseInfoModel.getNickname());
				map.put("ticketcount", (int) tuple.getScore());
				map.put("liveStatus", userBaseInfoModel.getLiveStatus());
				anchorList.add(map);
			}
		}
		result.put("anchor", anchorList);
		returnModel.setData(result);
		return returnModel;*/
	}
    
    /**
     * 获取织女排行榜
     * @param req
     * @param resp
     * @return
     */
    @RequestMapping(value="/qx/zhinvTop", method = RequestMethod.GET)
    @ResponseBody
	public ReturnModel getRankZhinvTop(HttpServletRequest req,HttpServletResponse resp) {
    	return returnFail();
    	/*Long lg = System.currentTimeMillis() / 1000;
		if (lg < 1470412800) {// 活动开始前直接返回空
			return returnModel;
		}
		// 获得初选赛的排名结果
		String zhinvTopKey = "zhinv";
		Set<Tuple> niulangTop = UserRedisService.getInstance().getRank(RedisContant.RankActivityTop+zhinvTopKey, 0, 9);
		List<Map<String, Object>> anchorList = new ArrayList<Map<String, Object>>();
		Map<String, Object> result = new HashMap<String, Object>();
		for (Tuple tuple : niulangTop) {
			Map<String, Object> map = new HashMap<String, Object>();
			// 获取用户缓存信息
			UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(Integer.valueOf(tuple.getElement()), false);
			if (userBaseInfoModel != null) {
				map.put("uid", tuple.getElement());
				map.put("headimage", userBaseInfoModel.getLivimage());
				map.put("nickname", userBaseInfoModel.getNickname());
				map.put("ticketcount", (int) tuple.getScore());
				map.put("liveStatus", userBaseInfoModel.getLiveStatus());
				anchorList.add(map);
			}
		}
		result.put("anchor", anchorList);
		returnModel.setData(result);
		return returnModel;*/
	}
    
    /**
     * 获取牛郎织女排行榜
     * @param req
     * @param resp
     * @return
     */
    @RequestMapping(value="/qx/nzTop", method = RequestMethod.GET)
    @ResponseBody
	public ReturnModel getRankNZTop(HttpServletRequest req,HttpServletResponse resp) {
    	return returnFail();
    	/*Long lg = System.currentTimeMillis() / 1000;
		if (lg < 1470412800) {// 活动开始前直接返回空
			return returnModel;
		}
		String niuZTopKey = "nz";
		Set<Tuple> niulangTop = UserRedisService.getInstance().getRank(RedisContant.RankActivityTop+niuZTopKey, 0, 9);
		List<Map<String, Object>> anchorList = new ArrayList<Map<String, Object>>();
		Map<String, Object> result = new HashMap<String, Object>();
		for (Tuple tuple : niulangTop) {
			Map<String, Object> map = new HashMap<String, Object>();
			// 获取用户缓存信息
			String elument = tuple.getElement();
			String[] eluments = elument.split(":");
			String niulangId = eluments[0];
			String zhinvId = eluments[1];
			UserBaseInfoModel niulangBaseInfoModel = userService.getUserbaseInfoByUid(Integer.parseInt(niulangId), false);
			UserBaseInfoModel zhinvBaseInfoModel = userService.getUserbaseInfoByUid(Integer.parseInt(zhinvId), false);
			if (niulangBaseInfoModel != null && zhinvBaseInfoModel!=null) {
				map.put("niuUid", niulangBaseInfoModel.getUid());
				map.put("niuHeadimage", niulangBaseInfoModel.getLivimage());
				map.put("niuNickname", niulangBaseInfoModel.getNickname());
				map.put("niuLiveStatus", niulangBaseInfoModel.getLiveStatus());
				
				map.put("zhinvUid", zhinvBaseInfoModel.getUid());
				map.put("zhinvHeadimage", zhinvBaseInfoModel.getLivimage());
				map.put("zhinvNickname", zhinvBaseInfoModel.getNickname());
				map.put("zhinvLiveStatus", zhinvBaseInfoModel.getLiveStatus());
				
				map.put("ticketcount", (int) tuple.getScore());
				anchorList.add(map);
			}
		}
		result.put("anchor", anchorList);
		returnModel.setData(result);
		return returnModel;*/
	}

    /**
     * 国庆前的活动，充值返现 自己领取功能
     * @param req
     * @param resp
     * @return
     */
    @RequestMapping(value="/national", method = RequestMethod.GET)
    @ResponseBody
    public ReturnModel ReceiveMoney(HttpServletRequest req,HttpServletResponse resp){
    	return returnFail();
    	/*
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime","uid","type")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime","uid","type")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return returnModel;
		}
		
		int uid = Integer.valueOf(req.getParameter("uid"));
		
		Set<String> noInclude = RedisCommService.getInstance().zrevrange(RedisContant.RedisNameUser, RedisContant.noIncludePay, 0, -1);
		if (noInclude != null && noInclude.contains(uid)) {
			
			returnModel.setMessage("您不参加这个活动");
		}else {
			
			int type = Integer.valueOf(req.getParameter("type"));
			orderService.ReceiveMoney(uid, type,returnModel);
		}
		
    	return returnModel;
    	*/
    }
    
    /**
     * 马拉松活动
     * @param req
     * @param resp
     * @return
     */
    @RequestMapping(value="/marathon", method = RequestMethod.GET)
    @ResponseBody
    public ReturnModel getMarathon(HttpServletRequest req,HttpServletResponse resp){
    	return returnFail();
		/*if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return returnModel;
		}
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Map<String, Object> map = null;
		
		// 10048888 10138352 10000094
		UserBaseInfoModel userbase = userService.getUserbaseInfoByUid(10048888, false);
		if (userbase != null) {
			map = new HashMap<String,Object>();
			map.put("uid", userbase.getUid());
			map.put("headimage", userbase.getHeadimage());
			map.put("nickname", userbase.getNickname());
			map.put("anchorLevel", userbase.getAnchorLevel());
			map.put("liveStatus", userbase.getLiveStatus());
			list.add(map);
		}
		userbase = userService.getUserbaseInfoByUid(10138352, false);
		if (userbase != null) {
			map = new HashMap<String,Object>();
			map.put("uid", userbase.getUid());
			map.put("headimage", userbase.getHeadimage());
			map.put("nickname", userbase.getNickname());
			map.put("anchorLevel", userbase.getAnchorLevel());
			map.put("liveStatus", userbase.getLiveStatus());
			list.add(map);
		}
		userbase = userService.getUserbaseInfoByUid(10000094, false);
		if (userbase != null) {
			map = new HashMap<String,Object>();
			map.put("uid", userbase.getUid());
			map.put("headimage", userbase.getHeadimage());
			map.put("nickname", userbase.getNickname());
			map.put("anchorLevel", userbase.getAnchorLevel());
			map.put("liveStatus", userbase.getLiveStatus());
			list.add(map);
		}
		
		map = new HashMap<String,Object>();
		map.put("list", list);
		returnModel.setData(map);
		return returnModel;*/
    }
    
    /**
     * 充值抽奖活动
     * @param req
     * @param resp
     * @return
     */
    @RequestMapping(value="/rechargeLottery")
    @ResponseBody
    public ReturnModel rechargeLottery(HttpServletRequest req,HttpServletResponse resp){
    	return returnFail();
    	/*String token = req.getParameter("token");
    	if(StringUtils.isEmpty(token)){
    		returnModel.setCode(CodeContant.CONAUTHTOKEN);
			returnModel.setMessage("请先登录！");
			return returnModel;
    	}
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			returnModel.setCode(CodeContant.CONAUTHTOKEN);
			returnModel.setMessage("请先登录！");
			return returnModel;
		}
		int count = activityService.getLotteryCount(uid);
		if(count < 0){
			returnModel.setCode(CodeContant.lotteryCountDeduct);
			returnModel.setMessage("抽奖次数不够！");
			return returnModel;
		}
		ConcurrentHashMap<Integer, RechargeLotteryConfig> rechargeLotteryMap = ActivityConfigLib.getRechargeLotteryConfig();
		int prize = MathRandomUtil.PercentageRandom();
		RechargeLotteryConfig rechargeLotteryConfig = rechargeLotteryMap.get(prize);
		String lotteryMsg = "";
		if(rechargeLotteryConfig.getType() == 0){
			userItemService.insertUserItem(uid, rechargeLotteryConfig.getGid(), rechargeLotteryConfig.getNum(), ItemSource.rechargeActivity);
			ConfigGiftModel giftConfigModel = configService.getGiftInfoByGidNew(rechargeLotteryConfig.getGid());
			lotteryMsg = "恭喜您抽到了"+rechargeLotteryConfig.getNum()+"个"+giftConfigModel.getGname();
		}
		if(rechargeLotteryConfig.getType() == 3){
			UserVipInfoModel userVipInfoModel = ValueaddServiceUtil.getVipInfo(uid);
			Date date = new Date();
			Date startdate = new Date();
			if(userVipInfoModel != null){
					long endtime = userVipInfoModel.getEndtime();
					long starttime = userVipInfoModel.getStarttime();
					date = new Date(endtime*1000);
					startdate = new Date(starttime*1000);
			}
			Date newEndtime = DateUtils.getNDaysAfterDate(date, rechargeLotteryConfig.getNum());
			//加入身份记录 更新缓存
			ConfigGiftModel giftConfigModel = configService.getGiftInfoByGidNew(rechargeLotteryConfig.getGid());
			int rsc = 0;
			if(userVipInfoModel != null){
				if(userVipInfoModel.getGid()==44){
					if(rechargeLotteryConfig.getNum()==7){
						int zhnum = 100;
						lotteryMsg = "发现您已有钻石VIP，7天 白金VIP体验 系统默认置换 "+zhnum+"个棒棒糖";
						userItemService.insertUserItemNew(uid, 13, zhnum);
						userItemService.insertUserItem(uid, rechargeLotteryConfig.getGid(), rechargeLotteryConfig.getNum(), ItemSource.rechargeActivity);
					}else if(rechargeLotteryConfig.getNum()==30){
						int zhnum = 500;
						lotteryMsg = "发现您已有钻石VIP，30天 白金VIP体验 系统默认置换 "+zhnum+"个棒棒糖";
						userItemService.insertUserItemNew(uid, 13, zhnum);
						userItemService.insertUserItem(uid, rechargeLotteryConfig.getGid(), rechargeLotteryConfig.getNum(), ItemSource.rechargeActivity);
					}
				}else{
					rsc = vipInfoService.updUserVipInfo(uid,new Long(startdate.getTime()/1000).intValue(), new Long(newEndtime.getTime()/1000).intValue(), 0,rechargeLotteryConfig.getGid());
					userItemService.insertUserItemLog(uid, rechargeLotteryConfig.getGid(), rechargeLotteryConfig.getNum(), ItemSource.rechargeActivity);
					lotteryMsg = "恭喜您抽到了"+rechargeLotteryConfig.getNum()+"天"+giftConfigModel.getGname();
				}
			}else{
				UserVipInfoModel newUserVipInfoModel = vipInfoService.getUserVipInfoByUid(uid, rechargeLotteryConfig.getGid());
				if(newUserVipInfoModel != null){
					rsc = vipInfoService.updUserVipInfo(uid,new Long(startdate.getTime()/1000).intValue(), new Long(newEndtime.getTime()/1000).intValue(), 0,rechargeLotteryConfig.getGid());
				}else{
					rsc = vipInfoService.addUserVipInfo(uid, rechargeLotteryConfig.getGid(), new Long(startdate.getTime()/1000).intValue(), new Long(newEndtime.getTime()/1000).intValue(), 0);
				}
				userItemService.insertUserItemLog(uid, rechargeLotteryConfig.getGid(), rechargeLotteryConfig.getNum(), ItemSource.rechargeActivity);
				lotteryMsg = "恭喜您抽到了"+rechargeLotteryConfig.getNum()+"天"+giftConfigModel.getGname();
			}
			vipInfoService.getUserVipInfo(uid, true);
		}
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("lotteryIndex", prize);
		dataMap.put("surplusCount", count);
		dataMap.put("lotteryMsg", lotteryMsg);
		returnModel.setData(dataMap);
		return returnModel;*/
    }

    /**
     * 获取当前用户剩余的抽奖次数
     * @param req
     * @param resp
     * @return
     */
    @RequestMapping(value="/getRechargeLotteryCount")
    @ResponseBody
    public ReturnModel getRechargeLotteryCount(HttpServletRequest req,HttpServletResponse resp){
    	return returnFail();
    	/*String token = req.getParameter("token");
    	if(StringUtils.isEmpty(token)){
    		returnModel.setCode(CodeContant.CONAUTHTOKEN);
			returnModel.setMessage("请先登录！");
			return returnModel;
    	}
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			returnModel.setCode(CodeContant.CONAUTHTOKEN);
			returnModel.setMessage("请先登录！");
			return returnModel;
		}
		int count = activityService.getSurplusLotteryCount(uid);
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("surplusCount", count);
		returnModel.setData(dataMap);
    	return returnModel;*/
    }
    
    /**
     * 获取充值抽奖的记录
     * @param req
     * @param resp
     * @return
     */
    @RequestMapping(value="/getUserRechargeLottery")
    @ResponseBody
    public ReturnModel getUserRechargeLottery(HttpServletRequest req,HttpServletResponse resp){
    	return returnFail();
		/*List<Map<String,Object>> userItemLogModels = userItemService.selItemLogBySubType(ItemSource.rechargeActivity,0, 24);
		for(Map<String,Object> itemLogModelMap : userItemLogModels){
			UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid((int)itemLogModelMap.get("uid"), false);
			int gid = (int)itemLogModelMap.get("gid");
			ConfigGiftModel giftConfigModel = configService.getGiftInfoByGidNew(gid);
			itemLogModelMap.put("gname", giftConfigModel.getGname());
			itemLogModelMap.put("nickname", userBaseInfoModel.getNickname());
		}
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("lotteryList", userItemLogModels);
		returnModel.setData(data);
		return returnModel;*/
    }
    
    /**
     * 抽奖 新年奖励
     * @param req
     * @param resp
     * @return
     */
    @RequestMapping(value="/newYear/lottery")
    @ResponseBody
    public ReturnModel newYearLottery(HttpServletRequest req,HttpServletResponse resp){
    	return returnFail();
    	/*Long lg = System.currentTimeMillis() / 1000;
		if (lg < 1485446400 || lg >= 1485878400) { // 正式环境   lg < 1485446400 || lg >= 1485878400
			returnModel.setCode(CodeContant.activityNotInTime);
			returnModel.setMessage("活动不在时间内或已过期");
			return returnModel;
		}
    	String token = req.getParameter("token");
    	if(StringUtils.isEmpty(token)){
    		returnModel.setCode(CodeContant.CONAUTHTOKEN);
			returnModel.setMessage("请先登录！");
			return returnModel;
    	}
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			returnModel.setCode(CodeContant.CONAUTHTOKEN);
			returnModel.setMessage("请先登录！");
			return returnModel;
		}
		returnModel = activityService.newYearLottery(uid, returnModel);
		return returnModel;*/
    }
    
    /**
     * 获取当前用户剩余的抽奖次数 -新年活动
     * @param req
     * @param resp
     * @return
     */
    @RequestMapping(value="/newYear/getSurplusCount")
    @ResponseBody
    public ReturnModel getNewYearSurplusCount(HttpServletRequest req,HttpServletResponse resp){
    	return returnFail();
    	/*String token = req.getParameter("token");
    	if(StringUtils.isEmpty(token)){
    		returnModel.setCode(CodeContant.CONAUTHTOKEN);
			returnModel.setMessage("请先登录！");
			return returnModel;
    	}
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			returnModel.setCode(CodeContant.CONAUTHTOKEN);
			returnModel.setMessage("请先登录！");
			return returnModel;
		}
		int count = activityService.getNewYearSurplusCount(uid);
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("surplusCount", count);
		int price = 20;
		if(count == 1){
			price = 20;
		}
		if(count ==2){
			price = 10;
		}
		if(count ==3){
			price = 5;
		}
		if(count ==4){
			price = 1;
		}
		if(count ==5){
			price = 0;
		}
		dataMap.put("price", price);
		returnModel.setData(dataMap);
    	return returnModel;*/
    }
    
    /**
     * 获取充值抽奖的记录
     * @param req
     * @param resp
     * @return
     */
    @RequestMapping(value="/newYear/getUserLottery")
    @ResponseBody
    public ReturnModel getUserNewYearLottery(HttpServletRequest req,HttpServletResponse resp){
    	return returnFail();
    	/*long lg = System.currentTimeMillis()/1000;
		List<Map<String,Object>> userItemLogModels = userItemService.selItemLogBySubType(ItemSource.newYearConsumeLottery,0, 24);
		for(Map<String,Object> itemLogModelMap : userItemLogModels){
			UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid((int)itemLogModelMap.get("uid"), false);
			int gid = (int)itemLogModelMap.get("gid");
			ConfigGiftModel giftConfigModel = configService.getGiftInfoByGidNew(gid);
			itemLogModelMap.put("gname", giftConfigModel.getGname());
			itemLogModelMap.put("nickname", userBaseInfoModel.getNickname());
		}
		List<Map<String, Object>> rewardLottoryList = activityService.getRewardLottoryList(2);
		for(Map<String,Object> rewardLottoryMap : rewardLottoryList){
			UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid((int)rewardLottoryMap.get("uid"), false);
			rewardLottoryMap.put("gname", "猪头");
			rewardLottoryMap.put("num",rewardLottoryMap.get("reward"));
			rewardLottoryMap.put("subtype",0);
			rewardLottoryMap.put("nickname", userBaseInfoModel.getNickname());
			rewardLottoryMap.put("addtime", rewardLottoryMap.get("addtime"));
		}
		userItemLogModels.addAll(rewardLottoryList);
		
		List<Map<String,Object>> chepiaoList = new ArrayList<Map<String,Object>>();
		if(lg >= 1485519600){ //1485519600
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("nickname", "加肥猫");
			map.put("addtime",1485519600);
			map.put("gname","现金8888元");
			map.put("num","1");
			chepiaoList.add(map);
		}
		if(lg >= 1485681060){ //1485681060
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("nickname", "坏大叔");
			map.put("addtime",1485681060);
			map.put("gname","现金8888元");
			map.put("num","1");
			chepiaoList.add(map);
		}
			
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("lotteryList", userItemLogModels);
		data.put("chepiaoList", chepiaoList);
		returnModel.setData(data);
		return returnModel;*/
    }
    /**
     * 获取用户当前的任务进度 及相关奖励是否已领取
     * @param req
     * @param resp
     * @return
     */
    @RequestMapping(value="/getUserInvite")
    @ResponseBody
    public ReturnModel getUserInvite(HttpServletRequest req,HttpServletResponse resp){
    	return returnFail();
    	/*if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime","token")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			return returnModel;
		}
		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			return returnModel;
		}
		Map<String, Object> inviteInfo = activityService.getInviteInfo(uid);
		returnModel.setData(inviteInfo);
		return returnModel;*/
    }
    
    /**
     * 领取阶段奖励
     * @param req
     * @param resp
     * @return
     */
    @RequestMapping(value="/userInvite/reward")
    @ResponseBody
    public ReturnModel userInviteReward(HttpServletRequest req,HttpServletResponse resp){
    	return returnFail();
    	/*if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime","token","taskRewardId","forcebuy")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime","taskRewardId","forcebuy")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			return returnModel;
		}
		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			return returnModel;
		}
		Integer taskRewardId = Integer.parseInt(req.getParameter("taskRewardId"));
		Integer forcebuy = Integer.parseInt(req.getParameter("forcebuy"));
		if(taskRewardId==4 && forcebuy==0){ //白金礼包
			UserVipInfoModel userVipInfoModel = ValueaddServiceUtil.getVipInfo(uid);
			if(userVipInfoModel != null){
				if(userVipInfoModel.getGid()==44){
					returnModel.setCode(CodeContant.mall_force_buy_guard);
					returnModel.setMessage("亲，您已有钻石VIP，领取后白金VIP视为自动放弃");
					return returnModel;
				}
			}
		}else if(taskRewardId==5 && forcebuy==0){ //钻石礼包
			UserVipInfoModel userVipInfoModel = ValueaddServiceUtil.getVipInfo(uid);
			if(userVipInfoModel != null){
				if(userVipInfoModel.getGid()==43){
					returnModel.setCode(CodeContant.mall_force_buy_guard);
					returnModel.setMessage("亲，您已有白金VIP，是否覆盖");
					return returnModel;
				}
			}
		}
		activityService.getInviteReward(uid, taskRewardId, forcebuy, returnModel);
    	return returnModel;*/
    }
    
    /**
     * 领取用户入驻奖励
     * @param req
     * @param resp
     * @return
     */
    @RequestMapping(value="/userInvite/getUserReward")
    @ResponseBody
    public ReturnModel getInviteUserReward(HttpServletRequest req,HttpServletResponse resp){
    	return returnFail();
    	/*if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime","token","inviteUid","getAll")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime","inviteUid","getAll")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			return returnModel;
		}
		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			return returnModel;
		}
		Integer inviteUid = Integer.parseInt(req.getParameter("inviteUid"));
		Integer getAll = Integer.parseInt(req.getParameter("getAll"));
		activityService.getInviteUserReward(uid, inviteUid, getAll, returnModel);
		return returnModel;*/
    }
}