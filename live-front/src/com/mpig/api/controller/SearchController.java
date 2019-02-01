package com.mpig.api.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mpig.api.model.ReturnModel;
import com.mpig.api.model.SearchModel;
import com.mpig.api.service.IAuthService;
import com.mpig.api.service.ISearchService;
import com.mpig.api.utils.CodeContant;

@Controller
@Scope("prototype")
@RequestMapping("/search")
public class SearchController extends BaseController {

	@Resource
	private IAuthService authService;

	@Resource
	private ISearchService searchService;

	/**
	 * 全体用户中搜索
	 * 
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public void searchAll(HttpServletRequest req, final HttpServletResponse resp) {

		authToken(req, false);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}

		// nickname 昵称
		String searchData = req.getParameter("searchData");
		if (null == searchData || searchData.isEmpty()) {
			returnModel.setCode(CodeContant.SEARCH_FAILED);
			returnModel.setMessage("没有参数");
			writeJson(resp, returnModel);
			return;
		}
		// map uid --

		String token = req.getParameter("token");// 充值操作对象
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		List<SearchModel> rtData = searchService.searchUserInfo(searchData, uid);
		if (null == rtData) {
			returnModel.setCode(CodeContant.SEARCH_FAILED);
			returnModel.setMessage("搜索失败");
			writeJson(resp, returnModel);
			return;
		}
		returnModel.setCode(CodeContant.OK);
		returnModel.setMessage("搜索成功");
		Map<String, Object> mapdata = new HashMap<String, Object>();
		mapdata.put("Return", rtData);
		returnModel.setData(mapdata);
		writeJson(resp, returnModel);
	}
	
	/**
	 * 全体用户中搜索
	 * 
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/all/pc", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel searchAllForPc(HttpServletRequest req, final HttpServletResponse resp) {
 		String searchData = req.getParameter("searchData");
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<Map<String, Object>> recommend = new ArrayList<Map<String, Object>>();
		List<SearchModel> rtData = searchService.searchUserInfoForPc(searchData, 0);
		if (rtData.size() ==0) {
			recommend = searchService.getSearchRecommand();
		}
		dataMap.put("searchData", rtData);
		dataMap.put("recommendData", recommend);
		returnModel.setCode(CodeContant.OK);
		returnModel.setMessage("搜索成功");
		returnModel.setData(dataMap);
		return returnModel;
	}
}
