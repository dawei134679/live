package com.tinypig.admin;

public class constant {
	public static final String db_zhu_admin = "zhu_admin";
	public static final String db_zhu_web = "zhu_web";
	public static final String db_zhu_user = "zhu_user";
	public static final String db_zhu_pay = "zhu_pay";
	public static final String db_zhu_bill = "zhu_bill";
	public static final String db_zhu_live = "zhu_live";
	public static final String db_zhu_item = "zhu_item";
	public static final String db_zhu_config = "zhu_config";
	public static final String db_zhu_union = "zhu_union";
	public static final String db_zhu_game = "zhu_game";
	public static final String db_zhu_analysis = "zhu_analysis";
	//有效开播时长为5分钟
	public static final int VALID_LIVE_TIME = 5 * 60;
	//每天开播90分钟以上才算有效天数
	public static final int VALID_LIVE_DAY = 90 * 60;

	//table
	public static final String tb_union_info = "union_info";
	public static final String tb_union_anchor_ref = "union_anchor_ref";
	public static final String tb_userbaseinfo = "user_base_info_";
	public static final String tb_live_mic_time = "live_mic_time";
	public static final String tb_user_asset = "user_asset_";
	public static final String tb_user_account = "user_account_";
	public static final String tb_bill = "bill_";
	
	public static final String tb_payorder = "pay_order";
	public static final String tb_user_login_detail = "user_login_detail";
	
	public static final String tb_admin_user = "admin_user";
	
	public static final String db_master = "master";
	public static final String db_slave = "slave";
}
