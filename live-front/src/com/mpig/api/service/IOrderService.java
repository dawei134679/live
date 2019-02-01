package com.mpig.api.service;

import com.mpig.api.model.PayAccountModel;
import com.mpig.api.model.PayOrderModel;
import com.mpig.api.model.ReturnModel;

import java.util.List;

public interface IOrderService {

    /**
     * 生成订单号
     *
     * @return
     */
    public String CreateOrderNo(int os);


    /**
     * 检查是否存在刷单
     *
     * @param uid
     * @return =true刷单 =false 正常
     */
    public Boolean CheckBrush(Integer uid);

    /**
     * 生成支付宝订单号
     *
     * @param obj
     * @return
     */
    public void CreateAliOrder(String subject, String orderNo, Integer srcUid, Integer dstUid,
                               Integer amount, Integer creatAt, Integer paytime, int os,
                               String paytype, Boolean bl, String inpour_no, String details,String userSource,String channel,int registtime,
                               ReturnModel returnModel);

    /**
     * 生成订单
     *
     * @param notiryUrl   第三方回调接口
     * @param orderNo     订单号
     * @param srcUid      操作者UID
     * @param dstUid      充值对象UID
     * @param amount      充值金额
     * @param creatAt     生成时间
     * @param paytime     支付时间
     * @param os          平台
     * @param paytype     支付类型
     * @param bl          支付状态
     * @param inpour_no   第三方交易单号
     * @param details     订单说明
     * @param userSource  用户来源
     * @param returnModel
     */
    public int CreateOrder(String notiryUrl, String orderNo, Integer srcUid, Integer dstUid,
                           Float amount, Integer creatAt, Integer paytime, int os,
                           String paytype, Boolean bl, String inpour_no, String details,String userSource,String channel,int registtime,
                           ReturnModel returnModel);

    /**
     * 支付宝支付成功后处理
     *
     * @param orderNo
     * @param trade_no
     * @param total_fee
     * @return
     */
    public int updPayStatus(String orderNo, String trade_no, Double total_fee,int zhutou, Integer status);
    
    /**
     * 修改订单状态
     * @param orderNo 订单号
     * @param trade_no 交易号
     * @param total_fee 充值金额
     * @param zhutou 猪头数
     * @param status 订单状态
     * @param paytime 充值时间
     * @return
     */
    public int updPayStatus(String orderNo, String trade_no, Double total_fee,int zhutou, Integer status,Long paytime);
    
    /**
     * 生成订单
     *
     * @param orderNo     订单号
     * @param srcUid      操作者UID
     * @param dstUid      充值对象UID
     * @param amount      充值金额
     * @param creatAt     生成时间
     * @param paytime     支付时间
     * @param os          平台
     * @param paytype     支付类型
     * @param status       支付状态
     * @param inpour_no   第三方交易单号
     * @param details     订单说明
     * @param returnModel
     */
    public int CreateOrderApp(String orderNo, Integer srcUid, Integer dstUid,
                           Float amount,int zhutou, Integer creatAt, Integer paytime, int os,
                           String paytype, int status, String inpour_no, String details,String userSource,String channel,int registtime,
                           ReturnModel returnModel);

    /**
     * 获取订单信息
     *
     * @param orderNo
     * @return
     */
    public PayOrderModel getPayOrderByOrderNo(String orderNo);


    /**
     * 获取用户 提现的账号
     *
     * @param uid
     * @return
     */
    public PayAccountModel getPayAccountByUid(int uid, Boolean bl);

    /**
     * 新增用户与提现账号的关系
     *
     * @param objs
     * @return
     */
    public int insertPayAccount(Object... objs);

    /**
     * 修改提现账号
     *
     * @param uid
     * @param objs
     * @return
     */
    public int updPayAccountByUid(int uid, String weixin, String alipay);

    int bindWeixin(String  unionId ,String openid);

    List<Integer> getUidByUnionid(String unionid);
    
    /**
     * 添加活动产生的猪头记录
     * @param uid  用户UID
     * @param source  =1任务 =2活动
     * @param act_id 活动id
     * @param act_name  活动名称
     * @param zhutou 猪头数
     * @return =1成功，其他则失败
     */
    public int insertPayActivity(int uid,int source,int act_id,String act_name,int zhutou);
    
    /**
     * 判断是否首充
     * @param uid
     * @return false未充值，true已充值
     */
    public boolean checkFirst(int uid);

    /**
     * 国庆活动 领取金币
     * @param uid
     * @param type  =1(100~500) =2(500~1000) =3(1000~)
     * @return
     */
    public void ReceiveMoney(int uid,int type,ReturnModel returnModel);
    
    /**
     * 修改支付方式
     * @param payType
     * @param orderNo
     * @return
     */
    public int updPayType(String payType,String orderNo);


	/**
	 * 根据第三方订单编号删除订单
	 * @param inpour_no
	 */
	public boolean delByInpourNo(String inpour_no);


	/**
	 * 查看订单是否存在
	 * @param inpour_no
	 */
	public PayOrderModel existByInpourNo(String inpour_no);
	
	/**
	 * 修改订单状态
	 * @param inpour_no
	 */
	public boolean updStatusByInpourNo(String inpour_no);

}
