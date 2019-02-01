package com.tinypig.newadmin.web.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tinypig.admin.TimerTask.TimeService;
import com.tinypig.newadmin.web.service.IGameService;

@Controller
@RequestMapping("/game")
public class GameAction {
	
	@Autowired
	private IGameService gameService;
	
	@RequestMapping(value = "/betwinlist")
	@ResponseBody
	public Map<String, Object> getBetWinlist(HttpServletRequest request,HttpServletResponse response){
		
		int uid = StringUtils.isEmpty(request.getParameter("uid"))?0:Integer.valueOf(request.getParameter("uid"));
		String stime = request.getParameter("s_time");
		String etime = request.getParameter("e_time");

		if (stime == null || etime == null) {
			return null;
		}

		Map<String, Object> map = new HashMap<String,Object>();
		int bets = 0;
		
		int page = Integer.valueOf(request.getParameter("page"));
		int size = Integer.valueOf(request.getParameter("rows"));

		map = gameService.getBetWinList(uid, stime, etime,page,size);
		
		bets = gameService.getBetWins(uid, stime, etime);
		
		map.put("total", bets);
		return map;
	}
	

	@RequestMapping(value = "/nidbetwinlist")
	@ResponseBody
	public HashMap<String, Object> getNidBetWin(HttpServletRequest request,HttpServletResponse response){

		int nid = StringUtils.isEmpty(request.getParameter("nid"))?0:Integer.valueOf(request.getParameter("nid"));
		String stime = request.getParameter("s_time");
		String etime = request.getParameter("e_time");

		List<Map<String, Object>> betList = new ArrayList<Map<String,Object>>();
		int bets = 0;

		betList = gameService.getNidBetWinlist(nid, stime, etime);
		
		bets = gameService.getNidBetWins(nid, stime, etime);
		
		HashMap<String, Object> map = new HashMap<String,Object>();
		map.put("rows", betList);
		map.put("total", bets);
		return map;
	}
}
