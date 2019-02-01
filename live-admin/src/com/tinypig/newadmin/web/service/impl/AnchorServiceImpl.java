package com.tinypig.newadmin.web.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tinypig.newadmin.web.dao.AnchorDao;
import com.tinypig.newadmin.web.service.IAnchorService;

@Service
public class AnchorServiceImpl implements IAnchorService {

	@Autowired
	private AnchorDao anchorDao;

	@Override
	public List<Map<String, Object>> getPunishAnchorlistByUnion(int unionId) {

		// select a.unionname,b.anchorid,a.adminname from zhu_union.union_info
		// a,zhu_union.union_anchor_ref b where a.unionid=b.unionid and
		// a.unionid=#{unionid,jdbcType=INTEGER}

		List<Map<String, Object>> anchorlist = anchorDao.getanchorlistByUnionId(unionId);
//		if (anchorlist != null && anchorlist.size() > 0) {
//			for (Map<String, Object> map : anchorlist) {
//				String uid = map.get("uid").toString();
//				Map<String, Object> punishAnchorInfoByUid = anchorDao
//						.getPunishAnchorInfoByUid(uid.substring(uid.length() - 2, uid.length()), Integer.valueOf(uid),stime,etime);
//				if (punishAnchorInfoByUid != null) {
//					map.putAll(punishAnchorInfoByUid);
//				}
//			}
//		}
		return anchorlist;
	}

	@Override
	public List<Map<String, Object>> getPunishAnchorListByUnionName(String unionName,Integer stime,Integer etime) {

		// select a.unionname,b.anchorid,a.adminname from zhu_union.union_info
		// a,zhu_union.union_anchor_ref b where a.unionid=b.unionid and
		// a.unionname=#{unionname,jdbcType=VARCHAR}
		
		List<Map<String, Object>> anchorlist = anchorDao.getanchorlistByUnionName(unionName);
		if (anchorlist != null && anchorlist.size() > 0) {
			for (Map<String, Object> map : anchorlist) {
				String uid = map.get("uid").toString();
				Map<String, Object> punishAnchorInfoByUid = anchorDao
						.getPunishAnchorInfoByUid(uid.substring(uid.length() - 2, uid.length()), Integer.valueOf(uid),stime,etime);
				if (punishAnchorInfoByUid != null) {
					map.putAll(punishAnchorInfoByUid);
				}
			}
		}
		return anchorlist;
	}

	@Override
	public List<Map<String, Object>> getPunishAnchorListByUid(String sufix,Integer anchoruid,Integer stime,Integer etime) {

		List<Map<String, Object>> map = new ArrayList<Map<String,Object>>();
		Map<String, Object> anchorlist = anchorDao.getPunishAnchorInfoByUid(sufix, anchoruid,stime,etime);
		
		if (anchorlist != null && anchorlist.size() > 0) {
			
			map = anchorDao.getanchorlistByUid(anchoruid);
			if (map != null && map.size() > 0) {
				for(Map<String, Object> _map:map){
					_map.putAll(anchorlist);
				}
			}else {
				anchorlist.put("unionname", "");
				anchorlist.put("anchorid", anchoruid);
				anchorlist.put("adminname", "");
				map.add(anchorlist);
			}
		}
		return map;
	}

	@Override
	public Map<String, Object> getPunishAnchorInfoByUid(String sufix, int anchoruid,Integer stime,Integer etime) {

		return null;
	}

}
