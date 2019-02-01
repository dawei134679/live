package com.mpig.api.dictionary.lib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mpig.api.SqlTemplete;
import com.mpig.api.dictionary.GameAppConfig;

public class GameAppConfigLib implements SqlTemplete{
	private static final Map<Integer, GameAppConfig> gameAppMap = new HashMap<Integer, GameAppConfig>();
	private static final List<GameAppConfig> gameAppList = new ArrayList<GameAppConfig>();
	private static final List<GameAppConfig> unmodifyGameAppList = Collections.unmodifiableList(gameAppList);
	
	private static final Set<Integer> roomIds = new HashSet<Integer>();
	private static final Set<Integer> unmodifyRoomIds = Collections.unmodifiableSet(roomIds);
	
	public static boolean containGameAppFor(int appId){
		return gameAppMap.containsKey(appId);
	}
	
	public static GameAppConfig getGameAppFor(int appId){
		return gameAppMap.get(appId);
	}
	
	public static List<GameAppConfig> allGameApp(){
		return unmodifyGameAppList;
	}
	
	public static Set<Integer> allGameAppRoomIds(){
		return unmodifyRoomIds;
	}
	
	public static synchronized boolean loadLevelConfigFromDb(){
		gameAppMap.put(100, new GameAppConfig().initWith(100, "十二生肖", "99001",
				"web.xiaozhutv.com:8002","http://octn4r6eo.bkt.clouddn.com/bet_banner_12_750_280.jpg")
				.initWithRequire(0, ""));
		roomIds.add(99001);
		gameAppList.addAll(gameAppMap.values());
		return true;
	}
}
