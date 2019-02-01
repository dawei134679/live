package com.mpig.api.service.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.mpig.api.db.DataSource;
import com.mpig.api.model.LiveMicTimeModel;
import com.mpig.api.model.SearchModel;
import com.mpig.api.model.UserBaseInfoModel;
import com.mpig.api.redis.service.OtherRedisService;
import com.mpig.api.redis.service.RelationRedisService;
import com.mpig.api.service.IConfigService;
import com.mpig.api.service.ILiveService;
import com.mpig.api.service.IRoomService;
import com.mpig.api.service.ISearchService;
import com.mpig.api.service.IUserService;
import com.mpig.api.utils.StringUtils;
import com.mpig.api.utils.VarConfigUtils;

@Service
public class SearchServiceImpl implements ISearchService {
	private static final Logger logger = Logger.getLogger(SearchServiceImpl.class);

	@Resource
	private IUserService userService;

	private static ISearchService instanceISearchService = new SearchServiceImpl();
	
	public static ISearchService getInstance(){
		return instanceISearchService;
	}

	static private Client client = null;

	static public synchronized void initWithHostAndPort(final String addr, final int port) {
		Settings settings = Settings.settingsBuilder().put("cluster.name", "elasticsearch").build();
		try {
			setClient(TransportClient.builder().settings(settings).build()
					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(addr), port)));
		} catch (UnknownHostException e) {
			e.printStackTrace();
			logger.error(String.format("ISearchService initWithHostAndPort{%s,%d} failed,cause %s", addr, port, e));
		}
	}

	static public synchronized void destroy() {
		if (null != client) {

			client.close();
			client = null;
		}
	}

	@Override
	public List<SearchModel> searchUserInfo(String searchData, int uid) {
		List<SearchModel> rt = new ArrayList<SearchModel>();
		if (getClient() == null) {
			return rt;
		}

		SearchRequestBuilder sb1 = null;
		SearchRequestBuilder sb2 = null;
		MultiSearchResponse sr = null;

		sb1 = getClient().prepareSearch("all").setTypes("user").setQuery(QueryBuilders.termQuery("numb", searchData));
		sb2 = getClient().prepareSearch("all").setTypes("user").setFrom(0).setSize(20)
				.setQuery(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("nickname", searchData))
						.mustNot(QueryBuilders.termQuery("numb", searchData)));

		sr = getClient().prepareMultiSearch().add(sb1).add(sb2).execute().actionGet();

		for (MultiSearchResponse.Item item : sr.getResponses()) {
			SearchResponse searchResponse = item.getResponse();
			if (null == searchResponse) {
				continue;
			}

			SearchHits searchHits = searchResponse.getHits();
			if (null == searchHits) {
				continue;
			}
			for (SearchHit searchHit : searchHits) {

				String strNumb = (String) searchHit.getSource().get("numb");
				String strNickname = (String) searchHit.getSource().get("nickname");
				if (org.apache.commons.lang.StringUtils.isNotEmpty(strNumb)&&org.apache.commons.lang.StringUtils.isNotEmpty(strNickname)&&false == strNumb.equalsIgnoreCase(searchData)
						&& false == strNickname.toLowerCase().contains(searchData.toLowerCase())) {
					continue;
				}

				SearchModel data = new SearchModel();
				Integer id = (Integer) searchHit.getSource().get("uid");
				if(id == null){
					continue;
				}
				UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(id, false);
				if (null == userBaseInfoModel) {
					continue;
				}
				data.setLiving(userBaseInfoModel.getLiveStatus());
				data.setVerified(userBaseInfoModel.isVerified());

				String numb = (String) searchHit.getSource().get("numb");
				String nickname = (String) searchHit.getSource().get("nickname");
				String avatar = (String) searchHit.getSource().get("avatar");
				String slogan = (String) searchHit.getSource().get("slogan");
				Boolean sex = (Boolean) searchHit.getSource().get("sex");
				data.setUid(id);
				data.setNumb(numb);
				data.setNickname(nickname);
				data.setAvatar(avatar);
				data.setSex(sex);
				data.setSlogan(slogan);
				data.setIsAttention(RelationRedisService.getInstance().isFan(uid, id));
				data.setUserLevel(userBaseInfoModel.getUserLevel());
				data.setAnchorLevel(userBaseInfoModel.getAnchorLevel());
				rt.add(data);
			}
		}
		return rt;
	}
	
	@Override
	public List<SearchModel> searchUserInfoForPc(String searchData, int uid) {
		List<SearchModel> rt = new ArrayList<SearchModel>();
		if (getClient() == null) {
			return rt;
		}

		SearchRequestBuilder sb1 = null;
		SearchRequestBuilder sb2 = null;
		MultiSearchResponse sr = null;

		sb1 = getClient().prepareSearch("all").setTypes("user").setQuery(QueryBuilders.termQuery("numb", searchData));
		sb2 = getClient().prepareSearch("all").setTypes("user").setFrom(0).setSize(20)
				.setQuery(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("nickname", searchData))
						.mustNot(QueryBuilders.termQuery("numb", searchData)));

		sr = getClient().prepareMultiSearch().add(sb1).add(sb2).execute().actionGet();

		for (MultiSearchResponse.Item item : sr.getResponses()) {
			SearchResponse searchResponse = item.getResponse();
			if (null == searchResponse) {
				continue;
			}

			SearchHits searchHits = searchResponse.getHits();
			if (null == searchHits) {
				continue;
			}
			for (SearchHit searchHit : searchHits) {

				String strNumb = (String) searchHit.getSource().get("numb");
				String strNickname = (String) searchHit.getSource().get("nickname");
				if (org.apache.commons.lang.StringUtils.isNotEmpty(strNumb)&&org.apache.commons.lang.StringUtils.isNotEmpty(strNickname)&&false == strNumb.equalsIgnoreCase(searchData)
						&& false == strNickname.toLowerCase().contains(searchData.toLowerCase())) {
					continue;
				}

				SearchModel data = new SearchModel();
				Integer id = (Integer) searchHit.getSource().get("uid");
				if(id == null){
					continue;
				}
				UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(id, false);
				if (null == userBaseInfoModel) {
					continue;
				}
				data.setLiving(userBaseInfoModel.getLiveStatus());
				data.setVerified(userBaseInfoModel.isVerified());

				String numb = (String) searchHit.getSource().get("numb");
				String nickname = (String) searchHit.getSource().get("nickname");
				String avatar = (String) searchHit.getSource().get("avatar");
				String slogan = (String) searchHit.getSource().get("slogan");
				Boolean sex = (Boolean) searchHit.getSource().get("sex");
				data.setUid(id);
				data.setNumb(numb);
				data.setNickname(nickname);
				data.setAvatar(avatar.replace("50x50", "250x250"));
				data.setSex(sex);
				data.setSlogan(slogan);
				data.setUserLevel(userBaseInfoModel.getUserLevel());
				data.setAnchorLevel(userBaseInfoModel.getAnchorLevel());
				data.setFansCount(RelationRedisService.getInstance().getFansTotal(id));
				long opentime = 0l;
				if(userBaseInfoModel.getOpentime() > 0){
					opentime = System.currentTimeMillis()/1000 - userBaseInfoModel.getOpentime();
				}
				data.setOpentime(opentime);
				rt.add(data);
			}
		}
		return rt;
	}

	/**
	 * 插入ES用户注册数据
	 */
	@Override
	public void insertUserInfo(SearchModel d) {
		if (getClient() == null || null == d) {
			return;
		}

		Map<String, Object> mapData = new HashMap<String, Object>();
		mapData.put("uid", d.getUid());
		mapData.put("numb", d.getNumb());
		mapData.put("sex", d.getSex());
		mapData.put("slogan", d.getSlogan());
		mapData.put("avatar", d.getAvatar());
		mapData.put("nickname", d.getNickname());

		IndexResponse indexResponse = getClient().prepareIndex("all", "user", d.getUid().toString()).setSource(mapData).execute().actionGet();
		if(!indexResponse.isCreated()){
			String str = JSONObject.toJSONString(mapData);
			logger.info("ES insertUserInfo failed : " + str);
		}

		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 批量插入ES用户注册数据
	 */
	@Override
	public void insertUserInfosAnsyc(List<SearchModel> data, boolean isSync) {
		if (getClient() == null || null == data) {
			return;
		}

		BulkRequestBuilder bulkRequest = getClient().prepareBulk();
		for (SearchModel d : data) {
			Map<String, Object> mapData = new HashMap<String, Object>();
			mapData.put("uid", d.getUid());
			mapData.put("numb", d.getNumb());
			mapData.put("sex", d.getSex());
			mapData.put("slogan", d.getSlogan());
			mapData.put("avatar", d.getAvatar());
			mapData.put("nickname", d.getNickname());

			bulkRequest.add(getClient().prepareIndex("all", "user", d.getUid().toString()).setSource(mapData));
		}
		if(isSync){
			bulkRequest.execute();
		}else{
		bulkRequest.execute(new ActionListener<BulkResponse>() {

			@Override
			public void onFailure(Throwable arg0) {
				logger.debug(arg0.toString());
			}

			@Override
			public void onResponse(BulkResponse arg0) {
				logger.debug(arg0.toString());
			}
		});
		}
	}

	@Override
	public void updateUserFieldsAnsyc(String strUid, String field, Object value) {
		if (getClient() == null || null == field || null == value) {
			return;
		}

		BulkRequestBuilder bulkRequest = getClient().prepareBulk();
		bulkRequest.add(getClient().prepareUpdate("all", "user", strUid).setDoc(field, value));

		bulkRequest.execute(new ActionListener<BulkResponse>() {

			@Override
			public void onFailure(Throwable arg0) {
				logger.debug(arg0.toString());
			}

			@Override
			public void onResponse(BulkResponse arg0) {
				logger.debug(arg0.toString());
			}
		});
	}

	@Override
	public void updateUsersAnsyc(SearchModel data) {
		if (getClient() == null || null == data) {
			return;
		}
		BulkRequestBuilder bulkRequest = getClient().prepareBulk();
		Map<String, Object> mapData = new HashMap<String, Object>();
		mapData.put("uid", data.getUid());
		mapData.put("numb", data.getNumb());
		mapData.put("sex", data.getSex());
		mapData.put("slogan", data.getSlogan());
		mapData.put("avatar", data.getAvatar());
		mapData.put("nickname", data.getNickname());
		bulkRequest.add(getClient().prepareUpdate("all", "user", data.getUid().toString()).setDoc(mapData));
		
		bulkRequest.execute(new ActionListener<BulkResponse>() {

			@Override
			public void onFailure(Throwable arg0) {
				logger.debug(arg0.toString());
			}

			@Override
			public void onResponse(BulkResponse arg0) {
				logger.debug(arg0.toString());
			}
		});
	}
	
	@Override
	public void updateUserInfosAnsyc(List<SearchModel> data) {
		if (getClient() == null || null == data) {
			return;
		}
		BulkRequestBuilder bulkRequest = getClient().prepareBulk();
		for (SearchModel d : data) {
			Map<String, Object> mapData = new HashMap<String, Object>();
			mapData.put("uid", d.getUid());
			mapData.put("numb", d.getNumb());
			mapData.put("sex", d.getSex());
			mapData.put("slogan", d.getSlogan());
			mapData.put("avatar", d.getAvatar());
			mapData.put("nickname", d.getNickname());
			bulkRequest.add(getClient().prepareUpdate("all", "user", d.getUid().toString()).setDoc(mapData));
		}
		bulkRequest.execute(new ActionListener<BulkResponse>() {

			@Override
			public void onFailure(Throwable arg0) {
				logger.debug(arg0.toString());
			}

			@Override
			public void onResponse(BulkResponse arg0) {
				logger.debug(arg0.toString());
			}
		});
	}
	
	@Override
	public void deleteUserInfosAnsyc(List<SearchModel> data) {}

	static public Client getClient() {
		return client;
	}

	static public void setClient(Client clientArg) {
		client = clientArg;
	}

	// 根据用户UID 获取用户基本信息
	public static final String SQL_GetUserBaseInfoAll = "SELECT * FROM %s";

	public static synchronized void updateEsData() {
		SearchServiceImpl impl = new SearchServiceImpl();
		for (int uid = 0; uid < 100; uid++) {
			Connection conn = null;
			PreparedStatement statement = null;
			ResultSet rs = null;
			try {
				conn = DataSource.instance.getPool(VarConfigUtils.dbZhuUser).getConnection();
				statement = conn.prepareStatement(StringUtils.getSqlString(SQL_GetUserBaseInfoAll, "user_base_info_", uid));
				rs = statement.executeQuery();
				if (rs != null) {
					int nCount = 0;
					while (rs.next()) {
						UserBaseInfoModel userModel = new UserBaseInfoModel();
						userModel.populateFromResultSet(rs);
						SearchModel searchModel = new SearchModel();
						searchModel.setNickname(userModel.getNickname());
						searchModel.setNumb(userModel.getUid().toString());
						searchModel.setUid(userModel.getUid());
						searchModel.setAvatar(userModel.getHeadimage());
						searchModel.setSex(userModel.getSex());
						searchModel.setSlogan(userModel.getSignature());
						nCount++;
						impl.insertUserInfo(searchModel);
					}
					logger.error("<updateES->OK: count:>" + nCount);
				} else {
					logger.error("<updateES->SQL RETURN NULL>");
				}
			} catch (Exception e) {
				e.printStackTrace();
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
		}
		return;
	}
	
	@Override
	public List<Map<String, Object>> getSearchRecommand(){
		Set<String> setLivingList = OtherRedisService.getInstance().getRecommendRoom(0, 5);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;

		if (setLivingList != null && setLivingList.size() > 0) {
			ILiveService liveService = new LiveServiceImpl();
			IUserService userService = new UserServiceImpl();
			IConfigService configService = new ConfigServiceImpl();
			IRoomService roomService = new RoomServiceImpl();

			Map<String, UserBaseInfoModel> userbaseMap = userService.getUserbaseInfoByUid(setLivingList.toArray(new String[0]));
			Map<String, LiveMicTimeModel> livingMap = liveService.getLiveIngByUid(setLivingList.toArray(new String[0]));

			if (userbaseMap == null || livingMap == null) {

			} else {
				UserBaseInfoModel userBaseInfoModel;
				LiveMicTimeModel liveMicTimeModel;
				for (String suid : setLivingList) {
					map = new HashMap<String, Object>();
					int uid = Integer.valueOf(suid);
					userBaseInfoModel = userbaseMap.get(suid);
					liveMicTimeModel = livingMap.get(suid);

					if (userBaseInfoModel == null || liveMicTimeModel == null || liveMicTimeModel.getType() || !userBaseInfoModel.getLiveStatus()) {
						continue;
					} else {
						map.put("status", true);
						map.put("uid", suid);
						map.put("nickname", userBaseInfoModel.getNickname().trim());
						map.put("headimage", userBaseInfoModel.getHeadimage());
						map.put("anchorLevel", userBaseInfoModel.getAnchorLevel());
						map.put("slogan", liveMicTimeModel.getSlogan().trim());
						map.put("city", org.apache.commons.lang.StringUtils.isEmpty(liveMicTimeModel.getCity().trim()) ? VarConfigUtils.Location : liveMicTimeModel.getCity());
						map.put("enters", roomService.getRoomShowUsers(uid, userBaseInfoModel.getContrRq()));
						map.put("mobileliveimg", userBaseInfoModel.getLivimage());
						if(userBaseInfoModel.getOpentime() > 0){
							map.put("opentime", System.currentTimeMillis()/1000 - userBaseInfoModel.getOpentime()); //System.currentTimeMillis()/1000 - Long.parseLong(map.get("opentime").toString())
						}else{
							map.put("opentime", 0);
						}
						map.put("sex", userBaseInfoModel.getSex());

						String stream = configService.getThirdStream(uid);
						int thirdstream = 0;
						if (null == stream) {
							map.put("domain",
									liveService.getVideoConfig(0, uid, userBaseInfoModel.getVideoline()).get("domain"));
						} else {
							thirdstream = 1;
							map.put("domain", stream);
						}
						map.put("fansCount",RelationRedisService.getInstance().getFansTotal(uid));
						map.put("thirdstream", thirdstream);
						map.put("verified", userBaseInfoModel.isVerified());
					}
					if (map.size() > 0) {
						list.add(map);
					}
				}
			}
		}
		return list;
	}
}
