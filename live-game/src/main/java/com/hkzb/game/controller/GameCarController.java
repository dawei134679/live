package com.hkzb.game.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hkzb.game.common.utils.Constants;
import com.hkzb.game.common.utils.LogUtils;
import com.hkzb.game.common.utils.ResultUtil;
import com.hkzb.game.common.utils.UserUtil;
import com.hkzb.game.dto.GameCarConfigResultDto;
import com.hkzb.game.dto.GameCarStakeRecordDto;
import com.hkzb.game.dto.LotteryRecordParamDto;
import com.hkzb.game.dto.ResultDto;
import com.hkzb.game.dto.StakeRecordParamDto;
import com.hkzb.game.dto.WinningResultDto;
import com.hkzb.game.model.GameCarRecord;
import com.hkzb.game.model.GameCarStakeRecord;
import com.hkzb.game.service.IGameCarService;

@Controller
@RequestMapping("/gameCar")
public class GameCarController {

	@Autowired
	private IGameCarService gameCarService;

	@RequestMapping(value = "/getGameCarConfig", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResultDto<List<GameCarConfigResultDto>> getGameCarConfig() {
		try {
			return gameCarService.getGameCarConfig();
		} catch (Exception e) {
			LogUtils.error(getClass(), "获取房间游戏信息异常", e);
			return ResultUtil.error("获取房间游戏信息失败");
		}
	}

	@RequestMapping(value = "/getGameCarTime", method = RequestMethod.GET)
	@ResponseBody
	public ResultDto<Map<String,Object>> getGameCarTime(HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			ResultDto<Map<String,Long>> rd = gameCarService.getGameCarTime();
			if(!ResultUtil.SUCCESS_CODE.equals(rd.getCode())) {
				return ResultUtil.fail(rd.getCode(),rd.getMsg());
			}
			Map<String,Long> map = rd.getData();
			Long time = map.get("time")+2;
			if(Constants.time>=time) {
				map.put("time", time);
			}
			resultMap.putAll(rd.getData());

			String first = request.getParameter("first");
			if("1".equals(first)) {
				resultMap.putAll(gameCarService.getUserStakeAndTotalStake(Long.valueOf(UserUtil.getUid(request))));
			}
			return ResultUtil.success(resultMap);
		} catch (Exception e) {
			LogUtils.error(getClass(), "获取房间游戏信息异常", e);
			return ResultUtil.error("获取房间游戏信息失败");
		}
	}
	
	@RequestMapping(value = "/stake", method = RequestMethod.POST)
	@ResponseBody
	public ResultDto<Object> stake(HttpServletRequest request, GameCarStakeRecord param) {
		try {
			param.setUid(Long.valueOf(UserUtil.getUid(request)));
			return gameCarService.stake(param);
		} catch (Exception e) {
			LogUtils.error(getClass(), "押注异常", e);
			return ResultUtil.error("押注失败");
		}
	}
	
	@RequestMapping(value = "/getLotteryByPeriods", method = RequestMethod.GET)
	@ResponseBody
	public ResultDto<WinningResultDto> getLotteryByPeriods(HttpServletRequest request,Long periods) {
		try {
			return gameCarService.getLotteryByPeriods(periods,Long.valueOf(UserUtil.getUid(request)));
		} catch (Exception e) {
			LogUtils.error(getClass(), "获取开奖结果异常", e);
			return ResultUtil.error("获取开奖结果失败");
		}
	}

	@RequestMapping(value = "/getStakeRecord", method = RequestMethod.POST)
	@ResponseBody
	public ResultDto<List<GameCarStakeRecordDto>> getStakeRecord(HttpServletRequest request, StakeRecordParamDto param) {
		try {
			param.setUid(Long.valueOf(UserUtil.getUid(request)));
			return gameCarService.getStakeRecord(param);
		} catch (Exception e) {
			LogUtils.error(getClass(), "获取押注记录异常", e);
			return ResultUtil.error("获取押注记录失败");
		}
	}

	@RequestMapping(value = "/getLotteryRecord", method = RequestMethod.POST)
	@ResponseBody
	public ResultDto<List<GameCarRecord>> getLotteryRecord(LotteryRecordParamDto param) {
		try {
			return gameCarService.getLotteryRecord(param);
		} catch (Exception e) {
			LogUtils.error(getClass(), "获取开奖记录异常", e);
			return ResultUtil.error("获取开奖记录失败");
		}
	}
}
