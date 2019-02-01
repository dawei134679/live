package com.mpig.api.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mpig.api.dictionary.lib.LevelsConfigLib;
import com.mpig.api.dictionary.lib.PayConfigLib;
import com.mpig.api.dictionary.lib.UrlConfigLib;
import com.mpig.api.model.AuthenticationModel;
import com.mpig.api.model.PayAccountModel;
import com.mpig.api.model.ReturnModel;
import com.mpig.api.model.UserAccountModel;
import com.mpig.api.model.UserAssetModel;
import com.mpig.api.model.UserBaseInfoModel;
import com.mpig.api.model.UserXiaozhuAuthModel;
import com.mpig.api.redis.service.OtherRedisService;
import com.mpig.api.redis.service.RedisCommService;
import com.mpig.api.redis.service.RelationRedisService;
import com.mpig.api.redis.service.UserRedisService;
import com.mpig.api.service.IBillService;
import com.mpig.api.service.ILiveService;
import com.mpig.api.service.IOrderService;
import com.mpig.api.service.ISearchService;
import com.mpig.api.service.IUserAlbumService;
import com.mpig.api.service.IUserService;
import com.mpig.api.service.impl.SensitiveWordsService;
import com.mpig.api.utils.CodeContant;
import com.mpig.api.utils.Constant;
import com.mpig.api.utils.ParamHandleUtils;
import com.mpig.api.utils.RedisContant;

@Controller
@Scope("prototype")
@RequestMapping("/user")
public class UserController extends BaseController {

	private static final Logger logger = Logger.getLogger(UserController.class);
	@Resource
	private IUserService userService;
	@Resource
	private ISearchService searchService;
	@Resource
	private IOrderService orderService;
	@Resource
	private IUserAlbumService userAlbumService;
	
	@Resource
	private IBillService iBillService;
	
	@Resource
	public ILiveService iLiveService;

	/**
	 * 获取用户基本信息
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "userdata", method = RequestMethod.GET)
	public void getUserData(HttpServletRequest req, HttpServletResponse resp) {

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

		// 验证auth及token
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}

		String token = req.getParameter("token");// 充值操作对象
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		//int dstUid = req.getParameter("dstuid") == null? uid:Integer.valueOf(req.getParameter("dstuid"));
		int dstUid = uid;
		Map<String, Object> map = userService.getUserDataMap(dstUid, uid);
		if (map == null) {
			returnModel.setCode(CodeContant.ACCOUNTEXCEPT);
			returnModel.setMessage("该用户异常，请重新登录");
		} else {
			map.put("auths", getAuths(uid));
			returnModel.setData(map);
		}
		writeJson(resp, returnModel);
	}

	
	/**
	 * 获取用户基本信息
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "userdata/v1", method = RequestMethod.GET)
	public void getUserDataV1(HttpServletRequest req, HttpServletResponse resp) {

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

		// 验证auth及token
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}

		String token = req.getParameter("token");// 充值操作对象
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		int dstUid = uid;
		Map<String, Object> map = userService.getUserDataMap(dstUid, uid);
		if (map == null) {
			returnModel.setCode(CodeContant.ACCOUNTEXCEPT);
			returnModel.setMessage("该用户异常，请重新登录");
		} else {
			map.put("auths", getAuths(uid));
			map.put("albums", userAlbumService.getUserAlbumDate(uid, false));
			map.put("supports", userService.getSupportByUid(dstUid, 0, 3));
			returnModel.setData(map);
		}
		writeJson(resp, returnModel);
	}
	/**
	 * 修改用户昵称
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "nickname", method = RequestMethod.GET)
	public void updNickName(HttpServletRequest req, HttpServletResponse resp) {

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "nickname")) {
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

		// 验证auth及token
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}

		String token = req.getParameter("token");// 充值操作对象
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		String nickName = "";
		try {
			nickName = URLDecoder.decode(req.getParameter("nickname"), "UTF-8");
			int ilen = com.mpig.api.utils.StringUtils.length(nickName);
			if (ilen > 16) {
				returnModel.setCode(CodeContant.nicknameLen);
				returnModel.setMessage("昵称超长");
				writeJson(resp, returnModel);
				return;
			}
			int isAuth = userService.selectXiaozhuAuthForStatus(uid);
			if(isAuth!=0){
				returnModel.setCode(CodeContant.nicknameIsAuth);
				returnModel.setMessage("您已认证!不能修改昵称!");
				writeJson(resp, returnModel);
				return;
			}
			
			String string = SensitiveWordsService.getInstance().replaceSensitiveWords(nickName, nickName);

			if ("failed".equalsIgnoreCase(string)) {
				returnModel.setCode(CodeContant.updSignatuerSensit);
				returnModel.setMessage("涉及到敏感词");
				writeJson(resp, returnModel);
				return;
			}
			String replace = nickName.replaceAll(" ", "");
			if (replace.contains("官方") || replace.contains("客服") || replace.contains("运营") || replace.contains("系统")) {
				returnModel.setCode(CodeContant.updSignatuerSensit);
				returnModel.setMessage("涉及到敏感词 110");
				writeJson(resp, returnModel);
				return;
			}
			int ires = userService.updUserNickName(uid, nickName);

			if (ires == 0) {
				returnModel.setCode(CodeContant.updUserNickName);
				returnModel.setMessage("更新昵称失败");
			} else {
				// TOSY 成功 更新ES搜索
				searchService.updateUserFieldsAnsyc(String.valueOf(uid), "nickname", nickName);
				// Integer numb = 0; // 靓号TODO
				// List<SearchModel> datas = new ArrayList<SearchModel>();
				// SearchModel data = new SearchModel();
				// data.setNickname(nickName);
				// data.setUid((int) req.getSession().getAttribute(Uidkey));
				// data.setNumb(numb);
				// datas.add(data);
				// SearchServiceImpl.getInstance().updateUserInfosAnsyc(datas);
			}

		} catch (UnsupportedEncodingException e) {
			logger.error("<updNickName->UnsupportedEncodingException>" + e.toString());
			returnModel.setCode(CodeContant.CONPARAMSEXCEPT);
			returnModel.setMessage("参数异常");
		}
		writeJson(resp, returnModel);
		return;
	}

	/**
	 * 修改用户签名
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "signature")
	public void updSignature(HttpServletRequest req, HttpServletResponse resp) {
		System.out.println("REQUEST METHOED : "+req.getMethod());
		System.out.println("ncode ： "+req.getParameter("ncode"));
		System.out.println("os : "+req.getParameter("os"));
		System.out.println("imei : "+req.getParameter("imei"));
		System.out.println("reqtime : "+req.getParameter("reqtime"));
		System.out.println("token :　"+req.getParameter("token"));
		System.out.println("sign : "+req.getParameter("sign"));
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "sign")) {
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

		// 验证auth及token
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}

		String token = req.getParameter("token");// 充值操作对象
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		System.out.println(req.getParameter("sign"));
		String signature = "";
		try {
			signature = URLDecoder.decode(req.getParameter("sign"), "UTF-8");
			int ilen = com.mpig.api.utils.StringUtils.length(signature);
			if (ilen  > 74) {
				returnModel.setCode(CodeContant.nicknameLen);
				returnModel.setMessage("签名超长");
				writeJson(resp, returnModel);
				return;
			}
			String string = SensitiveWordsService.getInstance().replaceSensitiveWords(signature, signature);

			if ("failed".equalsIgnoreCase(string)) {
				returnModel.setCode(CodeContant.updSignatuerSensit);
				returnModel.setMessage("涉及到敏感词");
				writeJson(resp, returnModel);
				return;
			} else if (signature.contains("官方") || signature.contains("客服") || signature.contains("运营")) {
				returnModel.setCode(CodeContant.updSignatuerSensit);
				returnModel.setMessage("涉及到敏感词 110");
				writeJson(resp, returnModel);
				return;
			} else {
				int ires = userService.updUserSignature(uid, signature);
				if (ires == 0) {
					returnModel.setCode(CodeContant.updUserSignature);
					returnModel.setMessage("更新签名失败");
					writeJson(resp, returnModel);
					return;
				} else {
					searchService.updateUserFieldsAnsyc(String.valueOf(uid), "slogan", signature);
				}
			}
		} catch (Exception e) {
			logger.error("<updSignature->UnsupportedEncodingException>" + e.toString());
			returnModel.setCode(CodeContant.CONPARAMSEXCEPT);
			returnModel.setMessage("参数异常");
		}
		writeJson(resp, returnModel);
		return;
	}
	
	/**
	 * 修改用户星座
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "updConstellation", method = RequestMethod.GET)
	public void updConstellation(HttpServletRequest req, HttpServletResponse resp) {

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

		// 验证auth及token
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}

		String token = req.getParameter("token");// 充值操作对象
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}

		String constellation = "";
		try {
			constellation = URLDecoder.decode(req.getParameter("constellation"), "UTF-8");
			int ilen = com.mpig.api.utils.StringUtils.length(constellation);
			if (ilen  > 74) {
				returnModel.setCode(CodeContant.constellationIsAuth);
				returnModel.setMessage("星座超长");
				writeJson(resp, returnModel);
				return;
			}
			
			int ires = userService.updUserConstellation(uid, constellation);
			if (ires == 0) {
				returnModel.setCode(CodeContant.updUserSignature);
				returnModel.setMessage("更新星座失败");
				writeJson(resp, returnModel);
				return;
			} else {
				//TODO 是否增加到个性化搜索词条
			}
		} catch (Exception e) {
			logger.error("<updConstellation->UnsupportedEncodingException>" + e.toString());
			returnModel.setCode(CodeContant.CONPARAMSEXCEPT);
			returnModel.setMessage("参数异常");
		}
		writeJson(resp, returnModel);
		return;
	}
	
	/**
	 * 修改用户爱好
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "updHobby", method = RequestMethod.GET)
	public void updHobby(HttpServletRequest req, HttpServletResponse resp) {

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

		// 验证auth及token
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}

		String token = req.getParameter("token");// 充值操作对象
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}

		String hobby = "";
		try {
			hobby = URLDecoder.decode(req.getParameter("hobby"), "UTF-8");
			int ilen = com.mpig.api.utils.StringUtils.length(hobby);
			if (ilen  > 74) {
				returnModel.setCode(CodeContant.constellationIsAuth);
				returnModel.setMessage("爱好超长");
				writeJson(resp, returnModel);
				return;
			}
			
			int ires = userService.updUserHobby(uid, hobby);
			if (ires == 0) {
				returnModel.setCode(CodeContant.updUserSignature);
				returnModel.setMessage("更新爱好失败");
				writeJson(resp, returnModel);
				return;
			} else {
				//TODO 是否增加到个性化搜索词条
			}
		} catch (Exception e) {
			logger.error("<updHobby->UnsupportedEncodingException>" + e.toString());
			returnModel.setCode(CodeContant.CONPARAMSEXCEPT);
			returnModel.setMessage("参数异常");
		}
		writeJson(resp, returnModel);
		return;
	}

	/**
	 * 修改性别
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "sex", method = RequestMethod.GET)
	public void updSex(HttpServletRequest req, HttpServletResponse resp) {

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "sex")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime", "sex")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}

		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}

		String token = req.getParameter("token");// 充值操作对象
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		int sex = Integer.valueOf(req.getParameter("sex"));// =0 女 =1 男

		Boolean bl = sex == 1 ? true : false;
		int ires = userService.updUserSex(uid, bl);
		if (ires == 0) {
			returnModel.setCode(CodeContant.updUserSex);
			returnModel.setMessage("性别更新失败");
		} else {
			Boolean bsex = false;
			if (1 == sex) {
				bsex = true;
			}
			searchService.updateUserFieldsAnsyc(Integer.valueOf(uid).toString(), "sex", bsex);
		}
		writeJson(resp, returnModel);
	}

	/**
	 * 删除粉丝
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "delfans", method = RequestMethod.GET)
	public void delFans(HttpServletRequest req, HttpServletResponse resp) {

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "dstuid")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime", "dstuid")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}

		// 验证auth及token
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}

		String token = req.getParameter("token");// 充值操作对象
		int srcUid = authService.decryptToken(token, returnModel);
		if (srcUid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		int dstUid = Integer.valueOf(req.getParameter("dstuid"));
		String os = "2".equals(req.getParameter("os")) ? "ios" : "android";

		RelationRedisService.getInstance().addFollows(dstUid, srcUid, 0.0, "off");
		UserRedisService.getInstance().delBroadcastAnchor(os, String.valueOf(dstUid), String.valueOf(srcUid));

		writeJson(resp, returnModel);
	}

	/**
	 * 获取声援榜单
	 *
	 * @param req		//tosy add arg for day...week..
	 * @param resp
	 */
	@RequestMapping(value = "suplist", method = RequestMethod.GET)
	public void getSupportByUid(HttpServletRequest req, HttpServletResponse resp) {

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "dstuid", "page")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime", "dstuid", "page")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}

