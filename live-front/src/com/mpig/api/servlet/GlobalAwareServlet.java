package com.mpig.api.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mashape.unirest.http.Unirest;
import com.mpig.api.async.AsyncManager;
import com.mpig.api.async.NotificationEngine;
import com.mpig.api.async.task.PKResultTask;
import com.mpig.api.db.DataSource;
import com.mpig.api.dictionary.GlobalConfig;
import com.mpig.api.dictionary.lib.ActivityConfigLib;
import com.mpig.api.dictionary.lib.BaseConfigLib;
import com.mpig.api.dictionary.lib.ClusterConfigLib;
import com.mpig.api.dictionary.lib.ExchangeConfigLib;
import com.mpig.api.dictionary.lib.GameAppConfigLib;
import com.mpig.api.dictionary.lib.GameBetConfigLib;
import com.mpig.api.dictionary.lib.GiftPromotionConfigLib;
import com.mpig.api.dictionary.lib.InviteRewardsConfigLib;
import com.mpig.api.dictionary.lib.LevelConfigLib;
import com.mpig.api.dictionary.lib.LevelsConfigLib;
import com.mpig.api.dictionary.lib.PayConfigLib;
import com.mpig.api.dictionary.lib.PushConfigLib;
import com.mpig.api.dictionary.lib.SignConfigLib;
import com.mpig.api.dictionary.lib.TaskConfigLib;
import com.mpig.api.dictionary.lib.UrlConfigLib;
import com.mpig.api.dictionary.lib.ValueaddServiceConfigLib;
import com.mpig.api.dictionary.lib.VersionConfigLib;
import com.mpig.api.dictionary.lib.VideoLineConfigLib;
import com.mpig.api.redis.core.RedisBucket;
import com.mpig.api.service.impl.ConfigServiceImpl;
import com.mpig.api.service.impl.RedEnvelopService;
import com.mpig.api.service.impl.SearchServiceImpl;
import com.mpig.api.service.impl.SensitiveWordsService;
import com.mpig.api.service.impl.WebServiceImpl;
import com.mpig.api.service.impl.WeixinServiceImpl;
import com.mpig.api.utils.Constant;
import com.mpig.api.utils.RedisContant;

import redis.clients.jedis.ShardedJedisPool;

/**
 * Servlet implementation class GlobalAware
 */
@WebServlet("/GlobalAwareServlet")
public class GlobalAwareServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	@SuppressWarnings("deprecation")
	public GlobalAwareServlet() {
		super();

		System.gc();

		// 准备环境路径
		String rootPrefix = this.getClass().getResource("/").getPath();
		rootPrefix = rootPrefix.substring(0, rootPrefix.lastIndexOf("classes"));

		// 装载全局配置文件
		GlobalConfig.getInstance().loadFromFile(rootPrefix + "global.properties");

		GameBetConfigLib.read(rootPrefix + "game_config.json");

		VideoLineConfigLib.read(rootPrefix + "domain.json");
		LevelsConfigLib.read(rootPrefix + "levels.json");
		UrlConfigLib.read(rootPrefix + "urlConfig.json");

		ExchangeConfigLib.read(rootPrefix + "exchange.properties");

		ClusterConfigLib.read(rootPrefix + "es.properties");
		PayConfigLib.read(rootPrefix + "payConfig.properties");
		WeixinServiceImpl.buildSSLClient();
		PushConfigLib.read(rootPrefix);

		// 构造redis连接池
		RedisBucket.getInstance().initialize(rootPrefix + "redis.json");

		// 构造datasource连接池
		DataSource.read(rootPrefix + "db.properties", rootPrefix + "db.json");

		Constant.initialize(rootPrefix + "system.properties");

		// 启动推送引擎
		NotificationEngine.getInstance().start(true);

		// 加载敏感词
		ShardedJedisPool pool = RedisBucket.getInstance().getShardPool(RedisContant.RedisBlackWords);
		SensitiveWordsService.getInstance().setJedisPool(pool);
		SensitiveWordsService.getInstance().loadWordsConfig();

		// 红包数据池
		// JedisPool redEnvelopPool =
		// RedisBucket.getInstance().getNoShardPool(RedisContant.RedisNameRedEnvelop);
		ShardedJedisPool redEnvelopPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameRedEnvelop);
		RedEnvelopService.getInstance().setJedisPool(redEnvelopPool);

		// 设置异步http全局设置
		Unirest.setConcurrency(3000, 2000);
		Unirest.setTimeouts(5000, 15000);

		// 链接全文搜索引擎
		// if (ClusterConfigLib.getUrl().getEsEnable()) {
		SearchServiceImpl.initWithHostAndPort(ClusterConfigLib.getUrl().getEsHost(),
				ClusterConfigLib.getUrl().getEsPort());
		// }

		// 更新礼物列表到内存
		ConfigServiceImpl.updateGiftListNew();// 新礼物
		ConfigServiceImpl.updateGiftListAct();// 活动礼物列表

		ConfigServiceImpl.updateThirdStream();
		// 加载客户端基础配置到内存
		BaseConfigLib.updateOpenScreen();
		BaseConfigLib.updateLiveNotice();
		BaseConfigLib.updateProbabilitys();
		// 加载任务数据到内存
		TaskConfigLib.getInstance().loadTaskConfigFromDb();

		// 加载等级数据到内存
		LevelConfigLib.loadLevelConfigFromDb();
		// 加载守护等级到内存
		LevelConfigLib.loadValueaddLevelConfigFromDb();
		// 加载签到数据到内存
		SignConfigLib.loadSignConfigFromDb();

		// 版本配置
		VersionConfigLib.readFromRedis();

		// 首充活动配置
		ActivityConfigLib.readFirstPay("");
		// 充值抽奖活动
		ActivityConfigLib.readRechargeLotteryConfig();
		
		// 加载邀请任务的任务列表及奖励列表
		InviteRewardsConfigLib.loadInvitationConfig();
		
		// 轮播图列表 不用暂时注册掉
//		GameAppConfigLib.loadLevelConfigFromDb();
		
		ValueaddServiceConfigLib.getList();
		// 获取促销活动配置
		GiftPromotionConfigLib.loadGiftPromotionConfigFromDb();

		// 加载扶持号相关配置
		GiftPromotionConfigLib.loadSupportForRedis();
		// 轮播图列表
		WebServiceImpl.updateBannerlist();

		// 推送模板池
		// PushTempeleConfigLib.readFromRedis();

		// 启动异步管理器
		AsyncManager.getInstance().initWith(3);
		
		try{
			AsyncManager.getInstance().execute(new PKResultTask());
		}catch (Exception ex){
			System.out.println("启动PK开奖异步线程异常");
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	@Override
	public void destroy() {
		AsyncManager.getInstance().onDestroyed();
		try {
			Unirest.shutdown();
		} catch (IOException e) {
			e.printStackTrace();
		}

		RedisBucket.getInstance().destroy();
		DataSource.instance.destroy();
		SearchServiceImpl.destroy();
	}
}
