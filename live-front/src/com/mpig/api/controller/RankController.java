package com.mpig.api.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import redis.clients.jedis.Tuple;

import com.mpig.api.model.UserBaseInfoModel;
import com.mpig.api.redis.service.OtherRedisService;
import com.mpig.api.service.IRankService;
import com.mpig.api.service.IUserService;
import com.mpig.api.utils.CodeContant;
import com.mpig.api.utils.ParamHandleUtils;
import com.mpig.api.utils.RedisContant;

@Controller
@Scope("prototype")
@RequestMapping("/rank")
public class RankController extends BaseController {

	
	@Resource
	private IRankService rankService;
	@Resource
	private IUserService userService;
	
	
	/**
	 * 奥运热度值排行榜
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/olypicrank", method = RequestMethod.GET)
	public void olypicrank(HttpServletRequest req, HttpServletResponse resp) {

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "type")) {
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
		
		String type = req.getParameter("type");// user,anchor
		
		List<Object> olist = new ArrayList<Object>();
		Set<Tuple> zrevrangeWithScores = OtherRedisService.getInstance().zrevrangeWithScores(RedisContant.RedisNameOther, RedisContant.olympicDayFixed, 1, 30);
		if (zrevrangeWithScores != null) {
			for (Tuple tuple : zrevrangeWithScores) {
				Map<String, Object> map = new HashMap<String, Object>();
				UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(Integer.valueOf(tuple.getElement()), false);
				if (userBaseInfoModel != null) {
					map.put("nickname", userBaseInfoModel.getNickname());
					map.put("uid", userBaseInfoModel.getUid());
					map.put("finishCount", tuple.getScore());
					map.put("blessValue", tuple.getScore()*10000);
					olist.add(map);
				}
			}
		}
		
		List<Map<String, Object>> olypicRank = rankService.getOlypicRank(type);
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("list", olypicRank);
		map.put("finishList", olist);
		returnModel.setData(map);
		writeJson(resp, returnModel);
	}

	/**
	 * 奥运任务进度接口
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/olypicrank/star", method = RequestMethod.GET)
	public void olypicrankstar(HttpServletRequest req, HttpServletResponse resp) {

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "uid")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime","uid")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}
		
		int uid = Integer.valueOf(req.getParameter("uid"));
		
		Map<String, Object> olypicStar = rankService.getOlypicStar(uid);
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("star", olypicStar);
		returnModel.setData(map);
		writeJson(resp, returnModel);
	}
	
	@RequestMapping(value = "/midAutumn", method = RequestMethod.GET)
	public void midAutumn(HttpServletRequest req,HttpServletResponse resp){

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime","uid")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime","uid")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}
		
		String uid = req.getParameter("uid");
		
		Map<String, Object> midAutumn = rankService.getMidAutumn(uid);
		returnModel.setData(midAutumn);
		writeJson(resp, returnModel);
	}
	
	/**
	 * 中秋 主播收到月饼数排名
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/midAutumn/receiverank", method = RequestMethod.GET)
	public void receiverank(HttpServletRequest req,HttpServletResponse resp){

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime")) {
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
		
		List<Map<String, Object>> midReceivedRank = rankService.getMidReceivedRank();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("list", midReceivedRank);
		returnModel.setData(map);
		writeJson(resp, returnModel);
	}
	
	/**
	 * 中秋 用户送中秋月饼中奖500的次数排名
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/midAutumn/multi", method = RequestMethod.GET)
	public void multi(HttpServletRequest req,HttpServletResponse resp){

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime")) {
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
		
		List<Map<String, Object>> midMutilRank = rankService.getMidMutilRank();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("list", midMutilRank);
		returnModel.setData(map);
		writeJson(resp, returnModel);
	}
	
	/**
	 * 获取 主播获取魅力值 排名及值  和 荣耀王者 的第一名
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/national", method = RequestMethod.GET)
	public void nationalDay(HttpServletRequest req,HttpServletResponse resp){

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime","uid")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime","uid")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}
		
		String uid = req.getParameter("uid");
		
		Map<String, Object> nationalDay = rankService.getNationalDay(uid);
		returnModel.setData(nationalDay);
		writeJson(resp, returnModel);
	}

	/**
	 * 国庆节前 的活动排名
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/national/rank", method = RequestMethod.GET)
	public void nationalDayRank(HttpServletRequest req,HttpServletResponse resp){

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime","type")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime","type")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}
		String type = req.getParameter("type");
		
		List<Map<String, Object>> midMutilRank = rankService.getNationalDayRank(type);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("list", midMutilRank);
		returnModel.setData(map);
		writeJson(resp, returnModel);
	}
	
	/**
	 * 获取 主播获取魅力值 排名及值  和 荣耀王者 的第一名
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/xiaozhuRun", method = RequestMethod.GET)
	public void xiaozhuRun(HttpServletRequest req,HttpServletResponse resp){

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime","uid")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime","uid")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}
		
		String uid = req.getParameter("uid");
		
		Map<String, Object> xiaozhuRun = rankService.getXiaozhuRun(uid);
		returnModel.setData(xiaozhuRun);
		writeJson(resp, returnModel);
	}
	
	/**
	 * 万圣节 获取 主播获取魅力值 排名及值  和 荣耀王者 的第一名
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/halloween", method = RequestMethod.GET)
	public void halloween(HttpServletRequest req,HttpServletResponse resp){

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime","uid")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime","uid")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}
		
		String uid = req.getParameter("uid");
		
		Map<String, Object> xiaozhuRun = rankService.getHalloween(uid);
		returnModel.setData(xiaozhuRun);
		writeJson(resp, returnModel);
	}

	/**
	 * 万圣节 的活动排名
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/halloween/rank", method = RequestMethod.GET)
	public void halloweenRank(HttpServletRequest req,HttpServletResponse resp){

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime","type")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime","type")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}
		String type = req.getParameter("type");
		
		List<Map<String, Object>> midMutilRank = rankService.getHalloweenRank(type);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("list", midMutilRank);
		returnModel.setData(map);
		writeJson(resp, returnModel);
	}

	/**
	 * 国庆节前 的活动排名
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/xiaozhuRun/rank", method = RequestMethod.GET)
	public void xiaozhuRunRank(HttpServletRequest req,HttpServletResponse resp){

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime","type")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime","type")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}
		String type = req.getParameter("type");
		
		List<Map<String, Object>> midMutilRank = rankService.getXiaozhuRunRank(type);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("list", midMutilRank);
		returnModel.setData(map);
		writeJson(resp, returnModel);
	}
	
	/**
	 * 双11活动 获取 主播获取魅力值 排名及值  和 荣耀王者 的第一名
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/singles", method = RequestMethod.GET)
	public void singlesDay(HttpServletRequest req,HttpServletResponse resp){

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime","uid")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime","uid")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}
		
		String uid = req.getParameter("uid");
		
		Map<String, Object> xiaozhuRun = rankService.getCommen(uid, RedisContant.SinglesDayAnchor, RedisContant.SinglesDayUser);
		returnModel.setData(xiaozhuRun);
		writeJson(resp, returnModel);
	}

	/**
	 * 双11活动 的活动排名
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/singles/rank", method = RequestMethod.GET)
	public void singlesDayRank(HttpServletRequest req,HttpServletResponse resp){

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime","type")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime","type")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}
		String type = req.getParameter("type");
		
		List<Map<String, Object>> midMutilRank = rankService.getCommenRank(type, RedisContant.SinglesDayAnchor, RedisContant.SinglesDayUser);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("list", midMutilRank);
		returnModel.setData(map);
		writeJson(resp, returnModel);
	}

	/**
	 * 艾滋病日活动 获取 主播获取魅力值 排名及值  和 荣耀王者 的第一名
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/aids", method = RequestMethod.GET)
	public void aidsDay(HttpServletRequest req,HttpServletResponse resp){

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime","uid")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime","uid")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}
		
		String uid = req.getParameter("uid");
		
		Map<String, Object> xiaozhuRun = rankService.getCommen(uid, RedisContant.AIDSAnchor, RedisContant.AIDSUser);
		returnModel.setData(xiaozhuRun);
		writeJson(resp, returnModel);
	}

	/**
	 * 艾滋病日活动 的活动排名
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/aids/rank", method = RequestMethod.GET)
	public void aidsDayRank(HttpServletRequest req,HttpServletResponse resp){

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime","type")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime","type")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}
		String type = req.getParameter("type");
		
		List<Map<String, Object>> midMutilRank = rankService.getCommenRank(type, RedisContant.AIDSAnchor, RedisContant.AIDSUser);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("list", midMutilRank);
		returnModel.setData(map);
		writeJson(resp, returnModel);
	}


	/**
	 * 艾滋病日活动 获取 主播获取魅力值 排名及值  和 荣耀王者 的第一名
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/doubletwo", method = RequestMethod.GET)
	public void doubleDay(HttpServletRequest req,HttpServletResponse resp){

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime","uid")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime","uid")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}
		
		String uid = req.getParameter("uid");
		
		Map<String, Object> xiaozhuRun = rankService.getCommen(uid, RedisContant.Double12Anchor, RedisContant.Double12User);
		returnModel.setData(xiaozhuRun);
		writeJson(resp, returnModel);
	}

	/**
	 * 艾滋病日活动 的活动排名
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/doubletwo/rank", method = RequestMethod.GET)
	public void doubleDayRank(HttpServletRequest req,HttpServletResponse resp){

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime","type")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime","type")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}
		String type = req.getParameter("type");
		
		List<Map<String, Object>> midMutilRank = rankService.getCommenRank(type, RedisContant.Double12Anchor, RedisContant.Double12User);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("list", midMutilRank);
		returnModel.setData(map);
		writeJson(resp, returnModel);
	}
	



	/**
	 * 房间活动小网页的接口 （通用）
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/giftActivity", method = RequestMethod.GET)
	public void topActivity(HttpServletRequest req,HttpServletResponse resp){

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime","uid")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime","uid")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}
		
		String uid = req.getParameter("uid");
		
		Map<String, Object> xiaozhuRun = rankService.getCommen(uid, RedisContant.ActivityNRAnchor, RedisContant.ActivityNRUser);
		returnModel.setData(xiaozhuRun);
		writeJson(resp, returnModel);
	}

	/**
	 * 活动排名接口 （通用）
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/giftActivity/rank", method = RequestMethod.GET)
	public void christmasDayRank(HttpServletRequest req,HttpServletResponse resp){

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime","type")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime","type")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}
		String type = req.getParameter("type");
		
		List<Map<String, Object>> midMutilRank = rankService.getCommenRank(type, RedisContant.ActivityNRAnchor, RedisContant.ActivityNRUser);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("list", midMutilRank);
		returnModel.setData(map);
		writeJson(resp, returnModel);
	}
}
