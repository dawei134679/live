package com.mpig.api.service.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.XML;
import org.springframework.stereotype.Service;

import com.ipaynow.utils.MD5;
import com.ipaynow.utils.NowPayUtils;
import com.mpig.api.SqlTemplete;
import com.mpig.api.db.DataSource;
import com.mpig.api.dictionary.lib.PayConfigLib;
import com.mpig.api.model.PayAccountModel;
import com.mpig.api.model.ReturnModel;
import com.mpig.api.service.IOrderService;
import com.mpig.api.service.IPayService;
import com.mpig.api.service.IUserService;
import com.mpig.api.utils.VarConfigUtils;

/**
 * 现在支付服务 Created by YX on 2016/4/12.
 */
@Service("ipaynowService")
public class IPaynowServiceImpl implements IPayService, SqlTemplete {

	private static final Logger logger = Logger.getLogger("paylog");
	@Resource
	private IUserService userService;
	@Resource
	private WeixinServiceImpl weixinService;
	@Resource
	private IOrderService orderService;

	@Override
	public String buildSign(Map<String, String> param) {

		String paydata = param.get("paydata");
		try {

			logger.debug("<SignIpayNow->paydata>" + paydata);
			String signature = MD5.md5(paydata + "&" + MD5.md5(PayConfigLib.getConfig().getIpayNow_appKey(), "utf-8"),
					"utf-8");
			String sign = signature + "&mhtSignType=" + PayConfigLib.getConfig().getIpayNow_signType();
			logger.debug("<SignIpayNow->sign>" + sign);
			return sign;
		} catch (Exception e) {
			logger.error("<SignIpayNow->error>" + e);
			return null;
		}
	}

	@Override
	public boolean checkNotify(HttpServletRequest req) {
		try {
			return NowPayUtils.verifySignature(req, PayConfigLib.getConfig().getIpayNow_appKey());
		} catch (IOException e) {
			logger.debug("<ipayNowNotify-> error：>" + e);
		}
		return false;
	}

	/**
     * 后台通过审核 发红包接口
     * @param id
     * @param billno
     * @return
    */
	public void verifyWithdraw(int id, String billno,int adminid,ReturnModel returnModel) {

		returnModel.setCode(200);
		returnModel.setMessage("发红包成功");
		
		Map<String, Object> map = userService.getTixianInfoById(id);
		if (map != null && "0".equals(map.get("isSecc").toString())) {
			if (billno.equalsIgnoreCase(map.get("billno").toString())) {
				
				int uid = Integer.valueOf(map.get("uid").toString());
				double dAmount = Double.valueOf(map.get("amount").toString());
				
				// 发送红包
				String returnBody = weixinService.sendRedPack(uid, (int) dAmount, billno);
				if (returnBody == null) {
					// 发红包失败
					returnModel.setCode(401);
					returnModel.setMessage("发红包失败");
				}else {

					Connection conn = null;
					PreparedStatement stmt = null;
					
					try {
						org.json.JSONObject jsonObject = XML.toJSONObject(returnBody).getJSONObject("xml");
						System.out.println("jsonObject:"+jsonObject.toString());
						String returnCode = jsonObject.get("result_code").toString();
						PayAccountModel payAccountModel = orderService.getPayAccountByUid(uid, false);
						if ("SUCCESS".equalsIgnoreCase(returnCode)) {
								conn = DataSource.instance.getPool(VarConfigUtils.dbZhuPay).getConnection();
								conn.setAutoCommit(false);
								// 修改提现信息
								stmt = conn.prepareStatement(SqlTemplete.SQL_UpdWithDrawById);
								stmt.setString(1, jsonObject.getString("send_listid"));
								stmt.setInt(2, 1);
								stmt.setLong(3, System.currentTimeMillis() / 1000);
								stmt.setInt(4, adminid);
								stmt.setInt(5, id);
								stmt.addBatch();
								
								if (payAccountModel.getIsUse() == 0) {
									// 修改
									stmt.addBatch(" update pay_account  set isUse = 1 where uid= " + uid);
								}
								
								int[] executeBatch = stmt.executeBatch();
					            conn.commit(); 
					            
					            if (executeBatch != null) {
					            	boolean blErr = false;
									for(int i = 0; i < executeBatch.length; i++){
										if (executeBatch[i] != 1) {
											blErr = true;
											break;
										}
									}
									if (blErr) {
										returnModel.setCode(201);
										returnModel.setMessage("发红包成功，修改信息失败201");
									}
								}else {
									returnModel.setCode(202);
									returnModel.setMessage("发红包成功，修改信息失败202");
								}
						} else {
							// 提现失败
							returnModel.setCode(402);
							returnModel.setMessage("发红包失败");
						}
					} catch (Exception e) {
						try {
							conn.rollback();
						} catch (SQLException e1) {
							logger.error("verifyWithdraw-SQLException:", e1 );
						}
						returnModel.setCode(403);
						returnModel.setMessage("系统异常");
						logger.error("verifyWithdraw-exception:", e );
					}finally {
						try {
							if (stmt != null) {
								stmt.close();
							}
							if (conn != null) {
								conn.close();
							}
						} catch (Exception e2) {
							logger.error("verifyWithdraw-finally-exception:", e2 );
						}
					}
				}
			}else {
				returnModel.setCode(405);
				returnModel.setMessage("参数异常405");
			}
		}else {
			returnModel.setCode(404);
			returnModel.setMessage("参数异常404");
		}
	}
}
