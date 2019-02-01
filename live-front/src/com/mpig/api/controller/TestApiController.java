package com.mpig.api.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import redis.clients.jedis.Tuple;

import com.alibaba.fastjson.JSONObject;
import com.mpig.api.db.DBHelper;
import com.mpig.api.db.DataSource;
import com.mpig.api.dictionary.lib.LivingListConfigLib;
import com.mpig.api.model.ReturnModel;
import com.mpig.api.model.UserAssetModel;
import com.mpig.api.model.UserVipInfoModel;
import com.mpig.api.redis.service.UserRedisService;
import com.mpig.api.service.IUserService;
import com.mpig.api.utils.DateUtils;
import com.mpig.api.utils.RedisContant;
import com.mpig.api.utils.VarConfigUtils;

@Controller
@Scope("prototype")
@RequestMapping("/data")
public class TestApiController extends BaseController{
	
	@Resource
	private IUserService userService;
	
	/**
	 * reload 
	 */
	@RequestMapping(value = "/reloadUserRank")
	@ResponseBody
	public ReturnModel reloadUserRank(HttpServletRequest req, HttpServletResponse resp){
//		Set<Tuple> Rank = UserRedisService.getInstance().zrevrangeWithScores("yuanlin", 0l, -1l);
//		for (Tuple tuple : Rank) {
//			Double score = tuple.getScore();
//			Double wealths = score*100;
//			System.out.println(wealths +"  ===   "+wealths.longValue());
//			UserRedisService.getInstance().zadd("yuanlin", wealths, tuple.getElement());
//		}
//		//day
//		String day = DateUtils.dateToString(null, "yyyyMMdd");
//		Set<Tuple> dayUserRank = UserRedisService.getInstance().zrevrangeWithScores(RedisContant.userDay + day, 0l, -1l);
//		for (Tuple tuple : dayUserRank) {
//			Double score = tuple.getScore();
//			Double wealths = score/100;
//			System.out.println(wealths +"  ===   "+wealths.longValue());
//			UserRedisService.getInstance().zadd(RedisContant.userDay + day, wealths, tuple.getElement());
//		}
//		
//		//week
//		String week = DateUtils.getWeekStart(0);
//		Set<Tuple> weekUserRank = UserRedisService.getInstance().zrevrangeWithScores(RedisContant.userWeek + week, 0l, -1l);
//		for (Tuple tuple : weekUserRank) {
//			double score = tuple.getScore();
//			double wealths = score/100;
//			System.out.println(wealths);
//			UserRedisService.getInstance().zadd(RedisContant.userWeek + week, wealths, tuple.getElement());
//		}
//		
//		//month
//		String month = DateUtils.getTimesMonthmorning("yyyyMM");
//		Set<Tuple> monthUserRank = UserRedisService.getInstance().zrevrangeWithScores(RedisContant.userMonth + month, 0l, -1l);
//		for (Tuple tuple : monthUserRank) {
//			double score = tuple.getScore();
//			double wealths = score/100;
//			System.out.println(wealths);
//			UserRedisService.getInstance().zadd(RedisContant.userMonth + month, wealths, tuple.getElement());
//		}
		
		//all
		Set<Tuple> allUserRank = UserRedisService.getInstance().zrevrangeWithScores(RedisContant.userAll, 0l, -1l);
		for (Tuple tuple : allUserRank) {
			int uid = new Integer(tuple.getElement());
			UserAssetModel assetModel = userService.getUserAssetByUid(uid, true);
			if(assetModel != null){
				System.out.println(assetModel.getWealth());
				UserRedisService.getInstance().zadd(RedisContant.userAll, Double.valueOf(assetModel.getWealth()/100), uid+"");
			}
		}
		
		//all
//		Set<Tuple> allUserRank = UserRedisService.getInstance().zrevrangeWithScores(RedisContant.anchorAll, 0l, -1l);
//		for (Tuple tuple : allUserRank) {
//			int uid = new Integer(tuple.getElement());
//			UserAssetModel assetModel = userService.getUserAssetByUid(uid, false);
//			if(assetModel != null){
//				System.out.println(assetModel.getWealth());
//				UserRedisService.getInstance().zadd(RedisContant.anchorAll, Double.valueOf(assetModel.getCreditTotal()), uid+"");
//			}
//		}
		
		return returnModel;
	}
	
	@RequestMapping(value = "/reloadUserVip")
	@ResponseBody
	public ReturnModel selUserAllGardInfo() {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		UserVipInfoModel userVipInfoModel = null;
		try {
			if (userVipInfoModel == null) {
				// 获取用户基本信息
				conn = DataSource.instance.getPool(VarConfigUtils.dbZhuUser).getConnection();
				statement = conn.prepareStatement("select * from user_vip_info where isdel = 0 and endtime >= UNIX_TIMESTAMP(NOW())");
				DBHelper.setPreparedStatementParam(statement);
				rs = statement.executeQuery();

				if (rs != null) {
					while (rs.next()) {
						userVipInfoModel = new UserVipInfoModel().populateFromResultSet(rs);
						String userVipKey = RedisContant.userVip+userVipInfoModel.getUid();
						UserRedisService.getInstance().set(userVipKey, JSONObject.toJSONString(userVipInfoModel));
						long nowtime = System.currentTimeMillis() / 1000;
						int endtime = userVipInfoModel.getEndtime();
						if (nowtime <= endtime) {
							long expiretime = endtime - nowtime;
							UserRedisService.getInstance().expire(userVipKey, (int) expiretime);
						}
					}
				}
			}
		} catch (SQLException e) {
			returnModel.setCode(500);
			returnModel.setMessage("处理出错");
		} catch (Exception e) {
			returnModel.setCode(500);
			returnModel.setMessage("处理出错");
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				
			}
		}
		return returnModel;
	}
	
	@RequestMapping(value="/ttttt")
	@ResponseBody
	public ReturnModel  getLivingList(){
		List<Map<String, Object>> livingList = LivingListConfigLib.getLivingList(1, 1, 0);
		 returnModel.setData(livingList.size());
		 return returnModel;
	}
}
