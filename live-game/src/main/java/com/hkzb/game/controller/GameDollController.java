package com.hkzb.game.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hkzb.game.common.utils.LogUtils;
import com.hkzb.game.common.utils.ResultUtil;
import com.hkzb.game.common.utils.UserUtil;
import com.hkzb.game.dto.GameDollConfigDto;
import com.hkzb.game.dto.ResultDto;
import com.hkzb.game.model.GameGraspDollRecord;
import com.hkzb.game.service.IGameDollService;

@Controller
@RequestMapping("/gameDoll")
public class GameDollController {

	@Autowired
	private IGameDollService gameDollService;

	@RequestMapping(value = "/getGameDollConfig", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResultDto<List<GameDollConfigDto>> getGameDollConfig() {
		try {
			return gameDollService.getGameDollConfig();
		} catch (Exception e) {
			LogUtils.error(getClass(), "获取房间游戏【抓娃娃】信息失败", e);
			return ResultUtil.error("获取房间游戏信息失败");
		}
	}

	@RequestMapping(value = "/getGameDollMoney", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ResultDto<Map<String, Object>> getGameDollMoney() {
		try {
			return gameDollService.getGameDollMoney();
		} catch (Exception e) {
			LogUtils.error(getClass(), "获取抓娃娃游戏爪子信息失败", e);
			return ResultUtil.error("获取抓娃娃游戏爪子信息失败");
		}
	}

	/**
	 * 抓到娃娃调用
	 */
	@RequestMapping(value = "/grab", method = RequestMethod.POST)
	@ResponseBody
	public ResultDto<Object> grab(HttpServletRequest request, GameGraspDollRecord gameGraspDollRecord) {
		try {
			gameGraspDollRecord.setUid(Long.valueOf(UserUtil.getUid(request)));
			return gameDollService.grab(gameGraspDollRecord);
		} catch (Exception e) {
			LogUtils.error(getClass(), "抓娃娃异常", e);
			return ResultUtil.error("抓娃娃失败");
		}
	}

	/**
	 * 推送中奖消息
	 */
	@RequestMapping(value = "/pushDollMsg", method = RequestMethod.POST)
	@ResponseBody
	public ResultDto<Object> pushDollMsg(HttpServletRequest request, Long recordId) {
		try {
			return gameDollService.pushGameDollMsg(recordId);
		} catch (Exception e) {
			LogUtils.error(getClass(), "推送中奖消息异常", e);
			return ResultUtil.error("推送中奖消息失败");
		}
	}

}
