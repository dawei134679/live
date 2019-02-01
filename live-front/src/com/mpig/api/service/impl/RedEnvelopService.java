package com.mpig.api.service.impl;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mpig.api.dictionary.GlobalConfig;
import com.mpig.api.dictionary.lib.UrlConfigLib;
import com.mpig.api.modelcomet.CModProtocol;
import com.mpig.api.redis.core.RedisBucket;
import com.mpig.api.redis.service.RedisCommService;
import com.mpig.api.utils.EncryptUtils;
import com.mpig.api.utils.RedEnvelopAlgorithm;
import com.mpig.api.utils.RedisContant;
import com.mpig.api.utils.VarConfigUtils;

/**
 * RedEnvelopService
 * 
 * @author jackzhang
 */
public class RedEnvelopService {
	private static final Logger logger = Logger.getLogger(RedEnvelopService.class);
	static int threadCount = 1;

	private final static String RED_ENVELOP_LIST_RKEY_PREFIX = "re:%d:%d:all";
	// 后台也在用，修改时要通知一下
	private final static String RED_ENVELOP_TAKED_LIST_RKEY_PREFIX = "re:%d:%d:taked_list";
	private final static String RED_ENVELOP_TAKED_MAP_RKEY_PREFIX = "re:%d:%d:taked_map";

	public static RedEnvelopService getInstance() {
		return RedEnvelopServiceHolder.instance;
	}

	private static class RedEnvelopServiceHolder {
		public static final RedEnvelopService instance = new RedEnvelopService();
	}

	/**
	 * 请设置为固定redis池 不要分片
	 */
	protected ShardedJedisPool jedisPool;

