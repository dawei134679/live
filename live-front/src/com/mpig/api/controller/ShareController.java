package com.mpig.api.controller;

import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mpig.api.dictionary.lib.VideoLineConfigLib;
import com.mpig.api.model.LiveMicTimeModel;
import com.mpig.api.model.ReturnModel;
import com.mpig.api.model.UserAssetModel;
import com.mpig.api.model.UserBaseInfoModel;
import com.mpig.api.redis.service.RelationRedisService;
import com.mpig.api.redis.service.UserRedisService;
import com.mpig.api.service.ILiveService;
import com.mpig.api.service.IRoomService;
import com.mpig.api.service.IUserItemService;
import com.mpig.api.service.IUserItemService.ItemSource;
import com.mpig.api.service.IUserService;
import com.mpig.api.service.impl.ConfigServiceImpl;
import com.mpig.api.utils.ChatMessageUtil;
import com.mpig.api.utils.CodeContant;
import com.mpig.api.utils.Constant;
import com.mpig.api.utils.DateUtils;
import com.mpig.api.utils.MathRandomUtil;
import com.mpig.api.utils.ParamHandleUtils;
import com.mpig.api.utils.RedisContant;
import com.mpig.api.utils.WxShareUtil;

@RestController
@Scope("prototype")
@RequestMapping("/share")
public class ShareController extends BaseController {

	private static final Logger logger = Logger.getLogger(ShareController.class);

	@Resource
	private IUserService userService;
	@Resource
	private ILiveService liveService;
	@Resource
	private IRoomService roomService;
	@Resource
	private IUserItemService userItemService;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel shareVideo(HttpServletRequest req, HttpServletResponse resp) {

		// String sharlUrlPc = "rtmp://pili-live-rtmp.xiaozhutv.com:1935/xiaozhu/%s";
		// String sharlUrl = "http://pili-live-hls.xiaozhutv.com/xiaozhu/%s.m3u8";

		UserBaseInfoModel userBaseInfoModel = null;
		UserAssetModel userAssetModel = null;
		LiveMicTimeModel liveMicTimeModel = null;
		if (ParamHandleUtils.isBlank(req, "uid")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "uid")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}

		int uid = Integer.valueOf(req.getParameter("uid"));
		userBaseInfoModel = userService.getUserbaseInfoByUid(uid, false);
		userAssetModel = userService.getUserAssetByUid(uid, false);

		if (userBaseInfoModel == null || userAssetModel == null) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("nickname", userBaseInfoModel.getNickname());
		map.put("credittotal", userAssetModel.getCreditTotal());
		map.put("status", userBaseInfoModel.getLiveStatus());
		map.put("headimage", userBaseInfoModel.getHeadimage());
		map.put("livimage", userBaseInfoModel.getLivimage());

