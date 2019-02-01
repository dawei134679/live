package com.tinypig.admin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.tinypig.admin.constant;
import com.tinypig.admin.model.PayOrderModel;
import com.tinypig.admin.util.DateUtil;
import com.tinypig.admin.util.DbUtil;

public class PayOrderDao extends BaseDao {
	
	private static final PayOrderDao intance = new PayOrderDao();
	
	public static PayOrderDao getInstance(){
		return intance;
	}
	
	/**
	 * 获取扶持号 的充值信息
	 * @param uid
	 * @param unionid
	 * @param stime
	 * @param etime
	 */
	public void getSupportPay(int uid,int unionid,Long stime,Long etime){
		
		String strYmd = DateUtil.convert2String(stime*1000, "yyyyMMdd");
		String sql = " select sum(zhutou) as zhutous from pay_order where status=2 and srcuid=" + uid + " and paytime >= "+stime + " and paytime < "+etime;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_pay, constant.db_slave);
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				while(rs.next()){
					if (rs.getInt("zhutous") > 0) {
						addSupportPay(Integer.valueOf(strYmd), uid, unionid, rs.getInt("zhutous"));
					}
				}
			}
		} catch (Exception e) {
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
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	/**
	 * 判断uid ymdtime是否存在
	 * @param ymdtime
	 * @param uid
	 * @return true 存在，false 不存在
	 */
	public Boolean checkSupport(int ymdtime,int uid){
		
		Boolean blResult = false;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = " select id from pay_union_support where ymdtime = " + ymdtime + " and uid = " + uid;
		try {
			
			con = DbUtil.instance().getCon(constant.db_zhu_pay, constant.db_master);
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if (rs != null) {
				while(rs.next()){
					if (rs.getInt("id") > 0) {
						blResult = true;
						break;
					}
				}
			}
		} catch (Exception e) {
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
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return blResult;
	}

	
	/**
	 * 添加扶持号 金币剩余
	 * @param ymdtime
	 * @param uid
	 * @param unionid
	 * @param pays
	 * @param money
	 */
	public void addSupportSurplus(int ymdtime,int uid,int unionid,int money){
		
		String sql = "";
		Boolean checkSupport = checkSupport(ymdtime, uid);
		if (checkSupport) {
			sql = " update pay_union_support set money=" + money + " where ymdtime=" + ymdtime + " and uid=" + uid;
		}else {
			sql = " insert into pay_union_support(ymdtime,uid,unionid,money,addtime) value("+ymdtime+","+uid+","+unionid+","+money+","+System.currentTimeMillis()/1000+")";
		}
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_pay, constant.db_master);
			pstmt = con.prepareStatement(sql);
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println("addSupportPay: " + sql);
			e.printStackTrace();
		} finally {
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
	}
	
	/**
	 * 添加扶持号 充值
	 * @param ymdtime
	 * @param uid
	 * @param unionid
	 * @param pays
	 * @param money
	 */
	public void addSupportPay(int ymdtime,int uid,int unionid,int pays){
		
		String sql = "";
		Boolean checkSupport = checkSupport(ymdtime, uid);
		if (checkSupport) {
			sql = " update pay_union_support set pays=" + pays + " where ymdtime=" + ymdtime + " and uid=" + uid;
		}else {
			sql = " insert into pay_union_support(ymdtime,uid,unionid,pays,addtime) value("+ymdtime+","+uid+","+unionid+","+pays+","+System.currentTimeMillis()/1000+")";
		}
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_pay, constant.db_master);
			pstmt = con.prepareStatement(sql);
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println("addSupportPay: " + sql);
			e.printStackTrace();
		} finally {
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
	}
	
	/**
	 * 添加扶持号 消耗
	 * @param ymdtime
	 * @param uid
	 * @param unionid
	 */
	public void addSupportConsume(int ymdtime,int uid,int unionid,int consume,int amount){

		String sql = "";
		Boolean checkSupport = checkSupport(ymdtime, uid);
		if (checkSupport) {
			sql = " update pay_union_support set consume=consume+" + consume + ",amount=amount+" + amount +" where ymdtime=" + ymdtime + " and uid=" + uid;
		}else {
			sql = " insert into pay_union_support(ymdtime,uid,unionid,consume,amount,addtime) value("+ymdtime+","+uid+","+unionid+","+consume+","+amount+","+System.currentTimeMillis()/1000+")";
		}
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = DbUtil.instance().getCon(constant.db_zhu_pay, constant.db_master);
			pstmt = con.prepareStatement(sql);
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println("addSupportConsume: " + sql);
			e.printStackTrace();
		} finally {
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
	}

    public List<PayOrderModel> getPayOrderList(String srcuid, String strpaytype, String strstatus, String strtmstart, String strtmend) {
        List<PayOrderModel> rt = null;

        Integer nSrcid = valueOf(srcuid);
        Integer nTmStart = valueOf(strtmstart);
        Integer nTmEnd = valueOf(strtmend);
        Integer nstatus = valueOf(strstatus);


        String sql = "select * from " + constant.tb_payorder;
        if (null != nSrcid || null != nTmStart || null != nTmEnd) {
            sql += " where ";
            boolean bNeedAnd = false;
            if (null != nSrcid) {
                sql += " srcuid=" + nSrcid;
                bNeedAnd = true;
            }
            if (false == strpaytype.isEmpty()) {
                if (bNeedAnd) {
                    sql += " and ";
                }
                sql += " paytype" + "=" + strpaytype;
                bNeedAnd = true;
            }
            if (null != nstatus) {
                if (bNeedAnd) {
                    sql += " and ";
                }
                sql += " status" + "=" + nstatus;
                bNeedAnd = true;
            }
            if (null != nTmStart) {
                if (bNeedAnd) {
                    sql += " and ";
                }
                sql += " creatAt" + ">=" + nTmStart;
                bNeedAnd = true;
            }
            if (null != nTmEnd) {
                if (bNeedAnd) {
                    sql += " and ";
                }
                sql += " creatAt" + "<=" + nTmEnd;
                bNeedAnd = true;
            }
        }

        DbUtil.DbSimpleQuery dbq = new DbUtil.DbSimpleQuery();
        ResultSet rs = dbq.simpleQuery(constant.db_zhu_pay, sql);
        if (null != rs) {
            try {
                while (rs.next()) {
                    if (null == rt) {
                        rt = new ArrayList<PayOrderModel>();
                    }
                    PayOrderModel data = new PayOrderModel().populateFromResultSet(rs);
                    rt.add(data);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        dbq.release();

        return rt;
    }

    public List<PayOrderModel> getPayOrderList(Integer page, Integer rows, String sort, String order, Integer srcuid, Integer dstuid, Integer status,Long sTime,Long eTime, String channel) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "";

        List<PayOrderModel> rt = new ArrayList<PayOrderModel>();

        if(StringUtils.isNotEmpty(channel)){
        	channel = channel.substring(1);
        	channel = channel.replaceAll(",", "\",\"");
        	channel = "\"" + channel + "\"";
        	sql = "select pay.*, detail.channel from " + constant.db_zhu_pay + "." + constant.tb_payorder + " pay, " + constant.db_zhu_admin + "." + constant.tb_user_login_detail + " detail where pay.paytype != 'apple-test'";
        	sql += " and pay.srcuid = detail.uid";
        	sql += " and detail.istype = 1";
        	sql += " and detail.channel in (" + channel + ")";
        	if (srcuid > 0) {
                sql += " and pay.srcuid = " + srcuid;
            }
            if (status != 9) {
                sql += " and pay.status = " + status;
            }
            if (dstuid > 0) {
                sql += " and pay.dstuid = " + dstuid;
            }
            if (sTime > 0) {
    			sql += " and pay.paytime>= unix_timestamp('"+sTime+"')";
    		}
            if (eTime > 0) {
    			sql += " and pay.paytime < unix_timestamp('"+eTime+"')";
    		}
            int begin = (page - 1) * rows;

            sql += " ORDER BY pay.creatAt desc  limit " + begin + "," + rows;
        }else{
        	sql = "SELECT po.*, ud.channel from ( SELECT * FROM " + constant.db_zhu_pay + "." + constant.tb_payorder + " WHERE paytype != 'apple-test' ";
            if (srcuid > 0) {
                sql += " and srcuid = " + srcuid;
            }
            if (status != 9) {
                sql += " and status = " + status;
            }
            if (dstuid > 0) {
                sql += " and dstuid = " + dstuid;
            }
            if (sTime > 0) {
    			sql += " and paytime>= unix_timestamp('"+sTime+"')";
    		}
            if (eTime > 0) {
    			sql += " and paytime < unix_timestamp('"+eTime+"')";
    		}

            int begin = (page - 1) * rows;

            sql += " ORDER BY creatAt desc limit " + begin + "," + rows;
            sql += " ) po left join";
            sql += " (select uld.uid, uld.channel from " + constant.db_zhu_admin + "." + constant.tb_user_login_detail + " uld where uld.isType = 1) ud on po.srcuid = ud.uid";
        }

        try {
            con = DbUtil.instance().getCon("slave");
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if (null != rs) {
                try {
                    while (rs.next()) {
                        PayOrderModel data = new PayOrderModel().populateFromResultSet(rs);
                        rt.add(data);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
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
            } catch (Exception ex) {

            }
        }
        return rt;
    }

    public int countQuery(Integer page, Integer rows, String sort, String order, Integer srcuid, Integer dstuid, Integer status,Long sTime,Long eTime, String channel) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "";

        if(StringUtils.isNotEmpty(channel)){
        	channel = channel.substring(1);
        	channel = channel.replaceAll(",", "\",\"");
        	channel = "\"" + channel + "\"";
        	sql = "select count(*) from " + constant.db_zhu_pay + "." + constant.tb_payorder + " pay, " + constant.db_zhu_admin + "." + constant.tb_user_login_detail + " detail where 1 = 1";
        	sql += " and pay.srcuid = detail.uid";
        	sql += " and detail.istype = 1";
        	sql += " and detail.channel in (" + channel + ")";
        	if (srcuid > 0) {
                sql += " and pay.srcuid = " + srcuid;
            }
            if (status != 9) {
                sql += " and pay.status = " + status;
            }
            if (dstuid > 0) {
                sql += " and pay.dstuid = " + dstuid;
            }
            if (sTime > 0) {
    			sql += " and pay.paytime>= unix_timestamp('"+sTime+"')";
    		}
            if (eTime > 0) {
    			sql += " and pay.paytime < unix_timestamp('"+eTime+"')";
    		}
        }else{
        	sql = "SELECT COUNT(*) FROM " + constant.db_zhu_pay + "." + constant.tb_payorder + " WHERE 1=1 ";
            if (srcuid > 0) {
                sql += " and srcuid = " + srcuid;
            }
            if (status != 9) {
                sql += " and status = " + status;
            }
            if (dstuid > 0) {
                sql += " and dstuid = " + dstuid;
            }
            if (sTime > 0) {
    			sql += " and paytime>= unix_timestamp('"+sTime+"')";
    		}
            if (eTime > 0) {
    			sql += " and paytime < unix_timestamp('"+eTime+"')";
    		}
        }

        try {
            con = DbUtil.instance().getCon("slave");
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if (null != rs) {
                try {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
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
        return 0;
    }
}
