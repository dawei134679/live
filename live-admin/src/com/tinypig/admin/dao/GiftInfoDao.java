package com.tinypig.admin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.tinypig.admin.constant;
import com.tinypig.admin.model.ConfigGiftModel;
import com.tinypig.admin.model.GiftInfoModel;
import com.tinypig.admin.model.UserBaseInfoModel;
import com.tinypig.admin.redis.service.OtherRedisService;
import com.tinypig.admin.util.*;

public class GiftInfoDao extends BaseDao {

	private static final Logger logger = Logger.getLogger(GiftInfoDao.class);
	
	int time = (int) (System.currentTimeMillis() / 1000);
	
	private final static GiftInfoDao instance = new GiftInfoDao();

	public static GiftInfoDao getInstance() {
		return instance;
	}

	public List<GiftInfoModel> srcGiftInfo(int uid, String date, String page, String rows) {
		
		Calendar c = Calendar.getInstance();
		Calendar d = Calendar.getInstance();
		String startdate = "";
		String enddate = "";
		try {
			if (date.length() == 8) {
				startdate = date + "000000";
				enddate = date + "240000";
				c.setTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(startdate));
				d.setTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(enddate));
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			logger.error("srcGiftInfo-ParseException:",e);
		}
		List<GiftInfoModel> list = new ArrayList<GiftInfoModel>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String dbname = "";
		String sql = "";
		if (date.length() == 6) {
			dbname = "bill_" + date;
			sql = "select b.srcuid,b.srcnickname,b.dstuid,b.dstnickname,g.gname as giftname,b.count,b.getmoney,b.addtime from "
					+ dbname + " b,zhu_config.config_giftlist g where b.srcuid=" + uid+" and b.gid=g.gid";
		} else if (date.length() == 8) {
			dbname = "bill_" + date.substring(0, 6);
			sql = "select b.srcuid,b.srcnickname,b.dstuid,b.dstnickname,g.gname as giftname,b.count,b.getmoney,b.addtime from "
					+ dbname + " b,zhu_config.config_giftlist g where b.srcuid=" + uid + " and b.gid=g.gid and b.addtime>" + c.getTimeInMillis() / 1000
					+ " and b.addtime<" + d.getTimeInMillis() / 1000;
		}
		if (StringUtils.isNotEmpty(page)) {
			sql = sql + " limit " + (Integer.valueOf(page) - 1) * Integer.valueOf(rows) + "," + Integer.valueOf(rows);
		}
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_bill,"slave");
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					GiftInfoModel cUserModel = new GiftInfoModel().populateFromResultSet(rs);
					if (cUserModel != null) {
						list.add(cUserModel);
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public int srcTotal(int uid, String date) {
		Calendar c = Calendar.getInstance();
		Calendar d = Calendar.getInstance();
		String startdate = "";
		String enddate = "";
		try {
			if (date.length() == 8) {
				startdate = date + "000000";
				enddate = date + "240000";
				c.setTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(startdate));
				d.setTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(enddate));
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<GiftInfoModel> list = new ArrayList<GiftInfoModel>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String dbname = "";
		String sql = "";
		if (date.length() == 6) {
			dbname = "bill_" + date;
			sql = "select b.srcuid,b.srcnickname,b.dstuid,b.dstnickname,g.gname as giftname,b.count,b.getmoney,b.addtime from "
					+ dbname + " b,zhu_config.config_giftlist g where b.srcuid=" + uid+" and b.gid=g.gid";
		} else if (date.length() == 8) {
			dbname = "bill_" + date.substring(0, 6);
			sql = "select b.srcuid,b.srcnickname,b.dstuid,b.dstnickname,g.gname as giftname,b.count,b.getmoney,b.addtime from "
					+ dbname + " b,zhu_config.config_giftlist g where b.srcuid=" + uid + " and b.gid=g.gid and b.addtime>" + c.getTimeInMillis() / 1000
					+ " and b.addtime<" + d.getTimeInMillis() / 1000;
		}
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_bill,"slave");
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					GiftInfoModel cUserModel = new GiftInfoModel().populateFromResultSet(rs);
					if (cUserModel != null) {
						list.add(cUserModel);
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list.size();
	}
	
	/**
	 * 添加新礼物
	 * @param type =1易耗品(弹幕、喇叭) =2房间内背包(可送出) =3时效道具
	 * @param subtype 礼物类型(0=>普通礼物 1=>弹幕 2=>喇叭 3=>VIP 4=>贵族 5=>座驾 6=>徽章 7=>守护 8=>商城道具)
	 * @param gname 礼物名称
	 * @param gprice 礼物价格
	 * @param gpriceaudit 审核礼物价格
	 * @param wealth 用户财富值
	 * @param credit 声援值
	 * @param gcover 礼物封面图名称
	 * @param gtype 礼物类型  =1礼物消息连 =2动态图片(半屏 带离子效果) =3动画效果(半瓶 不带离子效果) =4动画全屏 =5半屏带文字效果
	 * @param gpctype =1静态 =2SWF
	 * @param gframeurl 小动画zip地址
	 * @param gframeurlios ios小动画zip地址
	 * @param simgs 小动画序列帧数
	 * @param bimgs 大动画序列帧数
	 * @param pimgs 离子图片名字数组数
	 * @param gnumtype 礼物可先择数量
	 * @param gduration 小礼物展示一遍总时间
	 * @param isshow =0不显示 =1显示
	 * @param isvalid =0无效 =1有效
	 * @param gsort 排序
	 * @param icon 礼物图标
	 * @param skin 礼物引用皮肤 等于自己的物品编号就代表强制更新
	 * @param useDuration 时效物品有效时间单位为天
	 * @param category =1热门 =2豪华 =3活动 =4专属
	 * @return
	 */
	public int addGiftInfo(int type,int subtype,String gname,int gprice,int gpriceaudit,int wealth,int credit,int charm,String gcover,int gtype,int gpctype,
			String gframeurl,String gframeurlios,int simgs,int bimgs,int pimgs,String gnumtype,float gduration,int isshow,int isvalid,int gsort,
			String icon,int skin,int useDuration,int category,int adminid){
		String sql = "INSERT INTO `config_giftlist` (`type`, `subtype`, `gname`, `gprice`,`gpriceaudit`, `wealth`, `credit`, `charm`, `gcover`, `gtype`, `gpctype`, `gframeurl`, `gframeurlios`, `simgs`, `bimgs`, `pimgs`, `gnumtype`, `gduration`, `isshow`, `isvalid`, `gsort`, `createAt`, `icon`, `skin`, `useDuration`, `category`)"
				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int iresult = 0;
		try {
			con = DbUtil.instance().getCon("zhu_config", "master");
			pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, type);
			pstmt.setInt(2, subtype);
			pstmt.setString(3, gname);
			pstmt.setInt(4, gprice);
			pstmt.setInt(5, gpriceaudit);
			pstmt.setInt(6, wealth);
			pstmt.setInt(7, credit);
			pstmt.setInt(8, charm);
			pstmt.setString(9, gcover);
			pstmt.setInt(10, gtype);
			pstmt.setInt(11, gpctype);
			pstmt.setString(12, gframeurl);
			pstmt.setString(13, gframeurlios);
			pstmt.setInt(14, simgs);
			pstmt.setInt(15, bimgs);
			pstmt.setInt(16, pimgs);
			pstmt.setString(17, gnumtype);
			pstmt.setFloat(18, gduration);
			pstmt.setInt(19, isshow);
			pstmt.setInt(20, isvalid);
			pstmt.setInt(21, gsort);
			pstmt.setLong(22, System.currentTimeMillis()/1000);
			pstmt.setString(23, icon);
			pstmt.setInt(24, skin);
			pstmt.setInt(25, useDuration);
			pstmt.setInt(26, category);
			
			pstmt.executeUpdate();
			rs = pstmt.getGeneratedKeys();
			if (rs != null && rs.next()) {
				iresult = rs.getInt(1);
			}
			if (iresult > 0) {
				// 添加成功
				ConfigGiftModel giftInfo = getGiftInfo(iresult);
				String jsonString = JSONObject.toJSONString(giftInfo);
				AddOperationLog("config_giftlist", String.valueOf(iresult), "新增礼物", 1, "", jsonString, adminid);
				OtherRedisService.getInstance().setGiftVer();
				// 通知业务端 更新缓存
				NoticeAPIUtile.noticeApi("updateGift", "adminid", "admin");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return iresult;
	}

	/**
	 * 
	 * @param gid
	 * @param type
	 * @param subtype
	 * @param gname
	 * @param gprice
	 * @param wealth
	 * @param credit
	 * @param gcover
	 * @param gtype
	 * @param gpctype
	 * @param gframeurl
	 * @param simgs
	 * @param bimgs
	 * @param pimgs
	 * @param gnumtype
	 * @param gduration
	 * @param isshow
	 * @param isvalid
	 * @param gsort
	 * @param icon
	 * @param skin
	 * @param useDuration
	 * @param category
	 * @param adminid
	 * @return
	 */
	public int updGiftInfo(int gid,int type,int subtype, String gname, int gprice,int gpriceaudit, int wealth, int credit,int charm, String gcover, int gtype,int gpctype,
			String gframeurl,String gframeurlios, int simgs, int bimgs, int pimgs, String gnumtype, float gduration, boolean isshow,
			boolean isvalid, int gsort,String icon,int skin,int useDuration,int category,int adminid) {
		int executeUpdate = 0;
		ConfigGiftModel giftInfo = this.getGiftInfo(gid);
		if (giftInfo != null) {
			int gver = giftInfo.getGver();
			int sver = giftInfo.getSver();
			boolean gbl = false;
			boolean sbl = false;
			
			if (!gname.equals(giftInfo.getGname())) {
				gbl = true;
			} else if (gprice != giftInfo.getGprice()) {
				gbl = true;
			} else if (gpriceaudit != giftInfo.getGpriceaudit()) {
				gbl = true;
			} else if (wealth != giftInfo.getWealth()) {
				gbl = true;
			} else if (credit != giftInfo.getCredit()) {
				gbl = true;
			}else if (charm != giftInfo.getCharm()) {
				gbl = true;
			}  else if (!gcover.equals(giftInfo.getGcover())) {
				gbl = true;
			} else if (gtype != giftInfo.getGtype()) {
				gbl = true;
			} else if (!gnumtype.equals(giftInfo.getGnumtype())) {
				gbl = true;
			} else if (gduration != giftInfo.getGduration()) {
				gbl = true;
			} else if (gsort != giftInfo.getGsort()) {
				gbl = true;
			} else if (skin != giftInfo.getSkin()) {
				gbl = true;
			} else if (type != giftInfo.getType()) {
				gbl = true;
			} else if (subtype != giftInfo.getSubtype()) {
				gbl = true;
			} else if (gpctype != giftInfo.getGpctype()) {
				gbl = true;
			} else if (!icon.equals(giftInfo.getIcon())) {
				gbl = true;
			} else if (useDuration != giftInfo.getUseDuration()) {
				gbl = true;
			} else if (category != giftInfo.getCategory()) {
				gbl = true;
			} else {
				if (isshow != giftInfo.getIsshow()) {
					gbl = true;
				}
				if (isvalid != giftInfo.getIsvalid()) {
					gbl = true;
				}
			}

			if (!gframeurl.equals(giftInfo.getGframeurl()) || !gframeurlios.equals(giftInfo.getGframeurlios())) {
				gbl = true;
				sbl = true;
			} else if (simgs != giftInfo.getSimgs()) {
				gbl = true;
				sbl = true;
			} else if (bimgs != giftInfo.getBimgs()) {
				gbl = true;
				sbl = true;
			} else if (pimgs != giftInfo.getPimgs()) {
				gbl = true;
				sbl = true;
			}
			if (gbl || sbl) {
				String sql = "update config_giftlist set gname=?, gprice=?,gpriceaudit=?, wealth=?, credit=?, charm=?, gcover=?, gtype=?, gframeurl=?,gframeurlios=?,"
						+ " simgs=?,bimgs=?,pimgs=?, gnumtype=?, gduration=?, gver=?, sver=?, isshow=?, isvalid=?, gsort=?,skin=?,icon=?,gpctype=?,category=?,type=?,subtype=?,useDuration=? where gid=?";
				Connection con = null;
				PreparedStatement pstmt = null;
				try {
					con = DbUtil.instance().getCon(constant.db_zhu_config,"master");
					pstmt = con.prepareStatement(sql);
					pstmt.setString(1, gname);
					pstmt.setInt(2, gprice);
					pstmt.setInt(3, gpriceaudit);
					pstmt.setInt(4, wealth);
					pstmt.setInt(5, credit);
					pstmt.setInt(6, charm);
					pstmt.setString(7, gcover);
					pstmt.setInt(8, gtype);
					pstmt.setString(9, gframeurl);
					pstmt.setString(10, gframeurlios);
					pstmt.setInt(11, simgs);
					pstmt.setInt(12, bimgs);
					pstmt.setInt(13, pimgs);
					pstmt.setString(14, gnumtype);
					pstmt.setFloat(15, gduration);
					pstmt.setInt(16, gbl?gver+1:gver);
					pstmt.setInt(17, sbl?sver+1:sver);
					pstmt.setBoolean(18, isshow);
					pstmt.setBoolean(19, isvalid);
					pstmt.setInt(20, gsort);
					pstmt.setInt(21, skin);
					pstmt.setString(22, icon);
					pstmt.setInt(23, gpctype);
					pstmt.setInt(24, category);
					pstmt.setInt(25, type);
					pstmt.setInt(26, subtype);
					pstmt.setInt(27, useDuration);
					pstmt.setInt(28, gid);
					
					executeUpdate = pstmt.executeUpdate();

					if (executeUpdate == 1) {
					
						OtherRedisService.getInstance().setGiftVer();
						// 通知业务端 更新缓存
						NoticeAPIUtile.noticeApi("updateGift", "adminid", "admin");
						

						ConfigGiftModel giftInfoNew = getGiftInfo(gid);
						String preString = JSONObject.toJSONString(giftInfo);
						String curString = JSONObject.toJSONString(giftInfoNew);
						AddOperationLog("config_giftlist", String.valueOf(gid), "修改礼物", 1, preString, curString, adminid);
					}
				} catch (Exception e) {
					logger.error("updGiftInfo exception:",e);
				}finally {
					try {
						if (pstmt != null) {
							pstmt.close();
						}
						if (con != null) {
							con.close();
						}
					} catch (Exception e2) {
						logger.error("updGiftInfo finally exception:",e2);
					}
				}
			}
		}

		return executeUpdate;
	}

	public Map<String, Object>  getGiftList(int page, int size, String gname, String types,String subtypes,Integer isvalid,Integer isshow) {
		Map<String, Object> mapResult = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder("select gid, type, subtype, gname, gprice,gpriceaudit, wealth, credit, charm, gcover, gtype, gpctype, gframeurl,gframeurlios, simgs,bimgs,pimgs, gnumtype, gduration, gver, sver, isshow, isvalid, gsort, createAt, icon,skin,useDuration,category from config_giftlist where 1=1 ");
		List<ConfigGiftModel> list = new ArrayList<ConfigGiftModel>();
		if(StringUtils.isNotBlank(gname)) {
			sql.append(" and gname like '%"+gname+"%'");
		}
		if(StringUtils.isNotBlank(types)) {
			String[] split = StringUtils.split(types,",");
			sql.append(" and (");
			for (String s : split) {
				sql.append(" type = "+s+" or");
			}
			sql = sql.delete(sql.length()-2,sql.length());
			sql.append(")");
		}
		if(StringUtils.isNotBlank(subtypes)) {
			String[] split = StringUtils.split(subtypes,",");
			sql.append(" and (");
			for (String s : split) {
				sql.append(" subtype = "+s+" or");
			}
			sql = sql.delete(sql.length()-2,sql.length());
			sql.append(")");
		}
		if(isvalid != null) {
			sql.append(" and isvalid="+isvalid);
		}
		if(isshow != null) {
			sql.append(" and isshow="+isshow);
		}
		String sqlList = sql.toString() + " limit " + (page-1)*size + "," + size;
		StringBuilder sqlTotal = new StringBuilder("select count(gid) total from config_giftlist where 1=1 ");
		if(StringUtils.isNotBlank(gname)){
			sqlTotal.append(" and gname like '%"+gname+"%'");
		}
		if(StringUtils.isNotBlank(types)) {
			String[] split = StringUtils.split(types,",");
			sqlTotal.append(" and (");
			for (String s : split) {
				sqlTotal.append(" type = "+s+" or");
			}
			sqlTotal = sqlTotal.delete(sqlTotal.length()-2,sqlTotal.length());
			sqlTotal.append(")");
		}
		if(StringUtils.isNotBlank(subtypes)) {
			String[] split = StringUtils.split(subtypes,",");
			sqlTotal.append(" and (");
			for (String s : split) {
				sqlTotal.append(" subtype = "+s+" or");
			}
			sqlTotal = sqlTotal.delete(sqlTotal.length()-2,sqlTotal.length());
			sqlTotal.append(")");
		}
		if(isvalid != null) {
			sqlTotal.append(" and isvalid="+isvalid);
		}
		if(isshow != null) {
			sqlTotal.append( " and isshow="+isshow);
		}
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_config,"slave");
			pstmt = con.prepareStatement(sqlList);
			rs = pstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					ConfigGiftModel configGiftModel = new ConfigGiftModel().populateFromResultSet(rs);
					list.add(configGiftModel);
				}
				mapResult.put("rows", list);
				rs.close();
			}
			if(pstmt != null) {
				pstmt.close();
			}
			pstmt = con.prepareStatement(sqlTotal.toString());
			rs = pstmt.executeQuery();
			int total = 0;
			if(rs != null) {
				rs.next();
				total = rs.getInt("total");
			}
			mapResult.put("total", total);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return mapResult;
	}

	public ConfigGiftModel getGiftInfo(int gid) {
		String sql = "select gid, type, subtype, gname, gprice,gpriceaudit, wealth, credit, charm, gcover, gtype, gpctype, gframeurl,gframeurlios, simgs,bimgs,pimgs, gnumtype, gduration, gver, sver, isshow, isvalid, gsort, createAt, icon,skin,useDuration,category from config_giftlist where gid=?";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ConfigGiftModel configGiftModel = null;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_config,"slave");
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, gid);

			rs = pstmt.executeQuery();
			if (rs != null && rs.next()) {
				configGiftModel = new ConfigGiftModel().populateFromResultSet(rs);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return configGiftModel;
	}

	public List<GiftInfoModel> dstGiftInfo(int uid, String date, String page, String rows,String type) {
		Calendar c = Calendar.getInstance();
		Calendar d = Calendar.getInstance();
		String startdate = "";
		String enddate = "";
		try {
			if (date.length() == 8) {
				startdate = date + "000000";
				enddate = date + "240000";
				c.setTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(startdate));
				d.setTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(enddate));
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<GiftInfoModel> list = new ArrayList<GiftInfoModel>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String dbname = "";
		String sql = "";
		
		String param = "";
		if ("dst".equals(type)) {
			param = " b.dstuid=" + uid;
		}else {
			param = " b.srcuid=" + uid;
		}
		if (date.length() == 6) {
			dbname = "bill_" + date;
			sql = "select b.srcuid,b.srcnickname,b.dstuid,b.dstnickname,g.gname as giftname,b.count,b.getmoney,b.addtime,(b.count*b.price) as zhutou from "
					+ dbname + " b,zhu_config.config_giftlist g where " + param + " and b.gid=g.gid";
		} else if (date.length() == 8) {
			dbname = "bill_" + date.substring(0, 6);
			sql = "select b.srcuid,b.srcnickname,b.dstuid,b.dstnickname,g.gname as giftname,b.count,b.getmoney,b.addtime,(b.count*b.price) as zhutou from "
					+ dbname + " b,zhu_config.config_giftlist g where " + param + " and b.gid=g.gid and b.addtime>" + c.getTimeInMillis() / 1000
					+ " and b.addtime<" + d.getTimeInMillis() / 1000;
		}
		if (StringUtils.isNotEmpty(page)) {
			sql = sql + " limit " + (Integer.valueOf(page) - 1) * Integer.valueOf(rows) + "," + Integer.valueOf(rows);
		}
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_bill,"slave");
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					GiftInfoModel cUserModel = new GiftInfoModel().populateFromResultSet(rs);
					if (cUserModel != null) {
						list.add(cUserModel);
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	/**
	 * 
	 * @param uid
	 * @param date
	 * @param type =src送出  =dst收到
	 * @return
	 */
	public Map<String, Object> dstTotal(int uid, String date,String type) {
		Map<String, Object> mapResult = new HashMap<String, Object>();
		
		int icount = 0;
		int amount = 0;
		
		Calendar c = Calendar.getInstance();
		Calendar d = Calendar.getInstance();
		String startdate = "";
		String enddate = "";
		try {
			if (date.length() == 8) {
				startdate = date + "000000";
				enddate = date + "240000";
				c.setTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(startdate));
				d.setTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(enddate));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String dbname = "";
		String sql = "";

		String param = "";
		if ("dst".equals(type)) {
			param = " b.dstuid=" + uid;
		}else {
			param = " b.srcuid=" + uid;
		}
		
		if (date.length() == 6) {
			dbname = "bill_" + date;
			sql = "select b.srcuid,b.srcnickname,b.dstuid,b.dstnickname,g.gname as giftname,b.count,b.getmoney,b.addtime,(b.count*b.price) as zhutou from "
					+ dbname + " b,zhu_config.config_giftlist g where " + param +" and b.gid=g.gid";
		} else if (date.length() == 8) {
			dbname = "bill_" + date.substring(0, 6);
			sql = "select b.srcuid,b.srcnickname,b.dstuid,b.dstnickname,g.gname as giftname,b.count,b.getmoney,b.addtime,(b.count*b.price) as zhutou from "
					+ dbname + " b,zhu_config.config_giftlist g where " + param + " and b.gid=g.gid and b.addtime>" + c.getTimeInMillis() / 1000
					+ " and b.addtime<" + d.getTimeInMillis() / 1000;
		}
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_bill,"slave");
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					icount++;
				
					if ("src".equals(type)) {
						amount = amount + rs.getInt("zhutou");
					}else if ("dst".equals(type)) {
						amount = amount + rs.getInt("getmoney");
					}
				}
			}
			mapResult.put("total", icount);
			mapResult.put("amount", amount);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return mapResult;
	}
	
	/**
	 * 获取 填充下拉框的礼物列表
	 * @param subtype =99 全部
	 * @return
	 */
	public List<Map<String, Object>> getGiftForSelect(int subtype){
		
		List<Map<String, Object>> listResult = new ArrayList<Map<String,Object>>();
		
		String sql = " select gid,gname from config_giftlist where isvalid=1 ";
		if (subtype != 99) {
			sql = sql + " and subtype = " + subtype; 
		}
		
		Connection con =  null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_config, constant.db_slave);
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				while(rs.next()){
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("gid", rs.getInt("gid"));
					map.put("gname", rs.getString("gname"));
					
					listResult.add(map);
				}
			}
		} catch (Exception e) {
			System.out.println("getGiftForSelect-Exception:" + e.getMessage());
			System.out.println("sql: " + sql);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null){
					con.close();
				}
			} catch (Exception e2) {
				System.out.println("getGiftForSelect-Exception-finally:" + e2.getMessage());
			}
		}
		
		return listResult;
	}

	
	/**
	 * 获取 填充下拉框的礼物列表
	 * @param subtype =99 全部
	 * @param notEqual true不等于 false等于
	 * @return
	 */
	public List<Map<String, Object>> forSelectByType(int type, boolean notEqual){
		
		List<Map<String, Object>> listResult = new ArrayList<Map<String,Object>>();
		
		String sql = " select gid,gname from config_giftlist where isvalid=1 ";
		if (type != 99) {
			if (notEqual) {
				sql = sql + " and type != " + type + " and subtype in (0,1,2)"; 
			}else {
				sql = sql + " and type = " + type; 
			}
		}
		
		Connection con =  null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_config, constant.db_slave);
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				while(rs.next()){
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("gid", rs.getInt("gid"));
					map.put("gname", rs.getString("gname"));
					
					listResult.add(map);
				}
			}
		} catch (Exception e) {
			System.out.println("forSelectByType-Exception:" + e.getMessage());
			System.out.println("sql: " + sql);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null){
					con.close();
				}
			} catch (Exception e2) {
				System.out.println("forSelectByType-Exception-finally:" + e2.getMessage());
			}
		}
		
		return listResult;
	}
	
	/**
	 * 获取使用vip的用户列表
	 * @param uid
	 * @param stime
	 * @param etime
	 * @return
	 */
	public Map<String, Object> getUsedVIPList(int uid,Long stime,Long etime,int page,int size){
		
		Map<String, Object> mapResult = new HashMap<String, Object>();
		
		String sql = " select a.id,a.uid,a.gid,b.gname,a.starttime,a.endtime,a.isdel "
				+ " from user_vip_info a,zhu_config.config_giftlist b "
				+ " where a.gid=b.gid and endtime >= " + stime;
		if (uid > 0) {
			sql = sql + " and uid = " + uid;
		}
		
		String sqlList = sql + " order by endtime desc limit " + (page-1)*size + "," + size;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_user, constant.db_slave);
			pstmt = con.prepareStatement(sqlList);
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
				while(rs.next()){
					Map<String, Object> map = new HashMap<String, Object>();
					UserBaseInfoModel userBaseInfo = UserDao.getInstance().getUserBaseInfo(rs.getInt("uid"), false);
					if (userBaseInfo != null) {
						map.put("nickname", userBaseInfo.getNickname());
					}else {
						map.put("nickname", "未知");
					}
					map.put("id", rs.getInt("id"));
					map.put("uid", rs.getInt("uid"));
					map.put("gid", rs.getInt("gid"));
					map.put("gname", rs.getString("gname"));
					map.put("starttime", rs.getLong("starttime"));
					map.put("endtime", rs.getLong("endtime"));
					map.put("isdel", rs.getInt("isdel"));
					
					list.add(map);
				}
				mapResult.put("rows", list);
				rs.close();
			}
			
			if (pstmt != null) {
				pstmt.close();
			}
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			int total = 0;
			if (rs != null) {
				rs.last();
				total = rs.getRow();
			}
			mapResult.put("total", total);
			
		} catch (Exception e) {
			System.out.println("Dao getUsedVIPList-Exception:" + e.getMessage());
			System.out.println("sql:" + sqlList);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				System.out.println("Dao getUsedVIPList-finally-Exception:" + e2.getMessage());
			}
		}
		return mapResult;
	}

	/**
	 * 获取使用Car的用户列表
	 * @param uid
	 * @param stime
	 * @param etime
	 * @return
	 */
	public Map<String, Object> getUsedCarList(int uid,Long stime,Long etime,int page,int size){
		
		Map<String, Object> mapResult = new HashMap<String, Object>();
		
		String sql = " select a.id,a.uid,a.gid,b.gname,a.starttime,a.endtime,a.status "
				+ " from user_car_info a,zhu_config.config_giftlist b "
				+ " where a.gid=b.gid and endtime >= " + stime;
		if (uid > 0) {
			sql = sql + " and uid = " + uid;
		}
		
		String sqlList = sql + " order by endtime desc limit " + (page-1)*size + "," + size;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_user, constant.db_slave);
			pstmt = con.prepareStatement(sqlList);
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
				while(rs.next()){
					Map<String, Object> map = new HashMap<String, Object>();
					UserBaseInfoModel userBaseInfo = UserDao.getInstance().getUserBaseInfo(rs.getInt("uid"), false);
					if (userBaseInfo != null) {
						map.put("nickname", userBaseInfo.getNickname());
					}else {
						map.put("nickname", "未知");
					}
					map.put("id", rs.getInt("id"));
					map.put("uid", rs.getInt("uid"));
					map.put("gid", rs.getInt("gid"));
					map.put("gname", rs.getString("gname"));
					map.put("starttime", rs.getLong("starttime"));
					map.put("endtime", rs.getLong("endtime"));
					map.put("status", rs.getInt("status"));
					
					list.add(map);
				}
				mapResult.put("rows", list);
				rs.close();
			}
			
			if (pstmt != null) {
				pstmt.close();
			}
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			int total = 0;
			if (rs != null) {
				rs.last();
				total = rs.getRow();
			}
			mapResult.put("total", total);
			
		} catch (Exception e) {
			System.out.println("Dao getUsedCarList-Exception:" + e.getMessage());
			System.out.println("sql:" + sqlList);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				System.out.println("Dao getUsedCarList-finally-Exception:" + e2.getMessage());
			}
		}
		return mapResult;
	}
	
	/**
	 * 获取使用Guard的用户列表
	 * @param uid
	 * @param stime
	 * @param etime
	 * @return
	 */
	public Map<String, Object> getUsedGuardList(int uid,Long stime,Long etime,int page,int size){
		
		Map<String, Object> mapResult = new HashMap<String, Object>();
		
		String sql = " select a.id,a.roomid,a.uid,a.gid,b.gname,a.level,a.exp,a.starttime,a.endtime,a.cushiontime,a.isdel "
				+ " from user_guard_info a,zhu_config.config_giftlist b "
				+ " where a.gid=b.gid and a.endtime >= " + stime;
		if (uid > 0) {
			sql = sql + " and uid = " + uid;
		}
		
		String sqlList = sql + " order by a.endtime desc limit " + (page-1)*size + "," + size;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_user, constant.db_slave);
			pstmt = con.prepareStatement(sqlList);
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
				while(rs.next()){
					Map<String, Object> map = new HashMap<String, Object>();
					UserBaseInfoModel userBaseInfo = UserDao.getInstance().getUserBaseInfo(rs.getInt("uid"), false);
					if (userBaseInfo != null) {
						map.put("nickname", userBaseInfo.getNickname());
					}else {
						map.put("nickname", "未知");
					}
					UserBaseInfoModel anchorBaseInfo = UserDao.getInstance().getUserBaseInfo(rs.getInt("roomid"), false);
					if (anchorBaseInfo != null) {
						map.put("anchorname", anchorBaseInfo.getNickname());
					}else {
						map.put("anchorname", "主播未知");
					}
					map.put("id", rs.getInt("id"));
					map.put("uid", rs.getInt("uid"));
					map.put("anchoruid", rs.getInt("roomid")); 
					map.put("gid", rs.getInt("gid"));
					map.put("gname", rs.getString("gname"));
					map.put("level", rs.getInt("level"));
					map.put("exp", rs.getInt("exp"));
					map.put("cushiontime", rs.getLong("cushiontime"));
					map.put("starttime", rs.getLong("starttime"));
					map.put("endtime", rs.getLong("endtime"));
					map.put("isdel", rs.getInt("isdel"));
					
					list.add(map);
				}
				mapResult.put("rows", list);
				rs.close();
			}
			
			if (pstmt != null) {
				pstmt.close();
			}
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			int total = 0;
			if (rs != null) {
				rs.last();
				total = rs.getRow();
			}
			mapResult.put("total", total);
			
		} catch (Exception e) {
			System.out.println("Dao getUsedGuardList-Exception:" + e.getMessage());
			System.out.println("sql:" + sqlList);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				System.out.println("Dao getUsedGuardList-finally-Exception:" + e2.getMessage());
			}
		}
		return mapResult;
	}
	

	public Map<String, Object> getUsedGiftList(int uid,int page,int size){
		
		Map<String, Object> mapResult = new HashMap<String, Object>();
		
		String sql = " select a.gid,b.gname,a.num,a.uid from user_item a,zhu_config.config_giftlist b where a.gid=b.gid and a.num > 0 ";
		if (uid > 0) {
			sql = sql + " and a.uid = " + uid;
		}
		
		String sqlList = sql + " order by a.uid desc limit " + (page-1)*size + "," + size;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_item, constant.db_slave);
			pstmt = con.prepareStatement(sqlList);
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
				while(rs.next()){
					Map<String, Object> map = new HashMap<String, Object>();
					UserBaseInfoModel userBaseInfo = UserDao.getInstance().getUserBaseInfo(rs.getInt("uid"), false);
					if (userBaseInfo != null) {
						map.put("nickname", userBaseInfo.getNickname());
					}else {
						map.put("nickname", "未知");
					}
					map.put("gid", rs.getInt("gid"));
					map.put("gname", rs.getString("gname"));
					map.put("uid", rs.getInt("uid"));
					map.put("num", rs.getInt("num")); 
					
					list.add(map);
				}
				mapResult.put("rows", list);
				rs.close();
			}
			
			if (pstmt != null) {
				pstmt.close();
			}
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			int total = 0;
			if (rs != null) {
				rs.last();
				total = rs.getRow();
			}
			mapResult.put("total", total);
			
		} catch (Exception e) {
			System.out.println("Dao getUsedGuardList-Exception:" + e.getMessage());
			System.out.println("sql:" + sqlList);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				System.out.println("Dao getUsedGuardList-finally-Exception:" + e2.getMessage());
			}
		}
		return mapResult;
	}

	public Map<String, Object> getUsedBadgeList(int uid,int page,int size){
		
		Map<String, Object> mapResult = new HashMap<String, Object>();
		
		String sql = " select a.gid,b.gname,b.gprice,b.gpriceaudit,a.num,a.starttime,a.endtime,a.uid from user_item_special a,zhu_config.config_giftlist b where a.gid=b.gid and a.num > 0 and a.endtime > " + System.currentTimeMillis()/1000;
		if (uid > 0) {
			sql = sql + " and a.uid = " + uid;
		}
		
		String sqlList = sql + " order by a.endtime desc limit " + (page-1)*size + "," + size;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_item, constant.db_slave);
			pstmt = con.prepareStatement(sqlList);
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
				while(rs.next()){
					Map<String, Object> map = new HashMap<String, Object>();
					UserBaseInfoModel userBaseInfo = UserDao.getInstance().getUserBaseInfo(rs.getInt("uid"), false);
					if (userBaseInfo != null) {
						map.put("nickname", userBaseInfo.getNickname());
					}else {
						map.put("nickname", "未知");
					}
					map.put("gid", rs.getInt("gid"));
					map.put("gname", rs.getString("gname"));
					map.put("uid", rs.getInt("uid"));
					map.put("num", rs.getInt("num")); 
					map.put("gprice", rs.getInt("gprice"));
					map.put("gpriceaudit", rs.getInt("gpriceaudit"));
					map.put("starttime", rs.getLong("starttime"));
					map.put("endtime", rs.getLong("endtime"));
					
					list.add(map);
				}
				mapResult.put("rows", list);
				rs.close();
			}
			
			if (pstmt != null) {
				pstmt.close();
			}
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			int total = 0;
			if (rs != null) {
				rs.last();
				total = rs.getRow();
			}
			mapResult.put("total", total);
			
		} catch (Exception e) {
			System.out.println("Dao getUsedGuardList-Exception:" + e.getMessage());
			System.out.println("sql:" + sqlList);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				System.out.println("Dao getUsedGuardList-finally-Exception:" + e2.getMessage());
			}
		}
		return mapResult;
	}
	
	public Long checkUsedBadgeByUidGid(int uid,int gid){
		
		Long  lgEtime = null;
		String sql = " select endtime from user_item_special where gid=? and uid=? ";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_item, constant.db_slave);
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, gid);
			pstmt.setInt(2, uid);
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				while(rs.next()){
					lgEtime = rs.getLong("endtime");
					break;
				}
			}
			
		} catch (Exception e) {
			System.out.println("Dao checkUsedBadgeByUidGid-Exception:" + e.getMessage());
			System.out.println("sql:" + sql);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				System.out.println("Dao checkUsedBadgeByUidGid-finally-Exception:" + e2.getMessage());
			}
		}
		return lgEtime;
	}

	public int addUserBadge(int uid,int gid,int num, Long stime,int days,String remarks,int adminid){
		
		ConfigGiftModel giftInfo = GiftInfoDao.getInstance().getGiftInfo(gid);
		if (giftInfo == null) {
			return 0;
		}
		
		Long lgEtime = GiftInfoDao.getInstance().checkUsedBadgeByUidGid(uid, gid);
		
		String sql = "";
		if (lgEtime == null) {
			sql = "INSERT INTO `user_item_special` (`uid`, `gid`, `type`, `subtype`, `num`, `starttime`, `endtime`)"
					+ "select " + uid + ", " + gid + ", type,subtype,1," + stime + ","+(stime + days * 24 * 3600)+" from zhu_config.config_giftlist where gid = " + gid;
		}else if (lgEtime < System.currentTimeMillis()/1000) {
			sql = "update `user_item_special` set num = num + " + num + ",endtime = " + (stime + days * 24 * 3600) + " where uid=" + uid + " and gid=" + gid ;
		}else {
			sql = "update `user_item_special` set num = num + " + num + ",endtime = endtime+" + (days * 24 * 3600) + " where uid=" + uid + " and gid=" + gid ;
		}
		
		Connection con = null;
		PreparedStatement pstmt = null;
		int iresult = 0;
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_item, constant.db_master);
			pstmt = con.prepareStatement(sql);
			
			iresult = pstmt.executeUpdate();
			if (iresult > 0) {
				// 添加成功
				String current_version = "{uid:" + uid + ",gid:" + gid + " gname:" + giftInfo.getGname() + ",开始时间:" + stime + "," + days + "天,bak:" + remarks + "}";
				AddOperationLog("user_item_special", "0", "新增徽章", 1, "", current_version, adminid);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("sql:" + sql);
		}finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return iresult;
	}
}