		// 验证auth及token
		authToken(req, false);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}

		int uid = 0;
		String token = req.getParameter("token");// 充值操作对象
		if (!StringUtils.isEmpty(token)) {
			uid = authService.decryptToken(token, returnModel);
			if (uid <= 0) {
				writeJson(resp, returnModel);
				return;
			}
		}
		int dstUid = Integer.valueOf(req.getParameter("dstuid"));
		int page = Integer.valueOf(req.getParameter("page"));
		if (page <= 0) {
			page = 1;
		}
		
		String mode = req.getParameter("type");//tosy day(日)，week(周榜)，null为总榜
		userService.getSupportByUid(dstUid, uid, page, returnModel,mode);

		writeJson(resp, returnModel);
	}
	
	
	/**
	 * 获取主播新声援值贡献榜
	 */
	@RequestMapping(value = "getSupportContributionRankByDstuid", method = RequestMethod.GET)
	public void getSupportContributionRankByDstuid(HttpServletRequest req, HttpServletResponse resp) {
		try {
			if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "dstuid", "page")) {
				returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
				returnModel.setMessage("缺少参数或参数为空");
				writeJson(resp, returnModel);
				return;
			}
			if (!ParamHandleUtils.isInt(req, "os", "reqtime", "dstuid", "page")) {
				returnModel.setCode(CodeContant.ConParamTypeIsErr);
				returnModel.setMessage("参数类型错误");
				writeJson(resp, returnModel);
				return;
			}

			// 验证auth及token
			authToken(req, false);
			if (returnModel.getCode() != CodeContant.OK) {
				writeJson(resp, returnModel);
				return;
			}

			int uid = 0;
			String token = req.getParameter("token");// 充值操作对象
			if (!StringUtils.isEmpty(token)) {
				uid = authService.decryptToken(token, returnModel);
				if (uid <= 0) {
					writeJson(resp, returnModel);
					return;
				}
			}
			int dstuid = Integer.valueOf(req.getParameter("dstuid"));
			int page = Integer.valueOf(req.getParameter("page"));
			if (page <= 0) {
				page = 1;
			}
			
			Map<String,Object> dataMap = userService.getSupportContributionRankByDstuid(dstuid, uid, 3,page);
			returnModel.setData(dataMap);
			writeJson(resp, returnModel);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			writeJson(resp, returnModel);
		}
	} 
	
	/**
	 * 获取指定uid的粉丝列表
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "fans", method = RequestMethod.GET)
	public void getFans(HttpServletRequest req, HttpServletResponse resp) {

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "dstuid", "page")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime", "dstuid", "page")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}
		// 验证auth及token
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}

		String token = req.getParameter("token");// 充值操作对象
		int srcUid = authService.decryptToken(token, returnModel);
		if (srcUid <= 0) {
			writeJson(resp, returnModel);
			return;
		}

		Map<String, Object> mapFans = new HashMap<String, Object>();

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		UserBaseInfoModel userBaseInfoModel;
		int dstUid = Integer.valueOf(req.getParameter("dstuid"));

		int pages = Integer.valueOf(req.getParameter("page"));
		Set<String> setFans = RelationRedisService.getInstance().getFans(dstUid, pages);

		if (setFans.size() <= 0) {
			mapFans.put("count", 0);
			mapFans.put("list", list);
			returnModel.setData(mapFans);
		} else {
			Long isize = RelationRedisService.getInstance().getFansTotal(dstUid);
			isize = isize == null ? 0 : isize;
			for (String string : setFans) {
				userBaseInfoModel = userService.getUserbaseInfoByUid(Integer.valueOf(string), false);
				Map<String, Object> userFans = new HashMap<String, Object>();
				userFans.put("nickname", userBaseInfoModel.getNickname());
				userFans.put("uid", userBaseInfoModel.getUid());
				userFans.put("signature", userBaseInfoModel.getSignature());
				userFans.put("anchorLevel", userBaseInfoModel.getAnchorLevel());
				userFans.put("userLevel", userBaseInfoModel.getUserLevel());
				userFans.put("headimage", userBaseInfoModel.getHeadimage());
				if (srcUid != Integer.valueOf(string)) {
					Double fow = RelationRedisService.getInstance().isFollows(srcUid, Integer.valueOf(string));
					if (fow == null) {
						userFans.put("isFollow", false);
					} else {
						userFans.put("isFollow", true);
					}
				} else {
					userFans.put("isFollow", true);
				}
				userFans.put("sex", userBaseInfoModel.getSex());
				list.add(userFans);
			}
			mapFans.put("list", list);
			mapFans.put("count", isize);
		}
		returnModel.setData(mapFans);
		writeJson(resp, returnModel);
	}

	/**
	 * 根据用户uid获取用户信息
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/userbaseinfo", method = RequestMethod.GET)
	public void getUserBaseInfo(HttpServletRequest req, HttpServletResponse resp) {

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

		// 验证auth及token
		authToken(req, true);

		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}
		String token = req.getParameter("token");// 充值操作对象
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		Map<String, Object> map = userService.getAuthUserInfo(uid);
		if (map.size() <= 0) {
			returnModel.setCode(4000);
			returnModel.setMessage("该用户异常，请重新登录");
		} else {
			returnModel.setData(map);
		}
		writeJson(resp, returnModel);
	}

	/**
	 * 关注或取消关注
	 *
	 * @param req
	 * @param resq
	 */
	@RequestMapping(value = "/follows", method = RequestMethod.GET)
	public void addFollows(HttpServletRequest req, HttpServletResponse resp) {

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "dstuid", "type")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime", "dstuid")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}

		// 验证auth及token
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}

		String token = req.getParameter("token");// 充值操作对象
		int srcUid = authService.decryptToken(token, returnModel);
		if (srcUid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		String strType = req.getParameter("type");
		// uid 关注 dstuid
		Integer dstUid = Integer.valueOf(req.getParameter("dstuid")); // dstuid
		
		UserBaseInfoModel userbaseInfoByUid = userService.getUserbaseInfoByUid(dstUid,false);
		if (userbaseInfoByUid == null) {
			returnModel.setCode(CodeContant.FollowErr);
			returnModel.setMessage("对方不存在");
		}else {
			// 被 uid
			// 关注
			if (srcUid == dstUid) {
				returnModel.setCode(CodeContant.FollowErr);
				returnModel.setMessage("不能关注自己");
			} else {
				if (StringUtils.isNotEmpty(RelationRedisService.getInstance().getBlackUser(dstUid,srcUid))) {
					returnModel.setCode(CodeContant.user_black);
					returnModel.setMessage("你已被对方拉黑");
				} else {
					boolean bl = true;
					if (RelationRedisService.getInstance().getFollowsTimes(srcUid, dstUid)) {
						bl = false;
					}

					String os = "2".equals(req.getParameter("os")) ? "ios" : "android";
					boolean blFollows = RelationRedisService.getInstance().isFan(srcUid, dstUid);
					if (blFollows) {
						// 已经是粉丝
						if ("on".equalsIgnoreCase(strType)) {
							returnModel.setCode(CodeContant.FollowRep);
							returnModel.setMessage("不能重复关注");
						} else {
							userService.addFollows(strType, srcUid, dstUid);
							UserRedisService.getInstance().delBroadcastAnchor(os, String.valueOf(dstUid),
									String.valueOf(srcUid));
						}
					} else {
						// 不是粉丝
						if ("on".equalsIgnoreCase(strType)) {
							
							userService.addFollows(strType, srcUid, dstUid);
							if (StringUtils.isNotEmpty(RelationRedisService.getInstance().getBlackUser(srcUid,dstUid))) {
								// 解除黑名单
								RelationRedisService.getInstance().unBlackUser(srcUid, dstUid);
							}
							// 检查是否已存在os、主播、用户的devicetoken关系
							String broadcastAnchor = UserRedisService.getInstance().getBroadcastAnchor(os,
									String.valueOf(dstUid), String.valueOf(srcUid));
							if (StringUtils.isEmpty(broadcastAnchor)) {
								String appBroadcast = OtherRedisService.getInstance().getAppBroadcast(os,
										String.valueOf(srcUid));
								if (StringUtils.isNotEmpty(appBroadcast)) {
									UserRedisService.getInstance().setBroadcastAnchor(os, String.valueOf(dstUid),
											String.valueOf(srcUid), appBroadcast);
								}
							}
						} else {
							returnModel.setCode(CodeContant.FollowRep);
							returnModel.setMessage("不能重复关注");
						}
					}

					if (strType.equals("on") && bl) {
						userService.rpcAdminFollow(srcUid, dstUid, true);
					}
				}
			}
		}

		writeJson(resp, returnModel);
	}

	@RequestMapping(value = "/pcfollows", method = RequestMethod.GET)
	public void getPcFollows(HttpServletRequest req,HttpServletResponse resp){

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

		// 验证auth及token
		authToken(req, false);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}
		
		int uid = 0;
		String token = req.getParameter("token");// 充值操作对象
		if (StringUtils.isEmpty(token)) {
			uid = 0;
		}else {
			uid = authService.decryptToken(token, returnModel);
			if (uid <= 0) {
				writeJson(resp, returnModel);
				return;
			}
		}
		userService.getPCFollowsCenter(uid,returnModel);
		writeJson(resp, returnModel);
	}

	/**
	 * 用户中心获取关注列表
	 *
	 * @param req
	 * @param resq
	 */
	@RequestMapping(value = "/centerfols", method = RequestMethod.GET)
	public void getFollows(HttpServletRequest req, HttpServletResponse resp) {

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

		// 验证auth及token
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}

		String token = req.getParameter("token");// 充值操作对象
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		
		String dstUidstr = req.getParameter("dstuid");
		int dstUid = 0;
		if (dstUidstr != null) {
			dstUid = Integer.valueOf(dstUidstr);
		} else {
			dstUid = uid;
		}
		userService.getFollowsCenter(uid, dstUid,returnModel);
		writeJson(resp, returnModel);
	}

	/**
	 * 设置接收关注主播的推送消息开关
	 *
	 * @param req
	 * @param resq
	 */
	@RequestMapping(value = "/pushswitch", method = RequestMethod.GET)
	public void setPushSwitch(HttpServletRequest req, HttpServletResponse resp) {

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "dstuid", "type")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime", "dstuid", "type")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}
		// 验证auth及token
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}
		String token = req.getParameter("token");// 充值操作对象
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		String dstUid = req.getParameter("dstuid"); // =0 全关 其他则指定
		String type = req.getParameter("type");
		if (dstUid == null || type == null || type == "") {
			returnModel.setCode(CodeContant.CONPARAMSEXCEPT);
			returnModel.setMessage("参数错误");
		} else {
			if ("0".equals(dstUid)) {
				userService.setPushSwitch(uid, 0, Integer.valueOf(type));
			} else {
				userService.setPushSwitch(uid, Integer.valueOf(dstUid), Integer.valueOf(type));
			}
		}
		writeJson(resp, returnModel);
	}

	/**
	 * 手机绑定接口
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/bindmobile", method = RequestMethod.GET)
	public void bindMobile(HttpServletRequest req, HttpServletResponse resp) {

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "mobile", "code")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime", "mobile")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}
		// 验证auth及token
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}
		String mobile = req.getParameter("mobile").trim();
		String code = req.getParameter("code");
		String token = req.getParameter("token");// 充值操作对象
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}

		String bings = RedisCommService.getInstance().get(RedisContant.RedisNameUser, RedisContant.KeyMobileBing+mobile);
		if (bings != null && "3".equals(bings)) {
			returnModel.setCode(CodeContant.mobileBindErr);
			returnModel.setMessage("该手机已经多次绑定账号了，请使用其他手机号");
		}else {
			if (OtherRedisService.getInstance().getSendCode(mobile, code)) {
				// 验证通过
				int ires = userService.bindMobileByUid(uid, mobile);
				if (ires <= 0) {
					returnModel.setCode(CodeContant.CONSYSTEMERR);
					returnModel.setMessage("系统繁忙，请稍后再试");
				}else{
					String auths = UserRedisService.getInstance().get(RedisContant.AllAuth+uid);
					JSONObject jsonObject = JSONObject.parseObject(auths);
					jsonObject.put("phoneAuth", 3);
					UserRedisService.getInstance().set(RedisContant.AllAuth+uid, jsonObject.toJSONString());
					
					RedisCommService.getInstance().incrBy(RedisContant.RedisNameUser, RedisContant.KeyMobileBing+mobile, 1, 0);
				}
			} else {
				returnModel.setCode(CodeContant.MobileCodeErr);
				returnModel.setMessage("验证码不正确");
			}
		}
		writeJson(resp, returnModel);
	}

	/**
	 * 修改头像
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/headimg", method = RequestMethod.GET)
	public void updHeadimage(HttpServletRequest req, HttpServletResponse resp) {

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "urlimg")) {
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

		// 验证auth及token
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}

		String token = req.getParameter("token");// 充值操作对象
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		String headimage = Constant.qn_headImage_bucket_domain + "/" + req.getParameter("urlimg");
		Long lgLong = System.currentTimeMillis() / 1000;
		int ires = userService.updUserBaseHeadimg(uid, headimage + "?imageMogr2/thumbnail/50x50&v=" + lgLong,
				headimage + "?v=" + lgLong);
		if (ires == 0) {
			returnModel.setCode(CodeContant.updUserImage);
			returnModel.setMessage("图像更新失败");
		} else {
			Map<String, String> map = new HashMap<String, String>();
			map.put("headimage", headimage + "?imageMogr2/thumbnail/50x50");
			map.put("livimage", headimage);
			returnModel.setData(map);
			// 更新ES头像
			searchService.updateUserFieldsAnsyc(String.valueOf(uid), "avatar",
					headimage + "?imageMogr2/thumbnail/50x50");
		}
		writeJson(resp, returnModel);
	}

	/**
	 * 绑定提现账号
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/payaccount", method = RequestMethod.GET)
	public void updPayAccount(HttpServletRequest req, HttpServletResponse resp) {

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "weixin")) {
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

		// 验证auth及token
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}

		String token = req.getParameter("token");// 充值操作对象
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		String weixin = req.getParameter("weixin");
		if (weixin == null) {
			weixin = "";
		}
		String alipay = req.getParameter("alipay");
		if (alipay == null) {
			alipay = "";
		}
		PayAccountModel payAccountModel = orderService.getPayAccountByUid(uid, false);
		int ires = 0;
		if (payAccountModel == null) {
			ires = orderService.insertPayAccount(uid, weixin, alipay, System.currentTimeMillis() / 1000);
		} else {
			ires = orderService.updPayAccountByUid(uid, weixin, alipay);
		}
		if (ires != 1) {
			returnModel.setCode(CodeContant.updPayAccount);
			returnModel.setMessage("绑定提现账号失败");
		}
		writeJson(resp, returnModel);
	}

	/**
	 * 获取 可提现信息
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/withdraw", method = RequestMethod.GET)
	public void getWithDraw(HttpServletRequest req, HttpServletResponse resp) {
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

		// 验证auth及token
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}
		String token = req.getParameter("token");// 充值操作对象
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}

		userService.getWithDraw(uid, returnModel);
		writeJson(resp, returnModel);
	}

	/**
	 * 内兑接口
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/exchange", method = RequestMethod.GET)
	public void exchange(HttpServletRequest req,HttpServletResponse resp){

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "money")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime", "money")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}
		// 验证auth及token
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}
		int money = Integer.valueOf(req.getParameter("money"));
		
		String token = req.getParameter("token");// 充值操作对象
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		
		userService.exchange(uid, money, returnModel);
		writeJson(resp, returnModel);
	}
	
	/**
	 * 兑换配置表
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/exchangeconfig", method = RequestMethod.GET)
	public void exchangeConfig(HttpServletRequest req,HttpServletResponse resp){

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
		// 验证auth及token
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}
		
		String token = req.getParameter("token");// 充值操作对象
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		
		userService.exchangeConfig(returnModel);
		writeJson(resp, returnModel);
	}
	
	/**
	 * 兑换列表
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/exchangelist", method = RequestMethod.GET)
	public void exchangeList(HttpServletRequest req,HttpServletResponse resp){

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
		// 验证auth及token
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}
		
		String token = req.getParameter("token");// 充值操作对象
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		
		userService.exchangeList(uid, returnModel);
		writeJson(resp, returnModel);
	}

	/**
	 * 提现接口
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/tixian", method = RequestMethod.GET)
	public void pushTixian(HttpServletRequest req, HttpServletResponse resp) {

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "type", "amount")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime", "amount")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}
		// 验证auth及token
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}

		String token = req.getParameter("token");// 充值操作对象
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}

		if (OtherRedisService.getInstance().getWithDraw(uid) == 3) {
			returnModel.setCode(CodeContant.PayTixianTimes);
			returnModel.setMessage("提现超过规定的次数");
			writeJson(resp, returnModel);
			return;
		}
		// =alipay = weixin
		String type = req.getParameter("type");
		
		int amount = Integer.valueOf(req.getParameter("amount")); // 单位为元
		if (amount < 1 ) {
			returnModel.setCode(CodeContant.PayTixianMonyErr);
			returnModel.setMessage("提现金额不正确");
			writeJson(resp, returnModel);
			return;
		}
		
		userService.tixian(type, amount, uid, returnModel);
		writeJson(resp, returnModel);
	}

	/**
	 * 提现记录
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/txlist", method = RequestMethod.GET)
	public void getTiXianList(HttpServletRequest req, HttpServletResponse resp) {

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
		// 验证auth及token
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}

		String token = req.getParameter("token");// 充值操作对象
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		userService.getTixianList(uid, returnModel);
		writeJson(resp, returnModel);
	}

	@RequestMapping(value = "/bindWeiboVrified", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel bindWeiboVrified(HttpServletRequest req) {

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "openid", "accesstoken", "token")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}

		authToken(req, false);
		if (returnModel.getCode() != CodeContant.OK) {
			return returnModel;
		}

		String openid = req.getParameter("openid");
		String accessToken = req.getParameter("accesstoken");
		String token = req.getParameter("token");

		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			return returnModel;
		}
		UserAccountModel userAccountModel = userService.getUserAccountByUid(uid, false);
		if (userAccountModel == null) {
			returnModel.setCode(CodeContant.ACCOUNTEXCEPT);
			returnModel.setMessage("该账号异常，请重新登录");
			return returnModel;
		}

		HttpResponse<JsonNode> asJson = authService.getThirdUserInfo(accessToken, openid, "sina", null);

		if (asJson == null) {
			returnModel.setCode(CodeContant.THIRDINTERFACE);
			returnModel.setMessage("第三方接口不稳定");
			return returnModel;
		}

		int result = asJson.getStatus();
		if (result == 200) {
			JSONObject userData = JSON.parseObject(asJson.getBody().toString());
			if (!userData.getBoolean("verified")) {
				returnModel.setCode(CodeContant.WEIBO_NOT_VERIFIED);
				returnModel.setMessage("微博账号尚未认证");
				return returnModel;
			}
			String verified_reason = userData.getString("verified_reason");
			int i = userService.updateWeiboVrified(uid, true, verified_reason);
			if (i != 1) {
				returnModel.setCode(CodeContant.WEIBO_BIND);
				returnModel.setData("微博认证绑定失败");
				return returnModel;
			}

			returnModel.setCode(CodeContant.OK);
			HashMap<String, Object> map = new HashMap<>();
			map.put("verified", true);
			map.put("verified_reason", verified_reason);
			returnModel.setData(map);

		} else {
			returnModel.setCode(CodeContant.THIRDINTERFACE);
			returnModel.setMessage("第三方接口不稳定");
			return returnModel;
		}

		return returnModel;
	}

	@RequestMapping(value = "/unbindWeiboVrified", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel unbindWeiboVrified(HttpServletRequest req) {

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

		authToken(req, false);
		if (returnModel.getCode() != CodeContant.OK) {
			return returnModel;
		}

		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			return returnModel;
		}
		UserAccountModel userAccountModel = userService.getUserAccountByUid(uid, false);
		if (userAccountModel == null) {
			returnModel.setCode(CodeContant.ACCOUNTEXCEPT);
			returnModel.setMessage("该账号异常，请重新登录");
			return returnModel;
		}

		int i = userService.updateWeiboVrified(uid, false, "");
		if (i != 1) {
			returnModel.setCode(CodeContant.WEIBO_BIND);
			returnModel.setData("微博认证解绑失败");
			return returnModel;
		}

		returnModel.setCode(CodeContant.OK);
		returnModel.setData(null);
		return returnModel;
	}

	@RequestMapping("/bindWeixin")
	public String bindWeixin(HttpServletRequest req) {
		String url = req.getParameter("url");
		if (url == null) {
			return null;
		}
		logger.debug("<bindWeixin>-->url" + url);
		String encode;
		try {
			encode = URLEncoder.encode(url, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}

		logger.debug("redirect:" + String.format(UrlConfigLib.getUrl("url").getWeixinBindUrl(),
				PayConfigLib.getConfig().getWeixin_appid(), encode));
		return "redirect:" + String.format(UrlConfigLib.getUrl("url").getWeixinBindUrl(),
				PayConfigLib.getConfig().getWeixin_appid(), encode);

	}

	@RequestMapping("/wxUserList")
	@ResponseBody
	public Map wxUserList(HttpServletRequest req, HttpServletResponse resp) {

		HashMap<String, Object> param = new HashMap<>();

		try {

			String access_token;
			String openid;
			String unionid;

			if (req.getParameter("code") != null) {
				JSONObject jsonObject = userService.getUserWXToken(req.getParameter("code"));
				if (jsonObject == null
						|| jsonObject.get("errcode") != null && jsonObject.get("errcode").equals("40001")) {
					param.put("success", false);
					param.put("message", "unAuth");
					return param;
				}
				if (jsonObject.getString("access_token") == null) {
					param.put("success", false);
					param.put("message", "出错了。。。");
					return param;
				}
				openid = jsonObject.getString("openid");
				unionid = jsonObject.getString("unionid");
				access_token = jsonObject.getString("access_token");
			} else {
				param.put("success", false);
				param.put("message", "unAuth");
				return param;
			}

			logger.info("<bindWeixin>--unionid openid " + unionid + "  " + openid);
			orderService.bindWeixin(unionid, openid);

			List<Integer> uids = orderService.getUidByUnionid(unionid);

			ArrayList<Map> usermaps = new ArrayList<>();

			for (Integer id : uids) {
				UserBaseInfoModel userBaseInfo = userService.getUserbaseInfoByUid(id, false);// 要刷新缓存
				orderService.getPayAccountByUid(id, true);
				
				UserAssetModel userAssetModel = userService.getUserAssetByUid(id, false);
				int rate = LevelsConfigLib.getForAdvanced(userBaseInfo.getAnchorLevel()).getRate();
				HashMap<String, Object> map = new HashMap<>();
				if (rate == 0) {
					map.put("credit", 0);
				} else {
					int credit = userAssetModel.getCredit() / 100;
					credit = credit * rate / 100;
					map.put("credit", credit);
				}

				map.put("uid", userBaseInfo.getUid());
				map.put("nickname", userBaseInfo.getNickname());
				map.put("headimage", userBaseInfo.getHeadimage());

				usermaps.add(map);
			}

			param.put("success", true);
			param.put("userList", usermaps);
			return param;

		} catch (Exception e) {
			logger.error("<wxUserList>-->error" + e);
			param.put("success", false);
			param.put("message", "出错了。。。");
			return param;
		}

	}

	@RequestMapping("/updcity")
	@ResponseBody
	public ReturnModel updateUserCity(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "city")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}

		authToken(req, false);
		if (returnModel.getCode() != CodeContant.OK) {
			return returnModel;
		}

		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			return returnModel;
		}
		
		String city = req.getParameter("city");

		userService.changeCityByUid(uid, city, returnModel);
		return returnModel;
	}

	@RequestMapping("/wxUserInfo")
	@ResponseBody
	public Map wxUserInfo(HttpServletRequest req, HttpServletResponse resp) {

		HashMap<String, Object> param = new HashMap<>();

		if (req.getParameter("uid") == null || req.getParameter("code") == null) {
			param.put("success", false);
			param.put("message", "出错了。。。");
			return param;
		}

//		logger.debug("<wxUserInfo>--url" + JSON.toJSONString(req.getParameterMap()));
//
//		logger.debug("code is " + req.getParameter("code"));
//		if (req.getParameter("code") != null) {
//			JSONObject jsonObject = userService.getUserWXToken(req.getParameter("code"));
//			logger.debug("jsonObject=" + jsonObject.toJSONString());
////			if (jsonObject.containsKey("errcode")) {
////				param.put("success", false);
////				param.put("message", jsonObject.get("errmsg"));
////				return param;
////			}
//			if (jsonObject.get("errcode") != null && jsonObject.get("errcode").equals("40001")) {
//				param.put("success", false);
//				param.put("message", "unAuth");
//				return param;
//			}
//			if (jsonObject.getString("access_token") == null) {
//				param.put("success", false);
//				param.put("message", "出错了。。。");
//				return param;
//			}
//		} else {
//			param.put("success", false);
//			param.put("message", "unAuth");
//			return param;
//		}

		try {
			Integer uid = Integer.valueOf(req.getParameter("uid"));
			logger.debug("<wxUserInfo>--uid" + uid);
			// PayAccountModel payAccount = orderService.getPayAccountByUid(uid,
			// true);
			//
			// if (payAccount == null || payAccount.getWx_openid().equals("")) {
			// param.put("success", false);
			// param.put("message", "unAuth");
			// return param;
			// }
			//
			// logger.debug("<wxUserInfo>--openid" + payAccount.getWx_openid());

			UserBaseInfoModel userBaseInfo = userService.getUserbaseInfoByUid(uid, false);
			UserAssetModel userAssetModel = userService.getUserAssetByUid(uid, false);
			int rate = LevelsConfigLib.getForAdvanced(userBaseInfo.getAnchorLevel()).getRate();
			
			HashMap<String, Object> map = new HashMap<>();
			 int limit = OtherRedisService.getInstance().getWithDrawLimit();
			 if (rate == 0) {
				 map.put("credit", 0);
				 map.put("limit", 0);
			 } else {
				 int credit = userAssetModel.getCredit() / 100;
				 credit = credit * rate / 100;
				 map.put("credit", credit);
				
				 if (limit > credit) {
					 map.put("limit", credit);
				 } else {
					 map.put("limit", limit);
				 }
			 }

			map.put("uid", userBaseInfo.getUid());
			map.put("nickname", userBaseInfo.getNickname());
			map.put("headimage", userBaseInfo.getHeadimage());
			map.put("code", req.getParameter("code"));

			param.put("success", true);
			param.put("userInfo", map);
			return param;

		} catch (Exception e) {
			e.printStackTrace();
			param.put("success", false);
			param.put("message", "出错了。。。");
			return param;
		}
	}

	@RequestMapping("/paylist")
	@ResponseBody
	public ReturnModel getPaylist(HttpServletRequest req, HttpServletResponse resp) {
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

		authToken(req, false);
		if (returnModel.getCode() != CodeContant.OK) {
			return returnModel;
		}

		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			return returnModel;
		}

		userService.getPaylist(uid, returnModel);
		return returnModel;
	}

	@RequestMapping("/authen")
	@ResponseBody
	public ReturnModel setAuthentication(HttpServletRequest req, HttpServletResponse resp) {
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

		authToken(req, false);
		if (returnModel.getCode() != CodeContant.OK) {
			return returnModel;
		}

		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			return returnModel;
		}

		String realName = req.getParameter("realName");// 用户真实姓名
		String cardID = req.getParameter("cardID");// 身份证ID
		String cardNo = req.getParameter("cardNo");// 银行卡号
		String bankAccount = req.getParameter("bankAccount");// 开户银行
		String provinceOfBank = req.getParameter("provinceOfBank");// 开户行省份
		String cityOfBank = req.getParameter("cityOfBank");// 开户行城市
		String branchBank = req.getParameter("branchBank");// 支行名称
		String positiveImage = req.getParameter("positiveImage"); // 正面照
		String negativeImage = req.getParameter("negativeImage"); // 反面照
		String handImage = req.getParameter("handImage"); // 手持照

		if (StringUtils.isEmpty(realName)) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("用户真实姓名不能为空");
		} else if (StringUtils.isEmpty(cardID)) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("身份证号码不能为空");
		} else if (StringUtils.isEmpty(cardNo)) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("银行卡号不能为空");
		} else if (StringUtils.isEmpty(bankAccount)) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("开户银行名称不能为空");
		} else if (StringUtils.isEmpty(provinceOfBank)) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("开户行省份不能为空");
		} else if (StringUtils.isEmpty(cityOfBank)) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("开户行城市不能为空");
		} else if (StringUtils.isEmpty(branchBank)) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("支行名称不能为空");
		} else if (StringUtils.isEmpty(positiveImage)) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("正面照不能为空");
		} else if (StringUtils.isEmpty(negativeImage)) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("反面照不能为空");
		} else if (StringUtils.isEmpty(handImage)) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("手持照不能为空");
		} else {
			int authentication = userService.insertAuthentication(uid, realName, cardID, cardNo, bankAccount,
					provinceOfBank, cityOfBank, branchBank, positiveImage, negativeImage, handImage);
			if (authentication != 1) {
				returnModel.setCode(CodeContant.authenErr);
				returnModel.setMessage("认证信息添加失败");
			}else{
				String auths = UserRedisService.getInstance().get(RedisContant.AllAuth+uid);
				JSONObject jsonObject = JSONObject.parseObject(auths);
				jsonObject.put("realnameAuth", 1);
				UserRedisService.getInstance().set(RedisContant.AllAuth+uid, jsonObject.toJSONString());
			}
		}
		return returnModel;
	}

	@RequestMapping("/getauthen")
	@ResponseBody
	public ReturnModel getAuthentication(HttpServletRequest req, HttpServletResponse resp) {
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

		authToken(req, false);
		if (returnModel.getCode() != CodeContant.OK) {
			return returnModel;
		}

		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			return returnModel;
		}
		AuthenticationModel authentication = userService.getAuthentication(uid);
		Map<String, Object> map = new HashMap<String, Object>();
		if (authentication == null) {
			map.put("auditStatus", 0);
			map.put("cause", "");
		} else {
			map.put("auditStatus", authentication.getAuditStatus());
			map.put("cause", authentication.getCause());
		}
		returnModel.setData(map);
		return returnModel;
	}

	/**
	 * 获取用户卡片信息接口
	 * 
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/usercard")
	@ResponseBody
	public ReturnModel getUserCard(HttpServletRequest req, HttpServletResponse resp) {

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "dstuid", "anchoruid")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime", "dstuid", "anchoruid")) {
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

		String token = req.getParameter("token");// 充值操作对象
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			return returnModel;
		}
		String dstUid = req.getParameter("dstuid");
		String anchoruid = req.getParameter("anchoruid");

		Map<String, Object> map = userService.getUserCardMap(uid, Integer.valueOf(dstUid), Integer.valueOf(anchoruid));
		if (map == null) {
			returnModel.setCode(CodeContant.ACCOUNTEXCEPT);
			returnModel.setMessage("该用户异常，请重新登录");
		} else {
			returnModel.setData(map);
		}
		return returnModel;
	}

	/**
	 * 获取用户卡片信息接口
	 * 
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/usercard/v1")
	@ResponseBody
	public ReturnModel getUserCardV1(HttpServletRequest req, HttpServletResponse resp) {

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "dstuid", "anchoruid")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime", "dstuid", "anchoruid")) {
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

		String token = req.getParameter("token");// 充值操作对象
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			return returnModel;
		}
		String dstUid = req.getParameter("dstuid");
		String anchoruid = req.getParameter("anchoruid");

		Map<String, Object> map = userService.getUserCardMap(uid, Integer.valueOf(dstUid), Integer.valueOf(anchoruid));
		if (map == null) {
			returnModel.setCode(CodeContant.ACCOUNTEXCEPT);
			returnModel.setMessage("该用户异常，请重新登录");
		} else {
			map.put("albums", userAlbumService.getUserAlbumDate(Integer.parseInt(dstUid), false));
			map.put("supports", userService.getSupportByUid(Integer.parseInt(dstUid), 0, 3));
			returnModel.setData(map);
		}
		return returnModel;
	}
	/**
	 * 用户中心获取用户信息
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping("/usercenter")
	@ResponseBody
	public ReturnModel getUserCenter(HttpServletRequest req, HttpServletResponse resp) {

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "dstuid")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime", "dstuid")) {
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

		String token = req.getParameter("token");// 充值操作对象
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			return returnModel;
		}
		String dstUid = req.getParameter("dstuid");

		Map<String, Object> map = userService.getUserCard(uid, Integer.valueOf(dstUid));
		returnModel.setData(map);

		return returnModel;
	}
	
	/**
	 * 小猪认证
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping("/xzauth")
	@ResponseBody
	public ReturnModel setXiaozhuAuth(HttpServletRequest req, HttpServletResponse resp) {
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

		authToken(req, false);
		if (returnModel.getCode() != CodeContant.OK) {
			return returnModel;
		}

		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			return returnModel;
		}
		try {
			
			String nickname = URLDecoder.decode(req.getParameter("nickname"),"UTF-8").trim();// 认证昵称
			String authContent = URLDecoder.decode(req.getParameter("authContent"),"UTF-8").trim();// 认证内容
			String authPics = req.getParameter("authPics");// 认证图片
			String authURLs = req.getParameter("authURLs");// 认证连接地址
			int ilen = com.mpig.api.utils.StringUtils.length(nickname);
			if (ilen > 16) {
				returnModel.setCode(CodeContant.nicknameLen);
				returnModel.setMessage("昵称超长");
				return returnModel;
			}
			if (StringUtils.isEmpty(nickname)) {
				returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
				returnModel.setMessage("认证昵称不能为空");
			} else if (StringUtils.isEmpty(authContent)) {
				returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
				returnModel.setMessage("认证内容不能为空");
			} else if (StringUtils.isEmpty(authPics)) {
				returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
				returnModel.setMessage("认证图片不能为空");
			} else {
				if(userService.getAuthenticationForStatus(uid)==0){
					returnModel.setCode(CodeContant.authenErr);
					returnModel.setMessage("请先实名认证!");
				}
				int isAuth = userService.selectXiaozhuAuthForStatus(uid);
				if(isAuth!=0){
					returnModel.setCode(CodeContant.authenErr);
					returnModel.setMessage("已认证,不能重复申请!");
				}else{
					int authNicknameCount = userService.getAuthNikenameCount(nickname);
					if(authNicknameCount==0){
						userService.cannelXzAuth(uid);
						int authentication = userService.insertXiaozhuAuth(uid, nickname, authContent, authPics, authURLs);
						if (authentication != 1) {
							returnModel.setCode(CodeContant.authenErr);
							returnModel.setMessage("认证信息添加失败");
						}else{
							String auths = UserRedisService.getInstance().get(RedisContant.AllAuth+uid);
							JSONObject jsonObject = JSONObject.parseObject(auths);
							jsonObject.put("xiaozhuAuth", 1);
							UserRedisService.getInstance().set(RedisContant.AllAuth+uid, jsonObject.toJSONString());
						}
					}else{
						returnModel.setCode(CodeContant.authenNicknameError);
						returnModel.setMessage("认证昵称已经存在");
					}
				}
			}
		} catch (Exception e) {
			returnModel.setCode(CodeContant.CONPARAMSEXCEPT);
			returnModel.setMessage("参数异常");
		}
		return returnModel;
	}
	

	public Map<String,Object> getAuths(int uid) {
		JSONObject jsonObject = new JSONObject();
		if(UserRedisService.getInstance().exists(RedisContant.AllAuth+uid)){
			String data = UserRedisService.getInstance().get(RedisContant.AllAuth+uid);
			jsonObject = JSONObject.parseObject(data);
		}else{
			Map<String,Object> authMap = new HashMap<String,Object>();
			UserBaseInfoModel baseInfoModel = userService.getUserbaseInfoByUid(uid, false);
			//手机认证
			if(StringUtils.isNotEmpty(baseInfoModel.getPhone())){
				authMap.put("phoneAuth", 3);
			}else{
				authMap.put("phoneAuth", 0);
			}
			//微博认证
			if(baseInfoModel.isVerified()){
				authMap.put("weiboAuth", 3);	
			}else{
				authMap.put("weiboAuth", 0);
			}
			//实名认证
			AuthenticationModel authenticationModel = userService.getAuthentication(uid);
			if(authenticationModel!=null){
				authMap.put("realnameAuth", authenticationModel.getAuditStatus());
			}else{
				authMap.put("realnameAuth", 0);//TODO 是否实名认证控制 3：已经认证
			}
			//小猪认证
			UserXiaozhuAuthModel isXiaozhuAuth = userService.getXiaozhuAuth(uid);
			if(isXiaozhuAuth != null){
				authMap.put("xiaozhuAuth", isXiaozhuAuth.getStatus());
			}else{
				authMap.put("xiaozhuAuth", 0);
			}
			UserRedisService.getInstance().set(RedisContant.AllAuth+uid, JSONObject.toJSONString(authMap));
			jsonObject = JSONObject.parseObject(JSONObject.toJSONString(authMap));
		}
		return jsonObject;
	}
	
	@RequestMapping("/getXzAuthInfo")
	@ResponseBody
	public ReturnModel getXzAuth(HttpServletRequest req, HttpServletResponse resp){
		Map<String,Object> authMap = new HashMap<String,Object>();
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

		authToken(req, false);
		if (returnModel.getCode() != CodeContant.OK) {
			return returnModel;
		}

		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			return returnModel;
		}
		
		UserXiaozhuAuthModel userXiaozhuAuthModel = userService.getXiaozhuAuth(uid);
		if(userXiaozhuAuthModel!=null){
			authMap.put("status", userXiaozhuAuthModel.getStatus());
			authMap.put("cause", userXiaozhuAuthModel.getCause());
			authMap.put("createAt", userXiaozhuAuthModel.getCreateAt());
			returnModel.setData(authMap);
		}else{
			authMap.put("status", 0);
			returnModel.setData(authMap);
		}
		return returnModel;
	}
	
	@RequestMapping("/getSinaAuthInfo")
	@ResponseBody
	public ReturnModel getSinaAuth(HttpServletRequest req, HttpServletResponse resp){
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

		authToken(req, false);
		if (returnModel.getCode() != CodeContant.OK) {
			return returnModel;
		}

		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			return returnModel;
		}
		
		Map<String, Object> SinaAuthMap = userService.getSinaVerified(uid);
		if(SinaAuthMap!=null){
			returnModel.setData(SinaAuthMap);
		}else{
			returnModel.setCode(CodeContant.ACCOUNTEXCEPT);
			returnModel.setMessage("该用户异常，请重新登录");
		}
		return returnModel;
	}
	
	/**
	 * 查询认证昵称是否存在
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping("/nickname/isuse")
	@ResponseBody
	public ReturnModel getNikenameIsUse(HttpServletRequest req, HttpServletResponse resp){
		Map<String,Object> dataMap = new HashMap<String,Object>();
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token","nickname")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}

		authToken(req, false);
		if (returnModel.getCode() != CodeContant.OK) {
			return returnModel;
		}

		String token = req.getParameter("token");
		authService.decryptToken(token, returnModel);
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			return returnModel;
		}
		String nickname = req.getParameter("nickname");
		int ilen = com.mpig.api.utils.StringUtils.length(nickname);
		if (ilen > 16) {
			returnModel.setCode(CodeContant.nicknameLen);
			returnModel.setMessage("昵称超长");
			return returnModel;
		}
		try {
			URLDecoder.decode(nickname,"UTF-8");
			int authNicknameCount = userService.getAuthNikenameCount(URLDecoder.decode(nickname,"UTF-8"));
			if(authNicknameCount > 0){
				dataMap.put("isuse", true);
			}else{
				dataMap.put("isuse", false);
			}
			returnModel.setData(dataMap);
		} catch (Exception e) {
			returnModel.setCode(CodeContant.CONPARAMSEXCEPT);
			returnModel.setMessage("参数异常");
		}
		return returnModel;
	}
	
	@RequestMapping("/cannelXzAuth")
	@ResponseBody
	public ReturnModel cannelXzAuth(HttpServletRequest req, HttpServletResponse resp){
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

		authToken(req, false);
		if (returnModel.getCode() != CodeContant.OK) {
			return returnModel;
		}

		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			return returnModel;
		}
		int flag = userService.cannelXzAuth(uid);
		if(flag==0){
			returnModel.setCode(CodeContant.authenCannelError);
			returnModel.setMessage("取消认证失败");
		}
		return returnModel;
	}
	
	@RequestMapping("/expnext")
	@ResponseBody
	public ReturnModel getUserExpNextLevel(HttpServletRequest req,HttpServletResponse resp){
		
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

		authToken(req, false);
		if (returnModel.getCode() != CodeContant.OK) {
			return returnModel;
		}

		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			return returnModel;
		}
		
		userService.getUserExpNextLevel(uid, returnModel);
		return returnModel;
	}

	@RequestMapping("/getfriends")
	@ResponseBody
	public ReturnModel getUserFriends(HttpServletRequest req,HttpServletResponse resp){
		
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

		authToken(req, false);
		if (returnModel.getCode() != CodeContant.OK) {
			return returnModel;
		}

		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			return returnModel;
		}
		
		int os = Integer.valueOf(req.getParameter("os"));
		int page = 0;
		userService.getUserFriendsInliving(uid,os,page, returnModel);
		
		return returnModel;
	}

	@RequestMapping("getallrecords")
	@ResponseBody
	public ReturnModel getAllRecords(HttpServletRequest req,HttpServletResponse resp){
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

		authToken(req, false);
		if (returnModel.getCode() != CodeContant.OK) {
			return returnModel;
		}
		
		String uid = req.getParameter("uid");
//		String uid = "10000281";
		if(null == uid){
			returnModel.setCode(CodeContant.CONPARAMSEXCEPT);
			returnModel.setMessage("参数错误");
		}else{
			userService.getRecordAllByUid(uid,returnModel);
			returnModel.setCode(CodeContant.OK);
			returnModel.setMessage("成功");
		}

		return returnModel;
	}
	
	@RequestMapping("delmyRecord")
	@ResponseBody
	public ReturnModel delmyRecord(HttpServletRequest req,HttpServletResponse resp){
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

		authToken(req, false);
		if (returnModel.getCode() != CodeContant.OK) {
			return returnModel;
		}
		
		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			return returnModel;
		}
		
		String time = req.getParameter("time");
		if(null == time){
			returnModel.setCode(CodeContant.CONPARAMSEXCEPT);
			returnModel.setMessage("参数错误");
		}else{
			userService.delmyRecordByTime(uid,time,returnModel);
		}

		return returnModel;
	}
	
	/**
	 * 添加待审核封面记录
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/addCover", method = RequestMethod.GET)
	public void addCover(HttpServletRequest req, HttpServletResponse resp) {

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

		// 验证auth及token
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}

		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		Map<String,Object> resultMap = userService.isNullId(uid,0);
		String cid = "";
		int uId =0;
		if(!resultMap.isEmpty()){
			cid = resultMap.get("id").toString();
		}
		if(!resultMap.isEmpty()){
			uId = Integer.parseInt(resultMap.get("uid").toString());
		}
		String 	picCover = req.getParameter("picCover");
		String	picCover1 = req.getParameter("picCover1");
		String	picCover2 = req.getParameter("picCover2");
		int counts[] = new int[3];
		if(cid.equals("")){		//主播上传封面原图
			if(picCover!=null){
				picCover = Constant.qn_liveCover_bucket_domain + "/"+picCover;
			}
			if(picCover1!=null){
				picCover1 = Constant.qn_liveCover_bucket_domain + "/"+picCover1;
			}
			if(picCover2!=null){
				picCover2 = Constant.qn_liveCover_bucket_domain + "/"+picCover2;
			}
			int result = userService.InsertUserCover(uid, picCover,picCover1,picCover2);
			if(result>0){
				counts[0]=1;
				returnModel.setData(counts);
				returnModel.setCode(CodeContant.OK);
				returnModel.setMessage("成功");
				writeJson(resp, returnModel);
				return;
			}
		}else{	
			counts[0]=1;
			if(picCover!=null){
				picCover = Constant.qn_liveCover_bucket_domain + "/"+picCover;
			}
			if(picCover1!=null){
				picCover1 = Constant.qn_liveCover_bucket_domain + "/"+picCover1;
			}
			if(picCover2!=null){
				picCover2 = Constant.qn_liveCover_bucket_domain + "/"+picCover2;
			}
			boolean result = userService.updUserCover(Integer.parseInt(cid),picCover,picCover1, picCover2);
			if(result){
				//获取到最新上传成功的记录
				Map<String,Object> map = userService.isNullId(uId,0);
				Object picCoverObj1 = map.get("picCover1");
				Object picCoverObj2 = map.get("picCover2");
				if(picCoverObj1!=null){
					counts[1]=1;
				}
				if(picCoverObj2!=null){
					counts[2]=1;
				}
				returnModel.setData(counts);
				returnModel.setCode(CodeContant.OK);
				returnModel.setMessage("成功");
				writeJson(resp, returnModel);
				return;
			}
		}
	}
	/**
	 * 获取主播上传封面审核状态
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/getStatus", method = RequestMethod.GET)
	public void getStatus(HttpServletRequest req, HttpServletResponse resp) {

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

		// 验证auth及token
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}

		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		//审核中或者审核驳回，则返回上次上传审核通过的封面图片,如果没有上传过，则返回图片为空；审核通过，则返回最新上传的封面图片（已审核通过）
		Map<String,Object> passResultMap = userService.isNullId(uid,1);			 //获取最新上传审核通过的记录
		Map<String,Object> causeResultMap = userService.isNullId(uid,2);		//获取最新上传审核驳回的记录
		Map<String,Object> resultMap = userService.getStatus(uid);			//获取主播最新上传的封面记录(只返回状态和驳回原因)
		Map<String, Object> newestMap = userService.getNewestRecord(uid);	//获取主播最新上传的封面记录(只返回最新上传的三张图片)
		int counts[] = null;
		Map<String,Object> map = new HashMap<String,Object>();
		if(resultMap.isEmpty()){		//用户尚未上传封面
			counts = new int[]{0,0,0};
			map.put("counts", counts);
			map.put("status", 3);//未上传封面
			map.put("cause", "");
			map.put("picCover", "");
			map.put("picCover1", "");
			map.put("picCover2", "");
			returnModel.setData(map);
			returnModel.setCode(CodeContant.OK);
			returnModel.setMessage("成功");
			writeJson(resp, returnModel);
			return;
		}else{					//用户已经上传封面
			int status = Integer.parseInt(resultMap.get("status").toString());
			if(status==0){		//待审核
				returnModel.setCode(CodeContant.OK);
				returnModel.setMessage("成功");
				counts = new int[3];
				if(newestMap.get("picCover")!=null){
					counts[0]=1;
				}
				if(newestMap.get("picCover1")!=null){
					counts[1]=1;
				}
				if(newestMap.get("picCover2")!=null){
					counts[2]=1;
				}
				if(!passResultMap.isEmpty()){
					if(passResultMap.get("picCover")!=null){
						resultMap.put("picCover", passResultMap.get("picCover").toString());
					}
					if(passResultMap.get("picCover1")!=null){
						resultMap.put("picCover1", passResultMap.get("picCover1").toString());
					}
					if(passResultMap.get("picCover2")!=null){
						resultMap.put("picCover2", passResultMap.get("picCover2").toString());
					}
				}
				resultMap.put("counts", counts);
				returnModel.setData(resultMap);
				writeJson(resp, returnModel);
				return;
			}else if(status==1){		//最新上传记录审核通过
				returnModel.setCode(CodeContant.OK);
				returnModel.setMessage("成功");
				counts = new int[]{0,0,0};
				/*if(newestMap.get("picCover")!=null){
					counts[0]=1;
				}
				if(newestMap.get("picCover1")!=null){
					counts[1]=1;
				}
				if(newestMap.get("picCover2")!=null){
					counts[2]=1;
				}*/
				passResultMap.put("counts", counts);
				returnModel.setData(passResultMap);
				writeJson(resp, returnModel);
				return;
			}else if(status==2){		//最新上传记录审核驳回
				returnModel.setCode(CodeContant.OK);
				returnModel.setMessage("成功");
				counts = new int[]{0,0,0};
				if(!passResultMap.isEmpty()){
					/*if(newestMap.get("picCover")!=null){
						counts[0]=1;
					}
					if(newestMap.get("picCover1")!=null){
						counts[1]=1;
					}
					if(newestMap.get("picCover2")!=null){
						counts[2]=1;
					}*/
					if(passResultMap.get("picCover")!=null){
						causeResultMap.put("picCover", passResultMap.get("picCover").toString());
					}
					if(passResultMap.get("picCover1")!=null){
						causeResultMap.put("picCover1", passResultMap.get("picCover1").toString());
					}
					if(passResultMap.get("picCover2")!=null){
						causeResultMap.put("picCover2", passResultMap.get("picCover2").toString());
					}
				}
				causeResultMap.put("counts", counts);
				returnModel.setData(causeResultMap);
				writeJson(resp, returnModel);
				return;
			}
		}
	}
	
	/**
	 * 消费记录
	 */
	@RequestMapping(value = "getConBillListByDate", method = RequestMethod.GET)
	public void getConBillListByDate(HttpServletRequest req, HttpServletResponse resp) {
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
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}
		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		String date = req.getParameter("date");
		int page = StringUtils.isBlank(req.getParameter("page"))?0:Integer.parseInt(req.getParameter("page"));
		int pageSize = StringUtils.isBlank(req.getParameter("pageSzie"))?Constant.defaultPageSize:Integer.parseInt(req.getParameter("pageSzie"));
		
		List<Map<String, Object>> list = iBillService.getCostRecord(uid, date,page,pageSize);
		long totalCount = iBillService.getCostRecordTotalCount(uid, date);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("list", list);
		map.put("totalPages", totalCount%pageSize==0?totalCount/pageSize:(totalCount/pageSize)+1);
		returnModel.setData(map);
		writeJson(resp, returnModel);
		return;
		
	}
	/**
	 *消费总数 
	 */
	@RequestMapping(value = "getConBillSumaryByDate", method = RequestMethod.GET)
	public void getConBillSumaryByDate(HttpServletRequest req, HttpServletResponse resp) {
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
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}

		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		String date = req.getParameter("date");
		long totalSum = iBillService.getConBillSumaryByDate(uid, date);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("sumzhutou", totalSum);
		returnModel.setData(map);
		writeJson(resp, returnModel);
		return;
	}
	
	
	/**
	 * 收礼记录
	 */
	@RequestMapping(value = "getRecBillListByDate", method = RequestMethod.GET)
	public void getRecBillListByDate(HttpServletRequest req, HttpServletResponse resp) {
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
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}
		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		String date = req.getParameter("date");
		int page = StringUtils.isBlank(req.getParameter("page"))?0:Integer.parseInt(req.getParameter("page"));
		int pageSize = StringUtils.isBlank(req.getParameter("pageSzie"))?Constant.defaultPageSize:Integer.parseInt(req.getParameter("pageSzie"));
		
		List<Map<String, Object>> list = iBillService.getRecBillListByDate(uid, date,page,pageSize);
		long totalCount = iBillService.getRecTotalCount(uid, date);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("list", list);
		map.put("totalPages", totalCount%pageSize==0?totalCount/pageSize:(totalCount/pageSize)+1);
		returnModel.setData(map);
		writeJson(resp, returnModel);
		return;
	}
	
	/**
	 * 收礼总数
	 */
	@RequestMapping(value = "getRecBillSumaryByDate", method = RequestMethod.GET)
	public void getRecBillSumaryByDate(HttpServletRequest req, HttpServletResponse resp) {
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
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}

		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		String date = req.getParameter("date");
		long totalCredit = iBillService.getRecBillSumaryByDate(uid, date);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("summoney", totalCredit);
		returnModel.setData(map);
		writeJson(resp, returnModel);
		return;
	}
	
	@RequestMapping(value = "getLivedTimeList", method = RequestMethod.GET)
	public void getLivedTimeList(HttpServletRequest req, HttpServletResponse resp) {
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
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}
		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		String type = req.getParameter("type");
		int page = StringUtils.isBlank(req.getParameter("page"))?0:Integer.parseInt(req.getParameter("page"));
		int pageSize = StringUtils.isBlank(req.getParameter("pageSzie"))?Constant.defaultPageSize:Integer.parseInt(req.getParameter("pageSzie"));
		
		List<Map<String, Object>> list = iLiveService.getLivedTimeList(uid, type, page, pageSize);
		long totalCount = iLiveService.getLivedTimeTotalCount(uid, type);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("list", list);
		map.put("totalPages", totalCount%pageSize==0?totalCount/pageSize:(totalCount/pageSize)+1);
		returnModel.setData(map);
		writeJson(resp, returnModel);
		return;
	}
	
	@RequestMapping(value = "getLivedTimeSumary", method = RequestMethod.GET)
	public void getLivedTimeSumary(HttpServletRequest req, HttpServletResponse resp) {
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
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}
		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		String type = req.getParameter("type");
		Map<String,Object> map = iLiveService.getLivedTimeSumary(uid, type);
		returnModel.setData(map);
		writeJson(resp, returnModel);
		return;
	}
	@RequestMapping(value = "getUserAsset", method = RequestMethod.GET)
	public void getUserAsset(HttpServletRequest req, HttpServletResponse resp) {
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
		UserAssetModel sendUserAssetModel = userService.getUserAssetByUid(uid, false);
		returnModel.setData(sendUserAssetModel);
		writeJson(resp, returnModel);
		return;
	}
	
	@RequestMapping(value = "getMicId", method = RequestMethod.GET)
	public void getMicId(HttpServletRequest req, HttpServletResponse resp) {
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
		Integer uid = Integer.parseInt(req.getParameter("uid"));
		if (uid==null||uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		String redisStr = OtherRedisService.getInstance().getRoomEndTime(uid);
		if(StringUtils.isBlank(redisStr)) {
			returnModel.setCode(CodeContant.CONSYSTEMERR);
			returnModel.setMessage("获取麦时异常");
			writeJson(resp, returnModel);
			return;
		}
		returnModel.setData(redisStr.split(",")[0]);
		writeJson(resp, returnModel);
		return;
	}
	@RequestMapping(value = "getUserBaseById", method = RequestMethod.GET)
	public void getUserBaseById(HttpServletRequest req, HttpServletResponse resp) {
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
		Integer uid = Integer.parseInt(req.getParameter("uid"));
		if (uid==null||uid <= 0) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}
		UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(uid, false);
		if(userBaseInfoModel==null) {
			returnModel.setCode(CodeContant.UserBaseInfoIsNull);
			returnModel.setMessage("用户不存在");
			writeJson(resp, returnModel);
			return;
		}
		returnModel.setData(userBaseInfoModel);
		writeJson(resp, returnModel);
		return;
	}
}