		if (!userBaseInfoModel.getLiveStatus()) {

			liveMicTimeModel = liveService.getliveMicInfoLivedByUid(uid, false);
			// 停播
			if (liveMicTimeModel == null) {
				map.put("livetime", 0);
				map.put("livenum", 0);
				map.put("love", 0);
			} else {
				int itime = (liveMicTimeModel.getEndtime() - liveMicTimeModel.getStarttime()) * 1000
						- TimeZone.getDefault().getRawOffset();
				SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");// 初始化Formatter的转换格式。
				String hms = formatter.format(itime);
				map.put("livetime", hms);
				map.put("livenum", liveMicTimeModel.getAudience());
				map.put("love", liveMicTimeModel.getLikes());
			}

		} else {
			// 正在直播
			map.put("livetime", 0);
			map.put("livenum", RelationRedisService.getInstance().getUsersInAnchor(uid));
			map.put("love", 0);
		}
		map.put("anchorlevel", Constant.qn_default_bucket_domain + "/level_ic_" + userBaseInfoModel.getAnchorLevel()
				+ ".png?v=" + System.currentTimeMillis());
		map.put("iosdownurl", Constant.apk_iosDownUrl);
		map.put("android", Constant.apk_androidDownUrl);
		String stream = new ConfigServiceImpl().getThirdStream(uid);
		if (null == stream) {
			map.put("videourl", VideoLineConfigLib.getForNormal(2).getHls() + "/" + uid + ".m3u8");
		} else {
			map.put("videourl", stream);
		}
		map.put("videourlpc", VideoLineConfigLib.getForAdvanced(2).getDomainPrefix() + "/" + uid);
		returnModel.setData(map);
		return returnModel;
	}

	@RequestMapping(value = "/sharelist", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel getRecommedList(HttpServletRequest req, HttpServletResponse resp) {
		roomService.getLiveingList(2, 1, returnModel, null);
		return returnModel;
	}

	@RequestMapping(value = "/room", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel shareRoom(Integer artistid, Integer uid, HttpServletRequest req, HttpServletResponse resp) {
		if (uid == null) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("参数不能为空!");
			return returnModel;
		}
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "uid", "artistid")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime", "artistid", "uid")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}

		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			return returnModel;
		}
		String token = req.getParameter("token");// 充值操作对象
		uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			return returnModel;
		}
		Long lg = System.currentTimeMillis() / 1000;
		String day = DateUtils.dateToString(null, "yyyyMMdd");
		UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(uid, false);
		// 判断是否使用默认的分享语句
		int defaultShare = 1;

		// 1482249600 1483200000
		if (lg >= 1482508800 && lg <= 1483286399) {
			String aoyunKey = "christmas:";
			String aoyunKeyShareKey = RedisContant.ShareApp + aoyunKey + uid + day; // 奥运分享的key
			boolean isshare = setUserShareDateByRoom(aoyunKeyShareKey, artistid, 0);
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			HashMap<String, Object> map = new HashMap<String, Object>();
			// 更改正式环境的aoyunId
			int aoyunId = MathRandomUtil.shareRandom();
			if (isshare && aoyunId > 0) {
				defaultShare = 0;
				userItemService.insertUserItem(uid, aoyunId, 1, ItemSource.Activity);
				map = new HashMap<String, Object>();
				map.put("nickname", userBaseInfoModel.getNickname());
				map.put("level", userBaseInfoModel.getUserLevel());
				map.put("islink", true);
				map.put("sex", userBaseInfoModel.getSex());
				map.put("uid", userBaseInfoModel.getUid());
				map.put("color", "#ff00ff");
				list.add(map);

				map = new HashMap<String, Object>();
				map.put("name", " 分享了本场直播，获得1个");
				map.put("color", "#ff00ff");
				list.add(map);

				map = new HashMap<String, Object>();
				map.put("gid", aoyunId);
				map.put("color", "");
				list.add(map);

				// 更新礼物至背包
				List<Map<String, Object>> giftList = new ArrayList<Map<String, Object>>();
				HashMap<String, Object> giftMap = new HashMap<String, Object>();
				HashMap<String, Object> toUserMap = new HashMap<String, Object>();
				giftMap.put("gid", aoyunId);
				giftMap.put("num", "1");
				giftList.add(giftMap);
				UserAssetModel sendUserAssetModel = userService.getUserAssetByUid(uid, false);
				ChatMessageUtil.sendGiftUpdateAssetAndBag(uid, null, sendUserAssetModel.getMoney(), toUserMap, giftList,
						null, null, null);
				ChatMessageUtil.shareRoomNotice(artistid, list, uid, userBaseInfoModel.getNickname(),
						userBaseInfoModel.getSex(), userBaseInfoModel.getUserLevel());
			}
		}

		if (defaultShare == 1) {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("nickname", userBaseInfoModel.getNickname());
			map.put("level", userBaseInfoModel.getUserLevel());
			map.put("islink", true);
			map.put("sex", userBaseInfoModel.getSex());
			map.put("uid", userBaseInfoModel.getUid());
			map.put("color", "#fff08c");
			list.add(map);

			map = new HashMap<String, Object>();
			map.put("name", " 分享了本场直播");
			map.put("color", "#e9fed7");
			list.add(map);
			ChatMessageUtil.shareRoomNotice(artistid, list, uid, userBaseInfoModel.getNickname(),
					userBaseInfoModel.getSex(), userBaseInfoModel.getUserLevel());
		}
		return returnModel;
	}

	/**
	 * 设置某个key的分享次数
	 * 
	 * @param key
	 *            需要设置分享次数的key
	 * @param uid
	 *            分享的用户id
	 * @param shareCounts
	 *            最高分享次数限制
	 * @param expireDay
	 *            过期天数
	 */
	public boolean setUserShareDate(String key, Integer uid, int shareCounts, int expireDay) {
		Long userShareCount = UserRedisService.getInstance().getMembersRankScore(key, uid.toString()); // 获取用户今天分享次数
		if (userShareCount == null || userShareCount < shareCounts) {
			// 增加用户分享的次数
			if (!UserRedisService.getInstance().exists(key)) {
				UserRedisService.getInstance().setRank(key, uid.toString(), (double) 1);
				int expire = expireDay * 60 * 60 * 24; // 天数为单位
				UserRedisService.getInstance().expire(key, expire);
			} else {
				UserRedisService.getInstance().setRank(key, uid.toString(), (double) 1);
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 设置某个key的分享次数
	 * 
	 * @param key
	 *            需要设置分享次数的key
	 * @param roomId
	 *            分享的房间id
	 * @param allShareCounts
	 *            最高分享次数限制
	 */
	public boolean setUserShareDateByRoom(String key, Integer roomId, int allShareCounts) {
		int userShareCount = UserRedisService.getInstance().zcard(key); // 获取用户今天分享次数
		if (allShareCounts == 0)
			allShareCounts = Integer.MAX_VALUE;
		if (userShareCount == 0 || userShareCount < allShareCounts) {
			if (!UserRedisService.getInstance().exists(key)) {
				UserRedisService.getInstance().setRank(key, roomId.toString(), (double) 1);
				UserRedisService.getInstance().expire(key, DateUtils.getSecondeToNextDay());
			} else {
				Long userShareRoomCount = UserRedisService.getInstance().getMembersRankScore(key, roomId.toString()); // 获取用户今天分享房间的次数
				if (userShareRoomCount == null) {
					UserRedisService.getInstance().setRank(key, roomId.toString(), (double) 1);
				} else {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 设置各种排行榜
	 * 
	 * @param rankKey
	 *            需要设置排行榜的key
	 * @param uid
	 *            排行榜uid
	 * @param count
	 *            排行榜需要增加的票数
	 */
	public void setTopRank(String rankKey, String uid, int count) {
		UserRedisService.getInstance().setRank(rankKey, uid, (double) count);
	}

	@RequestMapping(value = "/weixin/share/getsign", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel getWeiXinSignature(HttpServletRequest req, HttpServletResponse resp) {
		try {
			String shareUrl = req.getParameter("shareurl");
			if (StringUtils.isEmpty(shareUrl)) {
				returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
				returnModel.setMessage("缺少参数或参数为空");
			} else {
				Map<String, Object> signature = WxShareUtil.getSignature(shareUrl);
				returnModel.setData(signature);
			}
		} catch (NoSuchAlgorithmException e) {
			returnModel.setCode(400);
			returnModel.setMessage("加密失败1");
			logger.error("getWeiXinSignature-NoSuchAlgorithmException:" + e.toString());
		} catch (Exception e) {
			returnModel.setCode(400);
			returnModel.setMessage("加密失败2");
			logger.error("getWeiXinSignature-NoSuchAlgorithmException:" + e.toString());
		}
		return returnModel;
	}
}
