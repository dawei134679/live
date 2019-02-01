package com.tinypig.admin.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.tinypig.admin.dao.GiftInfoDao;
import com.tinypig.admin.dao.OperateDao;
import com.tinypig.admin.model.AdminUserModel;
import com.tinypig.admin.model.ConfigGiftModel;
import com.tinypig.admin.service.GiftServiceImpl;
import com.tinypig.admin.util.Constant;
import com.tinypig.admin.util.DateUtil;

@Controller
@RequestMapping("/gift")
public class GiftController {

	@Resource
	private GiftServiceImpl giftService;

	@RequestMapping(value = "/forSelectBySubtype")
	@ResponseBody
	public List<Map<String, Object>> getGiftForSelect(HttpServletRequest request, HttpServletResponse response) {

		String strSubType = request.getParameter("subtype");

		int subtype = StringUtils.isEmpty(strSubType) ? 99 : Integer.valueOf(strSubType);

		return giftService.getGiftForSelect(subtype);
	}

	@RequestMapping(value = "/forSelectByType")
	@ResponseBody
	public List<Map<String, Object>> forSelectByType(HttpServletRequest request, HttpServletResponse response) {

		String strType = request.getParameter("type");
		int type = StringUtils.isEmpty(strType) ? 99 : Integer.valueOf(strType);

		return giftService.forSelectByType(type, true);
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getUsedVIPList")
	@ResponseBody
	public Map<String, Object> getUsedVIPList(HttpServletRequest request, HttpServletResponse response) {

		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		if (adminUser == null) {
			return null;
		}

		String strUid = request.getParameter("uid");
		String strSTime = request.getParameter("startDate");
		String strETime = request.getParameter("endDate");

		if (StringUtils.isEmpty(strSTime) || StringUtils.isEmpty(strETime)) {
			return null;
		}

		int uid = StringUtils.isEmpty(strUid) ? 0 : Integer.valueOf(strUid);

		Long stime = DateUtil.dateToLong(strSTime, "yyyy-MM-dd", "other", 0);
		Long etime = DateUtil.dateToLong(strETime, "yyyy-MM-dd", "other", 0) + 24 * 3600;

		int ipage = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
		int irows = request.getParameter("rows") == null ? 20 : Integer.valueOf(request.getParameter("rows"));
		if (ipage == 0) {
			ipage = 1;
		}

		return giftService.getUsedVIPList(uid, stime, etime, ipage, irows);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/addUserVip", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addUserVip(HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> map = new HashMap<String, Object>();
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		if (adminUser == null) {
			map.put("success", 201);
			map.put("msg", "请先登录");
			return map;
		}

		String strUid = request.getParameter("uid");
		String strGid = request.getParameter("gid");
		String strNum = request.getParameter("num");
		String strDescrip = request.getParameter("descrip");

		try {
			int uid = StringUtils.isEmpty(strUid) ? 0 : Integer.valueOf(strUid);
			int gid = StringUtils.isEmpty(strGid) ? 0 : Integer.valueOf(strGid);
			int num = StringUtils.isEmpty(strNum) ? 0 : Integer.valueOf(strNum);
			if (uid <= 0 || gid <= 0 || num <= 0 || num > 365 || StringUtils.isEmpty(strDescrip)) {
				map.put("success", 202);
				map.put("msg", "参数不正确202");
			} else {

				HttpResponse<String> addVipJson = Unirest.get(
						Constant.business_server_url + "/admin/addVip?dstuid=" + uid + "&gid=" + gid + "&count=" + num)
						.asString();
				if (addVipJson.getStatus() == 200) {
					Map<String, Object> parse = (Map<String, Object>) JSONObject.parse(addVipJson.getBody().toString());

					if ("200".equals(parse.get("code").toString())) {
						// 添加成功
						String current_version = "{uid:" + uid + ",gid:" + gid + ",bak:" + strDescrip + "}";
						OperateDao.getInstance().AddOperationLog("user_vip_info", "0", "添加vip", 1, "", current_version,
								adminUser.getUid());

						map.put("success", 200);
						map.put("msg", "成功");
					} else {
						// 添加失败
						map.put("success", 203);
						map.put("msg", parse.get("message"));
					}
				} else {
					map.put("success", 205);
					map.put("msg", "服务器不稳定205");
				}
			}
		} catch (Exception e) {

			System.out.println("addUserVip-excep:" + e.getMessage());

			map.put("success", 204);
			map.put("msg", e.getMessage());
		}
		return map;
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getUsedCarList")
	@ResponseBody
	public Map<String, Object> getUsedCarList(HttpServletRequest request, HttpServletResponse response) {

		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		if (adminUser == null) {
			return null;
		}

		String strUid = request.getParameter("uid");
		String strSTime = request.getParameter("startDate");
		String strETime = request.getParameter("endDate");

		if (StringUtils.isEmpty(strSTime) || StringUtils.isEmpty(strETime)) {
			return null;
		}

		int uid = StringUtils.isEmpty(strUid) ? 0 : Integer.valueOf(strUid);

		Long stime = DateUtil.dateToLong(strSTime, "yyyy-MM-dd", "other", 0);
		Long etime = DateUtil.dateToLong(strETime, "yyyy-MM-dd", "other", 0) + 24 * 3600;

		int ipage = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
		int irows = request.getParameter("rows") == null ? 20 : Integer.valueOf(request.getParameter("rows"));
		if (ipage == 0) {
			ipage = 1;
		}

		return giftService.getUsedCarList(uid, stime, etime, ipage, irows);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/addUserCar", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addUserCar(HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> map = new HashMap<String, Object>();
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		if (adminUser == null) {
			map.put("success", 201);
			map.put("msg", "请先登录");
			return map;
		}

		String strUid = request.getParameter("uid");
		String strGid = request.getParameter("gid");
		String strNum = request.getParameter("num");
		String strDescrip = request.getParameter("descrip");

		try {
			int uid = StringUtils.isEmpty(strUid) ? 0 : Integer.valueOf(strUid);
			int gid = StringUtils.isEmpty(strGid) ? 0 : Integer.valueOf(strGid);
			int num = StringUtils.isEmpty(strNum) ? 0 : Integer.valueOf(strNum);
			if (uid <= 0 || gid <= 0 || num <= 0 || num > 365 || StringUtils.isEmpty(strDescrip)) {
				map.put("success", 202);
				map.put("msg", "参数不正确202");
			} else {

				HttpResponse<String> addVipJson = Unirest.get(
						Constant.business_server_url + "/admin/addCar?dstuid=" + uid + "&gid=" + gid + "&count=" + num)
						.asString();
				if (addVipJson.getStatus() == 200) {
					Map<String, Object> parse = (Map<String, Object>) JSONObject.parse(addVipJson.getBody().toString());

					if ("200".equals(parse.get("code").toString())) {
						// 添加成功
						String current_version = "{uid:" + uid + ",gid:" + gid + ",bak:" + strDescrip + "}";
						OperateDao.getInstance().AddOperationLog("user_car_info", "0", "添加Car", 1, "", current_version,
								adminUser.getUid());

						map.put("success", 200);
						map.put("msg", "成功");
					} else {
						// 添加失败
						map.put("success", 203);
						map.put("msg", parse.get("message"));
					}
				} else {
					map.put("success", 205);
					map.put("msg", "服务器不稳定205");
				}
			}
		} catch (Exception e) {

			System.out.println("addUserCar-excep:" + e.getMessage());

			map.put("success", 204);
			map.put("msg", e.getMessage());
		}
		return map;
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getUsedGuardList")
	@ResponseBody
	public Map<String, Object> getUsedGuardList(HttpServletRequest request, HttpServletResponse response) {

		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		if (adminUser == null) {
			return null;
		}

		String strUid = request.getParameter("uid");
		String strSTime = request.getParameter("startDate");
		String strETime = request.getParameter("endDate");

		if (StringUtils.isEmpty(strSTime) || StringUtils.isEmpty(strETime)) {
			return null;
		}

		int uid = StringUtils.isEmpty(strUid) ? 0 : Integer.valueOf(strUid);

		Long stime = DateUtil.dateToLong(strSTime, "yyyy-MM-dd", "other", 0);
		Long etime = DateUtil.dateToLong(strETime, "yyyy-MM-dd", "other", 0) + 24 * 3600;

		int ipage = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
		int irows = request.getParameter("rows") == null ? 20 : Integer.valueOf(request.getParameter("rows"));
		if (ipage == 0) {
			ipage = 1;
		}

		return giftService.getUsedGuardList(uid, stime, etime, ipage, irows);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/addUserGuard", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addUserGuard(HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> map = new HashMap<String, Object>();
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		if (adminUser == null) {
			map.put("success", 201);
			map.put("msg", "请先登录");
			return map;
		}

		String strUid = request.getParameter("uid");
		String strGid = request.getParameter("gid");
		String strNum = request.getParameter("num");
		String strDescrip = request.getParameter("descrip");
		String strAnchoruid = request.getParameter("anchoruid");

		try {
			int uid = StringUtils.isEmpty(strUid) ? 0 : Integer.valueOf(strUid);
			int gid = StringUtils.isEmpty(strGid) ? 0 : Integer.valueOf(strGid);
			int num = StringUtils.isEmpty(strNum) ? 0 : Integer.valueOf(strNum);
			int anchoruid = StringUtils.isEmpty(strAnchoruid) ? 0 : Integer.valueOf(strAnchoruid);

			if (uid <= 0 || gid <= 0 || num <= 0 || num > 365 || StringUtils.isEmpty(strDescrip) || anchoruid <= 0) {
				map.put("success", 202);
				map.put("msg", "参数不正确202");
			} else {

				HttpResponse<String> addVipJson = Unirest.get(Constant.business_server_url + "/admin/addGuard?srcuid="
						+ uid + "&gid=" + gid + "&count=" + num + "&dstuid=" + anchoruid).asString();
				if (addVipJson.getStatus() == 200) {
					Map<String, Object> parse = (Map<String, Object>) JSONObject.parse(addVipJson.getBody().toString());

					if ("200".equals(parse.get("code").toString())) {
						// 添加成功
						String current_version = "{uid:" + uid + ",gid:" + gid + ",anchoruid:" + anchoruid + ",bak:"
								+ strDescrip + "}";
						OperateDao.getInstance().AddOperationLog("user_guard_info", "0", "添加守护", 1, "", current_version,
								adminUser.getUid());

						map.put("success", 200);
						map.put("msg", "成功");
					} else {
						// 添加失败
						map.put("success", 203);
						map.put("msg", parse.get("message"));
					}
				} else {
					map.put("success", 205);
					map.put("msg", "服务器不稳定205");
				}
			}
		} catch (Exception e) {

			System.out.println("addUserGuard-excep:" + e.getMessage());

			map.put("success", 204);
			map.put("msg", e.getMessage());
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/addUserGift", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addUserGift(HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> map = new HashMap<String, Object>();
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		if (adminUser == null) {
			map.put("success", 201);
			map.put("msg", "请先登录");
			return map;
		}

		String strUid = request.getParameter("uid");
		String strGid = request.getParameter("gid");
		String strNum = request.getParameter("num");
		String strDescrip = request.getParameter("descrip");

		try {
			int uid = StringUtils.isEmpty(strUid) ? 0 : Integer.valueOf(strUid);
			int gid = StringUtils.isEmpty(strGid) ? 0 : Integer.valueOf(strGid);
			int num = StringUtils.isEmpty(strNum) ? 0 : Integer.valueOf(strNum);

			if (uid <= 0 || gid <= 0 || num <= 0 || num > 365 || StringUtils.isEmpty(strDescrip)) {
				map.put("success", 202);
				map.put("msg", "参数不正确202");
			} else {

				HttpResponse<String> addVipJson = Unirest.get(
						Constant.business_server_url + "/admin/addGift?dstuid=" + uid + "&gid=" + gid + "&count=" + num)
						.asString();
				if (addVipJson.getStatus() == 200) {
					Map<String, Object> parse = (Map<String, Object>) JSONObject.parse(addVipJson.getBody().toString());

					if ("200".equals(parse.get("code").toString())) {

						ConfigGiftModel giftInfo = GiftInfoDao.getInstance().getGiftInfo(gid);
						// 添加成功
						String current_version = "{uid:" + uid + ",gid:" + gid + " gname:" + giftInfo.getGname()
								+ ",count:" + num + ",bak:" + strDescrip + "}";
						OperateDao.getInstance().AddOperationLog("user_item", "0", "添加背包礼物", 1, "", current_version,
								adminUser.getUid());

						map.put("success", 200);
						map.put("msg", "成功");
					} else {
						// 添加失败
						map.put("success", 203);
						map.put("msg", parse.get("message"));
					}
				} else {
					map.put("success", 205);
					map.put("msg", "服务器不稳定205");
				}
			}
		} catch (Exception e) {

			System.out.println("addUserGift-excep:" + e.getMessage());

			map.put("success", 204);
			map.put("msg", e.getMessage());
		}
		return map;
	}

	/**
	 * 获取背包里的礼物列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getUsedGiftList")
	@ResponseBody
	public Map<String, Object> getUsedGiftList(HttpServletRequest request, HttpServletResponse response) {

		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		if (adminUser == null) {
			return null;
		}

		String strUid = request.getParameter("uid");

		int uid = StringUtils.isEmpty(strUid) ? 0 : Integer.valueOf(strUid);

		int ipage = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
		int irows = request.getParameter("rows") == null ? 20 : Integer.valueOf(request.getParameter("rows"));
		if (ipage == 0) {
			ipage = 1;
		}

		return giftService.getUsedGiftList(uid, ipage, irows);
	}

	/**
	 * 获取背包里的礼物列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getUsedBadgeList")
	@ResponseBody
	public Map<String, Object> getUsedBadgeList(HttpServletRequest request, HttpServletResponse response) {

		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		if (adminUser == null) {
			return null;
		}

		String strUid = request.getParameter("uid");

		int uid = StringUtils.isEmpty(strUid) ? 0 : Integer.valueOf(strUid);

		int ipage = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
		int irows = request.getParameter("rows") == null ? 20 : Integer.valueOf(request.getParameter("rows"));
		if (ipage == 0) {
			ipage = 1;
		}

		return giftService.getUsedBadgeList(uid, ipage, irows);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/addUserBadge", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addUserBadge(HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> map = new HashMap<String, Object>();
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		if (adminUser == null) {
			map.put("success", 201);
			map.put("msg", "请先登录");
			return map;
		}

		String strUid = request.getParameter("uid");
		String strGid = request.getParameter("gid");
		String strNum = request.getParameter("num");
		String strSTime = request.getParameter("startdate");
		String strDescrip = request.getParameter("descrip");

		try {
			int uid = StringUtils.isEmpty(strUid) ? 0 : Integer.valueOf(strUid);
			int gid = StringUtils.isEmpty(strGid) ? 0 : Integer.valueOf(strGid);
			int days = StringUtils.isEmpty(strNum) ? 0 : Integer.valueOf(strNum);

			if (uid <= 0 || gid <= 0 || days <= 0 || days > 365 || StringUtils.isEmpty(strDescrip)
					|| StringUtils.isEmpty(strSTime)) {
				map.put("success", 202);
				map.put("msg", "参数不正确202");
			} else {

				Long stime = DateUtil.dateToLong(strSTime, "yyyy-MM-dd HH:mm:ss", "other", 0);

				int addUserBadge = giftService.addUserBadge(uid, gid, 1, stime, days, strDescrip.trim(),
						adminUser.getUid());
				if (addUserBadge > 0) {
					HttpResponse<String> addVipJson = Unirest
							.get(Constant.business_server_url + "/admin/updateGift?adminid=admin").asString();
					if (addVipJson.getStatus() == 200) {
						Map<String, Object> parse = (Map<String, Object>) JSONObject
								.parse(addVipJson.getBody().toString());

						if ("200".equals(parse.get("code").toString())) {
							map.put("success", 200);
							map.put("msg", "成功");
						} else {
							// 添加失败
							map.put("success", 203);
							map.put("msg", parse.get("message"));
						}
					} else {
						map.put("success", 205);
						map.put("msg", "服务器不稳定205");
					}
				} else {
					map.put("success", 206);
					map.put("msg", "添加失败206");
				}
			}
		} catch (Exception e) {
			System.out.println("addUserBadge-excep:" + e.getMessage());
			map.put("success", 204);
			map.put("msg", e.getMessage());
		}
		return map;
	}
}