	public void setJedisPool(ShardedJedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	/**
	 * 生成红包数据
	 * 
	 * @param ownerId
	 *            发包者
	 * @param String
	 *            envelopId 红包编号
	 * @param total
	 *            总金额
	 * @param personCount
	 *            分数
	 * @param max
	 *            金额上限
	 * @param min
	 *            金额下限
	 * @param scope
	 *            1 主播 2 指定用户 3 房间内所有人
	 * @param roomOwner
	 *            房间编号
	 * @param receiver
	 *            接受者编号
	 * @param nick
	 *            昵称
	 * @param avatar
	 *            头像
	 * @param bless
	 *            祝福语
	 * @throws InterruptedException
	 */
	@SuppressWarnings("deprecation")
	public synchronized void generateRedEnvelopData(int ownerId, int envelopId, long total, final int personCount,
			long max, long min, String nick, int level, String avatar, String bless, int scope, int roomOwner,
			int receiver) {
		ShardedJedis jedis = null;
		try {
			if (total <= 0 || total < personCount || min <= 0 || max > total) {
				throw new RuntimeException("args invalid");
			}
			final long[] generate = RedEnvelopAlgorithm.generate(total, personCount, max, min);
			long t2 = System.currentTimeMillis();
			System.err.println(String.format("generate data count %d", generate.length));

			final String redEnvelopList = String.format(RED_ENVELOP_LIST_RKEY_PREFIX, ownerId, envelopId);
			jedis = jedisPool.getResource();

			boolean byPipeLined = GlobalConfig.getInstance().isByPipelined();
			if (byPipeLined) {
				final redis.clients.jedis.ShardedJedisPipeline p = jedis.pipelined();
				for (int i = 0; i < threadCount; ++i) {
					final int temp = i;
					// Thread thread = new Thread() {
					// public void run() {
					int per = personCount / threadCount;
					JSONObject object = new JSONObject();
					int count = (temp + 1) * per;
					for (int j = temp * per; j < count; j++) {
						object.put("id", j);
						object.put("money", generate[j]);
						p.lpush(redEnvelopList, object.toJSONString());
					}
					p.sync();
				}
				// };
				// thread.start();
			} else {
				for (int i = 0; i < threadCount; ++i) {
					final int temp = i;
					// Thread thread = new Thread() {
					// public void run() {
					int per = personCount / threadCount;
					JSONObject object = new JSONObject();
					int count = (temp + 1) * per;
					for (int j = temp * per; j < count; j++) {
						object.put("id", j);
						object.put("money", generate[j]);
						jedis.lpush(redEnvelopList, object.toJSONString());
					}
				}
				// };
				// thread.start();
			}

			long t3 = System.currentTimeMillis();
			System.err.println(jedis.llen(redEnvelopList) + " gen red envelop cost time>>>" + (t3 - t2));
		} catch (Exception ex) {
			System.err.println(String.format(
					"generateRedEnvelopData ex:>>>%s num idle %d ,num avtive %d,waiters num %d", ex.toString(),
					jedisPool.getNumIdle(), jedisPool.getNumActive(), jedisPool.getNumWaiters()));
			throw ex;
		} finally {
			jedis.close();
			System.err.println(String.format("finally num idle %d ,num avtive %d,waiters num %d",
					jedisPool.getNumIdle(), jedisPool.getNumActive(), jedisPool.getNumWaiters()));
			System.err.println("jedis.close()");
		}

		final JSONObject msgBody = new JSONObject();
		msgBody.put("owner", ownerId);
		msgBody.put("redEnvelopId", envelopId);
		msgBody.put("nick", nick);
		msgBody.put("avatar", avatar);
		msgBody.put("bless", bless);
		msgBody.put("type", scope);

		if (scope == 3) {
			msgBody.put("cometProtocol", CModProtocol.REDENVELOP_GENERATE_FOR_ROOM_ALL);
			final String url = UrlConfigLib.getUrl("url").getAdminrpc_publish_room();

			String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
					"appKey=" + VarConfigUtils.ServiceKey, "msgBody=" + msgBody.toString(),
					"roomOwner=" + roomOwner + "");

			Unirest.post(url).field("appKey", VarConfigUtils.ServiceKey).field("msgBody", msgBody.toString())
					.field("sign", signParams).field("roomOwner", roomOwner + "").asJsonAsync(new Callback<JsonNode>() {
						public void completed(HttpResponse<JsonNode> arg0) {
							System.out
									.println(String.format("send redEnvelop url=%s body=%s", url, msgBody.toString()));
						}

						@Override
						public void failed(UnirestException arg0) {
							logger.error("generateRedEnvelopData for room failed cause:" + arg0);
						}

						@Override
						public void cancelled() {
							logger.error("generateRedEnvelopData for room cancelled");
						}
					});
		} else {
			msgBody.put("cometProtocol", CModProtocol.REDENVELOP_GENERATE_FOR_ANCHOR);
			final String url = UrlConfigLib.getUrl("url").getAdminrpc_publish_live();

			String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
					"appKey=" + VarConfigUtils.ServiceKey, "msgBody=" + JSONObject.toJSONString(msgBody),
					"users=" + receiver + "");

			Unirest.post(url).field("appKey", VarConfigUtils.ServiceKey)
					.field("msgBody", JSONObject.toJSONString(msgBody)).field("users", receiver + "")
					.field("sign", signParams).asJsonAsync(new Callback<JsonNode>() {
						public void completed(HttpResponse<JsonNode> arg0) {
							System.out
									.println(String.format("send redEnvelop url=%s body=%s", url, msgBody.toString()));
						}

						@Override
						public void failed(UnirestException arg0) {
							logger.error("generateRedEnvelopData for anchor failed cause:" + arg0);
						}

						@Override
						public void cancelled() {
							logger.error("generateRedEnvelopData for anchor cancelled");
						}
					});

			if (scope == 1) {
				// 炫耀一下
				// msgBody.remove("redEnvelopId");
				// msgBody.remove("avatar");
				// msgBody.remove("bless");
				msgBody.put("level", level);
				msgBody.put("cometProtocol", CModProtocol.REDENVELOP_GENERATE_FOR_ANCHOR_NOTICE_ALL);

				signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
						"appKey=" + VarConfigUtils.ServiceKey, "msgBody=" + msgBody.toString(),
						"roomOwner=" + roomOwner + "");

				Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_room())
						.field("appKey", VarConfigUtils.ServiceKey).field("msgBody", msgBody.toString())
						.field("roomOwner", roomOwner + "").field("sign", signParams)
						.asJsonAsync(new Callback<JsonNode>() {
							public void completed(HttpResponse<JsonNode> arg0) {
								System.out.println(String.format("send redEnvelop url=%s body=%s",
										UrlConfigLib.getUrl("url").getAdminrpc_publish_room(), msgBody.toString()));
							}

							@Override
							public void failed(UnirestException arg0) {
								logger.error("generateRedEnvelopData for notice all failed cause:" + arg0);
							}

							@Override
							public void cancelled() {
								logger.error("generateRedEnvelopData for notice all cancelled");
							}
						});
			}
		}
	}

	/**
	 * -- 函数：尝试获得红包，如果成功，则返回json字符串，如果不成功，则返回空 -- 参数：红包队列名， 已消费的队列名，去重的Map名，用户ID
	 * -- 返回值：nil 或者 json字符串，包含用户ID：userId，红包ID：id，红包金额：money
	 **/
	private static String tryGetRedEnvelopScript = "if redis.call('hexists', KEYS[3], KEYS[4]) ~= 0 then\n"
			+ "return nil\n" + "else\n" + "local redEnvelop = redis.call('rpop', KEYS[1]);\n" + "if redEnvelop then\n"
			+ "local x = cjson.decode(redEnvelop);\n" + "x['userId'] = KEYS[4];\n" + "local re = cjson.encode(x);\n"
			+ "redis.call('hset', KEYS[3], KEYS[4], KEYS[4]);\n" + "redis.call('lpush', KEYS[2], re);\n"
			+ "return re;\n" + "end\n" + "end\n" + "return nil";
	
	/**
	 * 红包抢完了，删除redis缓存
	 * @param owner
	 * @param envelopId
	 */
	public void delRedEnvelop(int owner,int envelopId){
		try {
			final String redEnvelopTakedList = String.format(RED_ENVELOP_TAKED_LIST_RKEY_PREFIX, owner, envelopId);
			final String redEnvelopTakedMap = String.format(RED_ENVELOP_TAKED_MAP_RKEY_PREFIX, owner, envelopId);
			
			RedisCommService.getInstance().del(RedisContant.RedisNameRedEnvelop, redEnvelopTakedList);
			RedisCommService.getInstance().del(RedisContant.RedisNameRedEnvelop, redEnvelopTakedMap);
		} catch (Exception e) {
			logger.debug("delRedEnvelop excep",e);
		}
	}

	/**
	 * 获取红包数据
	 * 
	 * @param owner
	 *            红包发放者
	 * @param clientId
	 *            红包领取者
	 * @param String
	 *            envelopId 红包编号
	 * @throws InterruptedException
	 */
	public Object tryGetRedEnvelop(int owner, int envelopId, int clientId) {
		ShardedJedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			final String redEnvelopList = String.format(RED_ENVELOP_LIST_RKEY_PREFIX, owner, envelopId);
			final String redEnvelopTakedList = String.format(RED_ENVELOP_TAKED_LIST_RKEY_PREFIX, owner, envelopId);
			final String redEnvelopTakedMap = String.format(RED_ENVELOP_TAKED_MAP_RKEY_PREFIX, owner, envelopId);
			Collection<Jedis> allShards = jedis.getAllShards();
			boolean hasEvel = false;
			for (Iterator iterator = allShards.iterator(); iterator.hasNext();) {
				if (!hasEvel) {
					Jedis jedisClient = (Jedis) iterator.next();
					hasEvel = true;
					Object redEnvelop = jedisClient.eval(tryGetRedEnvelopScript, 4, redEnvelopList, redEnvelopTakedList,
							redEnvelopTakedMap, clientId + "");
					System.err
							.println(String.format("tryGetRedEnvelop>>owner:%d,envelopId:%d,clientId:%d redEnvelop:%s",
									owner, envelopId, clientId, redEnvelop.toString()));
					return redEnvelop;
				}
			}
		} catch (Exception ex) {
			System.err.println(String.format(
					"tryGetRedEnvelop ex:>>>%s jedis is null(%s) num idle %d ,num avtive %d,waiters num %d",
					ex.toString(), jedis == null ? "y" : "n", jedisPool.getNumIdle(), jedisPool.getNumActive(),
					jedisPool.getNumWaiters()));
			throw ex;
		} finally {
			if (jedis != null) {
				jedis.close();
				System.err.println(String.format("tryGetRedEnvelop finally num idle %d ,num avtive %d,waiters num %d",
						jedisPool.getNumIdle(), jedisPool.getNumActive(), jedisPool.getNumWaiters()));
			}
		}

		return null;
	}

	public static void main(String[] args) throws InterruptedException {
		String rootPrefix = RedEnvelopService.class.getResource("/").getPath();
		rootPrefix = rootPrefix.substring(0, rootPrefix.lastIndexOf("classes"));
		RedisBucket.getInstance().initialize(rootPrefix + "redis.json");

		RedEnvelopService instance = RedEnvelopService.getInstance();
		ShardedJedisPool redEnvelopPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameRedEnvelop);
		instance.setJedisPool(redEnvelopPool);

		ShardedJedis resource = redEnvelopPool.getResource();
		Jedis shard = resource.getShard("1");
		Collection<Jedis> allShards = resource.getAllShards();
		for (Iterator iterator = allShards.iterator(); iterator.hasNext();) {
			Jedis jedis = (Jedis) iterator.next();
		}

		int owner = 10000001;
		int envelopId = 3;
		String nick = "nick";
		String avatar = "avatar";
		String bless = "bless";
		int roomOwner = 10000001;
		int level = 10;
		long t1 = System.currentTimeMillis();
		// for(int i = 0;i<20;i++){
		instance.generateRedEnvelopData(owner, envelopId, 45, 2, 45, 1, nick, level, avatar, bless, 3, roomOwner, 11);
		// }

		Thread.sleep(2000);
		instance.generateRedEnvelopData(owner, envelopId, 45, 2, 45, 1, nick, level, avatar, bless, 3, roomOwner, 11);

		Thread.sleep(2000);
		instance.generateRedEnvelopData(owner, envelopId, 45, 2, 45, 1, nick, level, avatar, bless, 3, roomOwner, 11);

		long t2 = System.currentTimeMillis();
		System.err.println("cost time>>>" + (t2 - t1));

		int clientId = 10000001;
		Object tryGetRedEnvelop = instance.tryGetRedEnvelop(owner, envelopId, clientId);
		System.out.println(tryGetRedEnvelop);
	}
}
